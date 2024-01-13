package code;

public class BoardService {

    public static void print(char[][] board) {
        for (char[] row : board) {
            System.out.print("|");
            for (char c : row) {
                System.out.print(c + "|");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static String convertBoardToString(char[][] board) {
        StringBuilder boardString = new StringBuilder("   a b c d e f g h \n");
        for (int i = 0; i<8;i++) {
            boardString.append((i+1)).append(" |");
            for (char c : board[i]) {
                boardString.append(c).append("|");
            }
            boardString.append("\n");
        }
        boardString.append("\n");

        return boardString.toString();
    }

    public static char[][] getInitializeBoard() {
        return new char[][]{
                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}
        };
    }

}
