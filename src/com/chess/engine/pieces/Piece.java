package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;
import java.util.List;


public abstract class Piece {

    protected final PieceType pieceType;
    // Every piece has a piece position where it is placed, an alliance(white or black)

    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final PieceType pieceType,
          final int piecePosition,
          final Alliance pieceAlliance,
          final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) {
            return true;
        }
        if(!(other instanceof Piece)) {
            return false; // if other object is not a piece, then don;t bother comparing them...
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    /* Moves could also be stored in a set, as they should be unique. The method is abstract as the move calculations should be
    * different for different pieces -i.e- The legal moves for a knight will be different from the legal moves for a rook */
    public abstract Collection<Move> calculateLegalMoves(final Board board);
    public abstract Piece movePiece(Move move);

    public enum PieceType {

        PAWN(100, "P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT(200, "N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP(300, "B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK(500, "R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN(900, "Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING(10000, "K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        private int pieceValue;

        PieceType(final int pieceValue, final String pieceName) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public int getPieceValue() {
            return this.pieceValue;
        }
        public abstract boolean isKing();
        public abstract boolean isRook();
    }

}
