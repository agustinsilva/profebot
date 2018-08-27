package ar.com.profebot.Models;

import java.util.Map;

public class MultipleChoiceStep {

    private String equationBase;
    private String newEquationBase;
    private String summary;

    // Prosa de la opción A
    private String optionA;
    // Ecuación de la opción A. Ejemplo: 1+1/2=2x+3
    private String equationOptionA;

    // Prosa de la opción B
    private String optionB;
    // Ecuación de la opción B. Ejemplo: 1+1/2=2x+3
    private String equationOptionB;

    // Prosa de la opción C
    private String optionC;
    // Ecuación de la opción C. Ejemplo: 1+1/2=2x+3
    private String equationOptionC;

    private Integer correctOption;
    private String correctOptionJustification;
    private String incorrectOptionJustification1;
    private String incorrectOptionJustification2;
    private Boolean isSolved = false;

    public MultipleChoiceStep(String equationBase, String newEquationBase, String summary, String optionA, String equationOptionA,
                              String optionB, String equationOptionB, String optionC, String equationOptionC,
                              Integer correctOption, String correctOptionJustification, String incorrectOptionJustification1,
                              String incorrectOptionJustification2) {
        this.equationBase = equationBase;
        this.newEquationBase = newEquationBase;
        this.summary = summary;
        this.optionA = optionA;
        this.equationOptionA = equationOptionA;
        this.optionB = optionB;
        this.equationOptionB = equationOptionB;
        this.optionC = optionC;
        this.equationOptionC = equationOptionC;
        this.correctOption = correctOption;
        this.correctOptionJustification = correctOptionJustification;
        this.incorrectOptionJustification1 = incorrectOptionJustification1;
        this.incorrectOptionJustification2 = incorrectOptionJustification2;
    }

    public String getEquationBase() {
        return equationBase;
    }

    public String getNewEquationBase() {
        return newEquationBase;
    }

    public String getSummary() {
        return summary;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public Integer getCorrectOption() {
        return correctOption;
    }

    public String getCorrectOptionJustification() {
        return correctOptionJustification;
    }

    public String getIncorrectOptionJustification1() {
        return incorrectOptionJustification1;
    }

    public String getIncorrectOptionJustification2() {
        return incorrectOptionJustification2;
    }

    public Boolean getSolved() {
        return isSolved;
    }

    public void setSolved(Boolean solved) {
        isSolved = solved;
    }
}
