package id.hanifalfaqih.reuseit.network

import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.data.model.ResponseData
import id.hanifalfaqih.reuseit.data.model.ResponseDataDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("diy")
    suspend fun getAllDIY(): Response<ResponseData<Content>>

    @GET("diy/top-5")
    suspend fun getTop5DIY(): Response<ResponseData<Content>>

    @GET("course")
    suspend fun getAllCourse(): Response<ResponseData<Content>>

    @GET("course/top-5")
    suspend fun getTop5Course(): Response<ResponseData<Content>>

    @GET("content/{id}")
    suspend fun getContentDetail(
        @Path("id") id: Int
    ): Response<ResponseDataDetail<Content>>

    @POST("auth/login")
    fun postLogin()

    @POST("auth/register")
    fun postRegister()

    @GET("auth/register")
    fun getUserLoginDetail()

}