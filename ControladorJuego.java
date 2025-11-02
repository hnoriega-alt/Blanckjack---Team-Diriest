import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controla la comunicación entre la Vista y el Modelo.
 */
public class ControladorJuego implements ActionListener {

    private Juego modelo;
    private VistaJuego vista;

    public ControladorJuego(Juego modelo, VistaJuego vista) {
        this.modelo = modelo;
        this.vista = vista;
        vista.agregarListeners(this);
        vista.actualizarVista(modelo.getEstado());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        switch (comando) {
            case "Pedir":
                modelo.jugadorPedir();
                break;
            case "Plantarse":
                modelo.jugadorPlantarse();
                break;
            case "Doblar":
                modelo.jugadorDoblar();
                break;
            case "NuevaRonda":
                modelo.iniciarRondaGUI();
                break;
            case "Apostar":
                int nuevaApuesta = vista.obtenerApuestaIngresada();
                if (nuevaApuesta > 0) {
                    modelo.setApuesta(nuevaApuesta);
                } else {
                    vista.mostrarMensaje("Apuesta inválida.");
                }
                break;
        }

        vista.actualizarVista(modelo.getEstado());
    }

    public static void main(String[] args) {
        Juego modelo = new Juego();
        VistaJuego vista = new VistaJuego();
        new ControladorJuego(modelo, vista);
    }
}
