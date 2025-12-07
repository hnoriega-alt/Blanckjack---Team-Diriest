import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Juego {

    private Baraja baraja;

    private List<Carta> manoJugador;
    private List<Carta> manoCrupier;

    private int saldo;
    private int apuesta;

    private boolean rondaTerminada;
    private String mensajeActual;

    // ===============================
//    CLASE CARTA (ANIDADA)
// ===============================
public class Carta {

    private String palo;
    private String rango;
    private int valor;

    public Carta(String palo, String rango, int valor) {
        this.palo = palo;
        this.rango = rango;
        this.valor = valor;
    }

    public String getPalo() {
        return palo;
    }

    public String getRango() {
        return rango;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return rango + " de " + palo;
    }
}



// ===============================
//    CLASE BARAJA (ANIDADA)
// ===============================
public class Baraja {

    private List<Carta> cartas;

    public Baraja() {
        cartas = new ArrayList<>();
        construirBaraja();
        barajar();
    }

    private void construirBaraja() {

        String[] palos = { "Corazones", "Diamantes", "Tréboles", "Picas" };
        String[] rangos = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

        for (String palo : palos) {
            for (String rango : rangos) {

                int valor;
                switch (rango) {
                    case "A": valor = 11; break;
                    case "J":
                    case "Q":
                    case "K": valor = 10; break;
                    default:  valor = Integer.parseInt(rango); break;
                }

                cartas.add(new Carta(palo, rango, valor));
            }
        }
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public Carta sacarCarta() {
        if (cartas.isEmpty()) {
            construirBaraja();
            barajar();
        }
        return cartas.remove(0);
    }

    public int tamano() {
        return cartas.size();
    }
}


    public Juego() {
        saldo = 1000;
        apuesta = 0;

        baraja = new Baraja();
        baraja.barajar();

        manoJugador = new ArrayList<>();
        manoCrupier = new ArrayList<>();

        mensajeActual = "Bienvenido. Realiza tu apuesta para comenzar.";
        rondaTerminada = true;
    }

    // -----------------------------
    // MÉTODOS PARA LA APUESTA
    // -----------------------------
    public void establecerApuesta(int valor) {
        this.apuesta = valor;
    }

    public int getApuesta() {
        return apuesta;
    }

    public int getSaldo() {
        return saldo;
    }

    // -----------------------------
    // NUEVA RONDA DESDE GUI
    // -----------------------------
    public void nuevaRonda() {

        if (apuesta <= 0 || apuesta > saldo) {
            mensajeActual = "Debes ingresar una apuesta válida.";
            return;
        }

        if (baraja.tamano() < 15) {
            baraja = new Baraja();
            baraja.barajar();
        }

        manoJugador.clear();
        manoCrupier.clear();

        manoJugador.add(baraja.sacarCarta());
        manoJugador.add(baraja.sacarCarta());
        manoCrupier.add(baraja.sacarCarta());
        manoCrupier.add(baraja.sacarCarta());

        rondaTerminada = false;
        mensajeActual = "Ronda iniciada. ¡Buena suerte!";
    }

    // -----------------------------
    // ACCIONES DEL JUGADOR
    // -----------------------------
    public void jugadorPedir() {
        if (rondaTerminada) return;

        manoJugador.add(baraja.sacarCarta());

        if (calcularValor(manoJugador) > 21) {
            saldo -= apuesta;
            mensajeActual = "Te pasaste. Pierdes la ronda.";
            rondaTerminada = true;
        }
    }

    public void jugadorPlantarse() {
        if (rondaTerminada) return;

        turnoCrupier();
        determinarGanador();
    }

    public void jugadorDoblar() {
        if (rondaTerminada) return;
        if (apuesta * 2 > saldo) {
            mensajeActual = "No tienes saldo suficiente para doblar.";
            return;
        }

        apuesta *= 2;
        manoJugador.add(baraja.sacarCarta());

        if (calcularValor(manoJugador) > 21) {
            saldo -= apuesta;
            mensajeActual = "Te pasaste doblando. Pierdes.";
            rondaTerminada = true;
            return;
        }

        turnoCrupier();
        determinarGanador();
    }

    // -----------------------------
    // TURNO DEL CRUPIER
    // -----------------------------
    private void turnoCrupier() {
        while (calcularValor(manoCrupier) < 17) {
            manoCrupier.add(baraja.sacarCarta());
        }
    }

    // -----------------------------
    // COMPARAR MANOS
    // -----------------------------
    private void determinarGanador() {
        int jugador = calcularValor(manoJugador);
        int crupier = calcularValor(manoCrupier);

        if (crupier > 21 || jugador > crupier) {
            saldo += apuesta;
            mensajeActual = "Ganaste la ronda.";
        } else if (jugador < crupier) {
            saldo -= apuesta;
            mensajeActual = "Pierdes la ronda.";
        } else {
            mensajeActual = "Empate.";
        }

        rondaTerminada = true;
    }

    // -----------------------------
    // CALCULAR VALOR DE MANO
    // -----------------------------
    private int calcularValor(List<Carta> mano) {
        int total = 0;
        int ases = 0;

        for (Carta c : mano) {
            int v = c.getValor();
            if (v == 11) ases++;
            total += v;
        }

        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;
        }

        return total;
    }

    // -----------------------------
    // ESTADO PARA LA VISTA
    // -----------------------------
    public EstadoJuego getEstado() {
        return new EstadoJuego(
                new ArrayList<>(manoJugador),
                new ArrayList<>(manoCrupier),
                saldo,
                apuesta,
                calcularValor(manoJugador),
                calcularValor(manoCrupier),
                mensajeActual
        );
    }

    public boolean isRondaTerminada() {
        return rondaTerminada;
    }
}
