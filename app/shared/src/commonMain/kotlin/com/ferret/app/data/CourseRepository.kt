package com.ferret.app.data

import com.ferret.app.model.Article

interface CourseRepository {

    suspend fun getAllCourses(): List<Article>

}