package com.ferret.app.network

import com.ferret.app.model.Article
import de.jensklingenberg.ktorfit.http.GET

interface CoursesService {

    @GET("CoursesList")
    suspend fun getAllCourses(): List<Article>

}