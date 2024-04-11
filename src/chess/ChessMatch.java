package chess;

import application.UI;
import boardgame.Board;
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

        if (testCheck(opponent)) {
            this.isInCheck = true;
            testCheckMated(opponent);
        };

        if (!isCheckMated) nextTurn();


        return (ChessPiece) capturedPiece;
    }

    private void nextTurn() {
        this.turn++;
        this.currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece, MoveType moveType) {


        if (moveType == MoveType.CASTLING) {
            undoCastle(source, target);
            return;
        }

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
        MoveType moveType = determineMoveType(source, target);

        if (moveType == MoveType.CASTLING) {
            castle(source, target);
            return null;
        }

        return makeRegularMove(source, target);
    }

    private MoveType determineMoveType(Position source, Position target) {

        if (isCastlingMove(source, target)) {
            return MoveType.CASTLING;
        }

//        if (isEnPassantMove(source, target)) {
//            return MoveType.EN_PASSANT;
//        }
//
//        if (isPromotionMove(source, target)) {
//            return MoveType.PROMOTION;
//        }

        return MoveType.REGULAR;
    }

    private boolean isCastlingMove(Position source, Position target) {
        ChessPiece piece = (ChessPiece) board.piece(source);

        if ( !(piece instanceof King)) return false;

        if ( target.getColumn() == 6) return canKingSideCastle(piece.getColor());

        if ( target.getColumn() == 2) return canQueenSideCastle(piece.getColor());

        return false;
    }

//    private boolean isEnPassantMove(Position source, Position target) {
//    }

