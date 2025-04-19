package com.chrisp.setaraapp.Model.DataAuth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.UUID

sealed interface AuthResponse {
    data object Success : AuthResponse
    data class Error(val message: String?) : AuthResponse
}

// Data class matching the database schema
@Serializable
data class UserProfile(
    val id: String = "", // Primary key - matches auth ID
    val f_name: String,  // Full name in the database
    val email: String,
    val birth_date: String, // Birth date as string
    val cat_disability: String, // Category of disability
    val no_telp: String, // Phone number
    val address: String,
    val created_at: String = LocalDateTime.now().toString()
)

class AuthManager(
    private val context: Context
) {

    private val supabase = createSupabaseClient(
        supabaseUrl = "https://kvpupfnfondfwnguhjug.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt2cHVwZm5mb25kZnduZ3VoanVnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDA5ODAyMTgsImV4cCI6MjA1NjU1NjIxOH0.ijWBN0onfpzt16PjfIqR6javyLRSkpXukS7yIhAk_k4"
    ) {
        install(Auth)
        install(Postgrest)
    }

    fun signUpWithEmail(emailValue: String, passwordValue: String): Flow<AuthResponse> = flow {
        try {
            supabase.auth.signUpWith(Email) {
                email = emailValue
                password = passwordValue
            }

            emit(AuthResponse.Success)
        } catch (e: Exception) {
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }

    // Updated function to sign up with email and create profile with correct field names
    fun signUpWithEmailAndCreateProfile(
        email: String,
        password: String,
        fullName: String,
        birthDate: String,
        categoryDisability: String,
        phoneNumber: String,
        address: String
    ): Flow<AuthResponse> = flow {
        try {
            // Step 1: Sign up with Supabase Auth
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            // Step 2: Get the user ID after authentication
            val userId = supabase.auth.currentUserOrNull()?.id
                ?: throw Exception("Failed to get user ID")

            // Step 3: Create profile in the database with correct field names
            val profile = UserProfile(
                id = userId,
                f_name = fullName,
                email = email,
                birth_date = birthDate,
                cat_disability = categoryDisability,
                no_telp = phoneNumber,
                address = address
            )

            // Insert the profile data into the "profiles" table
            supabase.postgrest["profiles"].insert(profile)

            emit(AuthResponse.Success)
        } catch (e: Exception) {
            Log.e("ProfileCreation", "Error: ${e.localizedMessage}", e)
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }

    fun signInWithEmail(emailValue: String, passwordValue: String): Flow<AuthResponse> = flow {
        try {
            supabase.auth.signInWith(Email) {
                email = emailValue
                password = passwordValue
            }
            emit(AuthResponse.Success)

        } catch (e: Exception) {
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }

    fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { str, it ->
            str + "%02x".format(it)
        }
    }

    fun loginGoogleUser(): Flow<AuthResponse> = flow {
        val hashedNonce = createNonce()

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId("508479746986-cq5jdnsf2q780vc26fpciq6bfbbec0el.apps.googleusercontent.com")
            .setNonce(hashedNonce)
            .setAutoSelectEnabled(false)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        val credentialManager = CredentialManager.create(context)

        try {
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(result.credential.data)

            val googleIdToken = googleIdTokenCredential.idToken

            supabase.auth.signInWith(IDToken) {
                idToken = googleIdToken
                provider = Google
            }

            emit(AuthResponse.Success)

        } catch (e: Exception) {
            Log.e("google", e.localizedMessage)
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }

    // Updated function to sign up with Google and create profile
    fun signUpWithGoogleAndCreateProfile(
        fullName: String,
        birthDate: String,
        categoryDisability: String,
        phoneNumber: String,
        address: String
    ): Flow<AuthResponse> = flow {
        try {
            // First login with Google
            val hashedNonce = createNonce()

            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId("508479746986-cq5jdnsf2q780vc26fpciq6bfbbec0el.apps.googleusercontent.com")
                .setNonce(hashedNonce)
                .setAutoSelectEnabled(false)
                .setFilterByAuthorizedAccounts(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            val credentialManager = CredentialManager.create(context)

            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(result.credential.data)

            val googleIdToken = googleIdTokenCredential.idToken

            // Sign in with Google
            supabase.auth.signInWith(IDToken) {
                idToken = googleIdToken
                provider = Google
            }

            // Get user info after signing in
            val currentUser = supabase.auth.currentUserOrNull()
                ?: throw Exception("Failed to get user after Google sign-in")

            val userId = currentUser.id
            val email = currentUser.email ?: throw Exception("Failed to get user email")

            // Create and save profile with correct field names
            val profile = UserProfile(
                id = userId,
                f_name = fullName,
                email = email,
                birth_date = birthDate,
                cat_disability = categoryDisability,
                no_telp = phoneNumber,
                address = address
            )

            supabase.postgrest["profiles"].insert(profile)

            emit(AuthResponse.Success)
        } catch (e: Exception) {
            Log.e("GoogleProfileCreation", "Error: ${e.localizedMessage}", e)
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }

    // Updated function to get user profile from database
    fun getUserProfile(): Flow<Result<UserProfile>> = flow {
        try {
            // Get current user ID from session
            val userId = supabase.auth.currentUserOrNull()?.id
                ?: throw Exception("No authenticated user found")

            // Fetch profile from the database
            val profile = supabase.postgrest["profiles"]
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<UserProfile>()

            emit(Result.success(profile))
        } catch (e: Exception) {
            Log.e("Profile", "Error fetching profile: ${e.localizedMessage}")
            emit(Result.failure(e))
        }
    }

    fun checkIfUserProfileExists(): Flow<Boolean> = flow {
        try {
            val userId = supabase.auth.currentUserOrNull()?.id
            println("🔥 User ID: $userId")

            val result = supabase.postgrest["profiles"]
                .select {
                    filter {
                        eq("id", userId ?: "")
                    }
                }
                .decodeList<UserProfile>()

            println("🔥 Profile result: $result")

            emit(result.isNotEmpty())
        } catch (e: Exception) {
            println("🔥 Error checking profile: ${e.message}")
            emit(false)
        }
    }


}