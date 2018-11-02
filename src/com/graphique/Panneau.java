package com.graphique;

import com.singer.personal.Equipement;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;


public class Panneau extends JPanel {
    public static boolean equipement_is_missing = false;
    public static boolean humain_detecte = false;
    public static boolean humainS_detecte = false;
    public static boolean equipement_introuvable = false;


    public static String nom = "nom";
    public static String prenom = "prenom";
    public static String profession = "profession";


    public static ArrayList<Equipement> equipements_manquants = new ArrayList<>();

    public void paintComponent(Graphics g) {
        //On choisit une couleur de fond pour le rectangle
        g.setColor(Color.white);
        //On le dessine de sorte qu'il occupe toute la surface
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Font font = new Font("Courier", Font.BOLD, 30);
        g.setFont(font);
        g.setColor(Color.BLACK);

        int width_middle = this.getWidth() / 3;


        if (!humain_detecte){
            g.drawString("Veuillez passer sous le portique",width_middle,60);
        }else{
            if (humainS_detecte){
                g.drawString("Trops d'humains ont été detectés, veuillez reculer",width_middle,60);
            }else{
                g.drawString("Bonjour monsieur " + prenom +" "+nom, width_middle, 60);
                g.drawString("Votre profession est : "+ profession,width_middle, 100);

                if (equipement_is_missing) {
                    g.drawString("Il vous manque les équipements suivants :", width_middle, 120);
                    for (int i = 0; i < equipements_manquants.size(); i++) {
                        g.drawString("-" + equipements_manquants.get(i).getNom(), width_middle, 160 + i * 30);
                    }
                    if (equipement_introuvable){
                        g.drawString("Désolé, nous n'arrivons pas à détecter vos équipements manquants",width_middle,200);
                    }
                } else {
                    g.drawString("Vous avez tous vos équipements, bonne journée", width_middle, 140);
                }
            }

        }


    }
}
