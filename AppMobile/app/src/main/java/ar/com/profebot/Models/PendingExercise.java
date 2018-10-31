package ar.com.profebot.Models;

import ar.com.profebot.service.ExpressionsManager;

public class PendingExercise {
    private int position;
    private String infixEquation;
    private String description;
    private String difficulty;
    private String exerciseId;

    public PendingExercise(String infixEquation) {
        this.infixEquation = infixEquation;
        String[] equation = infixEquation.split(ExpressionsManager.getRootOfEquation(infixEquation));
        this.description = ExpressionsManager.isQuadraticExpression(equation[0]) || ExpressionsManager.isQuadraticExpression(equation[1])
                ? "Ecuación cuadrática"
                : "Ecuación lineal";

        try {
            Integer.parseInt(equation[1]);
            if(equation[0].length() <= 12){
                this.difficulty = "Facil";
            }else{
                this.difficulty = "Intermedio";
            }
        }
        catch(NumberFormatException e) {
            this.difficulty = "Difícil";
        }
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public String getInfixEquation() {
        return ExpressionsManager.removeDecimals(infixEquation);
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty(){
        return this.difficulty;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        this.exerciseId = "Ejercicio " + (position  + 1);
    }
}
