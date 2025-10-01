# Health AI (CNIT35500: Software Development for Mobile Devices I)

An AI-powered Android personal health and fitness assistant that helps users track their health metrics, discover exercises, and receive personalized workout and diet recommendations using Google's Gemini AI.

## Features

### Personal Health Tracking
- **Body Metrics Logging**: Track weight changes over time
- **Food Diary**: Log meals with detailed macronutrient breakdown
- **Daily Calorie Counter**: Real-time calorie tracking with visual summaries

### Exercise Database
- **Browse by Muscle Group**: Explore 1000+ exercises organized by body parts
- **Animated Demonstrations**: View exercise GIFs for proper form
- **Detailed Instructions**: Step-by-step guides for each exercise
- **Smart Caching**: Offline access to previously viewed exercises

### AI-Powered Coaching
- **Diet Recommendations**: Personalized nutrition advice based on your eating habits and goals
- **Workout Plan Generator**: Custom workout splits tailored to your preferences and experience level
- **Fitness Q&A**: Ask questions and get evidence-based answers with in-app AI agent

### Workout Logging
- **Exercise Tracking**: Log sets, reps, and weights for each workout
- **History View**: Review past workouts and track progress
- **Performance Analytics**: Visualize your fitness journey

---

## Architecture

This app follows **Clean Architecture** principles with the **MVVM (Model-View-ViewModel)** pattern for a scalable, maintainable codebase.

### Architecture Diagram

- Architecture diagram

### Key Components

- **ViewModels**: Manage UI state and business logic
- **Repositories**: Single source of truth for data operations
- **Room Database**: Local SQLite database for offline-first functionality
- **Retrofit**: RESTful API client for network operations
- **LiveData**: Observable data holders for reactive UI updates
- **Firebase Auth**: Secure user authentication
- **Google AI Edge**: Integrates generative AI features into the app

---

## Tech Stack

### Frontend
- **Language**: Java
- **UI**: XML Layouts with Material Design 3
- **Navigation**: Navigation Component with Bottom Navigation
- **Image Loading**: Glide (for exercise GIFs)

### Backend & Data
- **Local Database**: Room (SQLite)
- **Authentication**: Firebase Authentication
- **API Client**: Retrofit + OkHttp
- **JSON Parsing**: Gson

### External APIs
- **ExerciseDB API** (RapidAPI): Exercise database with 1000+ exercises
- **Google Gemini API**: Generative AI for personalized recommendations

### Architecture & Patterns
- **MVVM Pattern**: Clean separation of concerns
- **Repository Pattern**: Data abstraction layer
- **LiveData**: Reactive programming
- **Singleton Pattern**: Database and API clients