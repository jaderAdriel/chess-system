package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
    ChessMatch match;
    public Pawn(Board board, Color color, ChessMatch match) {
        super(board, color);
        this.match = match;
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


        int[] sides = {-1, 2};
        for (int i = 0; i  < 2 ; i++) {
            p.setColumn(p.getColumn() + sides[i]);
            if ( Math.abs(p.getRow() - this.getChessPosition().toPosition().getRow()) == 2 ) continue;
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        if (match.getEnPassantVulnerable() == null ) return;
        ChessPiece enPassantVulnerable = match.getEnPassantVulnerable();

        if (this.getColor() == enPassantVulnerable.getColor()) return;

        if ( this.getChessPosition().getRow() == enPassantVulnerable.getChessPosition().getRow() ) {

            Position side = enPassantVulnerable.getChessPosition().toPosition();
            if (Math.abs(side.getColumn() - this.getChessPosition().toPosition().getColumn()) == 1 ) {
                mat[side.getRow() + rowIncrement][side.getColumn()] = true;
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
            computeValidMoves(mvBlack, -2);
        }


        return (getColor() == Color.WHITE) ? mvWhite : mvBlack ;
    }
}
