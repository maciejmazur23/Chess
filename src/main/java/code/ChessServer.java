package code;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChessServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            while (true) {
                System.out.println("Szachy Serwer: Oczekiwanie na dwóch graczy...");

                // Oczekiwanie na połączenie pierwszego gracza
                Socket player1Socket = serverSocket.accept();
                System.out.println("Gracz 1 dołączył: " + player1Socket);
                ObjectOutputStream player1OutputStream = new ObjectOutputStream(player1Socket.getOutputStream());
                ObjectInputStream player1InputStream = new ObjectInputStream(player1Socket.getInputStream());

                // Wysłanie komunikatu oczekiwania na drugiego gracza
                sendMessageToClient(player1OutputStream, "Czekamy na drugiego gracza...");


                // Oczekiwanie na połączenie drugiego gracza
                Socket player2Socket = serverSocket.accept();
                System.out.println("Gracz 2 dołączył: " + player2Socket);
                ObjectOutputStream player2OutputStream = new ObjectOutputStream(player2Socket.getOutputStream());
                ObjectInputStream player2InputStream = new ObjectInputStream(player2Socket.getInputStream());

                // Wysłanie komunikatu o rozpoczęciu gry do obu graczy
                sendMessageToClient(player1OutputStream, "Zaczynamy gre!");
                sendMessageToClient(player2OutputStream, "Zaczynamy gre!");


                // Rozpoczęcie obsługi gry dla obu graczy
                ChessGameHandler player1Handler = new ChessGameHandler(
                        player1InputStream, player1OutputStream, player2InputStream, player2OutputStream
                );

                Thread thread = new Thread(player1Handler);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void sendMessageToClient(ObjectOutputStream outputStream, String mess) throws IOException {
        outputStream.writeObject(mess);
        outputStream.flush();
    }

}
