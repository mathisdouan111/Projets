package model;

import model.enumeration.EtatRemplissage;

import java.util.Stack;

public class BoiteStockage {
    // Pile pour stocker les pièces
    Stack<Piece> pieces;
    // État de remplissage de la boîte
    EtatRemplissage etatRemplissage;
    // Taux de remplissage de la boîte
    Float tauxRemplissage;
    // Capacité maximale de la boîte
    Integer capacite;

    /**
     * @author Mathis
     * Constructeur de la classe BoiteStockage.
     * @param capacite La capacité maximale de la boîte de stockage.
     */
    public BoiteStockage(int capacite) {
        // Initialisation de la pile de pièces
        this.pieces = new Stack<>();
        // Initialisation de l'état de remplissage à VIDE
        this.etatRemplissage = EtatRemplissage.VIDE;
        // Initialisation du taux de remplissage à 0
        this.tauxRemplissage = 0F;
        // Initialisation de la capacité de la boîte
        this.capacite = capacite;
    }

    /**
     * @author Mathis
     * Méthode pour ajouter une pièce à la boîte.
     * @param piece La pièce à ajouter.
     */
    public void ajouterPiece(Piece piece) {
        // ajout de la pièce à la pile
        this.pieces.push(piece);
        // mise à jour de l'état de remplissage à PARTIELLEMENT_PLEINE
        this.etatRemplissage = EtatRemplissage.PARTIELLEMENT_PLEINE;
        // si la taille de la pile atteint la capacité de la boîte
        if(this.pieces.size() == capacite){
            // mise à jour de l'état de remplissage à PLEINE
            this.etatRemplissage = EtatRemplissage.PLEINE;
            // mise à jour du taux de remplissage à 1 (100%)
            this.tauxRemplissage = 1F;
        }else{
            // sinon
            // mise à jour du taux de remplissage en fonction de la taille de la pile et de la capacité de la boîte
            this.tauxRemplissage =  ((float)this.pieces.size()) / ((float)(this.capacite));
        }
    }

    // Getters
    public Stack<Piece> getPieces() {
        return pieces;
    }
    public EtatRemplissage getEtatRemplissage() {
        return etatRemplissage;
    }
    public Float getTauxRemplissage() {
        return tauxRemplissage;
    }
}

