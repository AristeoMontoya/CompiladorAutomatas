package com.compiler.ui;

import com.compiler.process.CompilerPipeline;

import com.compiler.process.analizers.LexicalAnalyzer;
import com.compiler.process.analizers.SemanticAnalyzer;
import com.compiler.process.analizers.SyntacticAnalyzer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Window extends JFrame {

    private JPanel southPanel;
    private JTextArea editorTextArea;
    private JTextArea consoleTextArea;
    private JTextArea quadrupletsTextArea;
    private JToolBar toolBar;
    private JButton startButton;
    private JTabbedPane tabbedPane;
    private JScrollPane consoleScrollPane;
    private JScrollPane quadrupletsScrollPane;
    private JScrollPane editorScrollPane;
    private JScrollPane identifiersScrollPane;
    private JTable identifiersTable;
    private DefaultTableModel tableModel;
    private String header[];
    private boolean isIconAvailable = true;

    // TODO: Is it worth replacing this with log4j for such a small project?
    private Logger logger = Logger.getLogger(this.getClass().toString());

    public Window() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unable to load look and feel");
        }
        setTitle("Compilador");
        setSize(800, 600);
        try {
            setIconImage(loadIcon("icono_codigo.png"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El programa se ejecutará sin íconos", "Error al cargar íconos", JOptionPane.PLAIN_MESSAGE);
            isIconAvailable = false;
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        initialize();
    }

    private void initialize() {
        editorTextArea = new JTextArea();
        editorTextArea.setTabSize(2);
        editorTextArea.setFont(new Font("Andale Mono", Font.PLAIN, 12));
        editorScrollPane = new JScrollPane(editorTextArea);
        this.add(editorScrollPane, BorderLayout.CENTER);

        tabbedPane = new JTabbedPane();
        southPanel = new JPanel(new BorderLayout());
        southPanel.add(tabbedPane, BorderLayout.CENTER);
        southPanel.setPreferredSize(new Dimension(10, 200));
        this.add(southPanel, BorderLayout.SOUTH);

        consoleTextArea = new JTextArea();
        consoleTextArea.setEditable(false);
        consoleScrollPane = new JScrollPane(consoleTextArea);
        tabbedPane.add(consoleScrollPane, "Consola");


        header = new String[]{"Nombre", "Valor", "Tipo de dato", "Posición", "Alcance"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(header);
        identifiersTable = new JTable(tableModel);

        identifiersScrollPane = new JScrollPane(identifiersTable);

        tabbedPane.add(identifiersScrollPane, "Tabla de identificadores");

        quadrupletsTextArea = new JTextArea();
        quadrupletsTextArea.setEditable(false);
        quadrupletsTextArea.setFont(new Font("Andale Mono", Font.PLAIN, 12));
        quadrupletsScrollPane = new JScrollPane(quadrupletsTextArea);
        tabbedPane.add(quadrupletsScrollPane, "Cuadruplos");

        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        startButton = new JButton("Iniciar proceso");
        startButton.addActionListener(e -> runProcess());

        if (isIconAvailable) {
            tabbedPane.setIconAt(0, new ImageIcon(this.getClass().getClassLoader().getResource("icono_consola.png")));
            tabbedPane.setIconAt(1, new ImageIcon(this.getClass().getClassLoader().getResource("icono_lista.png")));
            startButton.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("icono_compilar.png")));
        }

        toolBar.add(startButton);
        this.add(toolBar, BorderLayout.NORTH);
    }

    private void runProcess() {
        String inputCode = editorTextArea.getText();

        CompilerPipeline compiler = CompilerPipeline
                .of(new LexicalAnalyzer())
                .withNextPipe(new SyntacticAnalyzer())
                .withNextPipe(new SemanticAnalyzer());

        compiler.runPipeline(inputCode);

        if (!compiler.getErrors().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            compiler.getErrors().forEach(builder::append);
            showCompilationResults(builder.toString());
        } else {
            showCompilationResults("");
            fillInTable(compiler.getTable());
            showQuadruplets(compiler.getQuadruplets());
        }
    }

    private void showCompilationResults(String resultado) {
        if (!resultado.isEmpty()) {
            consoleTextArea.setText(resultado);
        } else {
            consoleTextArea.setText("Proceso finalizado con éxito. No se encontraron errores");
        }
    }

    private void fillInTable(ArrayList<String[]> registros) {
        clearTable(identifiersTable.getRowCount() - 1);
        for (String[] registro : registros) {
            tableModel.addRow(registro);
        }
    }

    private void showQuadruplets(String cuadruplos) {
        String textoCuadruplos = String.format("| %-10s | %-10s | %-10s | %-10s |\n", "Operador", "Operando1", "Operando2", "Resultado");
        textoCuadruplos += cuadruplos;
        quadrupletsTextArea.setText(textoCuadruplos);
    }

    private void clearTable(int row) {
        if (row >= 0) {
            tableModel.removeRow(row);
            clearTable(row - 1);
        }
    }

    private BufferedImage loadIcon(String path) {
        try {
            InputStream imageInputStream = this.getClass().getClassLoader().getResourceAsStream(path);
            return ImageIO.read(imageInputStream);

        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}