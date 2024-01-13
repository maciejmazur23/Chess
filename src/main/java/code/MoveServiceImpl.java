package code;

import java.io.IOException;
import java.nio.file.DirectoryStream;

public class MoveServiceImpl implements MoveService {

    @Override
    public String executeMove(char[][] board, String move, char king) {
        String trimMove = move.replaceAll(" ", "");
        if (trimMove.length() != 4) {
            return "Niepoprawny ruch!";
        }

        try {
            int fromRow = Character.getNumericValue(trimMove.charAt(1)) - 1;
            int fromCol = trimMove.charAt(0) - 'a';
            int toRow = Character.getNumericValue(trimMove.charAt(3)) - 1;
            int toCol = trimMove.charAt(2) - 'a';

            char piece = board[fromRow][fromCol];

            if (piece == ' ') return "Nie można ruszyć się pustym polem!";
            else if (!((Character.isLowerCase(piece) && Character.isLowerCase(king))
                    || (Character.isUpperCase(piece) && Character.isUpperCase(king)))
            ) return "Nie można ruszyć się figurą przeciwnika!";
            else if (!isValidMoveForPiece(board, piece, fromRow, fromCol, toRow, toCol))
                return "Niepoprawny ruch dla figury!";

            board[fromRow][fromCol] = ' ';
            board[toRow][toCol] = piece;
        } catch (Exception e) {
            return "Niepoprawny ruch!";
        }

        return "ok";
    }


    @Override
    public boolean hasLegalMove(char[][] board, int kingRow, int kingCol, DirectoryStream.Filter<Character> filter) {
        char king = board[kingRow][kingCol];
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = kingRow + i;
                int newCol = kingCol + j;

                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    char targetPiece = board[newRow][newCol];

                    if (targetPiece == ' ') {
                        board[kingRow][kingCol] = ' ';

                        try {
                            if (!isFieldAttacked(board, newRow, newCol, filter)) return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        board[kingRow][kingCol] = king;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isCheck(char[][] board, char king) {
        DirectoryStream.Filter<Character> upperFilter = Character::isUpperCase;
        DirectoryStream.Filter<Character> lowerFilter = Character::isLowerCase;

        boolean isSmallKingAttacked = false;
        boolean isLargeKingAttacked = false;
        try {
            if (king == 'k') isSmallKingAttacked = isKingAttacked(board, 'k', lowerFilter, upperFilter);
            if (king == 'K') isLargeKingAttacked = isKingAttacked(board, 'K', upperFilter, lowerFilter);
            return isSmallKingAttacked || isLargeKingAttacked;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isCheckmate(char[][] board, char king) {
        boolean isCheckmateSmall = false;
        boolean isCheckmateLarge = false;
        try {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] == 'k' && king == 'k') {
                        isCheckmateSmall = isCheck(board, king) && !hasLegalMove(board, i, j, Character::isUpperCase);
                    } else if (board[i][j] == 'K' && king == 'K') {
                        isCheckmateLarge = isCheck(board, king) && !hasLegalMove(board, i, j, Character::isLowerCase);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return isCheckmateSmall || isCheckmateLarge;
    }


    private boolean isValidMoveForPiece(char[][] board, char piece, int fromRow, int fromCol, int toRow, int toCol) {
        char targetPiece = board[toRow][toCol];
        if (!isOpponent(piece, targetPiece)) return false;

        return switch (piece) {
            case 'P', 'p' -> isValidMoveForPawn(piece, board, fromRow, fromCol, toRow, toCol);
            case 'R', 'r' -> isValidMoveForRook(board, fromRow, fromCol, toRow, toCol);
            case 'B', 'b' -> isValidMoveForBishop(board, fromRow, fromCol, toRow, toCol);
            case 'N', 'n' -> isValidMoveForKnight(fromRow, fromCol, toRow, toCol);
            case 'Q', 'q' -> isValidMoveForQueen(board, fromRow, fromCol, toRow, toCol);
            case 'K', 'k' -> isValidMoveForKing(fromRow, fromCol, toRow, toCol);
            default -> true;
        };
    }

    private boolean isValidMoveForPawn(char piece, char[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        boolean isSameCol = fromCol == toCol;
        boolean maxNextMove = Math.abs(fromRow - toRow) == 1 &&
                ((piece == 'p' && toRow - fromCol > 0) || (piece == 'P' && fromRow - toRow > 0));
        boolean maxFirstMove = Math.abs(fromRow - toRow) == 2 &&
                ((piece == 'p' && fromRow == 1 && board[fromRow + 1][fromCol] == ' ') ||
                        (piece == 'P' && fromRow == 6 && board[fromRow - 1][fromCol] == ' '));
        boolean isCorrectMoveLength = maxNextMove || maxFirstMove;

        //krok
        if (isSameCol && isCorrectMoveLength) {
            return board[toRow][toCol] == ' ';
        }
        //bicie
        else {
            return Math.abs(toCol - fromCol) == 1 && toRow - fromRow == 1;
        }
    }

    private boolean isOpponent(char piece, char targetPiece) {
        if (targetPiece == ' ') return true;
        return (Character.isLowerCase(targetPiece) && Character.isUpperCase(piece)) ||
                (Character.isUpperCase(targetPiece) && Character.isLowerCase(piece));
    }

    private boolean isValidMoveForRook(char[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow != toRow && fromCol != toCol) return false;
        return checkFieldsBetween(board, fromRow, fromCol, toRow, toCol);
    }

    private boolean checkFieldsBetween(char[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        int rowDirection = Integer.compare(toRow, fromRow);
        int colDirection = Integer.compare(toCol, fromCol);

        for (int i = fromRow + rowDirection, j = fromCol + colDirection; i != toRow || j != toCol; i += rowDirection, j += colDirection) {
            if (board[i][j] != ' ') return false;
        }

        return true;
    }

    private boolean isValidMoveForBishop(char[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        if (Math.abs(toRow - fromRow) != Math.abs(toCol - fromCol)) return false;
        return checkFieldsBetween(board, fromRow, fromCol, toRow, toCol);
    }

    private boolean isValidMoveForKnight(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    private boolean isValidMoveForQueen(char[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        return isValidMoveForRook(board, fromRow, fromCol, toRow, toCol) ||
                isValidMoveForBishop(board, fromRow, fromCol, toRow, toCol);
    }

    private boolean isValidMoveForKing(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        return (rowDiff == 1 || rowDiff == 0) && (colDiff == 1 || colDiff == 0);
    }

    private boolean isKingAttacked(char[][] board, char king, DirectoryStream.Filter<Character> firstFilter,
                                   DirectoryStream.Filter<Character> secondFilter) throws IOException {
        char[][] tempBoard = new char[8][8];
        int kingRow = 0, kingCol = 4;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!(board[i][j] == king)) {
                    if (firstFilter.accept(board[i][j])) {
                        tempBoard[i][j] = '*';
                    } else {
                        tempBoard[i][j] = board[i][j];
                    }
                } else {
                    tempBoard[i][j] = king;
                    kingRow = i;
                    kingCol = j;
                }
            }
        }

        return isFieldAttacked(tempBoard, kingRow, kingCol, secondFilter);
    }

    private boolean isFieldAttacked(char[][] board, int kingRow, int kingCol, DirectoryStream.Filter<Character> filter) throws IOException {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (filter.accept(board[i][j])) {
                    if (isValidMoveForPiece(board, board[i][j], i, j, kingRow, kingCol)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
