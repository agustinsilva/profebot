package com.example.profebot.parser.utils;

public enum Aceptores {
    INICIO_ST_ACEPTORES(50), // Mayor a esto son aceptores
    ST_ACEPTA_NUMERO(50),
    ST_ACEPTA_X_CON_COEF(51),
    ST_ACEPTA_X_SIN_COEF(52),
    ST_ACEPTA_IGUAL(53),
    ST_ACEPTA_PARENTESIS_IZQ(54),
    ST_ACEPTA_PARENTESIS_DER(55),
    ST_ACEPTA_MAS(56),
    ST_ACEPTA_MENOS(57),
    ST_ACEPTA_NUMERO_NEGATIVO(58),
    ST_ACEPTA_X_CON_COEF_NEGATIVO(59),
    ST_ACEPTA_X_SIN_COEF_NEGATIVO(60),
    ST_ACEPTA_PRODUCTO(61),
    ST_ACEPTA_DIVISION(62),
    ST_ACEPTA_X_CON_COEF_Y_EXP(63),
    ST_ACEPTA_X_SIN_COEF_Y_EXP(64),
    ST_ACEPTA_X_CON_COEF_NEGATIVO_Y_EXP(65),
    ST_ACEPTA_X_SIN_COEF_NEGATIVO_Y_EXP(66),
    ST_ACEPTA_POTENCIA(67),
    ST_ACEPTA_RAIZ(68),
    ST_ACEPTA_FIN(98),
    ST_EXPRESION_INVALIDA(99);

    int id;
    Aceptores(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
