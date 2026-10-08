package quiz_practice.controller;

import quiz_practice.model.Question;
import quiz_practice.model.QuizAttempt;
import quiz_practice.model.Student;
import quiz_practice.service.QuizService;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static QuizService quizService = new QuizService();

    public static void main(String[] args) {

        while (true) {

            showMainMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    registerStudent();
                    break;

                case 2:
                    showAdminMenu();
                    break;

                case 3:
                    showStudentMenu();
                    break;

                case 0:
                    System.out.println(
                            "Thank you for using Quiz Practice Application."
                    );
                    scanner.close();
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }


    // ================= MAIN MENU =================

    private static void showMainMenu() {

        System.out.println();
        System.out.println("=================================");
        System.out.println("       QUIZ PRACTICE APP");
        System.out.println("=================================");
        System.out.println("1. Register Student");
        System.out.println("2. Admin Menu");
        System.out.println("3. Student Menu");
        System.out.println("0. Exit");
    }


    // ================= REGISTER STUDENT =================

    private static void registerStudent() {

        System.out.println();
        System.out.println("========== STUDENT REGISTRATION ==========");

        String name = readText("Enter student name: ");

        try {

            Student student =
                    quizService.registerStudent(name);

            System.out.println(
                    "Student registered successfully."
            );

            System.out.println(
                    "Student ID: " + student.getId()
            );

        } catch (RuntimeException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }


    // ================= ADMIN MENU =================

    private static void showAdminMenu() {

        while (true) {

            System.out.println();
            System.out.println("========== ADMIN MENU ==========");
            System.out.println("1. Add Question");
            System.out.println("2. View Question Bank");
            System.out.println("3. View Attempts");
            System.out.println("4. View Reports");
            System.out.println("0. Back");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    addQuestion();
                    break;

                case 2:
                    viewQuestionBank();
                    break;

                case 3:
                    viewAllAttempts();
                    break;

                case 4:
                    viewReports();
                    break;

                case 0:
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }


    // ================= ADD QUESTION =================

    private static void addQuestion() {

        System.out.println();
        System.out.println("========== ADD QUESTION ==========");

        String text =
                readText("Enter question: ");

        String[] options = new String[4];

        for (int i = 0; i < 4; i++) {

            options[i] =
                    readText("Enter option " + (i + 1) + ": ");
        }

        int correctOption =
                readInt("Enter correct option (1-4): ");

        try {

            Question question =
                    quizService.addQuestion(
                            text,
                            options,
                            correctOption
                    );

            System.out.println();
            System.out.println(
                    "Question added successfully."
            );

            System.out.println(
                    "Question ID: " + question.getId()
            );

        } catch (RuntimeException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }


    // ================= VIEW QUESTION BANK =================

    private static void viewQuestionBank() {

        System.out.println();
        System.out.println("========== QUESTION BANK ==========");

        ArrayList<Question> questions =
                quizService.getQuestions();

        if (questions.isEmpty()) {

            System.out.println("No records found.");
            return;
        }

        for (Question question : questions) {

            System.out.println();
            System.out.println(
                    "Question ID: " + question.getId()
            );

            question.showQuestion();

            System.out.println(
                    "Correct Option: "
                            + question.getCorrectOption()
            );
        }
    }


    // ================= VIEW ALL ATTEMPTS =================

    private static void viewAllAttempts() {

        System.out.println();
        System.out.println("========== ALL ATTEMPTS ==========");

        ArrayList<QuizAttempt> attempts =
                quizService.getAttempts();

        if (attempts.isEmpty()) {

            System.out.println("No attempts yet.");
            return;
        }

        for (QuizAttempt attempt : attempts) {

            attempt.showResult();
            System.out.println("-----------------------------");
        }
    }


    // ================= VIEW REPORT =================

    private static void viewReports() {

        quizService.showReport();

        System.out.println();
        System.out.println("Report displayed successfully.");
    }


    // ================= STUDENT MENU =================

    private static void showStudentMenu() {

        System.out.println();
        System.out.println("========== STUDENT MENU ==========");

        int studentId =
                readInt("Enter student ID: ");

        Student student =
                quizService.findStudentById(studentId);

        if (student == null) {

            System.out.println(
                    "Student not found."
            );

            return;
        }

        System.out.println(
                "Welcome, " + student.getName()
        );

        while (true) {

            System.out.println();
            System.out.println("1. Take Quiz");
            System.out.println("2. View My Attempts");
            System.out.println("0. Back");

            int choice =
                    readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    takeQuiz(student);
                    break;

                case 2:
                    viewMyAttempts(student);
                    break;

                case 0:
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }


    // ================= TAKE QUIZ =================

    private static void takeQuiz(Student student) {

        ArrayList<Question> questions =
                quizService.getQuestions();

        if (questions.size() < 5) {

            System.out.println();
            System.out.println(
                    "At least 5 questions are required to take a quiz."
            );

            return;
        }

        int[] answers = new int[5];

        System.out.println();
        System.out.println("========== QUIZ ==========");

        for (int i = 0; i < 5; i++) {

            Question question =
                    questions.get(i);

            System.out.println();
            System.out.println(
                    "Question " + (i + 1)
            );

            question.showQuestion();

            answers[i] =
                    readAnswer(
                            "Enter your answer (1-4): "
                    );
        }

        try {

            QuizAttempt attempt =
                    quizService.startQuiz(
                            student,
                            answers
                    );

            System.out.println();
            System.out.println("========== RESULT ==========");

            System.out.println(
                    "Quiz completed successfully."
            );

            System.out.println(
                    "Score: "
                            + attempt.getScore()
                            + " / 5"
            );

            quizService.showCorrectAnswers(attempt);

        } catch (RuntimeException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }


    // ================= MY ATTEMPTS =================

    private static void viewMyAttempts(
            Student student) {

        System.out.println();
        System.out.println("========== MY ATTEMPTS ==========");

        ArrayList<QuizAttempt> attempts =
                quizService.getStudentAttempts(
                        student.getId()
                );

        if (attempts.isEmpty()) {

            System.out.println("No attempts yet.");
            return;
        }

        for (QuizAttempt attempt : attempts) {

            attempt.showResult();

            System.out.println(
                    "-----------------------------"
            );
        }
    }


    // ================= INTEGER INPUT =================

    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine();

            try {

                return Integer.parseInt(
                        input.trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a number."
                );
            }
        }
    }


    // ================= ANSWER INPUT =================

    private static int readAnswer(String message) {

        while (true) {

            int answer = readInt(message);

            if (answer >= 1 && answer <= 4) {
                return answer;
            }

            System.out.println(
                    "Answer must be between 1 and 4."
            );
        }
    }


    // ================= TEXT INPUT =================

    private static String readText(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            if (!input.isEmpty()) {

                return input;
            }

            System.out.println(
                    "Input cannot be empty. Please try again."
            );
        }
    }
}