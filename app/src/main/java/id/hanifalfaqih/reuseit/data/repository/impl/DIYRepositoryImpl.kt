package id.hanifalfaqih.reuseit.data.repository.impl

import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.data.model.ResponseData
import id.hanifalfaqih.reuseit.data.repository.DIYRepository
import id.hanifalfaqih.reuseit.network.ApiService
import retrofit2.Response

class DIYRepositoryImpl(private val apiService: ApiService): DIYRepository {

    override suspend fun getAllDIYContent(): Response<ResponseData<Content>> {
        return apiService.getAllDIY()
    }

    override suspend fun getTop5DIYContent(): Response<ResponseData<Content>> {
        return apiService.getTop5DIY()
    }


}