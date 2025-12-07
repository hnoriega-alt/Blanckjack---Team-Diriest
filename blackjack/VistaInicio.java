import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VistaInicio extends JPanel {

    private JButton btnJugar;
    private JButton btnReglas;
    private JButton btnCreditos;

    private final Font fuenteTitulo = new Font("Serif", Font.BOLD, 42);
    private final Font fuenteBoton = new Font("Serif", Font.BOLD, 22);

    public VistaInicio() {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 70, 0)); // verde casino

        JLabel titulo = new JLabel("Blackjack Royale", SwingConstants.CENTER);
        titulo.setFont(fuenteTitulo);
        titulo.setForeground(new Color(255, 215, 0));
        titulo.setBorder(BorderFactory.createEmptyBorder(36, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JLabel subt = new JLabel("<html><center>Mesa clásica • Reglas estándar<br>Pulsa Jugar para comenzar</center></html>", SwingConstants.CENTER);
        subt.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subt.setForeground(Color.WHITE);
        add(subt, BorderLayout.CENTER);

        JPanel centro = new JPanel(new GridLayout(3, 1, 14, 14));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(10, 260, 100, 260));

        btnJugar = crearBoton("Jugar", "JUGAR");
        btnReglas = crearBoton("Reglas", "REGLAS");
        btnCreditos = crearBoton("Créditos", "CREDITOS");

        centro.add(btnJugar);
        centro.add(btnReglas);
        centro.add(btnCreditos);

        add(centro, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, String cmd) {
        JButton b = new JButton(texto);
        b.setActionCommand(cmd);
        b.setFont(fuenteBoton);
        b.setBackground(new Color(55, 20, 20));
        b.setForeground(new Color(255, 215, 0));
        b.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(220, 64));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { b.setBackground(new Color(90,20,20)); }
            public void mouseExited(java.awt.event.MouseEvent evt)  { b.setBackground(new Color(55,20,20)); }
        });
        return b;
    }

    public void agregarListeners(ActionListener l) {
        btnJugar.addActionListener(l);
        btnReglas.addActionListener(l);
        btnCreditos.addActionListener(l);
    }
}
