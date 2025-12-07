import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.border.EmptyBorder;

/**
 * Panel del juego (mesa). No tiene animaciones repetitivas.
 * Cuando se pide carta, la vista actualiza la mano y añade solo la carta nueva.
 */
public class VistaJuego extends JPanel {

    private JLabel lblSaldo;
    private JLabel lblApuesta;
    private JLabel lblMensaje;

    private JPanel panelCrupier;
    private JPanel panelJugador;

    private JButton btnPedir, btnPlantarse, btnDoblar, btnNuevaRonda, btnConfirmarApuesta;
    private JTextField campoApuesta;

    private final Font fuenteCasino = new Font("Serif", Font.BOLD, 18);

    public VistaJuego() {
        setLayout(new BorderLayout(8,8));
        setBackground(new Color(0, 70, 0));
        setBorder(new EmptyBorder(10,10,10,10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        top.setOpaque(false);

        lblSaldo = new JLabel("Saldo: $0");
        lblApuesta = new JLabel("Apuesta: $0");
        lblMensaje = new JLabel("Confirma apuesta y pulsa nueva ronda.");

        lblSaldo.setFont(fuenteCasino);
        lblApuesta.setFont(fuenteCasino);
        lblMensaje.setFont(fuenteCasino);

        lblSaldo.setForeground(Color.WHITE);
        lblApuesta.setForeground(Color.WHITE);
        lblMensaje.setForeground(new Color(255, 215, 0));

        top.add(lblSaldo);
        top.add(lblApuesta);
        top.add(lblMensaje);
        add(top, BorderLayout.NORTH);

        JPanel mesa = new JPanel(new GridLayout(2,1,8,8));
        mesa.setOpaque(false);

        panelCrupier = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelCrupier.setOpaque(true);
        panelCrupier.setBackground(new Color(0, 100, 0));
        panelCrupier.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255,215,0), 2),
                "Crupier",
                0, 0,
                new Font("Serif", Font.BOLD, 16),
                new Color(255,215,0)
        ));

        panelJugador = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelJugador.setOpaque(true);
        panelJugador.setBackground(new Color(0, 100, 0));
        panelJugador.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255,215,0), 2),
                "Jugador",
                0, 0,
                new Font("Serif", Font.BOLD, 16),
                new Color(255,215,0)
        ));

        mesa.add(panelCrupier);
        mesa.add(panelJugador);
        add(mesa, BorderLayout.CENTER);

        // bottom controls
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);

        JPanel apuesta = new JPanel(new FlowLayout(FlowLayout.LEFT));
        apuesta.setOpaque(false);
        campoApuesta = new JTextField("100", 6);
        btnConfirmarApuesta = new JButton("Confirmar apuesta");
        btnConfirmarApuesta.setActionCommand("CONFIRMAR_APUESTA");
        apuesta.add(new JLabel("Apuesta:"));
        apuesta.add(campoApuesta);
        apuesta.add(btnConfirmarApuesta);
        bottom.add(apuesta, BorderLayout.NORTH);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        botones.setOpaque(false);

        btnPedir = new JButton("Pedir"); btnPedir.setActionCommand("PEDIR");
        btnPlantarse = new JButton("Plantarse"); btnPlantarse.setActionCommand("PLANTARSE");
        btnDoblar = new JButton("Doblar"); btnDoblar.setActionCommand("DOBLAR");
        btnNuevaRonda = new JButton("Nueva ronda"); btnNuevaRonda.setActionCommand("NUEVA_RONDA");

        botones.add(btnPedir);
        botones.add(btnPlantarse);
        botones.add(btnDoblar);
        botones.add(btnNuevaRonda);
        bottom.add(botones, BorderLayout.CENTER);

        add(bottom, BorderLayout.SOUTH);
    }

    public void agregarListeners(ActionListener l) {
        btnConfirmarApuesta.addActionListener(l);
        btnPedir.addActionListener(l);
        btnPlantarse.addActionListener(l);
        btnDoblar.addActionListener(l);
        btnNuevaRonda.addActionListener(l);
    }

    public int leerApuesta() {
        try {
            return Integer.parseInt(campoApuesta.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Actualiza completamente las manos (sin animaciones).
     * Sirve para nueva ronda y para sincronizar pantalla.
     */
    public void actualizar(EstadoJuego estado) {
        lblSaldo.setText("Saldo: $" + estado.getSaldo());
        lblApuesta.setText("Apuesta: $" + estado.getApuesta());
        lblMensaje.setText(estado.getMensaje());

        dibujarMano(panelCrupier, estado.getManoCrupier());
        dibujarMano(panelJugador, estado.getManoJugador());
    }

    /**
     * Añade solo la carta nueva del jugador (evita duplicados y no vuelve a animar todo).
     * Llama a este método **solo** cuando sepas que se agregó una carta: al pedir.
     */
    public void añadirCartaNuevaJugador(Juego.Carta carta) {
        if (carta == null) return;
        panelJugador.add(crearCartaLabel(carta));
        panelJugador.revalidate();
        panelJugador.repaint();
    }

    private void dibujarMano(JPanel panel, List<Juego.Carta> mano) {
        panel.removeAll();
        if (mano != null) {
            for (Juego.Carta c : mano) panel.add(crearCartaLabel(c));
        }
        panel.revalidate();
        panel.repaint();
    }

    private JLabel crearCartaLabel(Juego.Carta c) {
        String simbolo;
        Color colorSimbolo;
        switch (c.getPalo()) {
            case "Corazones": simbolo = "♥"; colorSimbolo = Color.RED; break;
            case "Diamantes": simbolo = "♦"; colorSimbolo = Color.RED; break;
            case "Tréboles": simbolo = "♣"; colorSimbolo = Color.BLACK; break;
            default: simbolo = "♠"; colorSimbolo = Color.BLACK; break;
        }

        String txt = "<html><center>"
                + "<span style='font-size:16px;'>" + c.getRango() + "</span><br>"
                + "<span style='font-size:28px; font-weight:bold; color:" + (colorSimbolo == Color.RED ? "red" : "black") + ";'>" + simbolo + "</span>"
                + "</center></html>";

        JLabel l = new JLabel(txt, SwingConstants.CENTER);
        l.setPreferredSize(new Dimension(85, 115));
        l.setOpaque(true);
        l.setBackground(new Color(255, 255, 240));
        l.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return l;
    }

    public void mostrarDialogo(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
