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
        if (!positionExists(row, column)) {
            throw new BoardException("Position do not exists");
        }
        return this.pieces[row][column];
    }
    public Piece piece(Position position) {
        return this.pieces[position.getRow()][position.getColumn()];
    }

    public Piece removePiece(Position position) {
        if (!positionExists(position) || piece(position) == null){
            throw new BoardException("Position not on the board");
        }

        Piece aux = piece(position);
        aux.position = null;
        pieces[position.getRow()][position.getColumn()] = null;

        return aux;
    }

    public void placePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)) {
            throw new BoardException("Position already occupied by another piece");
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    private boolean positionExists(int row, int col) {
        return (row < this.rows && row >= 0) && (col < this.columns && col >= 0);
    }
    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    public boolean thereIsAPiece(Position position) {
        if (!positionExists(position.getRow(), position.getColumn())) {
            throw new BoardException("Position do not exists");
        }
        return piece(position) != null;
    }
}
