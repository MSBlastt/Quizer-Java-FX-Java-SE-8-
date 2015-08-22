package com.quizbuilder.model;

import com.quizbuilder.model.enums.QuestionType;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikhail Kholodkov
 *         on 24.06.2015
 */
public class Question implements Serializable {

    private String description;

    private Image image;

    private int time;

    private QuestionType questionType;

    private List<Answer> answers;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
