// src/main/java/com/yourpackage/model/Pawn.java
package com.example.chessmind.demo.model;

public class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int direction = isWhite() ? 1 : -1;

        if (endX < 0 || endX > 7 || endY < 0 || endY > 7) {
            return false;
        }

        // Move forward one square
        if (startX + direction == endX && startY == endY && board[endX][endY] == null) {
            return true;
        }

        // Move forward two squares on the first move
        if ((isWhite() && startX == 1 || !isWhite() && startX == 6) &&
            startX + 2 * direction == endX && startY == endY &&
            board[startX + direction][startY] == null && board[endX][endY] == null) {
            return true;
        }

        // Capture diagonally
        if (startX + direction == endX && (startY + 1 == endY || startY - 1 == endY) &&
            board[endX][endY] != null && board[endX][endY].isWhite() != isWhite()) {
            return true;
        }

        // En passant
        if (startX + direction == endX && (startY + 1 == endY || startY - 1 == endY) &&
            board[startX][endY] instanceof Pawn && board[startX][endY].isWhite() != isWhite() &&
            board[endX][endY] == null) {
            return true;
        }

        return false;
    }

    @Override
    public char fenChar() {
        return isWhite() ? 'P' : 'p';
    }
}
