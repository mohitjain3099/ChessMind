package com.example.chessmind.demo.model;

public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        if (startX != endX && startY != endY) {
            return false;
        }

        int xStep = Integer.compare(endX, startX);
        int yStep = Integer.compare(endY, startY);

        for (int x = startX + xStep, y = startY + yStep; x != endX || y != endY; x += xStep, y += yStep) {
            if (board[x][y] != null) {
                return false;
            }
        }

        return board[endX][endY] == null || board[endX][endY].isWhite() != isWhite();
    }

    @Override
    public char fenChar() {
        return isWhite() ? 'R' : 'r';
    }
}
