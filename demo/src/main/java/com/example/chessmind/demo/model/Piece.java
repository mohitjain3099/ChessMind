package com.example.chessmind.demo.model;

public abstract class Piece {
    protected boolean isWhite;
    protected boolean hasMoved;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
        this.hasMoved = false;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board);

    public abstract char fenChar();
}
