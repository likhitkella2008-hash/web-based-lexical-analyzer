import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

public class AILexicalAnalyzer extends JFrame {

    JTextArea codeArea;
    JTextArea errorArea;
    JTextArea correctedArea;

    JTable tokenTable;
    DefaultTableModel tokenModel;

    JLabel keywordLabel;
    JLabel identifierLabel;
    JLabel numberLabel;
    JLabel operatorLabel;

    int keywordCount = 0;
    int identifierCount = 0;
    int numberCount = 0;
    int operatorCount = 0;

    public AILexicalAnalyzer() {

        setTitle("AI Based Lexical Analyzer");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // TOP PANEL

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(22, 27, 34));

        JLabel title = new JLabel("AI Based Lexical Analyzer");
        title.setForeground(new Color(88, 166, 255));
        title.setFont(new Font("Consolas", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JButton analyzeButton = new JButton("Analyze Code");
        analyzeButton.setBackground(new Color(35, 134, 54));
        analyzeButton.setForeground(Color.WHITE);
        analyzeButton.setFont(new Font("Consolas", Font.BOLD, 16));

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(analyzeButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // CODE AREA

        codeArea = new JTextArea();
        codeArea.setBackground(new Color(13, 17, 23));
        codeArea.setForeground(Color.WHITE);
        codeArea.setCaretColor(Color.WHITE);
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 18));

        codeArea.setText(
                "innt a = 10\n\n" +
                "flot b = 5.5\n\n" +
                "if(a > 5){\n" +
                "    a = a + 1\n" +
                "}"
        );

        JScrollPane codeScroll = new JScrollPane(codeArea);
        codeScroll.setPreferredSize(new Dimension(1400, 300));

        add(codeScroll, BorderLayout.CENTER);

        // OUTPUT PANEL

        JPanel outputPanel = new JPanel(new GridLayout(1, 4));

        // TOKEN TABLE

        tokenModel = new DefaultTableModel();
        tokenModel.addColumn("Line");
        tokenModel.addColumn("Type");
        tokenModel.addColumn("Value");

        tokenTable = new JTable(tokenModel);
        tokenTable.setBackground(new Color(22, 27, 34));
        tokenTable.setForeground(Color.WHITE);
        tokenTable.setGridColor(Color.GRAY);
        tokenTable.setFont(new Font("Consolas", Font.PLAIN, 14));
        tokenTable.setRowHeight(25);

        JScrollPane tokenScroll = new JScrollPane(tokenTable);

        JPanel tokenPanel = new JPanel(new BorderLayout());
        tokenPanel.setBackground(new Color(13, 17, 23));

        JLabel tokenTitle = new JLabel("Tokens");
        tokenTitle.setForeground(new Color(88, 166, 255));
        tokenTitle.setFont(new Font("Consolas", Font.BOLD, 24));

        tokenPanel.add(tokenTitle, BorderLayout.NORTH);
        tokenPanel.add(tokenScroll, BorderLayout.CENTER);

        // ERROR PANEL

        errorArea = new JTextArea();
        errorArea.setBackground(new Color(13, 17, 23));
        errorArea.setForeground(new Color(255, 123, 114));
        errorArea.setFont(new Font("Consolas", Font.PLAIN, 16));

        JScrollPane errorScroll = new JScrollPane(errorArea);

        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setBackground(new Color(13, 17, 23));

        JLabel errorTitle = new JLabel("Errors");
        errorTitle.setForeground(new Color(88, 166, 255));
        errorTitle.setFont(new Font("Consolas", Font.BOLD, 24));

        errorPanel.add(errorTitle, BorderLayout.NORTH);
        errorPanel.add(errorScroll, BorderLayout.CENTER);

        // CORRECTED CODE PANEL

        correctedArea = new JTextArea();
        correctedArea.setBackground(new Color(22, 27, 34));
        correctedArea.setForeground(new Color(88, 166, 255));
        correctedArea.setFont(new Font("Consolas", Font.PLAIN, 16));

        JScrollPane correctedScroll = new JScrollPane(correctedArea);

        JPanel correctedPanel = new JPanel(new BorderLayout());
        correctedPanel.setBackground(new Color(13, 17, 23));

        JLabel correctedTitle = new JLabel("Corrected Code");
        correctedTitle.setForeground(new Color(88, 166, 255));
        correctedTitle.setFont(new Font("Consolas", Font.BOLD, 24));

        correctedPanel.add(correctedTitle, BorderLayout.NORTH);
        correctedPanel.add(correctedScroll, BorderLayout.CENTER);

        // STATISTICS PANEL

        JPanel statsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        statsPanel.setBackground(new Color(13, 17, 23));

        keywordLabel = createStatLabel("Keywords : 0");
        identifierLabel = createStatLabel("Identifiers : 0");
        numberLabel = createStatLabel("Numbers : 0");
        operatorLabel = createStatLabel("Operators : 0");

        statsPanel.add(keywordLabel);
        statsPanel.add(identifierLabel);
        statsPanel.add(numberLabel);
        statsPanel.add(operatorLabel);

        JPanel statsContainer = new JPanel(new BorderLayout());
        statsContainer.setBackground(new Color(13, 17, 23));

        JLabel statsTitle = new JLabel("Statistics");
        statsTitle.setForeground(new Color(88, 166, 255));
        statsTitle.setFont(new Font("Consolas", Font.BOLD, 24));

        statsContainer.add(statsTitle, BorderLayout.NORTH);
        statsContainer.add(statsPanel, BorderLayout.CENTER);

        // ADD PANELS

        outputPanel.add(tokenPanel);
        outputPanel.add(errorPanel);
        outputPanel.add(correctedPanel);
        outputPanel.add(statsContainer);

        add(outputPanel, BorderLayout.SOUTH);

        // BUTTON ACTION

        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeCode();
            }
        });

        setVisible(true);
    }

    // STAT LABEL

    public JLabel createStatLabel(String text) {

        JLabel label = new JLabel(text, SwingConstants.CENTER);

        label.setOpaque(true);
        label.setBackground(new Color(22, 27, 34));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Consolas", Font.BOLD, 18));

        return label;
    }

    // ANALYZE CODE

    public void analyzeCode() {

        tokenModel.setRowCount(0);
        errorArea.setText("");
        correctedArea.setText("");

        keywordCount = 0;
        identifierCount = 0;
        numberCount = 0;
        operatorCount = 0;

        String code = codeArea.getText();

        String[] lines = code.split("\\n");

        Pattern keywordPattern = Pattern.compile("^(int|float|if|else|while|return)$");
        Pattern numberPattern = Pattern.compile("^[0-9]+(\\.[0-9]+)?$");
        Pattern identifierPattern = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
        Pattern operatorPattern = Pattern.compile("^[+\\-*/=<>]+$");

        StringBuilder correctedCode = new StringBuilder();

        for(int i = 0; i < lines.length; i++) {

            String line = lines[i];
            String correctedLine = line;

            // AUTO CORRECTIONS

            correctedLine = correctedLine.replace("innt", "int");
            correctedLine = correctedLine.replace("flot", "float");
            correctedLine = correctedLine.replace("retun", "return");
            correctedLine = correctedLine.replace("whille", "while");
            correctedLine = correctedLine.replace("pritnf", "printf");

            // MISSING SEMICOLON

            String trim = correctedLine.trim();

            if(!trim.isEmpty() &&
               !trim.endsWith(";") &&
               !trim.contains("{") &&
               !trim.contains("}") &&
               !trim.startsWith("if") &&
               !trim.startsWith("while")) {

                correctedLine += ";";
            }

            correctedCode.append(correctedLine).append("\n");

            // ERRORS

            if(line.contains("innt")) {
                errorArea.append("Invalid keyword 'innt' at line " + (i+1) + "\n");
                errorArea.append("Suggestion : int\n\n");
            }

            if(line.contains("flot")) {
                errorArea.append("Invalid keyword 'flot' at line " + (i+1) + "\n");
                errorArea.append("Suggestion : float\n\n");
            }

            if(!line.trim().isEmpty() &&
               !line.trim().endsWith(";") &&
               !line.contains("{") &&
               !line.contains("}") &&
               !line.trim().startsWith("if") &&
               !line.trim().startsWith("while")) {

                errorArea.append("Missing semicolon ';' at line " + (i+1) + "\n\n");
            }

            // TOKENIZATION

            String[] tokens = line.split("\\s+");

            for(String token : tokens) {

                if(token.isEmpty())
                    continue;

                token = token.replace(";", "");
                token = token.replace("(", "");
                token = token.replace(")", "");
                token = token.replace("{", "");
                token = token.replace("}", "");

                if(token.isEmpty())
                    continue;

                if(keywordPattern.matcher(token).matches()) {

                    tokenModel.addRow(new Object[]{i+1, "KEYWORD", token});
                    keywordCount++;
                }

                else if(numberPattern.matcher(token).matches()) {

                    tokenModel.addRow(new Object[]{i+1, "NUMBER", token});
                    numberCount++;
                }

                else if(operatorPattern.matcher(token).matches()) {

                    tokenModel.addRow(new Object[]{i+1, "OPERATOR", token});
                    operatorCount++;
                }

                else if(identifierPattern.matcher(token).matches()) {

                    tokenModel.addRow(new Object[]{i+1, "IDENTIFIER", token});
                    identifierCount++;
                }
            }
        }

        correctedArea.setText(correctedCode.toString());

        if(errorArea.getText().isEmpty()) {
            errorArea.setText("No Lexical Errors");
        }

        keywordLabel.setText("Keywords : " + keywordCount);
        identifierLabel.setText("Identifiers : " + identifierCount);
        numberLabel.setText("Numbers : " + numberCount);
        operatorLabel.setText("Operators : " + operatorCount);
    }

    // MAIN

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AILexicalAnalyzer();
            }
        });
    }
}
