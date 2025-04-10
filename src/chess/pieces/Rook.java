package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {
    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "R";
    }

    @Override
    public String getType() {
        return "Rook";
    }

    private void computeValidMoves(boolean[][] mat, int rowIncrement, int columnIncrement) {
        Position p = new Position(position.getRow(), position.getColumn());

        p.setValues(p.getRow() + rowIncrement, p.getColumn() + columnIncrement);

        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() + rowIncrement, p.getColumn() + columnIncrement);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        computeValidMoves(mat, 0, 1);
        computeValidMoves(mat, 0, -1);
        computeValidMoves(mat, 1, 0);
        computeValidMoves(mat, -1, 0);

        return mat;
    }
}
