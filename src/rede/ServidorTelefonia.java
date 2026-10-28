package rede;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import model.Sessao;
import model.TabelaHash;
import utilidades.Util;

public class ServidorTelefonia {

    private static final int PORTA_PADRAO = 50001;
    private static final String ARQUIVO_PADRAO = "telephony_sessions.txt";

    public static void main(String[] args) {
        int porta = args.length > 0 ? Integer.parseInt(args[0]) : PORTA_PADRAO;
        String nomeArquivo = args.length > 1 ? args[1] : ARQUIVO_PADRAO;
        TabelaHash tabela = carregarSessoes(nomeArquivo);

        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("Servidor de telefonia ouvindo a porta " + porta);
            while (true) {
                try (Socket cliente = servidor.accept();
                        ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
                        ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream())) {
                    String numeroCelular = (String) entrada.readObject();
                    Sessao sessao = tabela.sessoes.get(numeroCelular);
                    saida.writeObject(sessao);
                    saida.flush();
                    System.out.println("Consulta recebida: " + numeroCelular
                            + (sessao == null ? " (não encontrada)" : " (encontrada)"));
                } catch (ClassNotFoundException | IOException | ClassCastException e) {
                    System.err.println("Erro ao atender cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Não foi possível iniciar o servidor: " + e.getMessage());
        }
    }

    private static TabelaHash carregarSessoes(String nomeArquivo) {
        TabelaHash tabela = new TabelaHash();
        StringBuilder dadosSessoes = new StringBuilder();
        String erro = Util.carregarArquivoEmHashMap(nomeArquivo, tabela.sessoes, dadosSessoes);
        if (!erro.isEmpty()) {
            throw new IllegalStateException(erro);
        }
        System.out.println("Sessões carregadas: " + tabela.sessoes.size());
        return tabela;
    }
}
