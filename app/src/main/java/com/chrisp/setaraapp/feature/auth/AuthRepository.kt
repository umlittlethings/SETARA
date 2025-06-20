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
    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val KEY_IS_LOGGED_IN = "is_logged_in"

    // Fungsi untuk menyimpan status login
    private fun setLoggedInStatus(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }


    fun signUpAndCreateProfile(
        email: String,
        password: String,
        fullName: String,
        birthDate: String,
        categoryDisability: String,
        phoneNumber: String,
        address: String
    ): Flow<AuthResponse> = flow {
        // ... (implementasi yang ada)
        try {
            // ... (logika sign up yang ada)

            // Setelah berhasil, simpan status login
            setLoggedInStatus(true)
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            // ... (error handling yang ada)
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
            // Setelah berhasil, simpan status login
            setLoggedInStatus(true)
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
        // Pertama, emit status dari SharedPreferences untuk respon cepat
        val cachedStatus = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        emit(cachedStatus)

        // Kemudian, verifikasi dengan Supabase
        try {
            val user = supabase.auth.currentUserOrNull()
            val networkStatus = user != null
            if (cachedStatus != networkStatus) {
                // Jika ada perbedaan, perbarui cache dan emit status baru
                setLoggedInStatus(networkStatus)
                emit(networkStatus)
            }
            Log.d(
                "AuthRepository",
                "isUserLoggedIn check, cached: $cachedStatus, network: $networkStatus"
            )
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error checking login status with network: ${e.message}", e)
            // Jika gagal menghubungi jaringan, kita tetap mengandalkan cache dan tidak mengubahnya
        }
    }.catch { e ->
        Log.e("AuthRepository", "Error in isUserLoggedIn flow: ${e.message}", e)
        // Jika terjadi error pada flow, emit nilai cache terakhir
        emit(sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false))
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
            setLoggedInStatus(false)
            Log.d("AuthRepository", "Sign out successful.")
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            Log.e("AuthRepository_SignOut", "Error: ${e.message}", e)
            emit(AuthResponse.Error(e.message ?: "Gagal keluar."))
        }
    }

    fun updateUserPassword(newPassword: String): Flow<AuthResponse> = flow {
        emit(AuthResponse.Loading)
        try {
            supabase.auth.updateUser {
                password = newPassword
            }
            Log.d("AuthRepository", "Password updated successfully.")
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            Log.e("AuthRepository_UpdatePass", "Error: ${e.message}", e)
            emit(AuthResponse.Error(e.message ?: "Gagal memperbarui kata sandi."))
        }
    }
}