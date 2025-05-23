package com.chrisp.setaraapp.feature.home

import android.util.Log
import com.chrisp.setaraapp.feature.sekerja.model.Course
import com.chrisp.setaraapp.feature.sekerja.model.CourseModule
import com.chrisp.setaraapp.feature.sekerja.model.Module
import com.chrisp.setaraapp.feature.repository.SupabaseInstance
import io.github.jan.supabase.postgrest.postgrest

interface HomeRepository {
    suspend fun getAllCoursesWithModules(): Result<List<Course>>
    suspend fun isUserEnrolled(userId: String, courseId: String): Boolean
}

class HomeRepositoryImpl : HomeRepository {
    private val supabase = SupabaseInstance.client
    private val tag = "HomeRepositoryImpl"

    override suspend fun getAllCoursesWithModules(): Result<List<Course>> {
        return try {
            val courseData = supabase.postgrest["course"].select().decodeList<Course>()
            val moduleData = supabase.postgrest["module"].select().decodeList<Module>()
            val courseModuleData = supabase.postgrest["course_module"].select().decodeList<CourseModule>()
            moduleData.forEachIndexed { index, module ->
                Log.d(tag, "Module[$index]: $module")
            }

            Log.d("HomeRepository", "Fetched courses: ${courseData.size}")
            Log.d("HomeRepository", "Fetched modules: ${moduleData.size}")
            Log.d("HomeRepository", "Fetched course_module relations: ${courseModuleData.size}")

            val coursesWithModules = courseData.map { course ->
                val relatedModuleIds = courseModuleData
                    .filter { it.courseId == course.courseId }
                    .sortedBy { it.orderNum }
                    .map { it.moduleId }

                val relatedModules = relatedModuleIds.mapNotNull { moduleId ->
                    moduleData.find { it.moduleId == moduleId }
                }
                course.copy(modules = relatedModules)
            }
            Result.success(coursesWithModules)
        } catch (e: Exception) {
            Log.e("HomeRepository", "Error loading courses from Supabase", e)
            Result.failure(e)
        }
    }

    override suspend fun isUserEnrolled(userId: String, courseId: String): Boolean {
        val enrollments = supabase.postgrest["daftar"]
            .select {
                filter {
                    eq("user_id", userId)
                    eq("course_id", courseId)
                }
            }
            .decodeList<Map<String, Any>>()
        return enrollments.isNotEmpty()
    }
}