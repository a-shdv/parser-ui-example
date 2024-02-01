package com.company;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.FileSystems;
import java.text.DecimalFormat;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main extends JFrame {
    static String length;
    static String lengthFiltered = "";
    static String encoderState = "";
    private Logger log = LoggerFactory.getLogger(Main.class);
    private JPanel panelMain;
    private JButton buttonFileChooser;
    private JTextField textFieldFirstCol;
    private JTextField textFieldSecondCol;
    private JTextField textFieldThirdCol;
    private JLabel labelSelectedFile;
    private JButton buttonParse;
    private File file;

    public Main() {
        buttonFileChooser.addActionListener(e -> {
            JFileChooser fileopen = new JFileChooser();
            fileopen.setCurrentDirectory(new File(System.getProperty("user.home") + FileSystems.getDefault().getSeparator() + "Desktop"));
            int result = fileopen.showDialog(null, "Browse files");
            if (result == JFileChooser.APPROVE_OPTION) {
                file = fileopen.getSelectedFile();
                labelSelectedFile.setText(file.getName());
            }
        });
        buttonParse.addActionListener(e -> {
            String firstColText = textFieldFirstCol.getText(), secondColText = textFieldSecondCol.getText(), thirdColText = textFieldThirdCol.getText();
            if (firstColText.isBlank() || secondColText.isBlank() || thirdColText.isBlank()
                    || file == null){
                JOptionPane.showMessageDialog(this, "Please, choose a file and fill in the textfields!");
            }
            parseFile(file, firstColText, secondColText, thirdColText);
        });
    }

    private void parseFile(File file, String firstColText, String secondColText, String thirdColText) {
        CompletableFuture.runAsync(() -> {
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream("C:\\Users\\Антон\\Desktop\\result.xlsx");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("parsed-data");

            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            Row rowInit = sheet.createRow(0);
            rowInit.createCell(0).setCellValue("Speed");
            rowInit.createCell(1).setCellValue("Length");
            rowInit.createCell(2).setCellValue("Length smoothed");
            rowInit.createCell(3).setCellValue("Time");

            try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                String line;
                int step = 1;
                while ((line = br.readLine()) != null) {

                    // Time
                    String time = line.split(" ")[2];

                    // length
                    Matcher lengthMatcher = Pattern.compile(firstColText).matcher(line);
                    if (lengthMatcher.find()) {
                        length = lengthMatcher.group(1);
                        System.out.println("---------------------------");
                        System.out.printf("Step: %d\n", step);
                        System.out.printf("Length: %s\n", length);
                        continue;
                    }

                    // smoothed length
                    Matcher lengthSmoothedMatcher = Pattern.compile(secondColText).matcher(line);
                    if (lengthSmoothedMatcher.find()) {
                        lengthFiltered = lengthSmoothedMatcher.group(1);
                        System.out.println("Length smoothed: " + lengthFiltered);
                        continue;
                    }

                    // encoder state
                    Matcher encoderStateMatcher = Pattern.compile("Encoder state: (\\d)").matcher(line);
                    if (encoderStateMatcher.find()) {
                        encoderState = encoderStateMatcher.group(1);
                        System.out.println("Encoder state: " + encoderState);
                        continue;
                    }

                    // speed
                    String speed = "";
                    Matcher speedMatcher = Pattern.compile(thirdColText).matcher(line);
                    if (speedMatcher.find()) {
                        speed = decimalFormat.format(Double.parseDouble(speedMatcher.group(1)));
                        System.out.println("Speed: " + speed);
                        System.out.println("Date: " + time);
                        System.out.println("---------------------------");
                    } else continue;

                    if (encoderState.equals("0")) {
                        Row rowNext = sheet.createRow(step);
                        Cell cell1 = rowNext.createCell(0);
                        cell1.setCellValue(speed);

                        Cell cell2 = rowNext.createCell(1);
                        cell2.setCellValue(length);

                        Cell cell3 = rowNext.createCell(2);
                        cell3.setCellValue(lengthFiltered);

                        Cell cell4 = rowNext.createCell(3);
                        cell4.setCellValue(time);

                        step++;
                        workbook.write(outputStream);
                    }
                }
                workbook.close();
                outputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage());
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
