package com.mycompany.clinica;

/**
 *
 * @author igor
 */
public class Procedimento {
    private TipoProcedimento tipoProcedimento;

    public Procedimento(TipoProcedimento tipoProcedimento) {
        this.tipoProcedimento = tipoProcedimento;
    }

    public TipoProcedimento getTipoProcedimento() {
        return tipoProcedimento;
    }

    public float calcularValor() {
        switch (tipoProcedimento) {
            case BASICO:
                return 50.00f;
            case COMUM:
                return 150.00f;
            case AVANCADO:
                return 500.00f;
            default:
                return 0.0f;
        }
    }
}

