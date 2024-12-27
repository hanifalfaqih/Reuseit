package id.hanifalfaqih.reuseit.data.repository.impl

import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.data.model.ResponseDataDetail
import id.hanifalfaqih.reuseit.data.repository.DetailContentRepository
import id.hanifalfaqih.reuseit.network.ApiService
import retrofit2.Response

class DetailContentRepositoryImpl(private val apiService: ApiService): DetailContentRepository {
    override suspend fun getDetailContent(id: Int): Response<ResponseDataDetail<Content>> {
        return apiService.getContentDetail(id)
    }
}