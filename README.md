# 🎮 Bosta Games App

A modern Android application that allows users to browse video games by genre using the RAWG API.  
The project demonstrates clean architecture principles, proper state management, and modern Android development using Jetpack Compose.

---

# 📱 Features

## 🕹 Games List Screen
- Displays games by selected genre
- Each game item includes:
  - Game name
  - Game image
  - Rating
- Pagination 
- Local search 
- Proper UI state handling:
  - Initial loading
  - Pagination loading
  - Error state with retry
  - Empty state

## 🎯 Game Details Screen
- Game name
- Image
- Release date
- Rating
- Description

## 🧪 Unit Testing

Basic unit tests are implemented for `GamesViewModel`.

The tests verify:
- Success state emission
- Error handling
- Empty state handling

---

# 🏗 Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose 
- **Architecture:** MVVM + Clean Architecture principles
- **Async:** Kotlin Coroutines + Flow
- **Networking:** Retrofit
- **Dependency Injection:** Dagger Hilt

---

# 🧠 Architecture Overview

The project follows **MVVM architecture combined with Clean Architecture principles**.

### Presentation Layer
- Jetpack Compose UI
- ViewModels
- State management using StateFlow

### Domain Layer
- Business logic
- UseCases
- Repository interfaces
- Domain models

### Data Layer
- Retrofit API service
- DTO models
- Repository implementations
- Mapping between DTOs and domain models
  
### Unit Testing
# 📂 Project Structure

```
├── data/
│   ├── api/              # Retrofit API service and DTOs
│   ├── repository/       # Repository implementation
│   └── mapper/           # DTO to Domain mappers
├── domain/
│   ├── model/            # Domain models (Game, GameDetails)
│   ├── repository/       # Repository interface
│   └── usecase/          # Business logic use cases
├── presentation/
│   ├── games/            # Games list screen (ViewModel + UI)
│   ├── details/          # Game details screen (ViewModel + UI)
│   ├── navigation/       # Navigation setup
│   └── common/           # Shared UI components and states
├── di/                   # Dependency injection modules


