# Task Assignment Android App

A production-ready Android application built with Jetpack Compose that consumes a FastAPI backend for event management and booking functionality.

## 🏗️ Architecture

This project follows **Clean Architecture** principles with strict separation of concerns:

```
app/
├── core/
│   ├── network/         # Networking utilities, API service, interceptors
│   ├── datastore/       # Data persistence with DataStore
│   └── ui/              # Shared UI components and theming
├── auth/
│   ├── data/            # DTOs, mappers, repository implementation
│   ├── domain/          # Entities, repository interface, use cases
│   └── presentation/    # ViewModels, UI screens, state management
├── events/
│   ├── data/            # DTOs, mappers, repository implementation
│   ├── domain/          # Entities, repository interface, use cases
│   └── presentation/    # ViewModels, UI screens, state management
├── bookings/
│   ├── data/            # DTOs, mappers, repository implementation
│   ├── domain/          # Entities, repository interface, use cases
│   └── presentation/    # ViewModels, UI screens, state management
└── navigation/          # Navigation setup and main screens
```

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material 3
- **Architecture**: Clean Architecture (Data/Domain/Presentation)
- **DI**: Hilt
- **Networking**: Retrofit + OkHttp + Kotlinx Serialization
- **Image Loading**: Coil
- **Navigation**: Navigation Compose
- **State Management**: ViewModel + StateFlow
- **Local Storage**: DataStore Preferences
- **Build System**: Gradle with Version Catalog

## ✨ Features

### 🔐 Authentication
- **Login Screen**: Email/password authentication with validation
- **Register Screen**: User registration with form validation
- **JWT Token Management**: Automatic token storage and refresh handling
- **Logout**: Secure token cleanup

### 📅 Events Management
- **Events List**: Browse all available events with pagination
- **Event Details**: Detailed view with image, description, and booking info
- **Top Booked Events**: Showcase of most popular events
- **Pull-to-refresh**: Update events data

### 📝 Booking System
- **Book Events**: One-tap event booking with confirmation
- **My Bookings**: View user's booked events
- **Error Handling**: Rate limiting, duplicate bookings, fully booked events

### 🎨 UI/UX
- **Material 3 Design**: Modern, consistent design system
- **Dark/Light Theme**: System-aware theming
- **Loading States**: Smooth loading indicators
- **Error States**: User-friendly error messages with retry options
- **Empty States**: Helpful empty state messages
- **Responsive Design**: Optimized for different screen sizes

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog | 2023.1.1 or later
- Kotlin 2.0.0+
- MinSDK 24, TargetSDK 34

### Backend Configuration

The app is configured to work with the FastAPI backend. By default:

- **Debug builds**: `http://10.0.2.2:8000/` (Android Emulator)
- **Release builds**: `https://taskassignmentbackend-latest.onrender.com/`

To change the backend URL, modify the `buildConfigField` in `app/build.gradle.kts`:

```kotlin
buildTypes {
    debug {
        buildConfigField("String", "BASE_URL", "\"YOUR_DEBUG_URL\"")
    }
    release {
        buildConfigField("String", "BASE_URL", "\"YOUR_PRODUCTION_URL\"")
    }
}
```

### Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd taskassignmentandroid
   ```

2. **Open in Android Studio**:
   - Launch Android Studio
   - Open the project directory

3. **Sync the project**:
   - Let Gradle sync and download dependencies

4. **Run the app**:
   - Connect an Android device or start an emulator
   - Click "Run" or press `Shift + F10`

### Backend Setup (Optional)

If you want to run the backend locally:

1. Navigate to the backend directory
2. Follow the backend setup instructions
3. Start the FastAPI server
4. The debug build will automatically connect to `http://10.0.2.2:8000/`

## 📱 App Flow

1. **Launch**: App checks authentication status
2. **Authentication**: Login or register if not authenticated
3. **Main Screen**: Tabbed interface with three sections:
   - **Events**: Browse all available events
   - **Top Events**: Most booked events
   - **My Bookings**: User's booked events
4. **Event Details**: Tap any event to view details and book
5. **Booking**: Confirm booking with success/error feedback

## 🏛️ Architecture Details

