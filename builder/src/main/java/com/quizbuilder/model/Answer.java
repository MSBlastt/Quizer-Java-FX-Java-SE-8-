package com.quizbuilder.model;

import java.io.Serializable;

/**
 * Created by Mikhail Kholodkov
 *         on 24.06.2015
 */
public class Answer implements Serializable {

    private String description;

    private boolean correct;

    public Answer() {
    }

    public Answer(String description, boolean correct) {
        this.description = description;
        this.correct = correct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
