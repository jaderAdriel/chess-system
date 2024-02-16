package chess;

import boardgame.Board;
import boardgame.BoardException;
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

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        validateSourcePosition(source);
        Piece capturedPiece = makeMove(source, target);
        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target) {
        Piece p = board.removePiece(source);
        Piece capturedPiece;

        try {
            capturedPiece = board.removePiece(target);
        }
        catch (BoardException e) {
            capturedPiece = null;
        }

        board.placePiece(p, target);

        return capturedPiece;
    }

    public void validateSourcePosition(Position source) {
        if (!board.thereIsAPiece(source)) {
            throw new ChessException("There is no piece on source position");
        }
        if (!board.piece(source).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    public void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The target position isn't valid!");
        }
    }

    public boolean[][] possibleMoves(ChessPosition source) {
        return board.piece(source.toPosition()).possibleMoves();
    }

    public void validateSourcePosition(ChessPosition source) {
        validateSourcePosition(source.toPosition());
    }

    private void initialSetup() {
        placeNewPiece("e1", new King(board, Color.WHITE));
        placeNewPiece("a1", new Rook(board, Color.WHITE));
        placeNewPiece("h1", new Rook(board, Color.WHITE));

        placeNewPiece("e8", new King(board, Color.BLACK));
        placeNewPiece("a8", new Rook(board, Color.BLACK));
        placeNewPiece("h8", new Rook(board, Color.BLACK));
    }
}