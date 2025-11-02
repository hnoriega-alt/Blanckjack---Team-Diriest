import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
// NO importamos Juego.Carta ni Juego.Mano, ya que no son paquetes.

/**
 * Representa la Interfaz Gráfica de Usuario (GUI).
 * NO contiene lógica de juego, solo componentes visuales.
 * (Esta clase DEBE estar en su propio archivo "VistaJuego.java")
 */
public class VistaJuego extends JFrame {
    
    // --- Componentes Visuales ---
    private JButton botonPedir;
    private JButton botonPlantarse;
    private JButton botonDoblar;
    private JButton botonDividir;
    
    private JLabel etiquetaSaldo;
    private JLabel etiquetaPuntuacionJugador;
    private JLabel etiquetaPuntuacionCrupier;
    
    private JPanel panelCartasJugador;
    private JPanel panelCartasCrupier;
    
    private JPanel panelControles;
    private JPanel panelInfoJugador;
    private JPanel panelInfoCrupier;
    
    public VistaJuego() {
        // --- 1. Configurar la ventana (JFrame) ---
        setTitle("Blackjack GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Layout principal

        // --- 2. Inicializar Paneles ---
        panelCartasCrupier = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelCartasCrupier.setBackground(new Color(0, 128, 0)); 
        panelCartasJugador = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelCartasJugador.setBackground(new Color(0, 128, 0));

        panelInfoCrupier = new JPanel(new BorderLayout());
        panelInfoJugador = new JPanel(new BorderLayout());
        
        panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // --- 3. Inicializar Componentes ---
        botonPedir = new JButton("Pedir");
        botonPlantarse = new JButton("Plantarse");
        botonDoblar = new JButton("Doblar");
        botonDividir = new JButton("Dividir");
        
        botonPedir.setActionCommand("Pedir");
        botonPlantarse.setActionCommand("Plantarse");
        botonDoblar.setActionCommand("Doblar");
        botonDividir.setActionCommand("Dividir");

        etiquetaSaldo = new JLabel("Saldo: $1000", SwingConstants.CENTER);
        etiquetaPuntuacionJugador = new JLabel("Jugador: 0", SwingConstants.CENTER);
        etiquetaPuntuacionCrupier = new JLabel("Crupier: 0", SwingConstants.CENTER);
        
        Font fuenteEtiquetas = new Font("Arial", Font.BOLD, 16);
        etiquetaSaldo.setFont(fuenteEtiquetas);
        etiquetaPuntuacionJugador.setFont(fuenteEtiquetas);
        etiquetaPuntuacionCrupier.setFont(fuenteEtiquetas);
        etiquetaSaldo.setForeground(Color.WHITE);
        etiquetaPuntuacionJugador.setForeground(Color.WHITE);
        etiquetaPuntuacionCrupier.setForeground(Color.WHITE);
        
        panelInfoCrupier.setBackground(Color.BLACK);
        panelInfoJugador.setBackground(Color.BLACK);

        // --- 4. Añadir componentes a los paneles ---
        panelControles.add(botonPedir);
        panelControles.add(botonPlantarse);
        panelControles.add(botonDoblar);
        panelControles.add(botonDividir);

        panelInfoCrupier.add(etiquetaPuntuacionCrupier, BorderLayout.NORTH);
        panelInfoCrupier.add(panelCartasCrupier, BorderLayout.CENTER);

        panelInfoJugador.add(etiquetaPuntuacionJugador, BorderLayout.NORTH);
        panelInfoJugador.add(panelCartasJugador, BorderLayout.CENTER);
        panelInfoJugador.add(etiquetaSaldo, BorderLayout.SOUTH);

        // --- 5. Añadir paneles principales al JFrame ---
        add(panelInfoCrupier, BorderLayout.NORTH);
        add(panelInfoJugador, BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);
        
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    // --- Métodos para el Controlador ---
    
    public void agregarListeners(ActionListener listener) {
        botonPedir.addActionListener(listener);
        botonPlantarse.addActionListener(listener);
        botonDoblar.addActionListener(listener);
        botonDividir.addActionListener(listener);
    }
    
    /**
     * CORRECCIÓN: Usamos "Juego.Mano" en lugar de solo "Mano".
     * Actualiza toda la información visual (cartas, puntuaciones, saldo).
     */
    public void actualizarVista(Juego.Mano manoJugador, Juego.Mano manoCrupier, int saldo, boolean mostrarTodaCrupier) {
        
        // 1. Actualizar Saldo y Puntuación Jugador
        etiquetaSaldo.setText("Saldo: $" + saldo);
        etiquetaPuntuacionJugador.setText("Jugador: " + manoJugador.getValor());
        
        // 2. Dibujar Cartas Jugador
        panelCartasJugador.removeAll();
        // CORRECCIÓN: Usamos "Juego.Carta"
        for (Juego.Carta carta : manoJugador.getCartas()) {
            panelCartasJugador.add(crearEtiquetaCarta(carta.toString()));
        }
        
        // 3. Dibujar Cartas Crupier
        panelCartasCrupier.removeAll();
        if (mostrarTodaCrupier) {
            etiquetaPuntuacionCrupier.setText("Crupier: " + manoCrupier.getValor());
            // CORRECCIÓN: Usamos "Juego.Carta"
            for (Juego.Carta carta : manoCrupier.getCartas()) {
                panelCartasCrupier.add(crearEtiquetaCarta(carta.toString()));
            }
        } else {
            if (manoCrupier.getCartas().size() > 0) {
                // CORRECCIÓN: Usamos "Juego.Carta"
                Juego.Carta cartaVisible = manoCrupier.getCartas().get(0);
                etiquetaPuntuacionCrupier.setText("Crupier: " + cartaVisible.getRango().getValor());
                panelCartasCrupier.add(crearEtiquetaCarta(cartaVisible.toString()));
                panelCartasCrupier.add(crearEtiquetaCarta("[CARTA OCULTA]"));
            }
        }
        
        panelCartasJugador.revalidate();
        panelCartasJugador.repaint();
        panelCartasCrupier.revalidate();
        panelCartasCrupier.repaint();
    }
    
    public void setBotonesHabilitados(boolean puedePedir, boolean puedePlantarse, boolean puedeDoblar, boolean puedeDividir) {
        botonPedir.setEnabled(puedePedir);
        botonPlantarse.setEnabled(puedePlantarse);
        botonDoblar.setEnabled(puedeDoblar);
        botonDividir.setEnabled(puedeDividir);
    }
    
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private JLabel crearEtiquetaCarta(String texto) {
        JLabel etiquetaCarta = new JLabel(texto, SwingConstants.CENTER);
        etiquetaCarta.setPreferredSize(new Dimension(100, 150));
        etiquetaCarta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        etiquetaCarta.setOpaque(true);
        etiquetaCarta.setBackground(Color.WHITE);
        etiquetaCarta.setFont(new Font("Arial", Font.BOLD, 12));
        return etiquetaCarta;
    }
}