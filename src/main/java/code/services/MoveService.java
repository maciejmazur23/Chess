package code.services;

import java.nio.file.DirectoryStream;

public interface MoveService {

    String executeMove(char[][] board, String move, char king);

    boolean hasLegalMove(char[][] board, int kingRow, int kingCol, DirectoryStream.Filter<Character> filter);

    boolean isCheck(char[][] board, char king);

    boolean isCheckmate(char[][] board, char king);
}
