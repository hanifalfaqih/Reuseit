package id.hanifalfaqih.reuseit.data.repository

import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.data.model.ResponseData
import retrofit2.Response

interface CourseRepository {
    suspend fun getAllCourseContent(): Response<ResponseData<Content>>
    suspend fun getTop5CourseContent(): Response<ResponseData<Content>>
}