package chess;

import boardgame.Board;
import boardgame.BoardException;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private final Board board;

    public ChessMatch() {
        this.board = new Board();
        this.turn = 1;
        this.currentPlayer = Color.WHITE;
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

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    private void placeNewPiece(String pos, ChessPiece piece) {
        board.placePiece(piece,  new ChessPosition(pos).toPosition());
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);
        nextTurn();
        return (ChessPiece) capturedPiece;
    }

    private void nextTurn() {
        this.turn++;
        this.currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
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
        if (currentPlayer != ((ChessPiece)board.piece(source)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
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
        placeNewPiece("c1", new Bishop(board, Color.WHITE));
        placeNewPiece("f1", new Bishop(board, Color.WHITE));
        placeNewPiece("g1", new Knight(board, Color.WHITE));
        placeNewPiece("b1", new Knight(board, Color.WHITE));
        placeNewPiece("d1", new Queen(board, Color.WHITE));

        for (int i = 0; i < 8; i++) {
            placeNewPiece((char)(i + 97) + "2", new Pawn(board, Color.WHITE));
            placeNewPiece((char)(i + 97) + "7", new Pawn(board, Color.BLACK));
        }

        placeNewPiece("e8", new King(board, Color.BLACK));
        placeNewPiece("a8", new Rook(board, Color.BLACK));
        placeNewPiece("h8", new Rook(board, Color.BLACK));
        placeNewPiece("c8", new Bishop(board, Color.BLACK));
        placeNewPiece("f8", new Bishop(board, Color.BLACK));
        placeNewPiece("g8", new Knight(board, Color.BLACK));
        placeNewPiece("b8", new Knight(board, Color.BLACK));
        placeNewPiece("d8", new Queen(board, Color.BLACK));

    }
}