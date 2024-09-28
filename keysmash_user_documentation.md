
# Keysmash User Documentation

## Table of Contents
- [Overview](#overview)
- [Installation](#installation)
- [Getting Started](#getting-started)
- [Features](#features)
- [Database Management](#database-management)
- [Using the Application](#using-the-application)


## Overview
Keysmash is a typing test application that allows users to improve / measure their typing speed and accuracy through typing. The application tracks user profiles, scores, and displays leaderboards based on performance.

## Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/keysmash.git
   ```

2. **Navigate to the project directory**:
   ```bash
   cd keysmash
   ```

3. **Set up MySQL Database**:
   - Create a new database named `keysmash_db`.
   - Create a user `newuser` with password `password` and grant necessary privileges to the `keysmash_db`.

4. **Build the project**:
   - Use Maven to build the project:
   ```bash
   mvn clean install
   ```

5. **Run the application**:
   - Execute the `Main` class

## Getting Started
- Upon launching the application, you will see the main window where you can see three buttons. That being:
  - **Start**: Click this button to create a new user profile, and after starting the test, you can view your scores.
  - **Leaderboard**: Click this button to view the top performers.
  - **Help**: Click this button to view whats the application about.
  - **Exit**: Click this button to close the application. 

## Features
- **Typing Test**: Tests users' typing speed and accuracy, where we check how fast and accurately the user can type. User can see if he hit the character correctly or not. And also correct the character if he made a mistake.
- **Score Tracking**: Scores are recorded for each test, including typing speed (WPM) and accuracy (error percentage).
- **Leaderboards**: View the top performers based on typing speed and accuracy.

## Database Management
- The application uses a MySQL database to store profiles, texts, scores, and leaderboard information.
- Database tables include:
  - `profiles`: Stores user information.
  - `texts`: Contains the text samples for typing tests. **Note**: Texts are not being used in the current version of the application.
  - `scores`: Records user scores for each typing test.
  - `leaderboards`: Keeps track of the top scores. **Note**: Leaderboards are not being used in the current version of the application.

## Using the Application
1. **Creating a Profile**:
   - Enter a username to create a new profile. Ensure the username is unique.

2. **Starting a Typing Test**:
   - Click the `Start` button to begin a typing test.
   - Your performance will be evaluated in real-time.

3. **Viewing Scores**:
   - After completing a test, your speed and accuracy will be displayed.
   - You can view the leaderboard or try again.

4. **Leaderboards**:
   - Access the leaderboard to see how your scores compare to other users.


---

*For the code documentation you can check the generated javadoc of the code.*
