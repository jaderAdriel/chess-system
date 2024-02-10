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

    public Piece piece(int row, int column) {
        return this.pieces[row][column];
    }
    public Piece piece(Position position) {
        return this.pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position) {
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }
}
