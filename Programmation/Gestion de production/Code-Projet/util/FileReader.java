package util;

import model.Piece;
import model.enumeration.EtatPiece;
import model.enumeration.Materiaux;
import model.enumeration.TypeMateriaux;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public class FileReader {

    public static Queue<Piece> readFile(String filePath) {
        Queue<Piece> pieces = new ArrayDeque<>();

        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            br.readLine();
            System.out.println("Liste des pièces chargées: ");
            System.out.println("----------------------------------------");
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) {
                    System.out.println(line);
                    Piece piece = new Piece();
                    piece.setId(data[0].trim());
                    piece.setDescription(data[1].trim());
                    piece.setMateriaux(Materiaux.valueOf(data[2].trim().toUpperCase()));
                    piece.setPoids(Float.parseFloat(data[3].trim()));
                    piece.setEtatPiece(EtatPiece.valueOf(data[4].trim().toUpperCase()));
                    piece.setTempsTraitement(Integer.parseInt(data[5].trim()));
                    piece.setTypeMateriaux(TypeMateriaux.valueOf(data[6].trim().toUpperCase()));
                    pieces.offer(piece);
                } else {
                    System.out.println("format de la ligne non valide: " + line);
                }
            }
            System.out.println();
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Impossible de trouver le fichier " + filePath);
        }

        return pieces;
    }
}