//    private boolean isPromotionMove(Position source, Position target) {
//    }

    private Piece makeRegularMove(Position source, Position target) {
        ChessPiece piece = (ChessPiece) board.removePiece(source);
        piece.increaseMove();
        Piece capturedPiece = null;

        if (board.thereIsAPiece(target)) {
            capturedPiece = board.removePiece(target);
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        board.placePiece(piece, target);

        return capturedPiece;
    }

    private Position findKing(Color color) {
        for (Piece piece : piecesOnTheBoard) {
            if ( ((ChessPiece)piece).getColor() == color &&  piece instanceof King ) {
                return ((King) piece).getChessPosition().toPosition();
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    public boolean isInCheck() {
        return isInCheck;
    }

    private boolean testCheck(Color color) {
        Position kingPosition = findKing(color);
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

        if (piece instanceof King) {
            newValidPossibleMoves = getValidCastlingPositions(piece.getColor(), newValidPossibleMoves);
        }

        Position source = piece.getChessPosition().toPosition();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (newValidPossibleMoves[i][j]) {

                    Position target = new Position(i , j);
                    MoveType moveType = isCastlingMove(source, target) ? MoveType.CASTLING : null;

                    Piece capturedPiece = makeMove(source, target );
                    boolean getInCheck = testCheck(piece.getColor());
                    newValidPossibleMoves[i][j] = !getInCheck;

                    undoMove(source, target, capturedPiece, moveType);
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

    private boolean canCastle(Position source, Position rookPosition) {


        
        ChessPiece king = (ChessPiece) board.piece(source);


        if (testCheck(king.getColor())) return false;

        ChessPiece rook = (ChessPiece) board.piece(rookPosition);
        if (rook == null ) return false;

        if (!(rook.getMoveCount() == 0 && king.getMoveCount() == 0)) return false;

        int columnDistance = source.getColumn() - rookPosition.getColumn();
        int x = (columnDistance > 0) ? -1 : 1;


        for (int i = 1; i < Math.abs(columnDistance); i++) {
            char targetFile = (char) (king.getChessPosition().getColumn() + (i * x));
            String targetPos = targetFile + ((king.getColor() == Color.WHITE) ? "1" : "8");
            Position pos = new ChessPosition(targetPos).toPosition();

            makeRegularMove(source, pos);
            boolean getsInCheck = testCheck(king.getColor());
            System.out.println(targetPos);
            undoMove(source, pos, null, MoveType.REGULAR);

            if (board.thereIsAPiece(pos) || getsInCheck) {
                return false;
            }
        }
        return true;
    }

    private boolean canKingSideCastle(Color kingColor) {
        Position kingPosition = findKing(kingColor);
        Position castleSidePosition = new Position(kingPosition.getRow() , 7);

        if (!(board.piece(kingPosition.getRow() , 7) instanceof Rook)) return false;
        return canCastle(kingPosition, castleSidePosition);
    }

    private boolean canQueenSideCastle(Color kingColor) {
        Position kingPosition = findKing(kingColor);

        Position castleSidePosition = new Position(kingPosition.getRow() , 0);

        if (!(board.piece(kingPosition.getRow() , 0) instanceof Rook)) return false;

        return canCastle(kingPosition, castleSidePosition);
    }

    private boolean[][] getValidCastlingPositions(Color kingColor, boolean[][] moves) {
        int row = (kingColor == Color.WHITE) ? 1 : 8;

        if (canKingSideCastle(kingColor)) {
            Position kingSidePos = new ChessPosition("g" + row).toPosition();
            moves[kingSidePos.getRow()][kingSidePos.getColumn()] = true;
        }

        if (canQueenSideCastle(kingColor)) {
            Position queenSidePos = new ChessPosition("c" + row).toPosition();
            moves[queenSidePos.getRow()][queenSidePos.getColumn()] = true;
        }

        return moves;
    }

    private void castle(Position source, Position target) {
        ChessPiece king = (ChessPiece) board.removePiece(source);
        int row = (king.getColor() == Color.WHITE) ? 0 : 7;

        if (target.getColumn() == 2) {
            ChessPiece rook = (ChessPiece) board.removePiece(new Position(row, 0));
            rook.increaseMove();
            board.placePiece(rook, new Position(row, 3));
        }
        if (target.getColumn() == 6) {
            ChessPiece rook = (ChessPiece) board.removePiece(new Position(row, 7));
            rook.increaseMove();
            board.placePiece(rook, new Position(row, 5));
        }

        king.increaseMove();
        board.placePiece(king, target);
    }

    private void undoCastle(Position source, Position target) {

        source = findKing((source.getRow() == 0) ? Color.WHITE : Color.BLACK );

        ChessPiece king = (ChessPiece) board.removePiece(source);

        int row = (king.getColor() == Color.WHITE) ? 0 : 7;

        if (source.getColumn() == 2) {
            ChessPiece rook = (ChessPiece) board.removePiece(new Position(row, 3));
            rook.decreaseMove();
            board.placePiece(rook, new Position(row, 0));
        }
        if (source.getColumn() == 6) {
            ChessPiece rook = (ChessPiece) board.removePiece(new Position(row, 5));
            rook.decreaseMove();
            board.placePiece(rook, new Position(row, 7));
        }

        king.decreaseMove();

        board.placePiece(king, new Position((king.getColor() == Color.WHITE) ? 0 : 7, 4));
    }

    private void initialSetup() {
        placeNewPiece("e1", new King(board, Color.WHITE));
        placeNewPiece("a1", new Rook(board, Color.WHITE));
        placeNewPiece("h1", new Rook(board, Color.WHITE));

        placeNewPiece("e8", new King(board, Color.BLACK));
        placeNewPiece("a8", new Rook(board, Color.BLACK));
        placeNewPiece("h8", new Rook(board, Color.BLACK));
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
//        placeNewPiece("a8", new King(board, Color.BLACK));
//        placeNewPiece("b8", new Rook(board, Color.BLACK));
//        placeNewPiece("h8", new Rook(board, Color.BLACK));
//        placeNewPiece("c8", new Bishop(board, Color.BLACK));
//        placeNewPiece("f8", new Bishop(board, Color.BLACK));
//        placeNewPiece("g8", new Knight(board, Color.BLACK));
//        placeNewPiece("b8", new Knight(board, Color.BLACK));
//        placeNewPiece("d8", new Queen(board, Color.BLACK));

    }
}