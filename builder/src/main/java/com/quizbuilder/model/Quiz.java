package com.quizbuilder.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikhail Kholodkov
 *         on 24.06.2015
 */
public class Quiz implements Serializable {

    private String title;

    private byte[] startImage;

    private List<Question> questions;

    private boolean showResults;

    private String emailForResults;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getStartImage() {
        return startImage;
    }

    public void setStartImage(byte[] startImage) {
        this.startImage = startImage;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public boolean isShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }

    public String getEmailForResults() {
        return emailForResults;
    }

    public void setEmailForResults(String emailForResults) {
        this.emailForResults = emailForResults;
    }
}
