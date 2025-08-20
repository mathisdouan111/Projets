package station;

import model.BoiteStockage;
import model.Piece;
import model.enumeration.EtatPiece;
import model.enumeration.EtatRemplissage;

import java.util.Queue;
import java.util.Stack;

// Classe pour la station de traitement
public class StationDeTraitement extends Station {

    // Capacité maximale des boîtes de stockage
    int CAPACITE_DES_BOITES;
    // Temps de traitement maximum autorisé pour une pièce en secondes
    int TEMPS_DE_TRAITEMENT_MAX_AUTORISE;
    // Convoyeurs d'entrée et de sortie
    Queue<Piece> convoyeurDEntree;
    Queue<Piece> convoyeurDeSortie;

    // Pile de stockage pour les pièces non traitées
    Stack<BoiteStockage>  piecesNonTraitees;
    // Compteur pour le nombre de pièces traitées
    int nombre_de_pieces_traitees;

    /**
     * @author Mathis
     * Constructeur de la classe StationDeTraitement.
     * @param convoyeurDEntree Le convoyeur d'entrée.
     * @param convoyeurDeSortie Le convoyeur de sortie.
     * @param piecesNonTraitees La pile de stockage pour les pièces non traitées.
     * @param TEMPS_DE_TRAITEMENT_MAX_AUTORISE Le temps de traitement maximum autorisé pour une pièce.
     * @param CAPACITE_DES_BOITES La capacité maximale des boîtes de stockage.
     */
    public StationDeTraitement(Queue<Piece> convoyeurDEntree,
                               Queue<Piece> convoyeurDeSortie,
                               Stack<BoiteStockage> piecesNonTraitees,
                               int TEMPS_DE_TRAITEMENT_MAX_AUTORISE,
                               int CAPACITE_DES_BOITES) {
        this.convoyeurDEntree = convoyeurDEntree;
        this.convoyeurDeSortie = convoyeurDeSortie;
        this.piecesNonTraitees = piecesNonTraitees;
        this.TEMPS_DE_TRAITEMENT_MAX_AUTORISE = TEMPS_DE_TRAITEMENT_MAX_AUTORISE;
        this.CAPACITE_DES_BOITES = CAPACITE_DES_BOITES;
    }

    /**
     * @author Mathis
     * Méthode pour lancer le traitement des pièces.
     */
    @Override
    public void lancerTraitement() {
        nombre_de_pieces_traitees = 0;
        // tant que le convoyeur d'entrée est non vide
        while (!convoyeurDEntree.isEmpty()) {
            Piece piece = getPiece();
            System.out.println("----------------------------------------");
            System.out.println("Démarrage du traitement de la pièce: " + piece.getId());
            System.out.println("La pièce: " + piece.getId() + " - est dans un état: EN_COURS");

            // Vérifier si le temps de traitement dépasse le maximum autorisé
            if (piece.getTempsTraitement() > TEMPS_DE_TRAITEMENT_MAX_AUTORISE) {
                // la pièce dans ce cas est non traitée
                piece.setEtatPiece(EtatPiece.NON_TRAITEE);
                System.out.println("Le traitement de la pièce " + piece.getId() + " est terminé avec un état: NON_TRAITEE");
                System.out.println("Temps dépasse le maximum: " + piece.getTempsTraitement());
                // ajoute la pièce à la pile de stockage des pièces non traitées
                ajouterPieceNonTraitee(piece);
            } else {
                // la pièce est traitée
                piece.setEtatPiece(EtatPiece.TRAITEE);
                // incrementer le nombre de pièces traitées
                nombre_de_pieces_traitees++;
                System.out.println("Le traitement de la pièce " + piece.getId() + " est terminé avec un état: TRAITEE");
                System.out.println("Temp de traitement pour: " + piece.getId() + " est: " + piece.getTempsTraitement());
                // ajouter la pièce à la file d'attente de sortie
                convoyeurDeSortie.offer(piece);
            }
            System.out.println("----------------------------------------");
        }
    }

    /**
     * @author Mathis
     * Méthode pour afficher les statistiques des pièces traitées.
     */
    @Override
    public void statistiques() {
        System.out.println("****************************************");
        System.out.println("Pièces traitées: " + nombre_de_pieces_traitees);
        System.out.println();
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
     * Méthode pour ajouter une pièce non traitée à la pile de stockage.
     * @param piece La pièce non traitée à ajouter.
     */
    private void ajouterPieceNonTraitee(Piece piece) {
        // si la pile de stockage des pièces non traitées est vide, on ajoute une boite
        // et on stocke la pièce dans cette boite
        if(piecesNonTraitees.isEmpty()){
            BoiteStockage boiteP = new BoiteStockage(CAPACITE_DES_BOITES);
            piecesNonTraitees.push(boiteP);
            piecesNonTraitees.peek().ajouterPiece(piece);
        }
        // sinon on vérifie si la dernière boite est pleine
        // si oui on ajoute une boite et on stocke la pièce dans cette boite
        else if(piecesNonTraitees.peek().getEtatRemplissage().equals(EtatRemplissage.PLEINE)){
            System.out.println("**** AVERTISSEMENT ****");
            System.out.println("La boîte pour plastique est pleine.");
            System.out.println("**** AVERTISSEMENT ****");
            BoiteStockage boiteP = new BoiteStockage(CAPACITE_DES_BOITES);
            piecesNonTraitees.push(boiteP);
            piecesNonTraitees.peek().ajouterPiece(piece);
        }else{
            // on vérifie si le taux de remplissage atteint 80%
            if(piecesNonTraitees.peek().getTauxRemplissage() >= 0.8F){
                System.out.println("**** AVERTISSEMENT ****");
                avertir80PourcentPlein("La boîte pour les pièces non traitées atteint plus que 80%.");
                System.out.println("**** AVERTISSEMENT ****");
            }
            // sinon on stocke la pièce dans la dernière boite insérée dans la pile
            piecesNonTraitees.peek().ajouterPiece(piece);
        }
    }
    /**
     * @author Mathis
     * Méthode pour récupérer une pièce de la file d'attente.
     * @return La pièce récupérée.
     */
    public Piece getPiece() {
        return this.convoyeurDEntree.poll();
    }

}
