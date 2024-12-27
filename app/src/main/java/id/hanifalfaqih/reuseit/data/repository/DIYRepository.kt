package id.hanifalfaqih.reuseit.data.repository

import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.data.model.ResponseData
import retrofit2.Response

interface DIYRepository {

    suspend fun getAllDIYContent(): Response<ResponseData<Content>>
    suspend fun getTop5DIYContent(): Response<ResponseData<Content>>

}