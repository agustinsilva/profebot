package ar.com.profebot.Models;

public class MultipleChoiceStep {

    private String equationBase;
    private String newEquationBase;
    private String summary;
    private String optionA;
    private String optionB;
    private String optionC;
    private Integer correctOption;
    private String correctOptionJustification;
    private String incorrectOptionJustification1;
    private String incorrectOptionJustification2;

    public MultipleChoiceStep(String equationBase, String newEquationBase, String summary, String optionA, String optionB, String optionC,
                              Integer correctOption, String correctOptionJustification, String incorrectOptionJustification1,
                              String incorrectOptionJustification2) {
        this.equationBase = equationBase;
        this.newEquationBase = newEquationBase;
        this.summary = summary;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
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
}
