package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {

    // crating a list of possible moves, using linear math instead of 2d coordinates(2d coordinates already implemented in python ches engine)
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, true);
    }


    public Knight(final Alliance pieceAlliance,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        // Iterate over all possible moves to check for possible moves.
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEightColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    /* Corner cases, where applying the offset actually moves the knight cyclically out of the board.
                    * If this is not included, the knight will be placed in the board, but after crossing one of the 4 borders of the board.
                    * This is an illegal move, as no piece can cyclically move out of the board
                    */
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate)); // To be modified when changing the move class
                } else {
                    // destination tile is occupied
                    final Piece pieceAtDestination  = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if(this.pieceAlliance != pieceAlliance) { // current piece alliance not same as destination piece alliance
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Knight movePiece(final Move move) {
        /* this creates a new Bishop that is just like the current Bishop, but in a new location.
         * We could have replaced the original bishop, but since the classes are made immutable,
         * we need to create a new Bishop to be placed on the destination tile */
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return Piece.PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {

        return BoardUtils.FIRST_COLUMN[currentPosition] &&
                (candidateOffset == -17 || candidateOffset == -10 ||
                        candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] &&
                (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] &&
                (candidateOffset == -6 || candidateOffset == 10);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] &&
                (candidateOffset == -15 || candidateOffset == -6 ||
                        candidateOffset == 10 || candidateOffset == 17);
    }
}
