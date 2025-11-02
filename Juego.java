import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// Se elimina Scanner

/**
 * Clase principal que CONTIENE todas las demás clases del modelo de dominio.
 * Esta versión ha sido refactorizada para ser el MODELO en un patrón MVC (GUI).
 * NO contiene lógica de consola (Scanner, System.out, bucles de juego).
 */
public class Juego {

    // --- Atributos de la clase Juego ---
    private Baraja baraja;
    private Jugador jugador;
    private Crupier crupier;
    private List<Apuesta> apuestasActivas; // Para Sprint 4
    
    private int apuestaActual;
    private static final int SALDO_INICIAL = 1000;
    
    // Estado para el control de la GUI
    private boolean rondaTerminada = false;

    // --- SPRINT 1: ENUMS Y CLASES DE DOMINIO FUNDAMENTALES (ANIDADAS) ---
    // (Estas clases anidadas no cambian. Omitidas por brevedad...
    // ... Palo, Rango, Carta, Mano, Baraja ...)
    
    public enum Palo { CORAZONES, DIAMANTES, PICAS, TREBOLES }
    public enum Rango {
        AS(11), DOS(2), TRES(3), CUATRO(4), CINCO(5), SEIS(6), SIETE(7),
        OCHO(8), NUEVE(9), DIEZ(10), JOTA(10), REINA(10), REY(10);
        private final int valor;
        Rango(int valor) { this.valor = valor; }
        public int getValor() { return valor; }
    }
    public static class Carta {
        private final Palo palo;
        private final Rango rango;
        public Carta(Palo palo, Rango rango) { this.palo = palo; this.rango = rango; }
        public Rango getRango() { return rango; }
        public Palo getPalo() { return palo; }
        @Override
        public String toString() { return rango + " de " + palo; }
    }
    public static class Mano {
        private List<Carta> cartas = new ArrayList<>();
        public void agregarCarta(Carta carta) { cartas.add(carta); }
        public void limpiar() { cartas.clear(); }
        public List<Carta> getCartas() { return cartas; }
        public int getValor() {
            int valorTotal = 0;
            int numAses = 0;
            for (Carta carta : cartas) {
                valorTotal += carta.getRango().getValor();
                if (carta.getRango() == Rango.AS) numAses++;
            }
            while (valorTotal > 21 && numAses > 0) {
                valorTotal -= 10;
                numAses--;
            }
            return valorTotal;
        }
        public boolean esBlackjack() { return cartas.size() == 2 && getValor() == 21; }
        public boolean sePaso() { return getValor() > 21; }
        @Override
        public String toString() { return cartas.toString() + " (Valor: " + getValor() + ")"; }
    }
    public static class Baraja {
        private List<Carta> cartas = new ArrayList<>();
        public Baraja(int numMazos) {
            for (int i = 0; i < numMazos; i++) {
                for (Palo p : Palo.values()) {
                    for (Rango r : Rango.values()) {
                        cartas.add(new Carta(p, r));
                    }
                }
            }
        }
        public void barajar() { Collections.shuffle(cartas); }
        public Carta repartirCarta() { 
            if (cartas.size() < 20) { // Re-barajar si quedan pocas cartas
                // System.out.println("Re-barajando...");
                cartas.clear();
                for (int i = 0; i < 6; i++) {
                    for (Palo p : Palo.values()) {
                        for (Rango r : Rango.values()) {
                            cartas.add(new Carta(p, r));
                        }
                    }
                }
                Collections.shuffle(cartas);
            }
            return cartas.remove(0);
        }
    }

    // --- SPRINT 2: CLASES DE PARTICIPANTES (ANIDADAS) ---
    // (Estas clases anidadas no cambian. Omitidas por brevedad...
    // ... Participante, Jugador, Crupier ...)

    public abstract static class Participante {
        protected Mano mano = new Mano();
        public Mano getMano() { return mano; }
        public void recibirCarta(Carta carta) { mano.agregarCarta(carta); }
        public void limpiarMano() { mano.limpiar(); }
        public int getValorMano() { return mano.getValor(); }
        public boolean sePaso() { return mano.sePaso(); }
    }
    public static class Jugador extends Participante {
        private int saldo;
        public Jugador(int saldoInicial) {
            super();
            this.saldo = saldoInicial;
        }
        public int getSaldo() { return saldo; }
        public void modificarSaldo(int monto) { this.saldo += monto; }
    }
    public static class Crupier extends Participante {
        public Crupier() { super(); }
        public Carta getCartaVisible() { return mano.getCartas().get(0); }
        
        // Modificado: Este método ya no imprime en consola
        public void jugarTurno(Baraja baraja) {
            while (getValorMano() < 17) { // Regla: plantarse con 17 o más
                recibirCarta(baraja.repartirCarta());
            }
        }
    }

