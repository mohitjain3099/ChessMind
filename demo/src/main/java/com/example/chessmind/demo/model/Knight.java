package com.example.chessmind.demo.model;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);

        if ((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) {
            return board[endX][endY] == null || board[endX][endY].isWhite() != isWhite();
        }

        return false;
    }

    @Override
    public char fenChar() {
        return isWhite() ? 'N' : 'n';
    }
}
