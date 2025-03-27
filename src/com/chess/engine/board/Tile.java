package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.Blocking;

import java.util.HashMap;
import java.util.Map;


/*
* We can create all empty tiles upfront, and whenever needed we can simply retrieve one from cache instead of creating one.
* If the classes we create can be made immutable, we should make them immutable. Good practice!
*/

public abstract class Tile {    //This class needs to be immutable, to reduce memory and garbage collection footprint

    protected final int tileCoordinate;     // Cannot reference this variable without a subclass

    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for(int i = 0; i < BoardUtils.NUM_TILES; ++i) {
            emptyTileMap.put(i, new EmptyTile(i));
        }

        // Collections.unmodifiableMap(emptyTileMap)   // <- we could have also used this to perform the same function as guava library method below.
        return ImmutableMap.copyOf(emptyTileMap);   // We want an immutable map, hence using this external library(from google, guava)
    }

    public static Tile createTile(final int tileCoordinate, final Piece piece) {        // factory method to access this immutable class
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    private Tile(final int tileCoordinate) {      // constructor private for immutability
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public int getTileCoordinate() {
        return this.tileCoordinate;
    }

    public static final class EmptyTile extends Tile {
        private EmptyTile(final int coordinate) {   // The argument is final and constructor is private, to further extend the immutability of the class Tile
            super(coordinate);
        }

        @Override
        public String toString() {
            return "-";
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile;    // Cannot reference this variable outside this class

        private OccupiedTile(final int tileCoordinate, final Piece pieceOnTile) {   // constructor private for immutability
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
                    getPiece().toString();
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
    }
}
