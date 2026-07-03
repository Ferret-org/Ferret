package com.ferret.app.domain

import com.ferret.app.data.CourseRepository
import com.ferret.app.model.Article
import com.ferret.app.network.CoursesService

class CourseRepositoryImpl(
    private val coursesService: CoursesService
) : CourseRepository {

    override suspend fun getAllCourses(): List<Article> = coursesService.getAllCourses()
}