import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interfaz gráfica del Blackjack.
 * Muestra las cartas, los botones y el saldo del jugador.
 */
public class VistaJuego extends JFrame {

    private JButton btnPedir;
    private JButton btnPlantarse;
    private JButton btnDoblar;
    private JButton btnNuevaRonda;
    private JButton btnApostar;

    private JTextField campoApuesta;
    private JLabel etiquetaSaldo;
    private JLabel etiquetaMensaje;

    private JPanel panelJugador;
    private JPanel panelCrupier;

    public VistaJuego() {
        setTitle("Blackjack - Proyecto POO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel superior (información del crupier) ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Crupier"));
        panelCrupier = new JPanel(new FlowLayout());
        panelSuperior.add(panelCrupier, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // --- Panel central (cartas del jugador) ---
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Tu mano"));
        panelJugador = new JPanel(new FlowLayout());
        panelCentral.add(panelJugador, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // --- Panel inferior (controles y mensajes) ---
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        add(panelInferior, BorderLayout.SOUTH);

        // --- Zona de apuesta y saldo ---
        JPanel panelApuesta = new JPanel(new FlowLayout());
        panelApuesta.add(new JLabel("Apuesta:"));
        campoApuesta = new JTextField("100", 6);
        panelApuesta.add(campoApuesta);
        btnApostar = new JButton("Confirmar apuesta");
        panelApuesta.add(btnApostar);

        etiquetaSaldo = new JLabel("Saldo: 1000");
        panelApuesta.add(etiquetaSaldo);
        panelInferior.add(panelApuesta, BorderLayout.NORTH);

        // --- Botones de acción ---
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 10, 10));
        btnPedir = new JButton("Pedir");
        btnPlantarse = new JButton("Plantarse");
        btnDoblar = new JButton("Doblar");
        btnNuevaRonda = new JButton("Nueva ronda");

        panelBotones.add(btnPedir);
        panelBotones.add(btnPlantarse);
        panelBotones.add(btnDoblar);
        panelBotones.add(btnNuevaRonda);

        panelInferior.add(panelBotones, BorderLayout.CENTER);

        // --- Mensaje del juego ---
        etiquetaMensaje = new JLabel(" ", SwingConstants.CENTER);
        etiquetaMensaje.setFont(new Font("Arial", Font.BOLD, 14));
        panelInferior.add(etiquetaMensaje, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ==========================================
    // MÉTODOS PARA EL CONTROLADOR
    // ==========================================

    public void agregarListeners(ActionListener listener) {
        btnPedir.setActionCommand("Pedir");
        btnPlantarse.setActionCommand("Plantarse");
        btnDoblar.setActionCommand("Doblar");
        btnNuevaRonda.setActionCommand("NuevaRonda");
        btnApostar.setActionCommand("Apostar");

        btnPedir.addActionListener(listener);
        btnPlantarse.addActionListener(listener);
        btnDoblar.addActionListener(listener);
        btnNuevaRonda.addActionListener(listener);
        btnApostar.addActionListener(listener);
    }

    public int obtenerApuestaIngresada() {
        try {
            return Integer.parseInt(campoApuesta.getText());
        } catch (NumberFormatException e) {
            return -1; // número inválido
        }
    }

    public void actualizarVista(EstadoJuego estado) {
        etiquetaSaldo.setText("Saldo: " + estado.getSaldo());
        etiquetaMensaje.setText(estado.getMensaje());

        // Mostrar cartas del jugador
        panelJugador.removeAll();
        mostrarCartas(panelJugador, estado.getManoJugador());

        // Mostrar cartas del crupier
        panelCrupier.removeAll();
        mostrarCartas(panelCrupier, estado.getManoCrupier());

        revalidate();
        repaint();
    }

    private void mostrarCartas(JPanel panel, List<String> cartas) {
        for (String carta : cartas) {
            JLabel lblCarta = new JLabel(carta);
            lblCarta.setFont(new Font("Arial", Font.BOLD, 20));
            lblCarta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            lblCarta.setPreferredSize(new Dimension(50, 70));
            lblCarta.setHorizontalAlignment(SwingConstants.CENTER);
            lblCarta.setOpaque(true);
            lblCarta.setBackground(Color.WHITE);
            panel.add(lblCarta);
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
