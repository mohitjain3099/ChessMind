package com.example.chessmind.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Piece[][] board;
    private int[] enPassantTarget;
    private boolean[] castlingRights; // K, Q, k, q
    private boolean whiteTurn;
    private int halfmoveClock;
    private int fullmoveNumber;

    public Board() {
        board = new Piece[8][8];
        initializeBoard();
        enPassantTarget = new int[]{-1, -1};
        castlingRights = new boolean[]{true, true, true, true}; // K, Q, k, q
        whiteTurn = true;
        halfmoveClock = 0;
        fullmoveNumber = 1;
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

    public boolean movePiece(int startX, int startY, int endX, int endY, String promoteTo) {
        Piece piece = board[startX][startY];
        if (piece != null && piece.isValidMove(startX, startY, endX, endY, board)) {
            // Handle en passant capture
            if (piece instanceof Pawn && board[endX][endY] == null && startY != endY) {
                if (endX == enPassantTarget[0] && endY == enPassantTarget[1]) {
                    board[startX][endY] = null;
                }
            }
            if (wouldBeInCheck(startX, startY, endX, endY)) {
                return false; // The move is invalid because it would put the player in check
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
                enPassantTarget = new int[]{startX + (endX - startX) / 2, startY};
            } else {
                enPassantTarget = new int[]{-1, -1};
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

            // Perform the move
            board[endX][endY] = piece;
            board[startX][startY] = null;

            // Update move flags
            piece.setHasMoved(true);
            whiteTurn = !whiteTurn;

            // Update halfmove and fullmove counters
            if (piece instanceof Pawn || board[endX][endY] != null) {
                halfmoveClock = 0;
            } else {
                halfmoveClock++;
            }
            if (!whiteTurn) {
                fullmoveNumber++;
            }
            // Handle pawn promotion
            if (piece instanceof Pawn && (endX == 0 || endX == 7)) {
                switch (promoteTo.toLowerCase()) {
                    case "knight":
                        board[endX][endY] = new Knight(piece.isWhite());
                        break;
                    case "bishop":
                        board[endX][endY] = new Bishop(piece.isWhite());
                        break;
                    case "rook":
                        board[endX][endY] = new Rook(piece.isWhite());
                        break;
                    case "queen":
                    default:
                        board[endX][endY] = new Queen(piece.isWhite());
                        break;
                }
            }

             // After the move is made, check for checkmate or stalemate
             boolean opponentColor = !piece.isWhite();
             if (isInCheckmate(opponentColor)) {
                 System.out.println(opponentColor ? "Black" : "White" + " is in checkmate! Game over.");
                 // You might want to set a game over flag or trigger an end game method here
             } else if (isInStalemate(opponentColor)) {
                 System.out.println("Stalemate! The game is a draw.");
                 // You might want to set a game over flag or trigger an end game method here
             }

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
        fen.append(' ');
        fen.append(whiteTurn ? 'w' : 'b');
        fen.append(' ');
        if (castlingRights[0]) fen.append('K');
        if (castlingRights[1]) fen.append('Q');
        if (castlingRights[2]) fen.append('k');
        if (castlingRights[3]) fen.append('q');
        if (!castlingRights[0] && !castlingRights[1] && !castlingRights[2] && !castlingRights[3]) fen.append('-');
        fen.append(' ');
        if (enPassantTarget[0] != -1) {
            fen.append((char) ('a' + enPassantTarget[1])).append(8 - enPassantTarget[0]);
        } else {
            fen.append('-');
        }
        fen.append(' ');
        fen.append(halfmoveClock);
        fen.append(' ');
        fen.append(fullmoveNumber);
        return fen.toString();
    }

    public boolean isInCheck(boolean whiteKing) {
        // Find the king's position
        int[] kingPosition = findKing(whiteKing);
        if (kingPosition == null) {
            return false; // King not found (shouldn't happen in a valid game state)
        }

        // Check if any opponent's piece can attack the king
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.isWhite() != whiteKing) {
                    if (piece.isValidMove(i, j, kingPosition[0], kingPosition[1], board)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private int[] findKing(boolean whiteKing) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece instanceof King && piece.isWhite() == whiteKing) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // Add this method to check if a move would put the current player in check
    public boolean wouldBeInCheck(int startX, int startY, int endX, int endY) {
        // Store the current board state
        Piece startPiece = board[startX][startY];
        Piece endPiece = board[endX][endY];

        // Make the move
        board[endX][endY] = startPiece;
        board[startX][startY] = null;

        // Check if the current player is in check after the move
        boolean inCheck = isInCheck(startPiece.isWhite());

        // Undo the move
        board[startX][startY] = startPiece;
        board[endX][endY] = endPiece;

        return inCheck;
    }

     public boolean isInCheckmate(boolean whiteKing) {
        if (!isInCheck(whiteKing)) {
            return false; // The king is not in check, so it can't be checkmate
        }

        // Check all possible moves for all pieces of the current player
        for (int startX = 0; startX < 8; startX++) {
            for (int startY = 0; startY < 8; startY++) {
                Piece piece = board[startX][startY];
                if (piece != null && piece.isWhite() == whiteKing) {
                    List<int[]> possibleMoves = getPossibleMoves(startX, startY);
                    for (int[] move : possibleMoves) {
                        int endX = move[0];
                        int endY = move[1];
                        if (!wouldBeInCheck(startX, startY, endX, endY)) {
                            return false; // Found a legal move, so it's not checkmate
                        }
                    }
                }
            }
        }

        return true; // No legal moves found, it's checkmate
    }

    private List<int[]> getPossibleMoves(int startX, int startY) {
        List<int[]> possibleMoves = new ArrayList<>();
        Piece piece = board[startX][startY];

        if (piece == null) {
            return possibleMoves;
        }

        for (int endX = 0; endX < 8; endX++) {
            for (int endY = 0; endY < 8; endY++) {
                if (piece.isValidMove(startX, startY, endX, endY, board)) {
                    possibleMoves.add(new int[]{endX, endY});
                }
            }
        }

        return possibleMoves;
    }

    public boolean isInStalemate(boolean whiteKing) {
        if (isInCheck(whiteKing)) {
            return false; // The king is in check, so it's not stalemate
        }

        // Check if there are any legal moves
        for (int startX = 0; startX < 8; startX++) {
            for (int startY = 0; startY < 8; startY++) {
                Piece piece = board[startX][startY];
                if (piece != null && piece.isWhite() == whiteKing) {
                    List<int[]> possibleMoves = getPossibleMoves(startX, startY);
                    for (int[] move : possibleMoves) {
                        int endX = move[0];
                        int endY = move[1];
                        if (!wouldBeInCheck(startX, startY, endX, endY)) {
                            return false; // Found a legal move, so it's not stalemate
                        }
                    }
                }
            }
        }

        return true; // No legal moves found, it's stalemate
    }

}
