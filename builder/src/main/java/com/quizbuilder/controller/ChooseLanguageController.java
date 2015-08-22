package com.quizbuilder.controller;

import com.quizbuilder.Settings;
import com.quizbuilder.model.enums.UILanguage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ChooseLanguageController {

    @FXML
    private Parent root;

    @FXML
    private Label languageLabel;

    @FXML
    private Button russianLanguage;

    @FXML
    private Button englishLanguage;

    @FXML
    private Button proceed;

    @FXML
    public Button changeLanguage;

    public void setLanguage(ActionEvent actionEvent) {
        Settings.getInstance().setUiLanguage(UILanguage.getLanguageByName(((Button) actionEvent.getSource()).getText()));
        System.out.println("Language: " + Settings.getInstance().getUiLanguage().getName());
        updateGreetingsForm();
    }

    private void updateGreetingsForm() {
        switch (Settings.getInstance().getUiLanguage()) {
            case RUSSIAN:   languageLabel.setText("Выбран язык: Русский");
                            proceed.setText("Продолжить");
                            changeLanguage.setText("Изменить");
                            break;
            case ENGLISH:   languageLabel.setText("Chosen language: English");
                            proceed.setText("Change");
                            proceed.setText("Proceed");
                            break;
            case UNDEFINED: return;
        }
        russianLanguage.setDisable(true);
        englishLanguage.setDisable(true);
        proceed.setVisible(true);
        changeLanguage.setVisible(true);
    }

    public void proceed(ActionEvent actionEvent) throws IOException {
        System.out.println("Proceeding...");

        Parent newContent = FXMLLoader.load(getClass().getResource("/resources/fxml/set_settings.fxml"));;
        Scene newScene = new Scene(newContent);

        Stage mainWindow = (Stage)  ((Node)actionEvent.getSource()).getScene().getWindow();
        mainWindow.setScene(newScene);
    }

    public void changeLanguageHandleAction() {
        System.out.println("Changing language...");
        changeLanguage.setVisible(false);
        proceed.setVisible(false);
        russianLanguage.setDisable(false);
        englishLanguage.setDisable(false);
        languageLabel.setText("Выберите язык/Choose language");
    }

}
