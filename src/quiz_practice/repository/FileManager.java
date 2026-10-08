package quiz_practice.repository;

import quiz_practice.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileManager {

    private static final String DATA_FOLDER = "data";
    private static final String STUDENT_FILE = DATA_FOLDER + "/students.txt";
    private static final String QUESTION_FILE = DATA_FOLDER + "/questions.txt";
    private static final String ATTEMPT_FILE = DATA_FOLDER + "/attempts.txt";

    public FileManager() {
        new File(DATA_FOLDER).mkdirs();
    }

    // ================= STUDENTS =================

    public void saveStudents(ArrayList<Student> students) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(STUDENT_FILE),
                        StandardCharsets.UTF_8))) {

            for (Student student : students) {
                writer.write(
                        student.getId() + "|" +
                                student.getName()
                );
                writer.newLine();
            }
        }
    }

    public ArrayList<Student> loadStudents() throws IOException {

        ArrayList<Student> students = new ArrayList<>();

        File file = new File(STUDENT_FILE);

        if (!file.exists()) {
            return students;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file),
                        StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split("\\|", -1);

                int id = Integer.parseInt(data[0]);
                String name = data[1];

                students.add(new Student(id, name));
            }
        }

        return students;
    }

    // ================= QUESTIONS =================

    public void saveQuestions(ArrayList<Question> questions)
            throws IOException {

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(QUESTION_FILE),
                        StandardCharsets.UTF_8))) {

            for (Question question : questions) {

                String[] options = question.getOptions();

                writer.write(
                        question.getId() + "|" +
                                question.getText() + "|" +
                                options[0] + "|" +
                                options[1] + "|" +
                                options[2] + "|" +
                                options[3] + "|" +
                                question.getCorrectOption()
                );

                writer.newLine();
            }
        }
    }

    public ArrayList<Question> loadQuestions()
            throws IOException {

        ArrayList<Question> questions = new ArrayList<>();

        File file = new File(QUESTION_FILE);

        if (!file.exists()) {
            return questions;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file),
                        StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split("\\|", -1);

                int id = Integer.parseInt(data[0]);

                String text = data[1];

                String[] options = {
                        data[2],
                        data[3],
                        data[4],
                        data[5]
                };

                int correctOption =
                        Integer.parseInt(data[6]);

                questions.add(
                        new Question(
                                id,
                                text,
                                options,
                                correctOption
                        )
                );
            }
        }

        return questions;
    }

    // ================= ATTEMPTS =================

    public void saveAttempts(ArrayList<QuizAttempt> attempts)
            throws IOException {

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(ATTEMPT_FILE),
                        StandardCharsets.UTF_8))) {

            for (QuizAttempt attempt : attempts) {

                writer.write(
                        attempt.getId() + "|" +
                                attempt.getStudentId() + "|" +
                                arrayToString(attempt.getQuestionIds()) + "|" +
                                arrayToString(attempt.getAnswers()) + "|" +
                                attempt.getScore()
                );

                writer.newLine();
            }
        }
    }

    public ArrayList<QuizAttempt> loadAttempts()
            throws IOException {

        ArrayList<QuizAttempt> attempts = new ArrayList<>();

        File file = new File(ATTEMPT_FILE);

        if (!file.exists()) {
            return attempts;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file),
                        StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split("\\|", -1);

                int id = Integer.parseInt(data[0]);

                int studentId =
                        Integer.parseInt(data[1]);

                int[] questionIds =
                        stringToArray(data[2]);

                int[] answers =
                        stringToArray(data[3]);

                int score =
                        Integer.parseInt(data[4]);

                attempts.add(
                        new QuizAttempt(
                                id,
                                studentId,
                                questionIds,
                                answers,
                                score
                        )
                );
            }
        }

        return attempts;
    }

    // ================= ARRAY METHODS =================

    private String arrayToString(int[] array) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < array.length; i++) {

            if (i > 0) {
                result.append(",");
            }

            result.append(array[i]);
        }

        return result.toString();
    }

    private int[] stringToArray(String text) {

        String[] values = text.split(",");

        int[] array = new int[values.length];

        for (int i = 0; i < values.length; i++) {
            array[i] = Integer.parseInt(values[i]);
        }

        return array;
    }
}