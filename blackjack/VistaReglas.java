import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VistaReglas extends JPanel {

    private JButton btnVolver;

    public VistaReglas() {

        setLayout(new BorderLayout());
        setBackground(new Color(0, 70, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("Reglas del Blackjack", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 34));
        titulo.setForeground(new Color(255, 215, 0));
        titulo.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(titulo, BorderLayout.NORTH);

        JTextArea texto = new JTextArea();
        texto.setText(
                "• Objetivo: acercarte a 21 sin pasarte.\n"
              + "• Figuras (J, Q, K) valen 10; As vale 1 u 11.\n"
              + "• Blackjack (As + 10) paga 3:2.\n"
              + "• Opciones: Pedir, Plantarse, Doblar (si aplica).\n"
              + "• El crupier pide con 16 o menos y se planta con 17 o más.\n"
              + "• Si te pasas pierdes la ronda inmediatamente."
        );
        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setFont(new Font("Serif", Font.PLAIN, 20));
        texto.setForeground(Color.WHITE);
        texto.setBackground(new Color(0, 90, 0));
        texto.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(new JScrollPane(texto), BorderLayout.CENTER);

        btnVolver = new JButton("Volver al menú");
        btnVolver.setActionCommand("VOLVER_MENU");
        btnVolver.setFont(new Font("Serif", Font.BOLD, 20));
        btnVolver.setBackground(new Color(55, 20, 20));
        btnVolver.setForeground(new Color(255, 215, 0));
        btnVolver.setBorder(BorderFactory.createLineBorder(new Color(255,215,0), 2));
        btnVolver.setFocusPainted(false);

        JPanel p = new JPanel();
        p.setOpaque(false);
        p.add(btnVolver);
        add(p, BorderLayout.SOUTH);
    }

    public void agregarListeners(ActionListener l) {
        btnVolver.addActionListener(l);
    }
}


