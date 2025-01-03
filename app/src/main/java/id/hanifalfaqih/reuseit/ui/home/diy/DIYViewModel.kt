package id.hanifalfaqih.reuseit.ui.home.diy

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.data.repository.DIYRepository
import kotlinx.coroutines.launch

class DIYViewModel(private val repository: DIYRepository): ViewModel() {

    private var _listAllDIYContent = MutableLiveData<List<Content>?>()
    val listAllDIYContent get() = _listAllDIYContent

    private var _listTop5DIYContent = MutableLiveData<List<Content>?>()
    val listTop5DIYContent get() = _listTop5DIYContent

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage get() = _errorMessage

    fun getAllDIYContent() {
        viewModelScope.launch {
            try {
                val response = repository.getAllDIYContent()
                if (response.isSuccessful) {
                    val responseData = response.body()
                    responseData?.let {
                        _listAllDIYContent.value = it.data
                    }
                } else {
                    _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }

    fun getTop5DIYContent() {
        Log.d(DIYViewModel::class.java.simpleName, "GET TOP 5 DIY CONTENT")
        viewModelScope.launch {
            try {
                val response = repository.getTop5DIYContent()
                if (response.isSuccessful) {
                    val responseData = response.body()
                    responseData?.let {
                        _listTop5DIYContent.value = it.data
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