package com.quizbuilder.utils;

import javafx.scene.control.Control;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;

/**
 * Created by Mikhail Kholodkov
 * on 25.06.2015
 */
public class Validation {

    public static ValidationSupport getMultiDecoratedValidationSupport() {
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.setValidationDecorator(new CompoundValidationDecoration(
                new GraphicValidationDecoration(), new StyleClassValidationDecoration()));
        return validationSupport;

    }

    /**Value should be not less than @start and not more than @end, inclusive*/
    public static Validator getIntegerValidatorByRange(int start, int end) {
        return new Validator<String>() {
            @Override
            public ValidationResult apply(Control control, String s) {
                return ValidationResult.fromErrorIf(control, "Введите число от " + start + " до " + end + "!",
                        (!s.matches("\\d+") || Integer.valueOf(s) < start || Integer.valueOf(s) > end));
            }
        };
    }

}
