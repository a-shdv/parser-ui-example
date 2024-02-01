package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.FileSystems;


public class Main extends JFrame {
    private JPanel panelMain;
    private JButton buttonFileChooser;
    private JTextField textFieldFirstCol;
    private JTextField textFieldSecondCol;
    private JTextField textFieldThirdCol;
    private JLabel labelSelectedFile;
    private JButton buttonParse;

    public Main() {
        buttonFileChooser.addActionListener(e -> {
            JFileChooser fileopen = new JFileChooser();
            fileopen.setCurrentDirectory(new File(System.getProperty("user.home") + FileSystems.getDefault().getSeparator() + "Desktop"));
            int result = fileopen.showDialog(null, "Browse files");
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                labelSelectedFile.setText(file.getName());
            }
        });
        buttonParse.addActionListener(e -> {
            if (textFieldFirstCol.getText().isBlank() || textFieldSecondCol.getText().isBlank() || textFieldThirdCol.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Please, fill in the textfield!");
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
