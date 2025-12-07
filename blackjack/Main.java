import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            Juego modelo = new Juego();
            new ControladorJuego(modelo, ventana);
        });
    }
}
