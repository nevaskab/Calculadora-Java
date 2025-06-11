import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.*;

/**
 * @author Andressa Martins
 * @since 11/06/2025
 */

public class CalculadoraGUI extends JFrame implements ActionListener {

    private final Color COR_FUNDO = new Color(0x313D5A);
    private final Color COR_TELA = new Color(0x183642);  
    private final Color COR_TEXTO = new Color(0xEAEAEA);     
    private final Color COR_NUMEROS = new Color(0x73628A); 
    private final Color COR_OPERADORES = new Color(0xCBC5EA); 
    private final Color COR_TEXTO_OPERADORES = new Color(0x183642);
    
    private final JTextField tela;
    private String entradaAtual = "";
    private double num1 = 0, resultado = 0;
    private String operador = "";
    private final DecimalFormat formatador = new DecimalFormat("0.##########");

    public CalculadoraGUI(){
        setTitle("Calculadora");
        setSize(350, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));
        getContentPane().setBackground(COR_FUNDO);

        tela = new JTextField("0");
        tela.setEditable(false);
        tela.setFont(new Font("Segoe UI", Font.BOLD, 48));
        tela.setHorizontalAlignment(JTextField.RIGHT);
        tela.setBackground(COR_TELA);
        tela.setForeground(COR_TEXTO);
        tela.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(tela, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(4, 4, 4, 4);

        String[][] teclas = {
                {"C", "←", "%", "/",},
                {"7", "8", "9", "*",},
                {"4", "5", "6", "-",},
                {"1", "2", "3", "+",},
                {"0", ".", "=",}
        };

        for (int y = 0; y < teclas.length; y++) {
            for (int x = 0; x < teclas[y].length; x++) {
                String text = teclas[y][x];
                gbc.gridx = x;
                gbc.gridy = y;
                gbc.gridwidth = 1; 

                if (text.equals("0")) {
                    gbc.gridwidth = 2;
                }

                JButton tecla = buttonCreate(text);
                buttonPanel.add(tecla, gbc);

                if (text.equals("0")) {
                    x++;
                }
            }
        }
        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton buttonCreate(String texto) {
        JButton tecla = new JButton(texto);
        tecla.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tecla.setFocusPainted(false);
        tecla.addActionListener(this);
        tecla.setBorder(BorderFactory.createEmptyBorder());

        if (texto.matches("[0-9]")) {
            tecla.setBackground(COR_NUMEROS);
            tecla.setForeground(COR_TEXTO);
        } else if (texto.equals(".")) {
            tecla.setBackground(COR_NUMEROS);
            tecla.setForeground(COR_TEXTO);
        } else if (texto.equals("C") || texto.equals("←") || texto.equals("%")) {
            tecla.setBackground(COR_OPERADORES);
            tecla.setForeground(COR_TEXTO_OPERADORES);
        } else { 
            tecla.setBackground(COR_OPERADORES);
            tecla.setForeground(COR_TEXTO_OPERADORES);
        }
        return tecla;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculadoraGUI());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.matches("[0-9]")) {
            pressNumber(comando);
        } else if (comando.equals(".")) {
            pressPoint();
        } else if (comando.equals("C")) {
            pressClear();
        } else if (comando.equals("←")) {
            pressDel();
        } else if (comando.equals("=")) {
            pressEqual();
        } else { 
            pressOperator(comando);
        }
    }

    private void pressNumber(String numero) {
        if (entradaAtual.length() < 12) {
            entradaAtual += numero;
            updateScene(entradaAtual);
        }
    }

    private void pressPoint() {
        if (!entradaAtual.contains(".")) {
            if (entradaAtual.isEmpty()) {
                entradaAtual = "0"; 
            }
            entradaAtual += ".";
            updateScene(entradaAtual);
        }
    }

    private void pressClear() {
        entradaAtual = "";
        operador = "";
        num1 = 0;
        resultado = 0;
        updateScene("0");
    }

    private void pressDel() {
        if (!entradaAtual.isEmpty()) {
            entradaAtual = entradaAtual.substring(0, entradaAtual.length() - 1);
            if (entradaAtual.isEmpty()) {
                updateScene("0");
            } else {
                updateScene(entradaAtual);
            }
        }
    }
    
    private void pressOperator(String op) {
        if (!entradaAtual.isEmpty()) {
            pressEqual();
            num1 = resultado; 
        }
        operador = op;
        entradaAtual = "";
    }
    
    private void pressEqual() {
        if (entradaAtual.isEmpty() || operador.isEmpty()) {
            if (!entradaAtual.isEmpty()) {
                resultado = Double.parseDouble(entradaAtual);
            }
            return;
        }

        double num2 = Double.parseDouble(entradaAtual);
        
        if (operador.equals("/") && num2 == 0) {
            tela.setText("Erro");
            entradaAtual = "";
            operador = "";
            num1 = 0;
            return;
        }
        
        switch (operador) {
            case "+" -> resultado = num1 + num2;
            case "-" -> resultado = num1 - num2;
            case "*" -> resultado = num1 * num2;
            case "/" -> resultado = num1 / num2;
            case "%" -> resultado = num1 * (num2 / 100);
        }

        updateScene(formatador.format(resultado));
        
        num1 = resultado;
        entradaAtual = "";
        operador = ""; 
    }

    private void updateScene(String texto) {
        tela.setText(texto);
    }
}
