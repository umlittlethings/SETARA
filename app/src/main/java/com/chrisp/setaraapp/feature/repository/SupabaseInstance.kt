package com.chrisp.setaraapp.feature.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage


object SupabaseInstance { // Using object for singleton
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://kvpupfnfondfwnguhjug.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt2cHVwZm5mb25kZnduZ3VoanVnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDA5ODAyMTgsImV4cCI6MjA1NjU1NjIxOH0.ijWBN0onfpzt16PjfIqR6javyLRSkpXukS7yIhAk_k4"
    ) {
        install(Postgrest)
        install(Auth)
        install(Storage)
    }
}