package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece {

    /*Moving the queen, will give us all the possible positions that were valid for rook as well as bishops*/
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};


    public Queen(final Alliance pieceAlliance,
                 final int piecePosition) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance, true);
    }


    public Queen(final Alliance pieceAlliance,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

                if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                        isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
                    /*Managing the border conditions where queen will move cyclically to next or previous tile which should
                     * be out of bounds practically. But due to linear nature of coordinates choose, the cyclical nature needs
                     * to be rectified in this way*/
                    break;
                }

                candidateDestinationCoordinate += candidateCoordinateOffset;
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
                        break; // If the blocking piece is friendly, then move is invalid, if not, still you cannot go any further than this tile. Hence, break
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Queen movePiece(final Move move) {
        /* this creates a new Bishop that is just like the current Bishop, but in a new location.
         * We could have replaced the original bishop, but since the classes are made immutable,
         * we need to create a new Bishop to be placed on the destination tile */
        return new Queen(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return Piece.PieceType.QUEEN.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7 || candidateOffset == -1);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9 || candidateOffset == 1);
    }
}
