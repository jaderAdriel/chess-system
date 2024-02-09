package boardgame;

public class Board {
    private final int rows = 8;
    private final int columns = 8;
    private Piece[][] pieces;

    public Board() {
        this.pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
