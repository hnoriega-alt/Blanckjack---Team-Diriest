import java.util.List;

/**
 * Estado que la vista consume para dibujar.
 * Solo datos: manos, saldo, apuesta, valores y mensaje.
 */
public class EstadoJuego {

    private List<Juego.Carta> manoJugador;
    private List<Juego.Carta> manoCrupier;
    private int saldo;
    private int apuesta;
    private int valorJugador;
    private int valorCrupier;
    private String mensaje;

    public EstadoJuego(List<Juego.Carta> manoJugador,
                       List<Juego.Carta> manoCrupier,
                       int saldo,
                       int apuesta,
                       int valorJugador,
                       int valorCrupier,
                       String mensaje) {
        this.manoJugador = manoJugador;
        this.manoCrupier = manoCrupier;
        this.saldo = saldo;
        this.apuesta = apuesta;
        this.valorJugador = valorJugador;
        this.valorCrupier = valorCrupier;
        this.mensaje = mensaje;
    }

    public List<Juego.Carta> getManoJugador() { return manoJugador; }
    public List<Juego.Carta> getManoCrupier() { return manoCrupier; }
    public int getSaldo() { return saldo; }
    public int getApuesta() { return apuesta; }
    public int getValorJugador() { return valorJugador; }
    public int getValorCrupier() { return valorCrupier; }
    public String getMensaje() { return mensaje; }
}
