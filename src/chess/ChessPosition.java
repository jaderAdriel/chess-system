package chess;

import boardgame.Position;

public class ChessPosition {
    private int row;
    private char column;

    public ChessPosition(int row, char column) {
        if (row < 1 || row > 8 || column < 'a' || column > 'h') {
            throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
        }
        this.row = row;
        this.column = column;
    }

    public ChessPosition(String position) {

        if (position.length() != 2) {
            throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
        }
        int row = Character.getNumericValue(position.charAt(1));
        char column = position.charAt(0);

        if (row < 1 || row > 8 || column < 'a' || column > 'h') {
            throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
        }
        this.row = row;
        this.column = column;
    }

    protected Position toPosition() {
        return new Position(row - 1, (int) column - 97);
    }

    protected Position fromPosition(Position position) {
        return new Position(position.getRow() + 1, (int) position.getColumn() - 97);
    }
}
