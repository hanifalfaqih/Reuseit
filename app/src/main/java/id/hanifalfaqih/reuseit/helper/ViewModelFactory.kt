package id.hanifalfaqih.reuseit.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GenericViewModelFactory<T : ViewModel>(
    private val repository: () -> T
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return repository() as T
    }
}