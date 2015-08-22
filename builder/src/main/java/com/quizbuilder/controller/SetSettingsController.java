package com.quizbuilder.controller;

import com.quizbuilder.Settings;
import com.quizbuilder.events.UploadImageEventListener;
import com.quizbuilder.utils.Validation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;

import java.io.IOException;
import java.util.*;

public class SetSettingsController {

    @FXML
    private Parent root;

    @FXML
    private TextField quizTitle;

    @FXML
    private ImageView titleImage;

    @FXML
    private TextField questionsCount;

    @FXML
    private CheckBox timeLimited;

    @FXML
    private TextField timeLimit;

    @FXML
    private Label title;

    @FXML
    private Button uploadImage;

    @FXML
    private Button proceedButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Button changeButton;

    private List<ValidationSupport> validationSupportList = new ArrayList<>();

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize() {
        //Preparation
        System.out.println("Initializing Settings Page...");
        timeLimit.setDisable(true);
        changeButton.setVisible(false);

        //Actions
        uploadImage.setOnAction(new UploadImageEventListener(titleImage));
        final ValidationSupport timeLimitValidationSupport = Validation.getMultiDecoratedValidationSupport();
        timeLimited.setOnAction(event -> {
            timeLimit.setDisable(!timeLimit.isDisabled());
            if (timeLimited.isSelected()) {
                timeLimitValidationSupport.setValidationDecorator(new CompoundValidationDecoration(new GraphicValidationDecoration(), new StyleClassValidationDecoration()));
                Validator timeLimitValidator = Validation.getIntegerValidatorByRange(1, 120);
                timeLimitValidationSupport.registerValidator(timeLimit, true, timeLimitValidator);
            }
            else {
                timeLimitValidationSupport.registerValidator(timeLimit, false, Validator.createEmptyValidator(""));
            }
        });

        quizTitle.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                questionsCount.requestFocus();
            }
        });

        //Validators
        ValidationSupport quizTitleValidationSupport = Validation.getMultiDecoratedValidationSupport();
        ValidationSupport questionsCountValidationSupport = Validation.getMultiDecoratedValidationSupport();
        validationSupportList.addAll(Arrays.asList(quizTitleValidationSupport, questionsCountValidationSupport, timeLimitValidationSupport));

        Validator questionsCountValidator = Validation.getIntegerValidatorByRange(1, 99);

        quizTitleValidationSupport.registerValidator(quizTitle, true, Validator.createEmptyValidator("Заполните поле!"));
        questionsCountValidationSupport.registerValidator(questionsCount, true, questionsCountValidator);

        //Start focus
        quizTitle.requestFocus(); //todo: разобраться почему getScene даёт null
    }


    public void complete() {
        //Clear error label
        errorLabel.setVisible(false);

        //Check all validators
        for (ValidationSupport validationSupport : validationSupportList) {
            if ((validationSupport.isInvalid() && !validationSupport.getRegisteredControls().iterator().next().equals(timeLimit)) ||
                    validationSupport.isInvalid() && validationSupport.getRegisteredControls().iterator().next().equals(timeLimit) && timeLimited.isSelected()) {
                errorLabel.setText("Заполните все необходимые поля корректными данными!");
                errorLabel.setVisible(true);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        Platform.runLater(() -> {
                            errorLabel.setVisible(false);
                            timer.cancel();
                        });
                    }
                }, 7500, 99999);
                return;
            }
        }

        //Disable all controls and prepare form to proceed
        quizTitle.setDisable(true);
        questionsCount.setDisable(true);
        timeLimited.setDisable(true);
        timeLimit.setDisable(true);
        proceedButton.setText("Продолжить");
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
        quizTitle.setDisable(false);
        questionsCount.setDisable(false);
        timeLimited.setDisable(false);
        timeLimit.setDisable(!timeLimited.isSelected());
        proceedButton.setText("Готово");
        proceedButton.setStyle("-fx-font-size: 18px;");
        changeButton.setVisible(false);
        proceedButton.setOnAction(event -> complete());
    }

    public void proceed(ActionEvent actionEvent) throws IOException {
        System.out.println("Saving settings...");
        Settings settings = Settings.getInstance();
        settings.setQuestionsCount(Integer.valueOf(questionsCount.getText()));
        settings.setQuizTitle(quizTitle.getText());
        settings.setTimeLimited(timeLimited.isSelected());
        if (settings.isTimeLimited()) settings.setTimeLimit(Integer.valueOf(timeLimit.getText()));
        settings.setImage(titleImage.getImage());

        //Load next page
        System.out.println("Proceeding...");

        Parent newContent = FXMLLoader.load(getClass().getResource("/resources/fxml/quiz_constructor1.fxml"));;
        Scene newScene = new Scene(newContent);

        Stage mainWindow = (Stage)  ((Node)actionEvent.getSource()).getScene().getWindow();
        mainWindow.setScene(newScene);

    }
}


