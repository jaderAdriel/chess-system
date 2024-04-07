package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
    public Pawn(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "P";
    }

    private void computeValidMoves(boolean[][] mat, int rowIncrement) {
        Position p = new Position(position.getRow(), position.getColumn());

        p.setValues(p.getRow() + rowIncrement, p.getColumn());

        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        for (int i = 1; i <= 2 ; i++) {
            p.setColumn(i);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mvWhite = new boolean[getBoard().getRows()][getBoard().getColumns()];
        boolean[][] mvBlack = new boolean[getBoard().getRows()][getBoard().getColumns()];

        computeValidMoves(mvWhite, 1);
        computeValidMoves(mvBlack, -1);

        if (getColor() == Color.WHITE && this.position.getRow() == 1) {
            computeValidMoves(mvWhite, 2);
        }

        if (getColor() == Color.BLACK && this.position.getRow() == 6) {
            computeValidMoves(mvWhite, -2);
        }


        return (getColor() == Color.WHITE) ? mvWhite : mvBlack ;
    }
}
