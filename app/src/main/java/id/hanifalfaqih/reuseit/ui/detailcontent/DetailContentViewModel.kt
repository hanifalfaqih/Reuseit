package id.hanifalfaqih.reuseit.ui.detailcontent

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.data.repository.DetailContentRepository
import kotlinx.coroutines.launch

class DetailContentViewModel(private val repository: DetailContentRepository): ViewModel() {

    private var _detailContent = MutableLiveData<Content?>()
    val detailContent get() = _detailContent

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage get() = _errorMessage

    fun getDetailContent(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailContent(id)
                if (response.isSuccessful) {
                    val responseData = response.body()
                    responseData?.let {
                        _detailContent.value = it.data?.get(0)
                        Log.d(DetailContentViewModel::class.java.simpleName, it.data.toString())
                    }
                } else {
                    _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }
}