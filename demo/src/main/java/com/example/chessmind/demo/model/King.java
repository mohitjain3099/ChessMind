package com.example.chessmind.demo.model;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        
        // Regular king move
        if (dx <= 1 && dy <= 1) {
            return board[endX][endY] == null || board[endX][endY].isWhite() != isWhite();
        }
        // Castling
        if (dx == 0 && dy == 2) {
            int rookY = endY > startY ? 7 : 0;  // Determine if it's kingside or queenside castling
            Piece rook = board[startX][rookY];

            if (rook instanceof Rook && rook.isWhite() == isWhite()) {
                // Check if spaces between king and rook are empty
                int step = (endY - startY) / 2;
                for (int y = startY + step; y != endY; y += step) {
                    if (board[startX][y] != null) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public char fenChar() {
        return isWhite() ? 'K' : 'k';
    }
}
