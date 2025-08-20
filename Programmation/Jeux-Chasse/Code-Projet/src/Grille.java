public class Grille {

    public GrilleType type;
    public boolean afficher;

    public Grille() {
        this.afficher = false;
        this.type = GrilleType.VIDE;
    }

    @Override
    public String toString() {
        String toStr = "       ";
        if(afficher) {
            switch (type) {
                case VIDE:
                    toStr ="Neutre!";
                    break;
                case TRESOR:
                    toStr ="Tresor!";
                    break;
                case PIEGE:
                    toStr ="Pieges!";
                    break;
            }
        }
        return toStr;
    }
}
