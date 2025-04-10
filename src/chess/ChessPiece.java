package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.ChessPosition;

public abstract class ChessPiece extends Piece {
    private Color color;
    private int moveCount = 0;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p.getColor() != color;
    }

    public abstract String getType();

    public Color getColor() {
        return color;
    }

    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(this.position);
    }

    public int getMoveCount() {
        return moveCount;
    }

    protected void increaseMove() {
        this.moveCount++;
    }

    protected void decreaseMove() {
        this.moveCount--;
    }
}
