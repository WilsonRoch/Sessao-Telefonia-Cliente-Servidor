package rede;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import model.Sessao;

public class ClienteTelefonia {

    private static final int PORTA_PADRAO = 50001;

    public static void main(String[] args) {
        String numeroCelular = args.length > 0
                ? args[0]
                : JOptionPane.showInputDialog(null, "Número de celular");
        if (numeroCelular == null || numeroCelular.isBlank()) {
            System.err.println("Número de celular não informado.");
            return;
        }

        int porta = args.length > 1 ? Integer.parseInt(args[1]) : PORTA_PADRAO;
        try (Socket cliente = new Socket("localhost", porta);
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream())) {
            saida.writeObject(numeroCelular);
            saida.flush();

            Sessao sessao = (Sessao) entrada.readObject();
            if (sessao == null) {
                System.out.println("Número de celular não encontrado.");
            } else {
                System.out.println("Sessão encontrada: " + sessao);
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.err.println("Erro ao consultar o servidor: " + e.getMessage());
        }
    }
}
