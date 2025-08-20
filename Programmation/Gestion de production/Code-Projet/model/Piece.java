package model;


import model.enumeration.EtatPiece;
import model.enumeration.Materiaux;
import model.enumeration.TypeMateriaux;

public class Piece {

    // Identifiant de la pièce
    String id;

    // Description de la pièce
    String description;

    // Matériaux utilisés pour fabriquer la pièce
    Materiaux materiaux;

    // Poids de la pièce
    Float poids;

    // État actuel de la pièce
    EtatPiece etatPiece;

    // Temps nécessaire pour traiter la pièce
    Integer tempsTraitement;

    // Type de matériaux utilisés
    TypeMateriaux typeMateriaux;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Materiaux getMateriaux() {
        return materiaux;
    }

    public void setMateriaux(Materiaux materiaux) {
        this.materiaux = materiaux;
    }

    public Float getPoids() {
        return poids;
    }

    public void setPoids(Float poids) {
        this.poids = poids;
    }

    public EtatPiece getEtatPiece() {
        return etatPiece;
    }

    public void setEtatPiece(EtatPiece etatPiece) {
        this.etatPiece = etatPiece;
    }

    public Integer getTempsTraitement() {
        return tempsTraitement;
    }

    public void setTempsTraitement(Integer tempsTraitement) {
        this.tempsTraitement = tempsTraitement;
    }

    public TypeMateriaux getTypeMateriaux() {
        return typeMateriaux;
    }

    public void setTypeMateriaux(TypeMateriaux typeMateriaux) {
        this.typeMateriaux = typeMateriaux;
    }

    @Override
    public String toString() {
        return "La pièce: (" + id + ") "  + description + " de poids " + poids + " est composée de " + materiaux;
    }
}
