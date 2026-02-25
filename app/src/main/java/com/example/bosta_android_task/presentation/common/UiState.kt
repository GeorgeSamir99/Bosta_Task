

/**
 * Sealed class representing different UI states
 * Used to handle loading, success, error, and empty states
 */
sealed class UiState<out T> {
    /**
     * Initial state before any operation
     */
    object Idle : UiState<Nothing>()
    
    /**
     * Loading state during operation
     */
    object Loading : UiState<Nothing>()
    
    /**
     * Success state with data
     */
    data class Success<T>(val data: T) : UiState<T>()
    
    /**
     * Error state with exception
     */
    data class Error(val exception: Throwable) : UiState<Nothing>() {
        val message: String
            get() = exception.message ?: "Unknown error occurred"
    }
    
    /**
     * Empty state when no data is available
     */
    object Empty : UiState<Nothing>()
}

/**
 * Extension function to check if state is loading
 */
fun <T> UiState<T>.isLoading(): Boolean = this is UiState.Loading

/**
 * Extension function to check if state is success
 */
fun <T> UiState<T>.isSuccess(): Boolean = this is UiState.Success

/**
 * Extension function to check if state is error
 */
fun <T> UiState<T>.isError(): Boolean = this is UiState.Error

/**
 * Extension function to get data if success
 */
fun <T> UiState<T>.getDataOrNull(): T? {
    return if (this is UiState.Success) data else null
}