### Data Layer
- **Retrofit API**: RESTful API communication
- **DTOs**: Data transfer objects for network responses
- **Mappers**: Convert DTOs to domain entities
- **Repositories**: Abstract data sources behind interfaces

### Domain Layer
- **Entities**: Pure Kotlin data classes
- **Use Cases**: Business logic encapsulation
- **Repository Interfaces**: Data layer contracts

### Presentation Layer
- **ViewModels**: UI state management with StateFlow
- **UI States**: Sealed classes for different UI states
- **Screens**: Jetpack Compose UI components
- **Navigation**: Type-safe navigation with arguments

### Dependency Injection
- **Hilt Modules**: Organized by feature
- **Scoped Dependencies**: Singleton for repositories, ViewModels for screens
- **Interface Binding**: Clean abstractions

## 🔧 Key Components

### Network Layer
- **AuthInterceptor**: Automatic JWT token attachment
- **Error Handling**: Comprehensive API error mapping
- **Token Management**: Secure token storage with DataStore

### UI Components
- **AppButton**: Reusable button with loading states
- **AppTextField**: Enhanced text field with validation
- **EventCard**: Consistent event display component
- **LoadingIndicator**: Centralized loading component
- **ErrorMessage**: Consistent error display with retry

### State Management
- **UI States**: Loading/Success/Error pattern
- **StateFlow**: Reactive state updates
- **Error Handling**: Graceful error display and recovery

## 🧪 Testing

The architecture supports easy testing:

- **Unit Tests**: Test use cases and view models in isolation
- **Integration Tests**: Test repository implementations
- **UI Tests**: Test Compose screens and navigation

## 🔒 Security

- **JWT Tokens**: Secure authentication with automatic refresh
- **HTTPS**: All network communication encrypted
- **Token Storage**: Secure storage using Android DataStore
- **Input Validation**: Client-side validation for all forms

## 🎯 Performance

- **Lazy Loading**: Efficient list rendering with LazyColumn
- **Image Caching**: Coil handles image loading and caching
- **State Preservation**: Configuration changes handled automatically
- **Minimal Recomposition**: Optimized Compose performance

## 🚦 API Endpoints Used

### Authentication
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `POST /auth/refresh` - Token refresh

### Events
- `GET /events/` - Get all events (with pagination)
- `GET /events/{id}` - Get event details
- `GET /events/top-booked` - Get top booked events

### Bookings
- `POST /bookings/` - Create new booking
- `GET /bookings/my` - Get user bookings

## 🛡️ Error Handling

The app handles various error scenarios:

- **Network Errors**: Connection issues with retry options
- **Authentication Errors**: Automatic logout on token expiry
- **Validation Errors**: Form validation with helpful messages
- **Rate Limiting**: Graceful handling of rate limit responses
- **Server Errors**: User-friendly error messages

## 📦 Dependencies

### Core
- `androidx.core:core-ktx`
- `androidx.lifecycle:lifecycle-runtime-ktx`
- `androidx.activity:activity-compose`

### Compose
- `androidx.compose.ui:ui`
- `androidx.compose.material3:material3`
- `androidx.lifecycle:lifecycle-viewmodel-compose`

### Networking
- `com.squareup.retrofit2:retrofit`
- `com.squareup.okhttp3:okhttp`
- `org.jetbrains.kotlinx:kotlinx-serialization-json`

### DI & Navigation
- `com.google.dagger:hilt-android`
- `androidx.navigation:navigation-compose`
- `androidx.hilt:hilt-navigation-compose`

### Storage & Images
- `androidx.datastore:datastore-preferences`
- `io.coil-kt:coil-compose`

## 🔄 Future Enhancements

- **Offline Support**: Cache events for offline viewing
- **Push Notifications**: Event reminders and updates
- **Event Search**: Filter and search functionality
- **User Profile**: Extended user management
- **Event Creation**: Allow users to create events
- **Social Features**: Event sharing and comments

## 📄 License

This project is built for demonstration and learning purposes.

## 🤝 Contributing

This is a demonstration project showcasing modern Android development practices with Clean Architecture, Jetpack Compose, and Material 3.

---

**Built with ❤️ using Jetpack Compose and Clean Architecture**