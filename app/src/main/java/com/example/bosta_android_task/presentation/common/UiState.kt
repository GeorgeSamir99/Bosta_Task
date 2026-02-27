

sealed class UiState<out T> {

    object Idle : UiState<Nothing>()
    

    object Loading : UiState<Nothing>()
    

    data class Success<T>(val data: T) : UiState<T>()
    

    data class Error(val exception: Throwable) : UiState<Nothing>() {
        val message: String
            get() = exception.message ?: "Unknown error occurred"
    }
    

    object Empty : UiState<Nothing>()
}


fun <T> UiState<T>.isLoading(): Boolean = this is UiState.Loading


fun <T> UiState<T>.isSuccess(): Boolean = this is UiState.Success

fun <T> UiState<T>.isError(): Boolean = this is UiState.Error


fun <T> UiState<T>.getDataOrNull(): T? {
    return if (this is UiState.Success) data else null
}
