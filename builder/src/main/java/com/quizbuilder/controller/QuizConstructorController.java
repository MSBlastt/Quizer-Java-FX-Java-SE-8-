package com.quizbuilder.controller;

import com.quizbuilder.Settings;
import com.quizbuilder.events.UploadImageEventListener;
import com.quizbuilder.model.Answer;
import com.quizbuilder.model.Question;
import com.quizbuilder.model.Quiz;
import com.quizbuilder.model.enums.QuestionType;
import com.quizbuilder.utils.Validation;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class QuizConstructorController {

    @FXML
    private Parent root;

    @FXML
    private Button addRowButton;

    @FXML
    private GridPane answersGrid;

    @FXML
    private Label answersAmountLabel;

    @FXML
    private Button uploadImageButton;

    @FXML
    private ImageView image;

    @FXML
    private TextField answersAmount;

    @FXML
    private Pane answersPane;

    @FXML
    private ChoiceBox answerType;

    @FXML
    private TextArea questionDescription;

    @FXML
    private TextField totalQuestions;

    @FXML
    private TextField currentQuestionNumber;

    @FXML
    private Button proceedButton;

    @FXML
    private Button changeButton;

    @FXML
    private Pagination paginator;

    private Quiz quiz;

    private Question currentQuestion;

    private List<Node> inputableNodes = new ArrayList<>();
    private List<Node> answersGridNodes = new ArrayList<>();
    private List<ValidationSupport> validationSupportList = new ArrayList<>();
    private ValidationSupport answersAmountValidationSupport;
    private QuestionType currentQuestionType;

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize() {
        //Preparation
        quiz = Settings.getInstance().getQuiz();
        quiz.setQuestions(new LinkedList<>());
        createNewQuestion();
        totalQuestions.setText(String.valueOf(Settings.getInstance().getQuestionsCount()));
        currentQuestionNumber.setText(String.valueOf(quiz.getQuestions().indexOf(currentQuestion) + 1));
        paginator.setPageCount(Settings.getInstance().getQuestionsCount());
        paginator.setMaxPageIndicatorCount(Settings.getInstance().getQuestionsCount());
        inputableNodes.addAll(Arrays.asList(answersAmount, questionDescription, answerType, answersGrid, uploadImageButton));

        //Actions
        uploadImageButton.setOnAction(new UploadImageEventListener(image));
        ObservableListWrapper<?> list = new ObservableListWrapper<>(Arrays.asList(QuestionType.values()));
        answerType.setItems(list);
        answerType.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() != -1) {
                if (answersGridIsEmpty() || (!answersGridIsEmpty() && showGridResetConfirmationDialog())) {
                    currentQuestionType = (QuestionType) answerType.getItems().get(newValue.intValue());
                    answersAmount.setText("");
                    answersAmountValidationSupport.redecorate();
                    buildAnswersGrid(currentQuestionType);
                }
            }
        });

        paginator.setPageFactory(param -> {
            //Change question
            questionDescription.setText("current page is " + param);
            TextField textField = new TextField(); //todo <-- remove this shit
            textField.setVisible(false);
            return textField;
        });

        addRowButton.setOnAction(event -> {
            int newRow = answersGrid.getChildren().size() / 3;
            if (newRow != 7) {
                answersAmount.setText(String.valueOf(newRow + 1));
                buildAnswersGridControls(currentQuestionType, newRow);
            }
        });

        //Validation
        answersAmountValidationSupport = Validation.getMultiDecoratedValidationSupport();
        validationSupportList.add(answersAmountValidationSupport);
        Validator answersAmountValidator = Validation.getIntegerValidatorByRange(1, 7);
        answersAmountValidationSupport.registerValidator(answersAmount, true, answersAmountValidator);

        answersAmount.addEventFilter(KeyEvent.KEY_TYPED, keyEvent ->

                {
                    if (!"1234567".contains(keyEvent.getCharacter())) {
                        keyEvent.consume();
                    }
                }

        );

    }

    private void buildAnswersGrid(QuestionType questionType) {
        resetForm();
        resetAnswersGrid();

        //Only one correct answer
        if (questionType.equals(QuestionType.SINGLE) || questionType.equals(QuestionType.MULTI)) {
            answersAmount.setVisible(true);
            answersAmountLabel.setVisible(true);
            addRowButton.setVisible(true);

            answersAmount.setOnKeyReleased(event -> {
                if (answersGridIsEmpty() || (!answersGridIsEmpty() && showGridResetConfirmationDialog())) {
                    resetAnswersGrid();
                    if (!answersAmountValidationSupport.isInvalid()) {
                        buildAnswersGridControls(questionType, 0);
                    }
                }
            });

        }

        //Correct answer should be written
        if (questionType.equals(QuestionType.WRITE)) {
            TextArea textFieldForSingleAnswer = new TextArea();
            answersGrid.add(textFieldForSingleAnswer, 0, 0);
            answersGridNodes.add(textFieldForSingleAnswer);

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setHgrow(Priority.ALWAYS);
            answersGrid.getColumnConstraints().add(col1);

            answersGrid.setVisible(true);
        }
    }

    private void buildAnswersGridControls(QuestionType questionType, int startRow) {

        int rowsCount = Integer.valueOf(answersAmount.getText());

        for (int i = startRow; i < rowsCount; i++) {
            Node node;
            if (questionType.equals(QuestionType.MULTI)) {
                node = new CheckBox();
                ((CheckBox) node).setSelected(false);
            } else {
                node = new RadioButton();
                if (i == 0) {
                    ((RadioButton) node).setSelected(true);
                }
                ((RadioButton) node).setOnAction(event1 -> changeSingleCorrectAnswer((RadioButton) node));
            }

            TextField textFieldForSingleAnswer = new TextField();

            Button deleteRowButton = new Button("X");
            deleteRowButton.setMaxWidth(36);
            deleteRowButton.setOnAction(event -> {
                answersGrid.getChildren().clear();
                answersGrid.getRowConstraints().clear();
                answersGridNodes.removeAll(Arrays.asList(node, textFieldForSingleAnswer, deleteRowButton));
                int iter = 0;
                for (int j = 0; j < answersGridNodes.size() / 3; j++) {
                    answersGrid.add(answersGridNodes.get(iter++), 0, j);
                    answersGrid.add(answersGridNodes.get(iter++), 1, j);
                    answersGrid.add(answersGridNodes.get(iter++), 2, j);
                }
                answersGrid.getChildren().addAll();
                int rows = answersGrid.getChildren().size() / 3;
                answersAmount.setText(String.valueOf(rows));
                rebuildRowConstraints(rows);
            });

            answersGrid.add(node, 0, i);
            answersGrid.add(textFieldForSingleAnswer, 1, i);
            answersGrid.add(deleteRowButton, 2, i);

            answersGridNodes.add(node);
            answersGridNodes.add(textFieldForSingleAnswer);
            answersGridNodes.add(deleteRowButton);
        }

        rebuildRowConstraints(rowsCount);
        rebuildColumnConstraints();
    }

    private void rebuildRowConstraints(int rowsCount) {
        answersGrid.getRowConstraints().clear();
        for (int i = 0; i < rowsCount; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100 / rowsCount);
            answersGrid.getRowConstraints().add(rowConstraints);
        }
    }


    private void rebuildColumnConstraints() {
        if (answersGrid.getColumnConstraints().size() == 0) {
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(8);
            col1.setHalignment(HPos.CENTER);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(86);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPercentWidth(6);
            col3.setHalignment(HPos.CENTER);
            answersGrid.getColumnConstraints().addAll(col1, col2, col3);
        }
    }


    private boolean showGridResetConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтвердите действие");
        alert.setHeaderText("В строках для ответа введен текст.");
        alert.setContentText("Вы уверены что хотите перестроить таблицу, при этом потеряв всё содержимое?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    //No filled TextFields or TextAreas
    private boolean answersGridIsEmpty() {
        return answersGridNodes.stream().filter(node ->
                (node.getClass().equals(TextField.class))).filter(node1 -> !((TextField) node1).getText().equals("")).
                map(node -> (TextField) node).collect(Collectors.toList()).size() == 0 && (answersGridNodes.stream().filter(node ->
                (node.getClass().equals(TextArea.class))).filter(node1 -> !((TextArea) node1).getText().equals("")).
                map(node -> (TextArea) node).collect(Collectors.toList()).size() == 0);

    }

    private void changeSingleCorrectAnswer(RadioButton radioButton) {
        List<RadioButton> radioButtons =
                answersGridNodes.stream().filter(node -> node.getClass().equals(RadioButton.class)).
                        map(node -> (RadioButton) node).collect(Collectors.toList());
        radioButtons.forEach(radioButton1 -> radioButton1.setSelected(false));
        radioButton.setSelected(true);
    }

    private void resetForm() {
        answersAmount.setVisible(false);
        answersAmountLabel.setVisible(false);
        addRowButton.setVisible(false);
    }


    private void resetAnswersGrid() {
        answersGridNodes.forEach(answersGrid.getChildren()::remove);
        answersGridNodes.clear();
        answersGrid.getColumnConstraints().clear();
        answersGrid.getRowConstraints().clear();
    }

    private void resetQuestionInput() {
        answersAmount.setText("");
        questionDescription.setText("");
        image.setImage(null);
        answerType.setValue(null);
    }

    private void createNewQuestion() {
        currentQuestion = new Question();
    }


    public Validator getCorrectAnswersAmountValidator(final int rowsCount) {
        return new Validator<String>() {
            @Override
            public ValidationResult apply(Control control, String s) {
                return ValidationResult.fromErrorIf(control, "Введите число от 1 до " + (rowsCount - 1) + "!",
                        (!s.matches("\\d+") || Integer.valueOf(s) < 1 || Integer.valueOf(s) > (rowsCount - 1)));
            }
        };
    }


    public void changeQuestion(int questionNumber) {

    }

    public void complete() {
        //todo check all validators
        //Disable all controls and prepare form to proceed
        changeInputtableNodesReadOnlyStatus(true);

        repaintAnswersGrid(true);

        proceedButton.setText(!isCurrentQuestionFinal() ? "Продолжить" : "Закончить");
        proceedButton.setStyle("-fx-font-weight: bold; -fx-font-size: 18px");
        changeButton.setVisible(true);
        proceedButton.setOnAction(event -> {
            try {
                proceed(new ActionEvent(proceedButton, tail -> null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        completeCurrentQuestionCreation(true);

    }

    private void repaintAnswersGrid(boolean finishedCreation) {
        //todo abstract with proceed method
        if (!answersGrid.getChildren().isEmpty()) {
            if (currentQuestionType.equals(QuestionType.WRITE)) {
                if (!finishedCreation) {
                    answersGrid.getChildren().iterator().next().setStyle("");
                } else {
                    answersGrid.getChildren().iterator().next().setStyle("-fx-control-inner-background: green");
                }
            } else {
                answersGrid.getChildren().stream().filter(node -> node.getClass().equals(TextField.class)).forEach(node -> {
                    if (currentQuestionType.equals(QuestionType.SINGLE)) {
                        node.setStyle(finishedCreation ? ((RadioButton) answersGrid.getChildren().stream().filter(node1 -> GridPane.getColumnIndex(node1) == 0 && Objects.equals(GridPane.getRowIndex(node1),
                                GridPane.getRowIndex(node))).findFirst().get()).isSelected() ? "-fx-control-inner-background: green" : "-fx-control-inner-background: red" : "");
                    } else {
                        node.setStyle(finishedCreation ? ((CheckBox) answersGrid.getChildren().stream().filter(node1 -> GridPane.getColumnIndex(node1) == 0 && Objects.equals(GridPane.getRowIndex(node1),
                                GridPane.getRowIndex(node))).findFirst().get()).isSelected() ? "-fx-control-inner-background: green" : "-fx-control-inner-background: red" : "");
                    }
                });
            }
        }
    }

    private void completeCurrentQuestionCreation(boolean completed) {
        Settings.getInstance().getQuestionCreationCompleted().put(currentQuestion, completed);
    }

    public void change() {
        //Rollback form to initial state
        repaintAnswersGrid(false);
        changeInputtableNodesReadOnlyStatus(false);

        proceedButton.setText("Готово");
        proceedButton.setStyle("-fx-font-size: 18px;");
        changeButton.setVisible(false);
        proceedButton.setOnAction(event -> complete());

        completeCurrentQuestionCreation(false);
    }

    public void proceed(ActionEvent actionEvent) throws IOException {
        //Save all data to current question
        currentQuestion.setDescription(questionDescription.getText());
        currentQuestion.setImage(image.getImage());
        currentQuestion.setQuestionType(currentQuestionType);

        if (currentQuestionType.equals(QuestionType.WRITE)) {
            Answer answer = new Answer();
            answer.setDescription(((TextArea) answersGrid.getChildren().iterator().next()).getText());
            answer.setCorrect(true);
            currentQuestion.setAnswers(Collections.singletonList(answer));
        } else {
            List<Answer> answers = new ArrayList<>();
            answersGrid.getChildren().stream().filter(node -> node.getClass().equals(TextField.class)).forEach(node -> {
                Answer answer = new Answer();
                answer.setDescription(((TextField) node).getText());
                if (currentQuestionType.equals(QuestionType.SINGLE)) {
                    answer.setCorrect(((RadioButton) answersGrid.getChildren().stream().filter(node1 -> GridPane.getColumnIndex(node1) == 0 && Objects.equals(GridPane.getRowIndex(node1),
                            GridPane.getRowIndex(node))).findFirst().get()).isSelected());
                } else {
                    answer.setCorrect(((CheckBox) answersGrid.getChildren().stream().filter(node1 -> GridPane.getColumnIndex(node1) == 0 && Objects.equals(GridPane.getRowIndex(node1),
                            GridPane.getRowIndex(node))).findFirst().get()).isSelected());
                }
                answers.add(answer);
            });
            currentQuestion.setAnswers(answers);
        }
        quiz.getQuestions().add(currentQuestion);
        System.out.println("Saving answer number " + (paginator.getCurrentPageIndex() + 1) + "...");
        if (!isCurrentQuestionFinal()) {
            System.out.println("Proceeding to answer number " + (paginator.getCurrentPageIndex() + 2) + "...");
            loadNextQuestion();
        } else {
            try {
                loadOverviewSection(new ActionEvent(proceedButton, tail -> null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadOverviewSection(ActionEvent actionEvent) throws IOException {
        //todo impl overview

        //Load next page
        System.out.println("Proceeding...");

        Parent newContent = FXMLLoader.load(getClass().getResource("/resources/fxml/quiz_overview.fxml"));
        Scene newScene = new Scene(newContent);

        Stage mainWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        mainWindow.setScene(newScene);
    }

    private boolean isCurrentQuestionFinal() {
        return quiz.getQuestions().size() == Settings.getInstance().getQuestionsCount();
    }

    private void loadNextQuestion() {
        resetQuestionInput();
        resetAnswersGrid();
        resetForm();
        change();
        completeCurrentQuestionCreation(true);
        paginator.setCurrentPageIndex(paginator.getCurrentPageIndex() + 1);
        createNewQuestion();
    }


    private void changeInputtableNodesReadOnlyStatus(boolean readOnly) {
        inputableNodes.forEach(node -> node.setDisable(readOnly));
    }

    private void changeInputtableNodes(boolean readOnly) {
        inputableNodes.forEach(node -> node.setDisable(readOnly));
    }
}


