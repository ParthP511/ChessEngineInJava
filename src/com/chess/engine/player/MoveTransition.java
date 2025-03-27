package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.concurrent.Future;

public class MoveTransition {

    /*Holds the data that is needed to transition the board from one state to another,
    * and holds the data that needs to be carried over form one board state to another.*/
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board transitionBoard,
                          final Move move,
                          final MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getTransitionBoard() {
        return this.transitionBoard;
    }
}
