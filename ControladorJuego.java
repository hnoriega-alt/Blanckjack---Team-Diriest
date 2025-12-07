import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ControladorJuego implements ActionListener {

    private Juego modelo;
    private VentanaPrincipal ventana;
    private VistaInicio vistaInicio;
    private VistaReglas vistaReglas;
    private VistaJuego vistaJuego;

    public ControladorJuego(Juego modelo, VentanaPrincipal ventana) {
        this.modelo = modelo;
        this.ventana = ventana;

        vistaInicio = new VistaInicio();
        vistaReglas = new VistaReglas();
        vistaJuego = new VistaJuego();

        vistaInicio.agregarListeners(this);
        vistaReglas.agregarListeners(this);
        vistaJuego.agregarListeners(this);

        ventana.mostrarPanel(vistaInicio);
        // mostrar estado inicial en juego (sin cartas hasta nueva ronda)
        vistaJuego.actualizar(modelo.getEstado());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {

            case "JUGAR":
                ventana.mostrarPanel(vistaJuego);
                vistaJuego.actualizar(modelo.getEstado());
                break;

            case "REGLAS":
                ventana.mostrarPanel(vistaReglas);
                break;

            case "CREDITOS":
                JOptionPane.showMessageDialog(ventana,
                        "Créditos:\nCarrillo Martínez, Santiago Andrés\nMartínez Pérez, Javier Eduardo\nMontaño Rojas, Mateo Leonardo\nNoriega Lozano, Héctor Andrés\nLópez Forero, Juan David\nProyecto POO - Blackjack 21",
                        "Créditos", JOptionPane.PLAIN_MESSAGE);
                break;

            case "VOLVER_MENU":
                ventana.mostrarPanel(vistaInicio);
                break;

            case "CONFIRMAR_APUESTA":
                int ap = vistaJuego.leerApuesta();
                if (ap <= 0) {
                    vistaJuego.mostrarDialogo("Ingresa una apuesta válida.");
                } else if (ap > modelo.getSaldo()) {
                    vistaJuego.mostrarDialogo("Saldo insuficiente.");
                } else {
                    modelo.establecerApuesta(ap);
                    vistaJuego.actualizar(modelo.getEstado());
                    vistaJuego.mostrarDialogo("Apuesta establecida: $" + ap);
                }
                break;

            case "NUEVA_RONDA":
                modelo.nuevaRonda();
                vistaJuego.actualizar(modelo.getEstado());
                if (modelo.isRondaTerminada()) {
                    // Ronda finalizó al repartir (ej. blackjack)
                    JOptionPane.showMessageDialog(ventana, modelo.getEstado().getMensaje());
                }
                break;

            case "PEDIR":
                // antes de pedir, guardamos tamaño para detectar nueva carta
                int tamAntes = modelo.getEstado().getManoJugador().size();
                modelo.jugadorPedir();
                // si la ronda no terminó y hay carta nueva, la añadimos solo a la vista
                int tamDespues = modelo.getEstado().getManoJugador().size();
                if (tamDespues > tamAntes) {
                    // añadimos la última carta visualmente (evita duplicados)
                    Juego.Carta ultima = modelo.getEstado().getManoJugador().get(tamDespues - 1);
                    vistaJuego.añadirCartaNuevaJugador(ultima);
                }
                vistaJuego.actualizar(modelo.getEstado());
                if (modelo.isRondaTerminada()) {
                    JOptionPane.showMessageDialog(ventana, modelo.getEstado().getMensaje());
                }
                break;

            case "PLANTARSE":
                modelo.jugadorPlantarse();
                vistaJuego.actualizar(modelo.getEstado());
                if (modelo.isRondaTerminada()) {
                    JOptionPane.showMessageDialog(ventana, modelo.getEstado().getMensaje());
                }
                break;

            case "DOBLAR":
                modelo.jugadorDoblar();
                vistaJuego.actualizar(modelo.getEstado());
                if (modelo.isRondaTerminada()) {
                    JOptionPane.showMessageDialog(ventana, modelo.getEstado().getMensaje());
                }
                break;

            default:
                System.out.println("Acción desconocida: " + cmd);
                break;
        }
    }
}
