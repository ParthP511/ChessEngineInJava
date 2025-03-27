package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.PawnPromotion;
import com.chess.engine.board.Move.PawnAttackMove;
import com.chess.engine.board.Move.PawnMove;
import com.chess.engine.board.Move.PawnJump;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {7, 8, 9, 16};

    public Pawn(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, true);
    }

    public Pawn(final Alliance pieceAlliance,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {

            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                // Single step ahead move and pawn promotion.
                if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                } else {
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
            } else if(currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.pieceAlliance.isBlack()) ||
                        (BoardUtils.SECOND_RANK[this.piecePosition] && this.pieceAlliance.isWhite()))) {
                /* First move being a 2 jump move covered here.
                * Also make sure the square being jumped in pawn's first move then only move to the second square */
                final int behindCandidateDestination = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if(!board.getTile(behindCandidateDestination).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            } else if(currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                /* Moving the pawn when attacker is diagonally one block away case 1(offset 7), also need to check if adding the offset to
                *  piece position moves it out of bounds. The checks for a piece present on the attacked tile are written below.*/
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                } else if(board.getEnPassantPawn() != null) {
                    if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            } else if(currentCandidateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                } else if(board.getEnPassantPawn() != null) {
                    if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }

        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(final Move move) {
        /* this creates a new Bishop that is just like the current Bishop, but in a new location.
         * We could have replaced the original bishop, but since the classes are made immutable,
         * we need to create a new Bishop to be placed on the destination tile */
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return Piece.PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece () {
        // Currently only promotions to queen are supported
        // TODO add in support for other piece types as well.
        return new Queen(this.pieceAlliance, this.piecePosition, false);
    }
}
