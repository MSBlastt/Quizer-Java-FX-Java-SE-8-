package com.quizbuilder.model.enums;

import java.io.Serializable;

/**
 * Created by Mikhail Kholodkov
 *         on 24.06.2015
 */
public enum QuestionType implements Serializable {
    SINGLE("Один правильный"), MULTI("Несколько правильных"), WRITE("Ввод текста");

    private String description;

    QuestionType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
