/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.clinica;

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
        return this.tipoLeito;
    }

    public int getQtdeDias() {
        return this.qtdeDias;
    }
}
