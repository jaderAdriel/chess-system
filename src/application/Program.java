package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ChessMatch match = new ChessMatch();

        while (true) {
            try {
                UI.printBoard(match.getPieces());

                System.out.println("\nSource: ");
                ChessPosition source = UI.readChessPosition(sc);

                System.out.println("Target: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = match.performChessMove(source, target);
            } catch (ChessException e) {
                System.out.println(e.getMessage());
                sc.next();
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
