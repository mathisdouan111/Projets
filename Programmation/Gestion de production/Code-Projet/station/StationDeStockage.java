package station;

import model.BoiteStockage;
import model.BoiteStockageMetal;
import model.BoiteStockagePlastique;
import model.Piece;
import model.enumeration.EtatPiece;
import model.enumeration.EtatRemplissage;
import model.enumeration.Materiaux;

import java.util.Collection;
import java.util.Queue;
import java.util.Stack;

// Classe pour la station de stockage
public class StationDeStockage extends Station {

    // capacité maximale des boîtes de stockage
    int CAPACITE_DES_BOITES;
    // convoyeur de sortie
    Queue<Piece> convoyeurDeSortie;
    // Piles de stockage pour les pièces traitées en métal et en plastique
    Stack<BoiteStockageMetal> piecesTraiteesMetalique;
    Stack<BoiteStockagePlastique> piecesTraiteesPlastique;

    /**
     * @author Mathis
     * Constructeur de la classe StationDeStockage.
     * @param convoyeurDeSortie Le convoyeur de sortie.
     * @param piecesTraiteesMetalique La pile de stockage pour les pièces traitées en métal.
     * @param piecesTraiteesPlastique La pile de stockage pour les pièces traitées en plastique.
     * @param CAPACITE_DES_BOITES La capacité maximale des boîtes de stockage.
     */
    public StationDeStockage(Queue<Piece> convoyeurDeSortie,
                             Stack<BoiteStockageMetal> piecesTraiteesMetalique,
                             Stack<BoiteStockagePlastique> piecesTraiteesPlastique,
                             int CAPACITE_DES_BOITES) {
        this.convoyeurDeSortie = convoyeurDeSortie;
        this.piecesTraiteesMetalique = piecesTraiteesMetalique;
        this.piecesTraiteesPlastique = piecesTraiteesPlastique;
        this.CAPACITE_DES_BOITES = CAPACITE_DES_BOITES;
    }

    /**
     * @author Mathis
     * Méthode pour lancer le traitement des pièces.
     */
    @Override
    public void lancerTraitement() {
        while (!convoyeurDeSortie.isEmpty()) {
            Piece piece = getPiece();
            // Si la pièce est traitée, elle est ajoutée à la pile correspondante en fonction de son matériau
            if(piece.getEtatPiece().equals(EtatPiece.TRAITEE)) {
                if (piece.getMateriaux().equals(Materiaux.METAL)) {
                    ajouterPieceMetalique(piece);
                }
                if (piece.getMateriaux().equals(Materiaux.PLASTIQUE)) {
                    ajouterPiecePlastique(piece);
                }
            }
        }
    }

    /**
     * @author Mathis
     * Méthode pour afficher les statistiques des pièces traitées.
     */
    @Override
    public void statistiques() {
        int stockageNum = 1;
        stockageNum = showStats(piecesTraiteesMetalique, stockageNum);
        stockageNum = showStats(piecesTraiteesPlastique, stockageNum);
    }

    /**
     * @author Mathis
     * Méthode pour afficher les statistiques des pièces traitées.
     * @param piecesTraitees La pile de stockage pour les pièces traitées en métal.
     * @param stockageNum Le numéro de stockage.
     * @return Le numéro de stockage mis à jour.
     */
    public static int showStats(Collection piecesTraitees, int stockageNum) {
        System.out.println();
        for (Object stockage : piecesTraitees) {
            BoiteStockage boite = (BoiteStockage) stockage;
            System.out.println("\t -Le stockage numéro " + stockageNum + " contient " + boite.getPieces().size() + " pièces:");
            for (Piece piece : boite.getPieces()) {
                System.out.println("\t\t -"+ piece);
            }
            System.out.println();
            stockageNum++;
        }
        return stockageNum;
    }

    /**
     * @author Mathis
     * Méthode pour avertir lorsque la boîte de stockage est remplie à 80%.
     * @param s Le message d'avertissement.
     */
    private static void avertir80PourcentPlein(String s) {
        System.out.println(s);
    }

