package ar.com.profebot.resolutor.container;

public class StepOptionInfo {

    private String optionText;
    private String equationText;
    private String justificationText;

    public StepOptionInfo(String optionText, String equationText, String justificationText) {
        this.optionText = optionText;
        this.equationText = equationText;
        this.justificationText = justificationText;
    }

    public String getOptionText() {
        return optionText;
    }

    public String getEquationText() {
        return equationText;
    }

    public String getJustificationText() {
        return justificationText;
    }
}
