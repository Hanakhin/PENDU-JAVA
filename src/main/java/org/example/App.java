package org.example;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {
  private List<String> mots;
  private Pendu jeuPendu;

  public static void main(String[] args) {
    App app = new App();
    app.chargerMots();
    app.start();
  }

  public void start() {
    System.out.println("Bienvenue au jeu du Pendu!");
    Scanner scan = new Scanner(System.in);
    String choix = "";

    while (!choix.equals("3")) {
      afficherMenu();
      choix = scan.nextLine().toUpperCase();
      switch (choix) {
        case "1":
          jouerPendu();
          break;
        case "2":
          System.out.println("Au revoir");
          System.exit(0);
          break;
        default:
          System.out.println("Choix invalide");
          break;
      }
    }
  }

  private void afficherMenu() {
    System.out.println("\nQue voulez-vous faire ?");
    System.out.println("1 - Jouer au Pendu");
    System.out.println("2 - Quitter");
    System.out.print("Votre choix : ");
  }

  private void chargerMots() {
    mots = new ArrayList<>();
    JSONParser parser = new JSONParser();
    try (FileReader reader = new FileReader(getClass().getResource("/file/json/Mots.json").getPath())) {
      JSONArray jsonArray = (JSONArray) parser.parse(reader);
      for (Object obj : jsonArray) {
        if (obj instanceof String) {
          mots.add((String) obj);
        }
      }
    } catch (Exception e) {
      System.out.println("Erreur lors du chargement des mots : " + e.getMessage());
    }
  }

  private void jouerPendu() {
    if (mots.isEmpty()) {
      System.out.println("Aucun mot disponible. Impossible de jouer.");
      return;
    }
    Random random = new Random();
    String motChoisi = mots.get(random.nextInt(mots.size()));
    jeuPendu = new Pendu(motChoisi);
    jeuPendu.jouer();
  }
}

class Pendu {
  private String motADeviner;
  private StringBuilder motActuel;
  private int erreurs;
  private static final int MAX_ERREURS = 7;

  public Pendu(String mot) {
    this.motADeviner = mot.toUpperCase();
    this.motActuel = new StringBuilder("_".repeat(motADeviner.length()));
    this.erreurs = 0;
  }

  public void jouer() {
    Scanner scanner = new Scanner(System.in);
    while (erreurs < MAX_ERREURS && motActuel.toString().contains("_")) {
      System.out.println("\nMot actuel : " + motActuel);
      System.out.println("Erreurs : " + erreurs + "/" + MAX_ERREURS);
      System.out.print("Proposez une lettre : ");
      String input = scanner.nextLine().toUpperCase();
      if (input.length() != 1) {
        System.out.println("Veuillez entrer une seule lettre.");
        continue;
      }
      char lettre = input.charAt(0);
      if (motADeviner.indexOf(lettre) != -1) {
        for (int i = 0; i < motADeviner.length(); i++) {
          if (motADeviner.charAt(i) == lettre) {
            motActuel.setCharAt(i, lettre);
          }
        }
      } else {
        erreurs++;
        System.out.println("Cette lettre n'est pas dans le mot.");
      }
    }

    if (erreurs == MAX_ERREURS) {
      System.out.println("Perdu ! Le mot était : " + motADeviner);
    } else {
      System.out.println("Gagné ! Le mot était : " + motADeviner);
    }
  }
}
