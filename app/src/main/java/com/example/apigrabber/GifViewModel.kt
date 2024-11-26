package com.example.apigrabber

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class GifViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val GIFS_KEY = "gifs_key"
    }

    private val _gifs = MutableStateFlow(savedStateHandle[GIFS_KEY] ?: emptyList<String>())
    val gifs: StateFlow<List<String>> = _gifs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val apiKey = "LIVDSRZULELA"

    fun fetchGif() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""
            try {
                val randomQuery = listOf("funny", "cat", "dog", "meme", "dance", "reaction").random()
                val response = RetrofitInstance.api.getRandomGif(apiKey, query = randomQuery, limit = 1)
                val gifUrl = response.results.firstOrNull()
                    ?.media?.firstOrNull()
                    ?.gif?.url
                if (gifUrl != null) {
                    _gifs.value += gifUrl
                } else {
                    _errorMessage.value = "Не удалось загрузить GIF"
                }
            } catch (e: HttpException) {
                _errorMessage.value = "HTTP ошибка: ${e.code()}"
            } catch (e: Exception) {
                _errorMessage.value = "Неизвестная ошибка: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}