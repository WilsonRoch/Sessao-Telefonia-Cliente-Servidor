package utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import model.Sessao;

public class Util {

    public static String carregarArquivoEmHashMap(String nomeArquivo, HashMap<String, Sessao> sessoes, StringBuilder dadosSessoes) {
        try {
            Path caminho = localizarArquivo(nomeArquivo);

            // 1. Lê todas as linhas do arquivo de uma vez só (Java 8+)
            List<String> linhas;
            if (Files.isRegularFile(caminho)) {
                linhas = Files.readAllLines(caminho);
            } else {
                String recurso = nomeArquivo.replace('\\', '/');
                if (recurso.startsWith("src/utilidades/")) {
                    recurso = recurso.substring("src/utilidades/".length());
                } else if (recurso.startsWith("utilidades/")) {
                    recurso = recurso.substring("utilidades/".length());
                }
                try (InputStream entrada = Util.class.getResourceAsStream("/utilidades/" + recurso)) {
                    if (entrada == null) {
                        return "Erro ao processar o arquivo: " + caminho;
                    }
                    linhas = new java.io.BufferedReader(
                            new java.io.InputStreamReader(entrada, StandardCharsets.UTF_8))
                            .lines().toList();
                }
            }

            // 2. Alimenta o StringBuilder e o HashMap em um único fluxo
            for (String linha : linhas) {

                // Adiciona a linha ao StringBuilder (mantendo a quebra de linha original)
                dadosSessoes.append(linha).append(System.lineSeparator());

                // Remove espaços e processa se não estiver vazia
                if (!linha.trim().isEmpty()) {
                    String[] dadosLinha = linha.split(";");

                    if (dadosLinha.length >= 2) {
                        Sessao sessao = new Sessao(dadosLinha[0], dadosLinha[1]);
                        sessoes.put(sessao.getNumeroCelular(), sessao);
                    }
                }
            }
            return "";
        } catch (IOException e) {
            //System.err.println();
            return "Erro ao processar o arquivo: " + e.getMessage();
        }
    }

    private static Path localizarArquivo(String nomeArquivo) {
        Path caminhoInformado = Paths.get(nomeArquivo);
        if (Files.isRegularFile(caminhoInformado)) {
            return caminhoInformado;
        }

        Path caminhoEmRecursosDoProjeto = Paths.get("src", "utilidades").resolve(nomeArquivo);
        if (Files.isRegularFile(caminhoEmRecursosDoProjeto)) {
            return caminhoEmRecursosDoProjeto;
        }

        Path caminhoEmRecursosCompilados = Paths.get("utilidades").resolve(nomeArquivo);
        if (Files.isRegularFile(caminhoEmRecursosCompilados)) {
            return caminhoEmRecursosCompilados;
        }

        return caminhoInformado;
    }
}