package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }

    private void computeValidMoves(boolean[][] mat, int rowIncrement, int columnIncrement) {
        Position p = new Position(position.getRow(), position.getColumn());

        p.setValues(p.getRow() + rowIncrement, p.getColumn() + columnIncrement);

        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
    }
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(position.getRow() - 1, position.getColumn() - 1);

        computeValidMoves(mat, 0, 1);
        computeValidMoves(mat, 1, 1);
        computeValidMoves(mat, -1, 1 );
        computeValidMoves(mat, 0, -1);
        computeValidMoves(mat, 1, -1);
        computeValidMoves(mat, -1, -1);
        computeValidMoves(mat, 1, 0);
        computeValidMoves(mat, -1, 0);

        return mat;
    }
}
