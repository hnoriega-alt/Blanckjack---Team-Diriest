import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Blackjack Royale");
        setSize(980, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Cambia al panel pasado como único contenido.
     * Simple, claro y efectivo para este proyecto.
     */
    public void mostrarPanel(JPanel panel) {
        getContentPane().removeAll();
        add(panel);
        revalidate();
        repaint();
    }
}
