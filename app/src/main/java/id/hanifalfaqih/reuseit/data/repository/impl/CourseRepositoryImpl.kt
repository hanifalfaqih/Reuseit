package id.hanifalfaqih.reuseit.data.repository.impl

import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.data.model.ResponseData
import id.hanifalfaqih.reuseit.data.repository.CourseRepository
import id.hanifalfaqih.reuseit.network.ApiService
import retrofit2.Response

class CourseRepositoryImpl(private val apiService: ApiService): CourseRepository {
    override suspend fun getAllCourseContent(): Response<ResponseData<Content>> {
        return apiService.getAllCourse()
    }

    override suspend fun getTop5CourseContent(): Response<ResponseData<Content>> {
        return apiService.getTop5Course()
    }
}