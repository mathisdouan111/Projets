import model.BoiteStockage;
import model.BoiteStockageMetal;
import model.BoiteStockagePlastique;
import model.Piece;
import station.Station;
import station.StationDeStockage;
import station.StationDeTraitement;
import util.FileReader;

import java.util.*;

public class GestionProduction {
    // temps de traitement maximum autorisé pour une pièce en secondes
    public static final int TEMPS_DE_TRAITEMENT_MAX_AUTORISE = 5;
    // capacité maximale des boîtes de stockage
    public static final int CAPACITE_DES_BOITES = 8;

    public static void main(String[] args) {
        // lecture des pièces à partir d'un fichier
        // et ajout dans le convoyeur d'entrée
        Queue<Piece> convoyeurDEntree = FileReader.readFile("pieces.txt");

        // convoyeur de sortie sous forme de file d'attente
        Queue<Piece> convoyeurDeSortie = new ArrayDeque<>();

        // piles de stockage pour les pièces traitées et non traitées
        Stack<BoiteStockageMetal> piecesTraiteesMetalique = new Stack<>();
        Stack<BoiteStockagePlastique> piecesTraiteesPlastique = new Stack<>();
        Stack<BoiteStockage> piecesNonTraitees = new Stack<>();

        // création des stations de traitement et de stockage
        Station stationDeTraitement = new StationDeTraitement(convoyeurDEntree,
                convoyeurDeSortie,
                piecesNonTraitees,
                TEMPS_DE_TRAITEMENT_MAX_AUTORISE,
                CAPACITE_DES_BOITES);
        Station stationDeStockage = new StationDeStockage(convoyeurDeSortie,
                piecesTraiteesMetalique,
                piecesTraiteesPlastique,
                CAPACITE_DES_BOITES);

        // lancement du traitement et du stockage
        stationDeTraitement.lancerTraitement();
        // afficher le nombre de pièces traitées
        stationDeTraitement.statistiques();
        stationDeStockage.lancerTraitement();
        // afficher le contenu de chaque boite de stockage des pièces traitées métalliques et plastiques
        stationDeStockage.statistiques();

        // affichage des statistiques des pièces non traitées
        showStats(piecesNonTraitees);
    }

    /**
     * @author Mathis
     * Affiche les statistiques des pièces non traitées.
     * @param piecesNonTraitees La pile de stockage pour les pièces non traitées.
     */
    public static void showStats(Stack<BoiteStockage> piecesNonTraitees) {
        System.out.println();
        int nombreTotal = 0;
        // calcul du nombre total de pièces non traitées
        for (BoiteStockage stockage : piecesNonTraitees) {
            nombreTotal += stockage.getPieces().size();
        }
        System.out.println("Pièces non traitées: " + nombreTotal);
        // affichage des détails des pièces non traitées
        for (BoiteStockage stockage : piecesNonTraitees) {
            for (Piece piece : stockage.getPieces()) {
                System.out.println("\t\t -"+ piece);
            }
            System.out.println();
        }
    }
}
