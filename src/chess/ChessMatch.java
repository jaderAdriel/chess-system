package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
    private final Board board;

    public ChessMatch() {
        this.board = new Board();
        this.initialSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];

        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getColumns(); c++) {
                mat[r][c] = (ChessPiece) board.piece(r, c);
            }
        }

        return mat;
    }

    private void placeNewPiece(String pos, ChessPiece piece) {
        board.placePiece(piece,  new ChessPosition(pos).toPosition());
    }

    private void initialSetup() {
        placeNewPiece("a1", new Rook(board, Color.WHITE));
        placeNewPiece("h1", new Rook(board, Color.WHITE));
    }
}