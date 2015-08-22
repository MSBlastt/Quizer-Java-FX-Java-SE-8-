package com.quizbuilder;

import com.quizbuilder.model.Question;
import com.quizbuilder.model.Quiz;
import com.quizbuilder.model.enums.UILanguage;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mikhail Kholodkov
 *         on 24.06.2015
 */
public class Settings {

    private static Settings instance = new Settings();

    public static Settings getInstance() {
        return instance;
    }

    private Settings() {
        quiz = new Quiz();
        questionCreationCompleted = new HashMap<>();
    }

    private String quizTitle;

    private int questionsCount;

    private boolean timeLimited;

    private int timeLimit;

    private Image image;

    private UILanguage uiLanguage;

    private final Quiz quiz;

    private final Map<Question, Boolean> questionCreationCompleted;

    private String savePath;

    public UILanguage getUiLanguage() {
        return uiLanguage;
    }

    public void setUiLanguage(UILanguage uiLanguage) {
        this.uiLanguage = uiLanguage;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public int getQuestionsCount() {
        return questionsCount;
    }

    public void setQuestionsCount(int questionsCount) {
        this.questionsCount = questionsCount;
    }

    public boolean isTimeLimited() {
        return timeLimited;
    }

    public void setTimeLimited(boolean timeLimited) {
        this.timeLimited = timeLimited;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Map<Question, Boolean> getQuestionCreationCompleted() {
        return questionCreationCompleted;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}
