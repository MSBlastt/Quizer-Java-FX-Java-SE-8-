package com.quizbuilder.controller;

import com.quizbuilder.Settings;
import com.quizbuilder.model.Answer;
import com.quizbuilder.model.Question;
import com.quizbuilder.model.Quiz;
import com.quizbuilder.model.enums.QuestionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class QuizOverviewController {

    @FXML
    private Parent root;

    @FXML
    private Label quizOverviewTitle;

    @FXML
    private Button proceedButton;

    @FXML
    private Button changeButton;

    @FXML
    private GridPane questionsOverviewGrid;

    @FXML
    private Label fileSavedTitle;

    private Quiz quiz;

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize() {
        //Preparation
        quiz = Settings.getInstance().getQuiz();
        if (fileSavedTitle == null) {
            quizOverviewTitle.setText(Settings.getInstance().getQuizTitle());
            fulfillQuestionsOverviewGrid();

            proceedButton.setDisable(Settings.getInstance().getQuestionCreationCompleted().values().
                    stream().filter(aBoolean -> !aBoolean).count() > 0);

            questionsOverviewGrid.setGridLinesVisible(true);

            //Actions

            //Validation
        }
        else {
            fileSavedTitle.setText(fileSavedTitle.getText() + Settings.getInstance().getSavePath());
        }
    }

    private void fulfillQuestionsOverviewGrid() {
        int currentRow;
        Question currentQuestion;
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            currentRow = i + 1;
            currentQuestion = quiz.getQuestions().get(i);
            questionsOverviewGrid.add(getLabelForGrid(String.valueOf(i + 1), 0), 0, currentRow);
            questionsOverviewGrid.add(getLabelForGrid(currentQuestion.getDescription(), 1), 1, currentRow);
            questionsOverviewGrid.add(getLabelForGrid(currentQuestion.getQuestionType().toString(), 2), 2, currentRow);
            questionsOverviewGrid.add(getLabelForGrid(getAnswersAmountText(currentQuestion), 3), 3, currentRow);
            questionsOverviewGrid.add(getLabelForGrid(currentQuestion.getImage() != null ? "Есть" : "Нет", 4), 4, currentRow);
            questionsOverviewGrid.add(getLabelForGrid(Settings.getInstance().getQuestionCreationCompleted().get(currentQuestion) ? "Да" : "Нет", 5), 5, currentRow);
        }
    }

    private Node getLabelForGrid(String s, int column) {
        Label label = new Label(s);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setMinHeight(30);
        label.setMaxWidth(questionsOverviewGrid.getColumnConstraints().get(column).getMaxWidth());
        if (column == 5) {
            label.setBackground(new Background(new BackgroundFill(Paint.valueOf("green"), CornerRadii.EMPTY, new Insets(1.5))));
            //label.setStyle(s.equals("Да") ? "-fx-background-color: green" : "-fx-background-color: red");
        }
        return label;
    }

    private String getAnswersAmountText(Question currentQuestion) {
        if (currentQuestion.getQuestionType().equals(QuestionType.WRITE)) {
            return "1";
        } else if (currentQuestion.getQuestionType().equals(QuestionType.SINGLE)) {
            return String.valueOf(currentQuestion.getAnswers().size());
        } else {
            return String.valueOf(currentQuestion.getAnswers().size()) + " (" +
                    currentQuestion.getAnswers().stream().filter(Answer::isCorrect).count() + ")";
        }
    }


    public void changeQuestion(int questionNumber) {

    }

    public void complete() {
        //todo check all validators
        //Disable all controls and prepare form to proceed
        questionsOverviewGrid.setDisable(true);

        proceedButton.setText("Сохранить Тест");
        proceedButton.setStyle("-fx-font-weight: bold; -fx-font-size: 18px");
        changeButton.setVisible(true);
        proceedButton.setOnAction(event -> {
            try {
                proceed(new ActionEvent(proceedButton, tail -> null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void change() {
        //Rollback form to initial state

    }

    public void proceed(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Quiz");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Quizer files", ".qdf"));
        File file = fileChooser.showSaveDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        if (file != null) {
            FileOutputStream fileOut =
                    new FileOutputStream(file + ".qdf"); //qdf = quiz data file lol
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(quiz);
            out.close();
            fileOut.close();
            Settings.getInstance().setSavePath(file.getPath());
            System.out.println("Quiz data is saved in" + file.getName() + ".qdf");
        }

        //Load next page
        System.out.println("Proceeding...");

        Parent newContent = FXMLLoader.load(getClass().getResource("/resources/fxml/bye_screen.fxml"));
        ;
        Scene newScene = new Scene(newContent);

        Stage mainWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        mainWindow.setScene(newScene);
    }
}


