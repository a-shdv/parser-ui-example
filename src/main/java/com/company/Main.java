package com.company;

import javax.swing.*;


public class Main extends JFrame {
    private JPanel panelMain;
    private JButton buttonFileChooser;

    public Main() {
        buttonFileChooser.addActionListener(e -> {
        });
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.setContentPane(main.panelMain);
        main.pack();
        main.setVisible(true);
        main.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
