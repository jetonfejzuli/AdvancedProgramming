package Lab8.Task2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum QuestionType {
    TRUEFALSE,
    FREEFORM
}


class TriviaQuestion {

    public String question;        // Actual question
    public String answer;        // Answer to question
    public int value;            // Point value of question
    public QuestionType type;            // Question type, TRUEFALSE or FREEFORM

    public TriviaQuestion(String question, String answer, int value, QuestionType type) {
        this.question = question;
        this.answer = answer;
        this.value = value;
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getValue() {
        return value;
    }

    public QuestionType getType() {
        return type;
    }

    public boolean isCorrectAnswer(String userAnswer) {
        if (type == QuestionType.TRUEFALSE) {
            return !userAnswer.isEmpty() &&
                    userAnswer.charAt(0) == answer.charAt(0);
        } else {
            return userAnswer.equalsIgnoreCase(answer);
        }
    }

    public void display(int questionNumber) {
        System.out.println("Question " + questionNumber + ".  " + value + " points.");
        System.out.println(question);
        if (type == QuestionType.TRUEFALSE) {
            System.out.println("Enter 'T' for true or 'F' for false.");
        }
    }
}

class TriviaData {

    private List<TriviaQuestion> questions;

    public TriviaData() {
        this.questions = new ArrayList<>();
    }

    public void addQuestion(String question, String answer, int value, QuestionType type) {
        questions.add(new TriviaQuestion(question, answer, value, type));
    }

    public int getQuestionCount() {
        return questions.size();
    }

    public TriviaQuestion getQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            return questions.get(index);
        }
        return null;
    }
}

class GameEngine {
    private TriviaData triviaData;
    private int score;
    private Scanner scanner;

    public GameEngine(TriviaData triviaData) {
        this.triviaData = triviaData;
        this.score = 0;
        this.scanner = new Scanner(System.in);
    }

    public void play() {
        int questionNumber = 1;

        while (questionNumber <= triviaData.getQuestionCount()) {
            TriviaQuestion question = triviaData.getQuestion(questionNumber - 1);

            if (question == null) break;

            question.display(questionNumber);
            String userAnswer = scanner.nextLine().trim();

            processAnswer(question, userAnswer);
            displayScore();

            questionNumber++;
        }

        endGame();
    }

    private void processAnswer(TriviaQuestion question, String userAnswer) {
        if (question.isCorrectAnswer(userAnswer)) {
            System.out.println("That is correct!  You get " + question.getValue() + " points.");
            score += question.getValue();
        } else {
            System.out.println("Wrong, the correct answer is " + question.getAnswer());
        }
    }

    private void displayScore() {
        System.out.println("Your score is " + score);
    }

    private void endGame() {
        System.out.println("Game over!  Thanks for playing!");
        scanner.close();
    }
}

public class TriviaGame {

    public static TriviaData loadQuestions() {
        TriviaData data = new TriviaData();

        data.addQuestion(
                "The possession of more than two sets of chromosomes is termed?",
                "polyploidy", 3, QuestionType.FREEFORM
        );
        data.addQuestion(
                "Erling Kagge skiied into the north pole alone on January 7, 1993.",
                "F", 1, QuestionType.TRUEFALSE
        );
        data.addQuestion(
                "1997 British band that produced 'Tub Thumper'",
                "Chumbawumba", 2, QuestionType.FREEFORM
        );
        data.addQuestion(
                "I am the geometric figure most like a lost parrot",
                "polygon", 2, QuestionType.FREEFORM
        );
        data.addQuestion(
                "Generics were introducted to Java starting at version 5.0.",
                "T", 1, QuestionType.TRUEFALSE
        );

        return data;
    }
    // Main game loop

    public static void main(String[] args) {
        TriviaData questions = loadQuestions();
        GameEngine game = new GameEngine(questions);
        game.play();
    }
}
