package code;

import code.services.MoveService;
import code.services.MoveServiceImpl;
import data.TestBoardData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveServiceImplTest {
    private final MoveService moveServiceImpl = new MoveServiceImpl();
    private final TestBoardData data = new TestBoardData();

    @Test
    void isCheck() {
        //given
        char[][] board1 = data.getBoards().get(0);
        char[][] board2 = data.getBoards().get(1);
        char[][] board3 = data.getBoards().get(2);
        char[][] board4 = data.getBoards().get(3);
        char[][] board5 = data.getBoards().get(4);

        // when
        boolean checkmate1 = moveServiceImpl.isCheck(board1, 'k');
        boolean checkmate2 = moveServiceImpl.isCheck(board2, 'k');
        boolean checkmate3 = moveServiceImpl.isCheck(board3, 'K');
        boolean checkmate4 = moveServiceImpl.isCheck(board4, 'K');
        boolean checkmate5 = moveServiceImpl.isCheck(board5, 'K');

        //than
        assertTrue(checkmate1);
        assertTrue(checkmate2);
        assertTrue(checkmate3);
        assertTrue(checkmate4);
        assertTrue(checkmate5);
    }

    @Test
    void isCheckmate() {
        //given
        char[][] board1 = data.getBoards().get(0);
        char[][] board2 = data.getBoards().get(1);
        char[][] board3 = data.getBoards().get(2);
        char[][] board4 = data.getBoards().get(3);
        char[][] board5 = data.getBoards().get(4);

        //when
        boolean checkmate1 = moveServiceImpl.isCheckmate(board1,  'k');
        boolean checkmate2 = moveServiceImpl.isCheckmate(board2,  'k');
        boolean checkmate3 = moveServiceImpl.isCheckmate(board3,  'K');
        boolean checkmate4 = moveServiceImpl.isCheckmate(board4,  'K');
        boolean checkmate5 = moveServiceImpl.isCheckmate(board5,  'K');

        //than
        assertTrue(checkmate1);
        assertTrue(checkmate2);
        assertFalse(checkmate3);
        assertTrue(checkmate4);
        assertFalse(checkmate5);
    }


    @Test
    void hasLegalMove() {
        //given
        char[][] board1 = data.getBoards().get(0);
        char[][] board2 = data.getBoards().get(1);
        char[][] board3 = data.getBoards().get(2);
        char[][] board4 = data.getBoards().get(3);
        char[][] board5 = data.getBoards().get(4);

        //when
        boolean checkmate1 = moveServiceImpl.hasLegalMove(board1, 0, 4, Character::isUpperCase);
        boolean checkmate2 = moveServiceImpl.hasLegalMove(board2, 3, 2, Character::isUpperCase);
        boolean checkmate3 = moveServiceImpl.hasLegalMove(board3, 3, 2, Character::isLowerCase);
        boolean checkmate4 = moveServiceImpl.hasLegalMove(board4, 7, 4, Character::isLowerCase);
        boolean checkmate5 = moveServiceImpl.hasLegalMove(board5, 7, 4, Character::isLowerCase);

        //than
        assertFalse(checkmate1);
        assertFalse(checkmate2);
        assertTrue(checkmate3);
        assertFalse(checkmate4);
        assertTrue(checkmate5);
    }
}