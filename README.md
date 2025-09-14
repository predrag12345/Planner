Planner App is a Android application built with Kotlin and Jetpack Compose, designed to help users organize their tasks, events, and daily routines. The app allows users to create new plans with start and end times, manage their schedules across weeks and days, and receive timely reminders through push notifications. It combines a clean user interface with powerful features, making it a practical tool for productivity.

The application uses Firebase Authentication for secure user login and registration, while plans are stored in Firebase Firestore, ensuring that data is persistent and available across devices. A key feature of the app is the reminder system, which leverages Android’s AlarmManager and a BroadcastReceiver to schedule notifications that alert users exactly when an event is about to begin. This ensures that important tasks are never forgotten.

From a technical perspective, Planner App follows the MVVM (Model-View-ViewModel) architecture. Business logic is encapsulated in a dedicated use case layer, while repositories manage communication with Firebase. The app integrates Hilt (Dagger Hilt) for dependency injection, ensuring modularity and testability. Navigation between screens is implemented with Navigation Compose, allowing for a smooth and declarative navigation flow. The UI is fully built in Jetpack Compose, giving the app a modern and consistent look.

Functionally, users can log in, register, and view their dashboard with upcoming plans. They can select a specific week or day to see detailed schedules, and create new plans by entering a title, description, and time range. Notifications can be toggled on or off for each plan, giving users flexibility over how they want to be reminded. The design prioritizes simplicity, making the app easy to use while still powerful enough to handle daily planning.

This project demonstrates practical use of modern Android development tools and libraries:

Kotlin as the main programming language.

Jetpack Compose for building declarative UI.

Firebase Authentication & Firestore for backend services.

AlarmManager + BroadcastReceiver for scheduling reminders and notifications.

Hilt for dependency injection.

Navigation Compose for screen navigation.
