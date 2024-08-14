package com.mycompany.clinica;

import static com.mycompany.clinica.TipoLeito.APARTAMENTO;
import static com.mycompany.clinica.TipoLeito.ENFERMARIA;

/**
 *
 * @author igor
 */
public class Internacao {
    private TipoLeito tipoLeito;
    private int qtdeDias;

    public Internacao(TipoLeito tipoLeito, int qtdeDias) {
        this.tipoLeito = tipoLeito;
        this.qtdeDias = qtdeDias;
    }

    public TipoLeito getTipoLeito() {
        return tipoLeito;
    }

    public int getQtdeDias() {
        return qtdeDias;
    }

    public float calcularValorDiarias() {
        if (tipoLeito == ENFERMARIA) {
            if (qtdeDias <= 3) {
                return 40.00f * qtdeDias;
            } else if (qtdeDias <= 8) {
                return 35.00f * qtdeDias;
            } else {
                return 30.00f * qtdeDias;
            }
        } else if (tipoLeito == APARTAMENTO) {
            if (qtdeDias <= 3) {
                return 100.00f * qtdeDias;
            } else if (qtdeDias <= 8) {
                return 90.00f * qtdeDias;
            } else {
                return 80.00f * qtdeDias;
            }
        }
        return 0.0f;
    }
}