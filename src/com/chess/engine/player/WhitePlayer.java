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

public class WhitePlayer extends Player {
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMove) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMove);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove()&& !this.isInCheck()) {
            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                // White's king side castle... king is not under check, and the 2 tiles in between are not occupied
                final Tile rookTile = this.board.getTile(63);

                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    // checking if Rook is on its original tile and has not made the first move yet
                    if(Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()) {
                        /* checking if the tiles between king and rook have any attacks which will make the castle move illegal,
                        * and the piece on rook's starting position has to be a rook! */
                        kingCastles.add(new KingSideCastleMove(this.board,
                                                                this.playerKing,
                                                                62,
                                                                (Rook)rookTile.getPiece(),
                                                                rookTile.getTileCoordinate(),
                                                                61));  // King's side castle, when represented in linear coordinate...
                    }
                }
            }
            if(!this.board.getTile(59).isTileOccupied() &&
                    !this.board.getTile(58).isTileOccupied() &&
                    !this.board.getTile(57).isTileOccupied()) {
                // White's queen side castle... checking if the 3 tiles in between the king and rook are unoccupied...
                final Tile rookTile = this.board.getTile(56);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()) {
                    // checking if queen side Rook is on its original tile and has not made the first move yet and tiles between rook and king are not underattack.
                    kingCastles.add(new QueenSideCastleMove(this.board,
                                                            this.playerKing,
                                                            58,
                                                            (Rook)rookTile.getPiece(),
                                                            rookTile.getTileCoordinate(),
                                                            59)); // Queen's side castle, when represented in linear coordinate...
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
