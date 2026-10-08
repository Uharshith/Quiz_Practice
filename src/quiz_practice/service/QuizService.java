package quiz_practice.service;

import quiz_practice.model.*;
import quiz_practice.repository.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class QuizService {

    private ArrayList<Student> students;
    private ArrayList<Question> questions;
    private ArrayList<QuizAttempt> attempts;

    private HashSet<String> questionKeys;

    private FileManager fileManager;

    private int nextStudentId = 1;
    private int nextQuestionId = 1;
    private int nextAttemptId = 1;


    // ================= CONSTRUCTOR =================

    public QuizService() {

        students = new ArrayList<>();
        questions = new ArrayList<>();
        attempts = new ArrayList<>();

        questionKeys = new HashSet<>();

        fileManager = new FileManager();

        loadData();
    }


    // ================= LOAD DATA =================

    private void loadData() {

        try {

            students = fileManager.loadStudents();

            questions = fileManager.loadQuestions();

            attempts = fileManager.loadAttempts();

            updateNextIds();

            rebuildQuestionKeys();

            System.out.println("Data loaded successfully.");

        } catch (Exception e) {

            System.out.println("Error loading data: "
                    + e.getMessage());
        }
    }


    // ================= UPDATE IDs =================

    private void updateNextIds() {

        for (Student student : students) {

            if (student.getId() >= nextStudentId) {
                nextStudentId = student.getId() + 1;
            }
        }

        for (Question question : questions) {

            if (question.getId() >= nextQuestionId) {
                nextQuestionId = question.getId() + 1;
            }
        }

        for (QuizAttempt attempt : attempts) {

            if (attempt.getId() >= nextAttemptId) {
                nextAttemptId = attempt.getId() + 1;
            }
        }
    }


    // ================= QUESTION KEYS =================

    private void rebuildQuestionKeys() {

        questionKeys.clear();

        for (Question question : questions) {

            String key = question.getText()
                    .trim()
                    .toLowerCase();

            questionKeys.add(key);
        }
    }


    // ================= STUDENTS =================

    public Student registerStudent(String name) {

        validateText(name);

        Student student =
                new Student(nextStudentId, name.trim());

        students.add(student);

        try {

            fileManager.saveStudents(students);

            nextStudentId++;

            return student;

        } catch (IOException e) {

            students.remove(student);

            throw new RuntimeException(
                    "Student could not be saved."
            );
        }
    }


    public Student findStudentById(int id) {

        for (Student student : students) {

            if (student.getId() == id) {
                return student;
            }
        }

        return null;
    }


    public ArrayList<Student> getStudents() {
        return students;
    }


    // ================= QUESTIONS =================

    public Question addQuestion(
            String text,
            String[] options,
            int correctOption) {

        validateText(text);

        if (options == null || options.length != 4) {
            throw new IllegalArgumentException(
                    "Exactly 4 options are required."
            );
        }

        for (String option : options) {
            validateText(option);
        }

        if (correctOption < 1 || correctOption > 4) {
            throw new IllegalArgumentException(
                    "Correct option must be between 1 and 4."
            );
        }

        String key = text.trim().toLowerCase();

        if (questionKeys.contains(key)) {
            throw new IllegalArgumentException(
                    "This question already exists."
            );
        }

        Question question =
                new Question(
                        nextQuestionId,
                        text.trim(),
                        options,
                        correctOption
                );

        questions.add(question);
        questionKeys.add(key);

        try {

            fileManager.saveQuestions(questions);

            nextQuestionId++;

            return question;

        } catch (IOException e) {

            questions.remove(question);
            questionKeys.remove(key);

            throw new RuntimeException(
                    "Question could not be saved."
            );
        }
    }


    public ArrayList<Question> getQuestions() {
        return questions;
    }


    // ================= QUIZ =================

    public QuizAttempt startQuiz(
            Student student,
            int[] selectedAnswers) {

        if (student == null) {
            throw new IllegalArgumentException(
                    "Student not found."
            );
        }

        if (questions.size() < 5) {
            throw new IllegalArgumentException(
                    "At least 5 questions are required."
            );
        }

        if (selectedAnswers == null
                || selectedAnswers.length != 5) {

            throw new IllegalArgumentException(
                    "Exactly 5 answers are required."
            );
        }

        int[] questionIds = new int[5];

        for (int i = 0; i < 5; i++) {

            if (selectedAnswers[i] < 1
                    || selectedAnswers[i] > 4) {

                throw new IllegalArgumentException(
                        "Answer must be between 1 and 4."
                );
            }

            questionIds[i] =
                    questions.get(i).getId();
        }

        int score =
                calculateScore(
                        questionIds,
                        selectedAnswers
                );

        QuizAttempt attempt =
                new QuizAttempt(
                        nextAttemptId,
                        student.getId(),
                        questionIds,
                        selectedAnswers,
                        score
                );

        saveAttempt(attempt);

        nextAttemptId++;

        return attempt;
    }


    // ================= SCORE =================

    public int calculateScore(
            int[] questionIds,
            int[] selectedAnswers) {

        int score = 0;

        for (int i = 0; i < 5; i++) {

            Question question =
                    findQuestionById(questionIds[i]);

            if (question == null) {
                continue;
            }

            if (question.getCorrectOption()
                    == selectedAnswers[i]) {

                score++;
            }
        }

        return score;
    }


    private Question findQuestionById(int id) {

        for (Question question : questions) {

            if (question.getId() == id) {
                return question;
            }
        }

        return null;
    }


    // ================= ATTEMPTS =================

    private void saveAttempt(QuizAttempt attempt) {

        attempts.add(attempt);

        try {

            fileManager.saveAttempts(attempts);

        } catch (IOException e) {

            attempts.remove(attempt);

            throw new RuntimeException(
                    "Attempt could not be saved."
            );
        }
    }


    public ArrayList<QuizAttempt> getAttempts() {
        return attempts;
    }


    public ArrayList<QuizAttempt> getStudentAttempts(
            int studentId) {

        ArrayList<QuizAttempt> result =
                new ArrayList<>();

        for (QuizAttempt attempt : attempts) {

            if (attempt.getStudentId() == studentId) {
                result.add(attempt);
            }
        }

        return result;
    }


    // ================= REPORT =================

    public void showReport() {

        System.out.println();
        System.out.println("========== REPORT ==========");

        System.out.println(
                "Total Students : " + students.size()
        );

        System.out.println(
                "Total Questions : " + questions.size()
        );

        System.out.println(
                "Total Attempts : " + attempts.size()
        );

        if (attempts.isEmpty()) {

            System.out.println("No attempts yet.");

            return;
        }

        int highest = 0;
        int total = 0;

        for (QuizAttempt attempt : attempts) {

            int score = attempt.getScore();

            total += score;

            if (score > highest) {
                highest = score;
            }
        }

        double average =
                (double) total / attempts.size();

        System.out.println(
                "Highest Score : " + highest + " / 5"
        );

        System.out.println(
                "Average Score : " + average
        );

        System.out.println();
        System.out.println("Student Best Scores:");

        for (Student student : students) {

            int best = -1;

            for (QuizAttempt attempt : attempts) {

                if (attempt.getStudentId()
                        == student.getId()) {

                    if (attempt.getScore() > best) {
                        best = attempt.getScore();
                    }
                }
            }

            if (best == -1) {

                System.out.println(
                        student.getName()
                                + " : No attempts"
                );

            } else {

                System.out.println(
                        student.getName()
                                + " : "
                                + best
                                + " / 5"
                );
            }
        }
    }


    // ================= CORRECT ANSWERS =================

    public void showCorrectAnswers(
            QuizAttempt attempt) {

        System.out.println();
        System.out.println("====== CORRECT ANSWERS ======");

        int[] questionIds =
                attempt.getQuestionIds();

        for (int i = 0; i < questionIds.length; i++) {

            Question question =
                    findQuestionById(questionIds[i]);

            if (question != null) {

                System.out.println(
                        (i + 1)
                                + ". "
                                + question.getCorrectOption()
                );
            }
        }
    }


    // ================= VALIDATION =================

    private void validateText(String text) {

        if (text == null || text.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Input cannot be empty."
            );
        }

        if (text.contains("|")) {

            throw new IllegalArgumentException(
                    "Input cannot contain |"
            );
        }

        if (text.contains("\n")
                || text.contains("\r")) {

            throw new IllegalArgumentException(
                    "Input cannot contain line breaks."
            );
        }
    }
}