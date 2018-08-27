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
    // Posiciones entre las que hay que colorear la ecuación del multiple choice (equationOptionA)
    // En el mapa, las keys representan posiciones de comienzo, y los values posiciones de fin de coloreado
    // Si en 1+1/2=2x+3 hay que colorear el +1/2, entonces el mapa debería tener una sola entrada,
    // con una key que sea 1, y un value que sea 4
    private Map<Integer, Integer> equationOptionAPositionsToPaint;

    // Prosa de la opción B
    private String optionB;
    // Ecuación de la opción B. Ejemplo: 1+1/2=2x+3
    private String equationOptionB;
    // Idem equationOptionAPositionsToPaint
    private Map<Integer, Integer> equationOptionBPositionsToPaint;

    // Prosa de la opción C
    private String optionC;
    // Ecuación de la opción C. Ejemplo: 1+1/2=2x+3
    private String equationOptionC;
    // Idem equationOptionAPositionsToPaint
    private Map<Integer, Integer> equationOptionCPositionsToPaint;

    private Integer correctOption;
    private String correctOptionJustification;
    private String incorrectOptionJustification1;
    private String incorrectOptionJustification2;
    private Boolean isSolved = false;

    public MultipleChoiceStep(String equationBase, String newEquationBase, String summary,
                              String optionA, String equationOptionA, Map<Integer, Integer> equationOptionAPositionsToPaint,
                              String optionB, String equationOptionB, Map<Integer, Integer> equationOptionBPositionsToPaint,
                              String optionC, String equationOptionC, Map<Integer, Integer> equationOptionCPositionsToPaint,
                              Integer correctOption, String correctOptionJustification, String incorrectOptionJustification1,
                              String incorrectOptionJustification2) {
        this.equationBase = equationBase;
        this.newEquationBase = newEquationBase;
        this.summary = summary;
        this.optionA = optionA;
        this.equationOptionA = equationOptionA;
        this.equationOptionAPositionsToPaint = equationOptionAPositionsToPaint;
        this.optionB = optionB;
        this.equationOptionB = equationOptionB;
        this.equationOptionBPositionsToPaint = equationOptionBPositionsToPaint;
        this.optionC = optionC;
        this.equationOptionC = equationOptionC;
        this.equationOptionCPositionsToPaint = equationOptionCPositionsToPaint;
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
