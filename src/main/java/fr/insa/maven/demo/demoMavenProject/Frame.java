package fr.insa.maven.demo.demoMavenProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Frame extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private JTextField textField;

    public Frame() {
        // Configuration de la fenêtre principale

        setTitle("Gestion des demandes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Modèle de liste pour stocker les demandes
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(list);


        // Champ de texte pour saisir une nouvelle demande
        textField = new JTextField(20);

        // Boutons Add et Suppr
        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Suppr");

        // ActionListener pour le bouton "Add"


        // ActionListener pour le bouton "Suppr"
        removeButton.addActionListener(e -> {
            int selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1) {
                listModel.remove(selectedIndex);
            }
        });

        // Mise en place des composants dans le panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(textField, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Ajout du panel à la fenêtre
        add(panel);
    }




}
