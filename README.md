# Quiz Practice Application

A Java console-based Quiz Practice Application that allows an admin to manage a question bank and students to register, take quizzes, and view their quiz attempts.

The application demonstrates Core Java concepts such as Object-Oriented Programming, Collections, Arrays, Exception Handling, and File Handling.

---

## 📌 Project Overview

The Quiz Practice Application provides two main roles:

- **Admin**
- **Student**

### Admin can:

- Add multiple-choice questions
- View the question bank
- View all quiz attempts
- View quiz reports

### Student can:

- Register with a generated student ID
- Take a five-question quiz
- View their own quiz attempts
- See their score and correct answers

The application stores student, question, and attempt information in text files so that data is available even after restarting the application.

---

## 🚀 Features

### Student Management

- Register new students
- Automatically generate student IDs
- Load previously registered students when the application starts
- Continue ID generation after restarting the application

### Question Management

- Add multiple-choice questions
- Each question contains four options
- Select the correct option
- Automatically generate question IDs
- Prevent duplicate questions
- View the complete question bank

### Quiz

- A quiz contains five questions
- Questions are selected from the question bank in saved order
- Each question has four options
- Each correct answer gives one point
- Score is calculated out of 5
- Correct answers are displayed after completing the quiz
- Multiple quiz attempts are supported

### Attempt Management

- Generate a unique attempt ID
- Save completed attempts
- View all attempts as an admin
- Students can view only their own attempts
- Attempt history remains available after restarting the application

### Reports

The admin can view:

- Total number of students
- Total number of questions
- Total completed attempts
- Highest score
- Average score
- Best score of each student

---

# 🛠️ Technologies Used

- Java
- Object-Oriented Programming
- ArrayList
- HashSet
- Arrays
- Exception Handling
- File Handling
- BufferedReader
- BufferedWriter
- UTF-8 file I/O
- Git
- GitHub

---

# 📂 Project Structure

```text
Quiz_Practice/
│
├── src/
│   └── quiz_practice/
│       │
│       ├── controller/
│       │   └── Main.java
│       │
│       ├── model/
│       │   ├── User.java
│       │   ├── Admin.java
│       │   ├── Student.java
│       │   ├── Question.java
│       │   └── QuizAttempt.java
│       │
│       ├── service/
│       │   └── QuizService.java
│       │
│       └── repository/
│           └── FileManager.java
│
├── data/
│   ├── students.txt
│   ├── questions.txt
│   └── attempts.txt
│
├── .gitignore
└── README.md