    /**
     * @author Mathis
     * Méthode pour ajouter une pièce traitée en plastique à la pile correspondante.
     * @param piece La pièce traitée en plastique à ajouter.
     */
    private void ajouterPiecePlastique(Piece piece) {
        // si la pile de stockage des pièces traitées est vide, on ajoute une boite
        // et on stocke la pièce dans cette boite
        if(piecesTraiteesPlastique.isEmpty()){
            BoiteStockagePlastique boiteP = new BoiteStockagePlastique(CAPACITE_DES_BOITES);
            piecesTraiteesPlastique.push(boiteP);
            piecesTraiteesPlastique.peek().ajouterPiece(piece);
        }
        // sinon on vérifie si la dernière boite est pleine
        // si oui on ajoute une boite
        // et on stocke la pièce dans cette boite
        else if(piecesTraiteesPlastique.peek().getEtatRemplissage().equals(EtatRemplissage.PLEINE)){
            System.out.println("**** AVERTISSEMENT ****");
            System.out.println("La boîte pour plastique est pleine.");
            System.out.println("**** AVERTISSEMENT ****");
            BoiteStockagePlastique boiteP = new BoiteStockagePlastique(CAPACITE_DES_BOITES);
            piecesTraiteesPlastique.push(boiteP);
            piecesTraiteesPlastique.peek().ajouterPiece(piece);
        }else{
            // on vérifie si le taux de remplissage atteint 80%
            if(piecesTraiteesPlastique.peek().getTauxRemplissage() >= 0.8F){
                System.out.println("**** AVERTISSEMENT ****");
                avertir80PourcentPlein("La boîte pour plastique atteint plus que 80%.");
                System.out.println("**** AVERTISSEMENT ****");
            }
            // sinon on stocke la pièce dans la dernière boite insérée dans la pile
            piecesTraiteesPlastique.peek().ajouterPiece(piece);
        }
    }

    /**
     * @author Mathis
     * Méthode pour ajouter une pièce traitée en métal à la pile correspondante.
     * @param piece La pièce traitée en métal à ajouter.
     */
    private void ajouterPieceMetalique(Piece piece) {
        // si la pile de stockage des pièces traitées est vide, on ajoute une boite
        // et on stocke la pièce dans cette boite
        if(piecesTraiteesMetalique.isEmpty()){
            BoiteStockageMetal boiteM = new BoiteStockageMetal(CAPACITE_DES_BOITES);
            piecesTraiteesMetalique.push(boiteM);
            piecesTraiteesMetalique.peek().ajouterPiece(piece);
        }
        // sinon on vérifie si la dernière boite est pleine
        // si oui on ajoute une boite et on stocke la pièce dans cette boite
        else if(piecesTraiteesMetalique.peek().getEtatRemplissage().equals(EtatRemplissage.PLEINE)){
            System.out.println("**** AVERTISSEMENT ****");
            System.out.println("La boîte pour métal est pleine.");
            System.out.println("**** AVERTISSEMENT ****");
            BoiteStockageMetal boiteM = new BoiteStockageMetal(CAPACITE_DES_BOITES);
            piecesTraiteesMetalique.push(boiteM);
            piecesTraiteesMetalique.peek().ajouterPiece(piece);
        }else{
            // on vérifie si le taux de remplissage atteint 80%
            if(piecesTraiteesMetalique.peek().getTauxRemplissage() >= 0.8F){
                System.out.println("**** AVERTISSEMENT ****");
                avertir80PourcentPlein("La boîte pour métal atteint plus que 80%.");
                System.out.println("**** AVERTISSEMENT ****");
            }
            // sinon on stocke la pièce dans la dernière boite insérée dans la pile
            piecesTraiteesMetalique.peek().ajouterPiece(piece);
        }
    }

    // Méthode pour récupérer une pièce de la file d'attente
    public Piece getPiece() {
        return this.convoyeurDeSortie.poll();
    }

}
