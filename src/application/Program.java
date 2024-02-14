package application;

import boardgame.Position;
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

        String errorMessage = "";
        while (true) {
            try {
                UI.printBoard(match.getPieces());

                if (!errorMessage.equals("")) {
                    System.out.println("\n\n" + UI.ANSI_RED + "* " + errorMessage + " *" + UI.ANSI_RESET);
                }

                System.out.println("\nSource: ");
                ChessPosition source = UI.readChessPosition(sc);


                match.validateSourcePosition(source);

                System.out.println("Target: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = match.performChessMove(source, target);
            } catch (ChessException | InputMismatchException e) {
                errorMessage = e.getMessage();
            }
        }

    }
}
