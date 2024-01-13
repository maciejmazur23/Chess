package code;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChessClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Połączono z serwerem: " + socket);

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());


            // Tutaj implementuj logikę klienta (np. GUI, obsługa wejścia użytkownika itp.)
            String message;
            boolean continueGame = true;

            while (continueGame) {
                message = readMessage(inputStream);
                switch (message) {
                    case "choiceKing" -> sendResponse(outputStream, "Wybierz twojego króla: ");
                    case "move" -> sendResponse(outputStream, "Twój ruch: ");
                    case "board" -> System.out.println(readMessage(inputStream));
                    case "Szach mat! Wygrałeś!", "Szach mat! Przegrałeś!" -> {
                        System.out.println(message);
                        continueGame = false;
                    }
                    default -> System.out.println(message);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void sendResponse(ObjectOutputStream outputStream, String message) throws IOException {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        String move = scanner.nextLine();

        outputStream.writeObject(move);
        outputStream.flush();
    }

    private static String readMessage(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        return (String) inputStream.readObject();
    }

}
