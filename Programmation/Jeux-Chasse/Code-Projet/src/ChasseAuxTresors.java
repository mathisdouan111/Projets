import java.io.*;
import java.util.Scanner;
import java.util.Random;

public class ChasseAuxTresors {
    // Variables globales pour le jeu
    private static String nomDuJoueur;
    private static int tailleDeLaGrille;
    private static String niveauDeDifficulte;
    private static Grille[][] grilleDeJeu;
    private static int score;
    private static int tentativesRestantes;
    private static int nombreDeTresorTrouvee;
    private static int nombreDeTresors;
    private static String fichierScores = "sauvegardes.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        String choix = "oui";

        while ("oui".equals(choix)|| "o".equals(choix)){

            // Initialisation du jeu
            initialiserJeu();

            String[] meilleurScore;

            // Boucle de jeu principale
            while (tentativesRestantes > 0 && nombreDeTresorTrouvee < (nombreDeTresors * 2)) {
                afficherGrille();
                faireUneTentative();
            }
            sauvegarderScore(fichierScores, nomDuJoueur, score);

            // afficher la grille à la fin
            afficherGrille();
            System.out.println("");
            sauvegarderScore(fichierScores, nomDuJoueur, score);

            //afficher le meilleur score et par quel utilisateur
            meilleurScore = meilleurScore(fichierScores);
            System.out.println("==== Meilleur score : " + meilleurScore[0]+" ====");
            System.out.println("==== Par l'utilisateur : " + nomDuJoueur+" ====");

            // Fin du jeu
            System.out.println("Fin du jeu. Votre score final est : " + score);
            System.out.print("Voulez vous continuer a jouer Oui/Non ?");
            choix = scanner.next().trim().toLowerCase();
        }
    }

    private static void initialiserJeu() {
        score = 0;
        nombreDeTresorTrouvee = 0;

        System.out.println("Bienvenue dans Chasse aux trésors!");
        if(nomDuJoueur == null){
            System.out.print("Donnez votre nom: ");
            nomDuJoueur = scanner.nextLine();
        }

        System.out.print("Veuillez entrer la taille de la grille: ");
        tailleDeLaGrille = scanner.nextInt();
        tentativesRestantes = tailleDeLaGrille * 3;

        System.out.print("Veuillez choisir un niveau de difficulté (Facile, Moyen, Difficile): ");
        niveauDeDifficulte = scanner.next();

        // Initialisation de la grille de jeu
        grilleDeJeu = new Grille[tailleDeLaGrille][tailleDeLaGrille];

        initierGrille();
        // Placement des trésors et des pièges
        placerTresorsEtPieges();
    }

    /**
     * cette methode consiste à placer les tressors et les peige
     */
    private static void placerTresorsEtPieges() {
        Random rand = new Random();

        // Calculer le nombre de trésors et de pièges en fonction du niveau de difficulté
        int nombreDePieges;
        switch (niveauDeDifficulte.toLowerCase()) {
            case "moyen":
                nombreDeTresors = (int) Math.ceil((tailleDeLaGrille * tailleDeLaGrille * 0.15));
                nombreDePieges = (int) Math.ceil((tailleDeLaGrille * tailleDeLaGrille * 0.15));
                break;
            case "difficile":
                nombreDeTresors = (int) Math.ceil((tailleDeLaGrille * tailleDeLaGrille * 0.1));
                nombreDePieges = (int) Math.ceil((tailleDeLaGrille * tailleDeLaGrille * 0.2));
                break;
            default: //  Facile
                nombreDeTresors = (int) Math.ceil((tailleDeLaGrille * tailleDeLaGrille * 0.2));
                nombreDePieges = (int) Math.ceil((tailleDeLaGrille * tailleDeLaGrille * 0.1));
                break;
        }


        // System.out.println("nombreDePieges "  + nombreDePieges);
        // System.out.println("nombreDeTresors "  + nombreDeTresors);

        // Placer les trésors
        int[] premiereMoitieInsereeX = new int[nombreDeTresors];
        int[] premiereMoitieInsereeY = new int[nombreDeTresors];

        // Placer les trésors - Première moitié
        for (int i = 0; i < nombreDeTresors; i++) {
            int ligne = rand.nextInt(tailleDeLaGrille);
            int colonne = rand.nextInt(tailleDeLaGrille);
            // Assurez-vous que la case est vide
            if (grilleDeJeu[ligne][colonne].type == GrilleType.VIDE) {
                grilleDeJeu[ligne][colonne].type = GrilleType.TRESOR;
                premiereMoitieInsereeX[i] = ligne;
                premiereMoitieInsereeY[i] = colonne;

            } else {
                i--;  // Réessayer si la case n'est pas vide
            }
        }

        // Boucler sur les trésors insérés dans la première moitié
        for (int i = 0; i < nombreDeTresors; i++) {
            int ligne = premiereMoitieInsereeX[i];
            int colonne = premiereMoitieInsereeY[i];
            // Boucler pour placer les trésors autour de chaque trésor inséré dans la première moitié
            for (int j = 0; j < 4; j++) {
                switch (j) {
                    case 0: // À droite
                        colonne++;
                        break;
                    case 1: // En bas
                        ligne++;
                        break;
                    case 2: // À gauche
                        colonne--;
                        break;
                    case 3: // En haut
                        ligne--;
                        break;
                }
                // Vérifier si la case est valide et vide
                if (ligne >= 0 && ligne < tailleDeLaGrille && colonne >= 0 && colonne < tailleDeLaGrille && grilleDeJeu[ligne][colonne].type == GrilleType.VIDE) {
                    grilleDeJeu[ligne][colonne].type = GrilleType.TRESOR;
                    break; // Sortir de la boucle car le trésor a été placé
                }
            }
        }


        // Placer les pièges
        for (int i = 0; i < nombreDePieges; i++) {
            int ligne = rand.nextInt(tailleDeLaGrille);
            int colonne = rand.nextInt(tailleDeLaGrille);
            // Assurez-vous que la case est vide
            if (grilleDeJeu[ligne][colonne].type == GrilleType.VIDE) {
                grilleDeJeu[ligne][colonne].type = GrilleType.PIEGE;  // 2 représente un piège
            } else {
                i--;  // Réessayer si la case n'est pas vide
            }
        }
    }

    private static void initierGrille() {
        for (int i = 0; i < tailleDeLaGrille; i++) {
            for (int j = 0; j < tailleDeLaGrille; j++) {
                grilleDeJeu[i][j] = new Grille();
            }
        }
    }

    private static void afficherGrille() {
        for (int i = 0; i < tailleDeLaGrille; i++) {

            for (int k = 0; k < tailleDeLaGrille; k++) {
                System.out.print("--------");
            }

            System.out.println("");

            for (int j = 0; j < tailleDeLaGrille; j++) {
                System.out.print("|" + grilleDeJeu[i][j]);
            }

            System.out.println("|");

        }

        for (int k = 0; k < tailleDeLaGrille; k++) {
            System.out.print("--------");
        }
    }

    private static void faireUneTentative() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("");

        System.out.print("Donnez i :");
        int ligne = scanner.nextInt();

        System.out.print("Donnez j :");
        int colonne = scanner.nextInt();

        // Vérifier si les coordonnées sont valides
        if (ligne < 0 || ligne >= tailleDeLaGrille || colonne < 0 || colonne >= tailleDeLaGrille) {
            System.out.println("Coordonnées invalides. Veuillez réessayer.");
            return;
        }

        // Vérifier si la case est déjà visitée
        if (grilleDeJeu[ligne][colonne].afficher) {
            System.out.println("Désolé, cette case est déjà occupée , choisir une autre.");
            return;
        }

        // Vérifier ce qui se trouve à ces coordonnées
        switch (grilleDeJeu[ligne][colonne].type) {
            case VIDE:
                break;
            case TRESOR:
                System.out.println("vous avez trouve la moitie du tresor");
                nombreDeTresorTrouvee++;
                score += 5;
                break;
            case PIEGE:
                score -= 10;
                break;
        }

        // Marquer la case comme visitée
        grilleDeJeu[ligne][colonne].afficher = true;

        tentativesRestantes--;
        System.out.println("Tentatives restantes : " + tentativesRestantes);
        System.out.println("Score : " + score);
    }

    public static void sauvegarderScore(String fichierScores, String username, int score) {
        String[][] tableauScores = chargerScores(fichierScores);
        boolean utilisateurExiste = false;

        // Parcourir le tableau pour vérifier si l'utilisateur existe déjà
        for (String[] ligne : tableauScores) {
            if (ligne[0].equals(username)) {
                utilisateurExiste = true;
                int ancienScore = Integer.parseInt(ligne[1]);
                if (score > ancienScore) {
                    ligne[1] = String.valueOf(score);
                }
                break;
            }
        }

        // Si l'utilisateur n'existe pas, ajouter une nouvelle entrée
        if (!utilisateurExiste) {
            String[] nouvelleEntree = {username, String.valueOf(score)};
            tableauScores = ajouterEntree(tableauScores, nouvelleEntree);
        }

        // Sauvegarder le tableau mis à jour dans le fichier
        sauvegarderTableau(fichierScores, tableauScores);
    }

    public static String[][] chargerScores(String fichierScores) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fichierScores));
             BufferedReader reader2 = new BufferedReader(new FileReader(fichierScores))) {
            String ligne;
            int nombreLignes = 0;
            while ((ligne = reader.readLine()) != null) {
                nombreLignes++;
            }

            String[][] tableauScores = new String[nombreLignes][2];
            int index = 0;
            reader.close();
            while ((ligne = reader2.readLine()) != null) {
                String[] elements = ligne.split(";");
                tableauScores[index][0] = elements[0];
                tableauScores[index][1] = elements[1];
                index++;
            }
            return tableauScores;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[][] ajouterEntree(String[][] tableau, String[] entree) {
        String[][] nouveauTableau = new String[tableau.length + 1][2];
        for (int i = 0; i < tableau.length; i++) {
            nouveauTableau[i] = tableau[i];
        }
        nouveauTableau[tableau.length] = entree;
        return nouveauTableau;
    }

    public static void sauvegarderTableau(String fichierScores, String[][] tableau) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichierScores))) {
            for (String[] ligne : tableau) {
                writer.write(ligne[0] + ";" + ligne[1] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] meilleurScore(String fichierScores) {

        File scoreFile = new File(fichierScores);

        // créer le fichier si n'existe pas
        try {
            scoreFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String meilleurUtilisateur = null;
        int meilleurScore = -1000;

        try (BufferedReader reader = new BufferedReader(new FileReader(fichierScores))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] elements = ligne.split(";");
                String utilisateur = elements[0];
                int score = Integer.parseInt(elements[1]);
                if (score > meilleurScore) {
                    meilleurScore = score;
                    meilleurUtilisateur = utilisateur;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String[]{String.valueOf(meilleurScore), meilleurUtilisateur};
    }


}