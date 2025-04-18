package application;

import boardgame.Position;
import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ChessMatch match = new ChessMatch();

        List<ChessPiece> captured = new ArrayList<>();

        String errorMessage = "";
        while (!match.isCheckMated()) {
            try {
                UI.printMatch(match, captured);

                if (!errorMessage.isEmpty()) {
                    System.out.println("\n\n" + UI.ANSI_RED + "* " + errorMessage + " *" + UI.ANSI_RESET);
                }

                System.out.println("\nSource: ");
                ChessPosition source = UI.readChessPosition(sc);

                match.validateSourcePosition(source);

                UI.printBoard(match.getPieces(), match.possibleMoves(source));

                System.out.println("\nTarget: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = match.performChessMove(source, target);

                if (capturedPiece != null) captured.add(capturedPiece);

                if (match.getPromoted() != null)  {
                    System.out.println("Enter piece for promotion (B/N/R/Q): ");
                    String type = sc.nextLine();
                    match.promote(type);
                }

            } catch (ChessException | InputMismatchException e) {
                errorMessage = e.getMessage();
                continue;
            }
            errorMessage = "";
        }
        UI.printEndMessage(match.getPieces(), match.getWinner());
    }
}
