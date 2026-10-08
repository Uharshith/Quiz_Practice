package quiz_practice.model;

public class QuizAttempt {

    private int id;
    private int studentId;
    private int[] questionIds;
    private int[] answers;
    private int score;

    public QuizAttempt(int id, int studentId,
                       int[] questionIds, int[] answers, int score) {

        this.id = id;
        this.studentId = studentId;
        this.questionIds = questionIds.clone();
        this.answers = answers.clone();
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int[] getQuestionIds() {
        return questionIds.clone();
    }

    public int[] getAnswers() {
        return answers.clone();
    }

    public int getScore() {
        return score;
    }

    public void showResult() {
        System.out.println("Attempt ID : " + id);
        System.out.println("Student ID : " + studentId);
        System.out.println("Score      : " + score + " / 5");
    }
}