import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Clase que maneja toda la lógica del Blackjack.
 * Se encarga de repartir, calcular valores y decidir el resultado de cada ronda.
 */
public class Juego {

    private List<String> mazo;
    private List<String> manoJugador;
    private List<String> manoCrupier;

    private int saldo;
    private int apuestaActual;
    private String mensajeActual;

    private boolean rondaTerminada;

    public Juego() {
        saldo = 1000;
        apuestaActual = 100;
        iniciarRondaGUI();
    }

    /**
     * Inicia una nueva ronda, baraja y reparte las cartas iniciales.
     */
    public void iniciarRondaGUI() {
        mazo = generarMazo();
        manoJugador = new ArrayList<>();
        manoCrupier = new ArrayList<>();
        rondaTerminada = false;
        mensajeActual = "";

        manoJugador.add(sacarCarta());
        manoCrupier.add(sacarCarta());
        manoJugador.add(sacarCarta());
        manoCrupier.add(sacarCarta());

        if (calcularValor(manoJugador) == 21) {
            mensajeActual = "¡Blackjack! Ganaste automáticamente.";
            saldo += (int)(apuestaActual * 1.5);
            rondaTerminada = true;
        }
    }

    /**
     * Permite modificar la apuesta antes de empezar una ronda.
     */
    public void setApuesta(int nuevaApuesta) {
        if (nuevaApuesta > 0 && nuevaApuesta <= saldo) {
            apuestaActual = nuevaApuesta;
        } else {
            mensajeActual = "Apuesta inválida.";
        }
    }

    public int getApuesta() {
        return apuestaActual;
    }

    /**
     * Cuando el jugador pide una carta.
     */
    public void jugadorPedir() {
        if (rondaTerminada) return;

        manoJugador.add(sacarCarta());
        int valor = calcularValor(manoJugador);

        if (valor > 21) {
            mensajeActual = "Te pasaste con " + valor + ". Pierdes la ronda.";
            saldo -= apuestaActual;
            rondaTerminada = true;
        } else if (valor == 21) {
            jugadorPlantarse();
        }
    }

    /**
     * Cuando el jugador se planta, juega el crupier.
     */
    public void jugadorPlantarse() {
        if (rondaTerminada) return;

        while (calcularValor(manoCrupier) < 17) {
            manoCrupier.add(sacarCarta());
        }

        int valorJugador = calcularValor(manoJugador);
        int valorCrupier = calcularValor(manoCrupier);

        if (valorCrupier > 21 || valorJugador > valorCrupier) {
            mensajeActual = "Ganaste la ronda.";
            saldo += apuestaActual;
        } else if (valorJugador == valorCrupier) {
            mensajeActual = "Empate. Se devuelve la apuesta.";
        } else {
            mensajeActual = "Perdiste la ronda.";
            saldo -= apuestaActual;
        }

        rondaTerminada = true;
    }

    /**
     * Opción de doblar la apuesta.
     */
    public void jugadorDoblar() {
        if (rondaTerminada || saldo < apuestaActual * 2) return;

        apuestaActual *= 2;
        manoJugador.add(sacarCarta());

        int valor = calcularValor(manoJugador);
        if (valor > 21) {
            mensajeActual = "Te pasaste con " + valor + ". Pierdes el doble.";
            saldo -= apuestaActual;
        } else {
            jugadorPlantarse();
        }

        rondaTerminada = true;
    }

    /**
     * Opción de dividir (no implementada completamente).
     */
    public void jugadorDividir() {
        mensajeActual = "La opción de dividir aún no está disponible.";
    }

    /**
     * Indica si la ronda ya terminó.
     */
    public boolean rondaTerminada() {
        return rondaTerminada;
    }

    /**
     * Devuelve el texto del resultado actual.
     */
    public String getResultadoRonda() {
        return mensajeActual;
    }

    /**
     * Devuelve el estado actual del juego.
     */
    public EstadoJuego getEstado() {
        return new EstadoJuego(
                new ArrayList<>(manoJugador),
                new ArrayList<>(manoCrupier),
                saldo,
                mensajeActual.isEmpty() ? "Tu turno..." : mensajeActual,
                apuestaActual
        );
    }

    // ==========================
    // Métodos internos
    // ==========================

    private List<String> generarMazo() {
        String[] palos = {"♠", "♥", "♦", "♣"};
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        List<String> nuevoMazo = new ArrayList<>();

        for (String palo : palos) {
            for (String valor : valores) {
                nuevoMazo.add(valor + palo);
            }
        }

        Collections.shuffle(nuevoMazo, new Random());
        return nuevoMazo;
    }

    private String sacarCarta() {
        if (mazo.isEmpty()) {
            mazo = generarMazo();
        }
        return mazo.remove(0);
    }

    private int calcularValor(List<String> mano) {
        int total = 0;
        int ases = 0;

        for (String carta : mano) {
            String valor = carta.replaceAll("[♠♥♦♣]", "");
            switch (valor) {
                case "J":
                case "Q":
                case "K":
                    total += 10;
                    break;
                case "A":
                    total += 11;
                    ases++;
                    break;
                default:
                    total += Integer.parseInt(valor);
            }
        }

        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;
        }

        return total;
    }
}
