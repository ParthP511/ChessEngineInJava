package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece {
    /*The king has same direction of moves as the queen, but it can only move one tile.*/
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.KING, piecePosition, pieceAlliance, true);
    }

    public King(final Alliance pieceAlliance,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate) && (isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset) ||
                isEighthColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset))) {
                continue;
            }
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
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
    public King movePiece(final Move move) {
        /* this creates a new Bishop that is just like the current Bishop, but in a new location.
         * We could have replaced the original bishop, but since the classes are made immutable,
         * we need to create a new Bishop to be placed on the destination tile */
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return Piece.PieceType.KING.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {

        return BoardUtils.FIRST_COLUMN[currentPosition] &&
                (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] &&
                (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }
}
