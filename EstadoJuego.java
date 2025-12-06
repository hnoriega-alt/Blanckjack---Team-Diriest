import java.util.List;

/**
 * Clase simple para transportar la información del estado del juego
 * entre el modelo (Juego) y la vista.
 */
public class EstadoJuego {

    private List<String> manoJugador;
    private List<String> manoCrupier;
    private int saldo;
    private String mensaje;
    private int apuesta;

    public EstadoJuego(List<String> manoJugador, List<String> manoCrupier, int saldo, String mensaje, int apuesta) {
        this.manoJugador = manoJugador;
        this.manoCrupier = manoCrupier;
        this.saldo = saldo;
        this.mensaje = mensaje;
        this.apuesta = apuesta;
    }

    public List<String> getManoJugador() {
        return manoJugador;
    }

    public List<String> getManoCrupier() {
        return manoCrupier;
    }

    public int getSaldo() {
        return saldo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getApuesta() {
        return apuesta;
    }
}

