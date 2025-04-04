package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {
    public BlackPlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMove) {
        super(board, blackStandardLegalMove, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove()&& !this.isInCheck()) {
            if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                // Black's king side castle... king is not under check, and the 2 tiles in between are not occupied
                final Tile rookTile = this.board.getTile(7);

                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    // checking if Rook is on its original tile and has not made the first move yet
                    if(Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        /* checking if the tiles between king and rook have any attacks which will make the castle move illegal,
                         * and the piece on rook's starting position has to be a rook! */
                        kingCastles.add(new KingSideCastleMove(this.board,
                                                                this.playerKing,
                                                                6,
                                                                (Rook) rookTile.getPiece(),
                                                                rookTile.getTileCoordinate(),
                                                                5)); // King's side castle, when represented in linear coordinate...
                    }
                }
            }
            if(!this.board.getTile(1).isTileOccupied() &&
                    !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(3).isTileOccupied()) {
                // Black's queen side castle... checking if the 3 tiles in between the king and rook are unoccupied...
                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnTile(2, opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(3, opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()) {
                    // checking if queen side Rook is on its original tile and has not made the first move yet and the tile between are not under attack.
                    kingCastles.add(new QueenSideCastleMove(this.board,
                                                            this.playerKing,
                                                            2,
                                                            (Rook) rookTile.getPiece(),
                                                            rookTile.getTileCoordinate(),
                                                            3)); // Queen's side castle, when represented in linear coordinate...
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }


}
