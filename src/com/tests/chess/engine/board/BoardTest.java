package com.tests.chess.engine.board;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.Iterables;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    public void initialBoard() {
        final Board board = Board.createStandardBoard();
        assertEquals(20, board.currentPlayer().getLegalMoves().size());
        assertEquals(20, board.currentPlayer().getOpponent().getLegalMoves().size());
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
        assertFalse(board.currentPlayer().isCastled());
//        assertTrue(board.currentPlayer().isKingSideCastleCapable());
//        assertTrue(board.currentPlayer().isQueenSideCastleCapable());
        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.currentPlayer().getOpponent().isCastled());
//        assertTrue(board.currentPlayer().getOpponent().isKingSideCastleCapable());
//        assertTrue(board.currentPlayer().getOpponent().isQueenSideCastleCapable());
//        assertEquals("White", board.whitePlayer().toString());
//        assertEquals("Black", board.blackPlayer().toString());

//        final Iterable<Piece> allPieces = board.getAllPieces();
//        final Iterable<Move> allMoves = Iterables.concat(board.whitePlayer().getLegalMoves(), board.blackPlayer().getLegalMoves());
//        for(final Move move : allMoves) {
//            assertFalse(move.isAttack());
//            assertFalse(move.isCastlingMove());
//            assertEquals(MoveUtils.exchangeScore(move), 1);
//        }

//        assertEquals(Iterables.size(allMoves), 40);
//        assertEquals(Iterables.size(allPieces), 32);
//        assertFalse(BoardUtils.isEndGame(board));
//        assertFalse(BoardUtils.isThreatenedBoardImmediate(board));
//        assertEquals(StandardBoardEvaluator.get().evaluate(board, 0), 0);
    }
}