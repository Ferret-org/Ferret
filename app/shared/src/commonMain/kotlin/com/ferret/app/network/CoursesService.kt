package com.ferret.app.network

import com.ferret.app.model.Article
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST

interface CoursesService {

    @GET("CoursesList")
    suspend fun getAllCourses(): List<Article>

}