import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
// NO importamos Juego.Jugador ni Juego.Crupier

/**
 * Actúa como intermediario entre el Modelo (Juego) y la Vista (VistaJuego).
 * (Esta clase DEBE estar en su propio archivo "ControladorJuego.java")
 */
public class ControladorJuego implements ActionListener {
    
    private Juego modelo;
    private VistaJuego vista;
    
    public ControladorJuego(Juego modelo, VistaJuego vista) {
        this.modelo = modelo;
        this.vista = vista;
        
        this.vista.agregarListeners(this);
        
        iniciarNuevaRonda();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        
        if (modelo.isRondaTerminada()) {
            return;
        }

        switch (comando) {
            case "Pedir":
                modelo.jugadorPedirCarta();
                actualizarVistaCompleta(false);
                
                if (modelo.getJugador().sePaso()) {
                    resolverRonda(false);
                }
                break;
                
            case "Plantarse":
                resolverRonda(true);
                break;
                
            case "Doblar":
                vista.mostrarMensaje("Opción 'Doblar' no implementada.");
                break;
                
            case "Dividir":
                vista.mostrarMensaje("Opción 'Dividir' no implementada.");
                break;
        }
    }
    
    private void iniciarNuevaRonda() {
        int apuesta = pedirApuesta();
        if (apuesta == 0) {
             System.exit(0);
        }

        modelo.iniciarRondaGUI(apuesta);
        
        if (modelo.getJugador().getMano().esBlackjack()) {
            actualizarVistaCompleta(false);
            resolverRonda(true);
        } else {
            actualizarVistaCompleta(false);
            vista.setBotonesHabilitados(true, true, false, false); 
        }
    }
    
    private void resolverRonda(boolean esPlantarse) {
        String mensajeResultado;
        
        if (esPlantarse) {
            mensajeResultado = modelo.jugadorPlantarseYResolver();
        } else {
            mensajeResultado = modelo.jugadorSePasaResolver();
        }

        actualizarVistaCompleta(true); 
        
        vista.setBotonesHabilitados(false, false, false, false);
        
        vista.mostrarMensaje(mensajeResultado);
        
        if (modelo.getJugador().getSaldo() > 0) {
            int opcion = JOptionPane.showConfirmDialog(vista,
                "¿Jugar otra ronda?\nSaldo actual: $" + modelo.getJugador().getSaldo(),
                "Ronda Terminada",
                JOptionPane.YES_NO_OPTION);
                
            if (opcion == JOptionPane.YES_OPTION) {
                iniciarNuevaRonda();
            } else {
                System.exit(0);
            }
        } else {
            vista.mostrarMensaje("Te has quedado sin saldo. ¡Gracias por jugar!");
            System.exit(0);
        }
    }

    /**
     * CORRECCIÓN: Usamos "Juego.Jugador" y "Juego.Crupier".
     */
    private void actualizarVistaCompleta(boolean mostrarTodaCrupier) {
        // CORRECCIÓN: Calificación completa
        Juego.Jugador j = modelo.getJugador();
        Juego.Crupier c = modelo.getCrupier();
        vista.actualizarVista(j.getMano(), c.getMano(), j.getSaldo(), mostrarTodaCrupier);
    }
    
    private int pedirApuesta() {
        while (true) {
            String apuestaStr = JOptionPane.showInputDialog(
                vista, 
                "Saldo actual: $" + modelo.getJugador().getSaldo() + "\nIntroduce tu apuesta:", 
                "Nueva Ronda", 
                JOptionPane.PLAIN_MESSAGE
            );

            if (apuestaStr == null) { 
                return 0; 
            }

            try {
                int apuesta = Integer.parseInt(apuestaStr);
                if (apuesta > 0 && apuesta <= modelo.getJugador().getSaldo()) {
                    return apuesta;
                } else if (apuesta > modelo.getJugador().getSaldo()) {
                    vista.mostrarMensaje("No tienes saldo suficiente.");
                } else {
                    vista.mostrarMensaje("La apuesta debe ser mayor que 0.");
                }
            } catch (NumberFormatException e) {
                vista.mostrarMensaje("Por favor, introduce un número válido.");
            }
        }
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Juego modelo = new Juego();
                VistaJuego vista = new VistaJuego();
                ControladorJuego controlador = new ControladorJuego(modelo, vista);
                vista.setVisible(true);
            }
        });
    }
}
