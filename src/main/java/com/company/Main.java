package com.company;

import javax.swing.*;
import java.io.File;


public class Main extends JFrame {
    private JPanel panelMain;
    private JButton buttonFileChooser;
    private JTextField textFieldFirstCol;
    private JTextField textFieldSecondCol;
    private JTextField textFieldThirdCol;
    private JLabel labelSelectedFile;

    public Main() {
        buttonFileChooser.addActionListener(e -> {
            JFileChooser fileopen = new JFileChooser();
            int result = fileopen.showDialog(null, "Browse files");
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                labelSelectedFile.setText(file.getName());
            }
        });
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.setContentPane(main.panelMain);
        main.pack();
        main.setSize(800, 400);
        main.setVisible(true);
        main.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
