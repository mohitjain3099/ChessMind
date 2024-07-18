package com.example.chessmind.demo.model;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        if (startX == endX || startY == endY) {
            return new Rook(isWhite()).isValidMove(startX, startY, endX, endY, board);
        } else if (Math.abs(endX - startX) == Math.abs(endY - startY)) {
            return new Bishop(isWhite()).isValidMove(startX, startY, endX, endY, board);
        }
        return false;
    }

    @Override
    public char fenChar() {
        return isWhite() ? 'Q' : 'q';
    }
}

