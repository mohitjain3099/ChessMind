package com.example.chessmind.demo.model;

public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        if (Math.abs(endX - startX) != Math.abs(endY - startY)) {
            return false;
        }

        int xStep = Integer.compare(endX, startX);
        int yStep = Integer.compare(endY, startY);

        for (int x = startX + xStep, y = startY + yStep; x != endX; x += xStep, y += yStep) {
            if (board[x][y] != null) {
                return false;
            }
        }

        return board[endX][endY] == null || board[endX][endY].isWhite() != isWhite();
    }

    @Override
    public char fenChar() {
        return isWhite() ? 'B' : 'b';
    }
}

