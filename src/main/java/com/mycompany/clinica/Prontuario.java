package com.mycompany.clinica;

/**
 *
 * @author igor
 */
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Prontuario {
    private String nomePaciente;
    private Internacao internacao;
    private Set<Procedimento> procedimentos = new HashSet<>();

    public Prontuario(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getNomePaciente() {
        return this.nomePaciente;
    }

    public void setInternacao(Internacao internacao) {
        this.internacao = internacao;
    }

    public Internacao getInternacao() {
        return this.internacao;
    }

    public void addProcedimento(Procedimento procedimento) {
        this.procedimentos.add(procedimento);
    }

    public Set<Procedimento> getProcedimentos() {
        return this.procedimentos;
    }

    public String imprimaConta() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        float valorDiarias = internacao != null ? internacao.calcularValorDiarias() : 0.0f;
        float valorTotalProcedimentos = (float) procedimentos.stream()
            .mapToDouble(Procedimento::calcularValor)
            .sum();

        StringBuilder conta = new StringBuilder();
        conta.append("----------------------------------------------------------------------------------------------")
            .append("\nA conta do(a) paciente ").append(nomePaciente).append(" tem valor total de __ ")
            .append(formatter.format(valorDiarias + valorTotalProcedimentos)).append(" __")
            .append("\n\nConforme os detalhes abaixo:");

        if (internacao != null) {
            conta.append("\n\nValor Total Diárias:\t\t\t").append(formatter.format(valorDiarias))
                .append("\n\t\t\t\t\t").append(internacao.getQtdeDias())
                .append(" diária").append(internacao.getQtdeDias() > 1 ? "s" : "")
                .append(" em ").append(internacao.getTipoLeito() == TipoLeito.APARTAMENTO ? "apartamento" : "enfermaria");
        }

        if (!procedimentos.isEmpty()) {
            conta.append("\n\nValor Total Procedimentos:\t\t").append(formatter.format(valorTotalProcedimentos));
            Map<TipoProcedimento, Long> procedimentosAgrupados = procedimentos.stream()
                .collect(Collectors.groupingBy(Procedimento::getTipoProcedimento, Collectors.counting()));

            for (Map.Entry<TipoProcedimento, Long> entry : procedimentosAgrupados.entrySet()) {
                conta.append("\n\t\t\t\t\t").append(entry.getValue()).append(" procedimento")
                    .append(entry.getValue() > 1 ? "s" : "").append(" ")
                    .append(entry.getKey().toString().toLowerCase());
            }
        }

        conta.append("\n\nVolte sempre, a casa é sua!")
            .append("\n----------------------------------------------------------------------------------------------");

        return conta.toString();
    }

    public static Prontuario carregueProntuario(String arquivoCsv) throws IOException {
        Path path = Paths.get(arquivoCsv);
        Prontuario prontuario = new Prontuario(null);

        try (Stream<String> linhas = Files.lines(path)) {
            Iterator<String> iterator = linhas.iterator();
            if (iterator.hasNext()) {
                iterator.next(); // Pula a primeira linha (cabeçalho)
            }
            while (iterator.hasNext()) {
                String linha = iterator.next();
                String[] dados = linha.split(",");
                prontuario.processarLinhaDados(dados);
            }
        }

        return prontuario;
    }

    private void processarLinhaDados(String[] dados) {
        String nomePaciente = dados[0].trim();
        TipoLeito tipoLeito = dados[1] != null && !dados[1].trim().isEmpty() ? TipoLeito.valueOf(dados[1].trim()) : null;
        int qtdeDiasInternacao = dados[2] != null && !dados[2].trim().isEmpty() ? Integer.parseInt(dados[2].trim()) : -1;
        TipoProcedimento tipoProcedimento = dados[3] != null && !dados[3].trim().isEmpty() ? TipoProcedimento.valueOf(dados[3].trim()) : null;
        int qtdeProcedimentos = dados.length == 5 && dados[4] != null && !dados[4].trim().isEmpty() ? Integer.parseInt(dados[4].trim()) : -1;

        if (this.nomePaciente == null) {
            this.nomePaciente = nomePaciente;
        }

        if (tipoLeito != null && qtdeDiasInternacao > 0) {
            this.internacao = new Internacao(tipoLeito, qtdeDiasInternacao);
        }

        if (tipoProcedimento != null && qtdeProcedimentos > 0) {
            for (int i = 0; i < qtdeProcedimentos; i++) {
                this.addProcedimento(new Procedimento(tipoProcedimento));
            }
        }
    }

    public String salveProntuario() throws IOException {
        List<String> linhas = new ArrayList<>();
        linhas.add("nome_paciente,tipo_leito,qtde_dias_internacao,tipo_procedimento,qtde_procedimentos");

        if (internacao != null) {
            String linhaInternacao = nomePaciente + "," + internacao.getTipoLeito() + "," + internacao.getQtdeDias() + ",,";
            linhas.add(linhaInternacao);
        }

        if (!procedimentos.isEmpty()) {
            Map<TipoProcedimento, Long> procedimentosAgrupados = procedimentos.stream()
                .collect(Collectors.groupingBy(Procedimento::getTipoProcedimento, Collectors.counting()));

            for (Map.Entry<TipoProcedimento, Long> entry : procedimentosAgrupados.entrySet()) {
                String linhaProcedimento = nomePaciente + ",,," + entry.getKey() + "," + entry.getValue();
                linhas.add(linhaProcedimento);
            }
        } else {
            linhas.add(nomePaciente + ",,,,");
        }

        Path path = Paths.get(nomePaciente.replaceAll(" ", "_").concat(String.valueOf(System.currentTimeMillis())).concat(".csv"));
        Files.write(path, linhas);

        return path.toString();
    }
}
