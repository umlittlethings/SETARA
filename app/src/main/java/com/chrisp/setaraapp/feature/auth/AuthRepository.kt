package com.chrisp.setaraapp.feature.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.chrisp.setaraapp.feature.repository.SupabaseInstance
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.security.MessageDigest
import java.util.UUID

class AuthRepository(
    private val context: Context
) {
    private val supabase = SupabaseInstance.client

    fun signUpAndCreateProfile(
        email: String,
        password: String,
        fullName: String,
        birthDate: String,
        categoryDisability: String,
        phoneNumber: String,
        address: String
    ): Flow<AuthResponse> = flow {
        emit(AuthResponse.Loading)
        try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            Log.d("AuthRepository", "Sign up successful for $email")
            var attempts = 0
            var user = supabase.auth.currentUserOrNull()
            while (user == null && attempts < 5) {
                delay(1000)
                user = supabase.auth.currentUserOrNull()
                attempts++
                Log.d("AuthRepository", "Attempt ${attempts} to get current user after sign up.")
            }

            if (user == null) {
                Log.w(
                    "AuthRepository",
                    "User is null after sign-up and retries. Email confirmation might be pending."
                )
                Log.d(
                    "AuthRepository",
                    "Explicitly signing in again to ensure session for profile creation."
                )
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                user = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Failed to get user even after explicit re-sign-in.")
            }

            val userId = user.id
            Log.d("AuthRepository", "User ID for profile: $userId")

            val profile = User(
                id = userId,
                f_name = fullName,
                email = email,
                birth_date = birthDate,
                cat_disability = categoryDisability,
                no_telp = phoneNumber,
                address = address
            )

            supabase.postgrest["profiles"].insert(profile)
            Log.d("AuthRepository", "Profile insertion attempted for user ID: $userId")

            delay(1000)
            val profileCheck = supabase.postgrest["profiles"]
                .select { filter { eq("id", userId) } }
                .decodeList<User>()

            if (profileCheck.isEmpty()) {
                Log.e("AuthRepository", "Profile verification failed for user ID: $userId")
                emit(AuthResponse.Error("Verifikasi profil gagal."))
                return@flow
            }
            Log.d(
                "AuthRepository",
                "Profile successfully created and verified for user ID: $userId"
            )
            emit(AuthResponse.Success)

        } catch (e: Exception) {
            Log.e("AuthRepository_SignUp", "Error: ${e.message}", e)
            emit(AuthResponse.Error(e.message ?: "Terjadi kesalahan saat pendaftaran."))
        }
    }

    fun signInWithEmail(emailValue: String, passwordValue: String): Flow<AuthResponse> = flow {
        emit(AuthResponse.Loading)
        try {
            supabase.auth.signInWith(Email) {
                email = emailValue
                password = passwordValue
            }
            Log.d("AuthRepository", "Sign in successful for $emailValue")
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            Log.e("AuthRepository_SignIn", "Error: ${e.message}", e)
            emit(AuthResponse.Error(e.message ?: "Gagal masuk. Periksa kredensial Anda."))
        }
    }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun loginGoogleUser(): Flow<AuthResponse> = flow {
        emit(AuthResponse.Loading)
        val hashedNonce = try {
            createNonce()
        } catch (e: Exception) {
            Log.e("AuthRepository_Google", "Nonce creation failed", e)
            emit(AuthResponse.Error("Gagal memulai login Google."))
            return@flow
        }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId("508479746986-cq5jdnsf2q780vc26fpciq6bfbbec0el.apps.googleusercontent.com")
            .setNonce(hashedNonce)
            .setAutoSelectEnabled(false)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager =
            CredentialManager.Companion.create(context)

        try {
            val result = credentialManager.getCredential(context = context, request = request)
            val googleIdTokenCredential =
                GoogleIdTokenCredential.Companion.createFrom(result.credential.data)
            val googleIdToken = googleIdTokenCredential.idToken

            supabase.auth.signInWith(IDToken) {
                idToken = googleIdToken
                provider = Google
            }
            Log.d("AuthRepository", "Google Sign-In successful with Supabase.")
            emit(AuthResponse.Success)

        } catch (e: Exception) {
            Log.e("AuthRepository_Google", "Google Sign-In error: ${e.message}", e)
            emit(AuthResponse.Error(e.message ?: "Login dengan Google gagal."))
        }
    }

    fun signUpWithGoogleAndCreateProfile(
        fullName: String,
        birthDate: String,
        categoryDisability: String,
        phoneNumber: String,
        address: String
    ): Flow<AuthResponse> = flow {
        emit(AuthResponse.Loading)
        try {
            val hashedNonce = createNonce()
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId("508479746986-cq5jdnsf2q780vc26fpciq6bfbbec0el.apps.googleusercontent.com")
                .setNonce(hashedNonce)
                .setAutoSelectEnabled(false)
                .setFilterByAuthorizedAccounts(false)
                .build()
            val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
            val credentialManager = CredentialManager.Companion.create(context)

            val result = credentialManager.getCredential(context = context, request = request)
            val googleIdTokenCredential =
                GoogleIdTokenCredential.Companion.createFrom(result.credential.data)
            val googleIdToken = googleIdTokenCredential.idToken

            supabase.auth.signInWith(IDToken) {
                this.idToken = googleIdToken
                this.provider = Google
            }
            Log.d("AuthRepository", "Google Sign-In part of sign-up successful.")

            val currentUser = supabase.auth.currentUserOrNull()
                ?: throw Exception("Gagal mendapatkan pengguna setelah login Google.")
            val userId = currentUser.id
            val email = currentUser.email
                ?: throw Exception("Gagal mendapatkan email pengguna dari Google.")
            Log.d("AuthRepository", "User ID for Google profile: $userId, Email: $email")


            val profile = User(
                id = userId,
                f_name = fullName,
                email = email,
                birth_date = birthDate,
                cat_disability = categoryDisability,
                no_telp = phoneNumber,
                address = address
            )
            supabase.postgrest["profiles"].insert(profile)
            Log.d("AuthRepository", "Profile creation attempted for Google user: $userId")
            emit(AuthResponse.Success)

        } catch (e: Exception) {
            Log.e("AuthRepository_GoogleSignUp", "Error: ${e.message}", e)
            emit(AuthResponse.Error(e.message ?: "Pendaftaran dengan Google gagal."))
        }
    }

    fun isUserLoggedIn(): Flow<Boolean> = flow {
        val user = supabase.auth.currentUserOrNull()
        Log.d(
            "AuthRepository",
            "isUserLoggedIn check, user is: ${if (user != null) "NOT NULL" else "NULL"}"
        )
        emit(user != null)
    }.catch { e ->
        Log.e("AuthRepository", "Error checking login status: ${e.message}", e)
        emit(false)
    }

    fun getUser(): Flow<Result<User>> = flow {
        try {
            val userId = supabase.auth.currentUserOrNull()?.id
            if (userId == null) {
                emit(Result.failure(Exception("Pengguna tidak terautentikasi.")))
                return@flow
            }

            val profile = supabase.postgrest["profiles"]
                .select { filter { eq("id", userId) } }
                .decodeSingle<User>()
            emit(Result.success(profile))
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error fetching profile: ${e.message}", e)
            emit(Result.failure(Exception("Gagal mengambil data profil: ${e.message}")))
        }
    }

    fun checkIfUserExists(): Flow<Boolean> = flow {
        try {
            val userId = supabase.auth.currentUserOrNull()?.id
            if (userId == null) {
                Log.d("AuthRepository", "checkIfUserExists: No authenticated user.")
                emit(false)
                return@flow
            }

            val result = supabase.postgrest["profiles"]
                .select { filter { eq("id", userId) } }
                .decodeList<User>()

            Log.d(
                "AuthRepository",
                "checkIfUserExists: Profile query result count: ${result.size} for user $userId"
            )
            emit(result.isNotEmpty())
        } catch (e: Exception) {
            Log.e("AuthRepository", "checkIfUserExists: Error: ${e.message}", e)
            emit(false)
        }
    }

    fun signOut(): Flow<AuthResponse> = flow {
        emit(AuthResponse.Loading)
        try {
            supabase.auth.signOut()
            Log.d("AuthRepository", "Sign out successful.")
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            Log.e("AuthRepository_SignOut", "Error: ${e.message}", e)
            emit(AuthResponse.Error(e.message ?: "Gagal keluar."))
        }
    }
}