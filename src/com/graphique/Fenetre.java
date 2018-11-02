package com.graphique;

import javax.swing.*;
import java.awt.*;

public class Fenetre extends JFrame {

    public Fenetre() {
        // TODO Auto-generated constructor stub
        this.setTitle("detecteur d'EPI");
        this.setSize(1500, 1500);
        this.setLocationRelativeTo(null);

        //Instanciation d'un objet JPanel
        JPanel pan = new Panneau();

        //On prévient notre JFrame que notre JPanel sera son content pane
        this.setContentPane(pan);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
