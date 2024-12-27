package id.hanifalfaqih.reuseit.data.repository

import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.data.model.ResponseDataDetail
import retrofit2.Response

interface DetailContentRepository {

    suspend fun getDetailContent(id: Int): Response<ResponseDataDetail<Content>>
}