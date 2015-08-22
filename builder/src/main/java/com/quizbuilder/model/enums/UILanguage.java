package com.quizbuilder.model.enums;

/**
 * Created by Mikhail Kholodkov
 *         on 24.06.2015
 */
public enum UILanguage {
    RUSSIAN("Русский"), ENGLISH("English"), UNDEFINED("Not available");

    private String name;

    UILanguage(String name) {
        this.name = name;
    }

    public static UILanguage getLanguageByName(String name) {
        for (UILanguage  language : UILanguage.values()) {
            if (language.getName().equals(name)) {
                return language;
            }
        }
        return UILanguage.UNDEFINED;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
