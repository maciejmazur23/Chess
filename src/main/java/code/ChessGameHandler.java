package code;

import code.services.BoardService;
import code.services.MoveServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class ChessGameHandler implements Runnable {
    private final MoveServiceImpl moveServiceImpl;
    private ObjectOutputStream currentPlayerOutputStream;
    private ObjectInputStream currentPlayerInputStream;
    private ObjectOutputStream nextPlayerOutputStream;
    private ObjectInputStream nextPlayerInputStream;

    public ChessGameHandler(
            ObjectInputStream currentClientInputStream, ObjectOutputStream currentClientOutputStream,
            ObjectInputStream nextClientInputStream, ObjectOutputStream nextClientOutputStream
    ) {
        currentPlayerOutputStream = currentClientOutputStream;
        currentPlayerInputStream = currentClientInputStream;
        nextPlayerOutputStream = nextClientOutputStream;
        nextPlayerInputStream = nextClientInputStream;
        moveServiceImpl = new MoveServiceImpl();
    }

    @Override
    public void run() {
        boolean continueGame = true;

        try {
            String move;

            char king = choiceYourKing();
            char[][] board = BoardService.getInitializeBoard();
            printBoards(board);

            while (continueGame) {
                move = getPlayerMove();
                executeMove(board, move, king);

                isCheck(board, king);
                continueGame = isCheckmate(board, king);

                printBoards(board);
                king = changeTheOrderOfMovement(king);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                closeAllStreams();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private char choiceYourKing() throws IOException, ClassNotFoundException {
        String king;
        do {
            pingPlayer(currentPlayerOutputStream, "choiceKing");
            king = (String) currentPlayerInputStream.readObject();
        } while (!king.equals("k") && !king.equals("K"));
        return king.charAt(0);
    }

    private String getPlayerMove() throws IOException, ClassNotFoundException {
        pingPlayer(currentPlayerOutputStream, "move");
        return (String) currentPlayerInputStream.readObject();
    }

    private void executeMove(char[][] board, String move, char king) throws IOException, ClassNotFoundException {
        String error = moveServiceImpl.executeMove(board, move, king);
        while (!Objects.equals(error, "ok")) {
            pingPlayer(currentPlayerOutputStream, error);
            pingPlayer(currentPlayerOutputStream, "move");
            move = (String) currentPlayerInputStream.readObject();
            error = moveServiceImpl.executeMove(board, move, king);
        }
    }

    private void isCheck(char[][] board, char king) throws IOException {
        if (moveServiceImpl.isCheck(board, king)) pingPlayer(nextPlayerOutputStream, "Szach!");
    }

    private boolean isCheckmate(char[][] board, char king) throws IOException {
        if (moveServiceImpl.isCheckmate(board, king)) {
            pingPlayer(currentPlayerOutputStream, "Szach mat! Wygrałeś!");
            pingPlayer(nextPlayerOutputStream, "Szach mat! Przegrałeś!");
            return false;
        }
        return true;
    }

    private void closeAllStreams() throws IOException {
        currentPlayerOutputStream.close();
        currentPlayerInputStream.close();
        nextPlayerInputStream.close();
        nextPlayerOutputStream.close();
    }

    private char changeTheOrderOfMovement(char king) {
        ObjectOutputStream tempOutputStream = currentPlayerOutputStream;
        ObjectInputStream tempInputStream = currentPlayerInputStream;
        currentPlayerOutputStream = nextPlayerOutputStream;
        currentPlayerInputStream = nextPlayerInputStream;
        nextPlayerOutputStream = tempOutputStream;
        nextPlayerInputStream = tempInputStream;
        if (king == 'k') king = 'K';
        else if (king == 'K') king = 'k';
        return king;
    }

    private void printBoards(char[][] board) throws IOException {
        pingPlayer(currentPlayerOutputStream, "board");
        pingPlayer(nextPlayerOutputStream, "board");
        sendUpdatedBoard(BoardService.convertBoardToString(board));
    }

    private void pingPlayer(ObjectOutputStream outputStream, String message) throws IOException {
        outputStream.writeObject(message);
        outputStream.flush();
    }

    private void sendUpdatedBoard(String board) throws IOException {
        currentPlayerOutputStream.writeObject(board);
        currentPlayerOutputStream.flush();

        nextPlayerOutputStream.writeObject(board);
        nextPlayerOutputStream.flush();
    }

}