    // --- SPRINT 4: SISTEMA DE APUESTAS MODULAR (INTERFAZ Y CLASES ANIDADAS) ---
    // (Omitido por brevedad, sin cambios)
    public interface Apuesta {
        boolean evaluar(Mano manoJugador, Carta cartaVisibleCrupier, Mano manoCrupierCompleta);
        int calcularPago();
        void setMonto(int monto);
        int getMonto();
    }
    public static class ApuestaSeguro implements Apuesta {
        private int monto;
        @Override
        public boolean evaluar(Mano manoJugador, Carta cartaVisibleCrupier, Mano manoCrupierCompleta) {
            return manoCrupierCompleta.esBlackjack();
        }
        @Override
        public int calcularPago() { return monto * 3; }
        @Override
        public void setMonto(int monto) { this.monto = monto; }
        @Override
        public int getMonto() { return monto; }
    }


    // --- LÓGICA DE CONTROL PARA GUI (Nuevos Métodos) ---

    public Juego() {
        this.baraja = new Baraja(6); // 6 mazos
        this.baraja.barajar();
        this.jugador = new Jugador(SALDO_INICIAL);
        this.crupier = new Crupier();
        this.apuestasActivas = new ArrayList<>(); // Sprint 4
    }

    /**
     * Inicia una nueva ronda (llamado por el Controlador).
     * @param apuesta El monto apostado por el jugador.
     */
    public void iniciarRondaGUI(int apuesta) {
        rondaTerminada = false;
        jugador.limpiarMano();
        crupier.limpiarMano();
        apuestasActivas.clear();
        
        this.apuestaActual = apuesta;
        jugador.modificarSaldo(-apuesta);

        // Reparto inicial
        jugador.recibirCarta(baraja.repartirCarta());
        crupier.recibirCarta(baraja.repartirCarta());
        jugador.recibirCarta(baraja.repartirCarta());
        crupier.recibirCarta(baraja.repartirCarta());
        
        // Comprobar Blackjack inmediato
        if (jugador.getMano().esBlackjack()) {
            rondaTerminada = true;
        }
    }

    /**
     * El jugador pide una carta (llamado por el Controlador).
     */
    public void jugadorPedirCarta() {
        if (rondaTerminada) return; // No hacer nada si la ronda ya terminó
        
        jugador.recibirCarta(baraja.repartirCarta());
        
        if (jugador.sePaso()) {
            rondaTerminada = true;
        }
    }

    /**
     * El jugador se planta. Esto dispara el turno del crupier y
     * resuelve la ronda.
     * @return Un string con el mensaje del resultado.
     */
    public String jugadorPlantarseYResolver() {
        if (rondaTerminada) {
             // Esto sucede si el jugador tenía Blackjack y aun así pulsó "Plantarse"
             // Simplemente resolvemos
        }
        
        // Turno del Crupier (solo si el jugador no se pasó)
        if (!jugador.sePaso()) {
            crupier.jugarTurno(baraja);
        }
        
        rondaTerminada = true;
        return determinarGanador();
    }
    
    /**
     * Resuelve la ronda si el jugador se ha pasado.
     * @return Un string con el mensaje del resultado.
     */
    public String jugadorSePasaResolver() {
        rondaTerminada = true;
        return determinarGanador();
    }

    /**
     * Lógica de SPRINT 3 modificada para devolver un String
     * en lugar de imprimir a consola.
     */
    private String determinarGanador() {
        boolean jugadorTieneBlackjack = jugador.getMano().esBlackjack();
        int valorJugador = jugador.getValorMano();
        int valorCrupier = crupier.getValorMano();
        boolean jugadorPasado = jugador.sePaso();
        boolean crupierPasado = crupier.sePaso();

        // Caso 1: Jugador tiene Blackjack (pago 3:2)
        if (jugadorTieneBlackjack) {
            if (crupier.getMano().esBlackjack()) {
                jugador.modificarSaldo(apuestaActual); // Devuelve la apuesta
                return "Empate (Push). Ambos tienen Blackjack.";
            } else {
                int pago = (int) (apuestaActual * 1.5) + apuestaActual;
                jugador.modificarSaldo(pago);
                return "¡Blackjack! Ganas 3:2. (+$" + (pago - apuestaActual) + ")";
            }
        }
        // Caso 2: Jugador se pasa (pierde automáticamente)
        else if (jugadorPasado) {
            return "¡Te has pasado! Pierdes $" + apuestaActual;
        }
        // Caso 3: Crupier se pasa (y jugador no)
        else if (crupierPasado) {
            jugador.modificarSaldo(apuestaActual * 2); // Devuelve apuesta + ganancia
            return "Crupier se pasa. ¡Ganas 1:1! (+$" + apuestaActual + ")";
        }
        // Caso 4: Nadie se pasa, comparar valores
        else if (valorJugador > valorCrupier) {
            jugador.modificarSaldo(apuestaActual * 2);
            return "¡Ganas 1:1! (+$" + apuestaActual + ")";
        }
        else if (valorCrupier > valorJugador) {
            return "Pierdes. Crupier tiene más.";
        }
        // Caso 5: Empate (Push)
        else {
            jugador.modificarSaldo(apuestaActual); // Devuelve la apuesta
            return "Empate (Push). Se te devuelve tu apuesta.";
        }
        
        // TODO SPRINT 4: Evaluar y pagar apuestas especiales
    }
    
    // --- Getters para el Controlador ---
    
    public Jugador getJugador() { return jugador; }
    public Crupier getCrupier() { return crupier; }
    public boolean isRondaTerminada() { return rondaTerminada; }

    // Se elimina el 'main' de la versión de consola.
}
