package com.example.chessmind.demo.model;

public class Board {
    private Piece[][] board;
    private int[] enPassantTarget;
    private boolean[] castlingRights; // K, Q, k, q

    public Board() {
        board = new Piece[8][8];
        initializeBoard();
        enPassantTarget = new int[] {-1, -1};
        castlingRights = new boolean[]{true, true, true, true}; // K, Q, k, q
    }

    private void initializeBoard() {
        // Initialize pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(true);
            board[6][i] = new Pawn(false);
        }

        // Initialize other pieces
        board[0][0] = new Rook(true);
        board[0][7] = new Rook(true);
        board[7][0] = new Rook(false);
        board[7][7] = new Rook(false);

        board[0][1] = new Knight(true);
        board[0][6] = new Knight(true);
        board[7][1] = new Knight(false);
        board[7][6] = new Knight(false);

        board[0][2] = new Bishop(true);
        board[0][5] = new Bishop(true);
        board[7][2] = new Bishop(false);
        board[7][5] = new Bishop(false);

        board[0][3] = new Queen(true);
        board[7][3] = new Queen(false);

        board[0][4] = new King(true);
        board[7][4] = new King(false);
    }

    public boolean movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = board[startX][startY];
        if (piece != null && piece.isValidMove(startX, startY, endX, endY, board)) {
            // Handle en passant capture
            if (piece instanceof Pawn && board[endX][endY] == null && startY != endY) {
                board[startX][endY] = null;
            }
            // Handle castling
            if (piece instanceof King && Math.abs(endY - startY) == 2) {
                int rookY = endY > startY ? 7 : 0;
                int newRookY = endY > startY ? 5 : 3;
                board[startX][newRookY] = board[startX][rookY];
                board[startX][rookY] = null;
            }

            // Update en passant target square
            if (piece instanceof Pawn && Math.abs(endX - startX) == 2) {
                enPassantTarget = new int[] {startX + (endX - startX) / 2, startY};
            } else {
                enPassantTarget = new int[] {-1, -1};
            }

            // Update castling rights
            if (piece instanceof King) {
                if (piece.isWhite()) {
                    castlingRights[0] = false; // K
                    castlingRights[1] = false; // Q
                } else {
                    castlingRights[2] = false; // k
                    castlingRights[3] = false; // q
                }
            } else if (piece instanceof Rook) {
                if (startX == 0) {
                    if (startY == 0) castlingRights[1] = false; // Q
                    else if (startY == 7) castlingRights[0] = false; // K
                } else if (startX == 7) {
                    if (startY == 0) castlingRights[3] = false; // q
                    else if (startY == 7) castlingRights[2] = false; // k
                }
            }

            board[endX][endY] = piece;
            board[startX][startY] = null;
            return true;
        }
        return false;
    }

    public String toFen() {
        StringBuilder fen = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int emptyCount = 0;
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    fen.append(piece.fenChar());
                }
            }
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }
            if (i < 7) {
                fen.append('/');
            }
        }
        // Add additional FEN fields (active color, castling availability, en passant target square, etc.)
        fen.append(" w KQkq ");
        if (enPassantTarget[0] != -1) {
            fen.append((char) ('a' + enPassantTarget[1])).append(8 - enPassantTarget[0]);
        } else {
            fen.append('-');
        }
        fen.append(" 0 1");
        return fen.toString();
    }
}
