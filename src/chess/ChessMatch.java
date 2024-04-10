package chess;

import boardgame.Board;
import boardgame.BoardException;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private final Board board;
    private boolean isInCheck;
    private boolean isCheckMated = false;
    private Color winner = null;
    List<Piece> piecesOnTheBoard = new ArrayList<>();
    List<Piece> capturedPieces = new ArrayList<>();

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

    private void setCheckMated(Color kingColor) {
        this.isCheckMated = true;
    }

    public boolean isCheckMated() {
        return isCheckMated;
    }

    private void setWinner(Color winnerColor) {
        this.winner = winnerColor;
    }

    public Color getWinner() {
        return winner;
    }

    private void placeNewPiece(String pos, ChessPiece piece) {
        board.placePiece(piece,  new ChessPosition(pos).toPosition());
        piecesOnTheBoard.add(piece);
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        validateSourcePosition(source);
        validateTargetPosition(source, target);

        Piece capturedPiece = makeMove(source, target);

        Color opponent = (getCurrentPlayer() == Color.WHITE) ? Color.BLACK : Color.WHITE;

        if (testCheck(opponent)) testCheckMated(opponent);

        if (!isCheckMated) nextTurn();


        return (ChessPiece) capturedPiece;
    }

    private void nextTurn() {
        this.turn++;
        this.currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMove();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMove();
        Piece capturedPiece;


        try {
            capturedPiece = board.removePiece(target);
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }
        catch (BoardException e) {
            capturedPiece = null;
        }

        board.placePiece(p, target);

        return capturedPiece;
    }

    private ChessPiece findKing(Color color) {
        for (Piece piece : piecesOnTheBoard) {
            if ( ((ChessPiece)piece).getColor() == color &&  piece instanceof King ) {
                return (ChessPiece)piece;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    public boolean isInCheck() {
        return isInCheck;
    }

    private boolean testCheck(Color color) {
        Position kingPosition = findKing(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() != color).toList();

        for (Piece piece : opponentPieces) {

            boolean[][] opponentPossibleMoves = piece.possibleMoves();

            if ( opponentPossibleMoves[kingPosition.getRow()][kingPosition.getColumn()] ) {
                return true;
            }

        }
        return false;
    }

    private boolean testCheckMated(Color kingColor) {
        List<ChessPiece> allyPieces = piecesOnTheBoard.stream()
                .filter(x -> x instanceof ChessPiece && ((ChessPiece) x).getColor() == kingColor)
                .map(x -> (ChessPiece) x)
                .toList();


        if (!hasAnyPossibleMove(allyPieces) && testCheck(kingColor)) {
            this.isCheckMated = true;
            this.winner = (kingColor == Color.WHITE) ? Color.BLACK : Color.WHITE;
            return true;
        }

        return false;
    }

    private boolean[][] getValidMovesWithoutPuttingOwnKingInCheck(ChessPiece piece) {

        boolean[][] newValidPossibleMoves = piece.possibleMoves();
        Position originPosition = piece.getChessPosition().toPosition();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (newValidPossibleMoves[i][j]) {
                    Position targetPosition = new Position(i , j);
                    Piece capturedPiece = makeMove(originPosition, targetPosition );
                    boolean getInCheck = testCheck(piece.getColor());
                    undoMove(originPosition, targetPosition, capturedPiece);
                    newValidPossibleMoves[i][j] = !getInCheck;
                }
            }
        }

        return newValidPossibleMoves;
    }

    private boolean hasAnyPossibleMove(ChessPiece piece){
        boolean[][] possibleMoves = getValidMovesWithoutPuttingOwnKingInCheck(piece);
        for (int i = 0; i < possibleMoves.length; i++) {
            for (int j = 0; j < possibleMoves.length; j++) {
                if (possibleMoves[i][j]) return true;
            }
        }
        return false;
    }

    private boolean hasAnyPossibleMove(List<ChessPiece> pieces) {
        for (ChessPiece piece : pieces) {
            if (hasAnyPossibleMove(piece)) {
                return true;
            }
        }
        return false;
    }

    public void validateTargetPosition(Position source, Position target) {
        if (!possibleMoves(ChessPosition.fromPosition(source))[target.getRow()][target.getColumn()]) {
            throw new ChessException("The target position isn't valid!");
        }
    }

    public boolean[][] possibleMoves(ChessPosition source) {
        return getValidMovesWithoutPuttingOwnKingInCheck((ChessPiece) board.piece(source.toPosition()));
    }

    public void validateSourcePosition(Position source) {
        if (!board.thereIsAPiece(source)) {
            throw new ChessException("There is no piece on source position");
        }
        if (currentPlayer != ((ChessPiece)board.piece(source)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        }
        if (!hasAnyPossibleMove((ChessPiece)board.piece(source))) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    public void validateSourcePosition(ChessPosition source) {
        validateSourcePosition(source.toPosition());
    }

    private void initialSetup() {
        placeNewPiece("e1", new King(board, Color.WHITE));
        placeNewPiece("d1", new Rook(board, Color.WHITE));
        placeNewPiece("h7", new Rook(board, Color.WHITE));
//        placeNewPiece("c1", new Bishop(board, Color.WHITE));
//        placeNewPiece("f1", new Bishop(board, Color.WHITE));
//        placeNewPiece("e1", new Knight(board, Color.WHITE));
//        placeNewPiece("b1", new Knight(board, Color.WHITE));
//        placeNewPiece("d1", new Queen(board, Color.WHITE));

//        for (int i = 0; i < 8; i++) {
//            placeNewPiece((char)(i + 97) + "2", new Pawn(board, Color.WHITE));
//            placeNewPiece((char)(i + 97) + "7", new Pawn(board, Color.BLACK));
//        }
//
        placeNewPiece("a8", new King(board, Color.BLACK));
        placeNewPiece("b8", new Rook(board, Color.BLACK));
//        placeNewPiece("h8", new Rook(board, Color.BLACK));
//        placeNewPiece("c8", new Bishop(board, Color.BLACK));
//        placeNewPiece("f8", new Bishop(board, Color.BLACK));
//        placeNewPiece("g8", new Knight(board, Color.BLACK));
//        placeNewPiece("b8", new Knight(board, Color.BLACK));
//        placeNewPiece("d8", new Queen(board, Color.BLACK));

    }
}