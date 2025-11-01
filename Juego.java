import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que CONTIENE todas las demás clases del modelo de dominio
 * como clases anidadas. Esto es para Sprints 1-4 (consola).
 */
public class Juego {

    // --- Atributos de la clase Juego ---
    private Baraja baraja;
    private Jugador jugador;
    private Crupier crupier;
    private Scanner scanner;
    private List<Apuesta> apuestasActivas; // Para Sprint 4

    // --- SPRINT 1: ENUMS Y CLASES DE DOMINIO FUNDAMENTALES (ANIDADAS) ---

    /**
     * Enum para los Palos de la baraja.
     */
    public enum Palo {
        CORAZONES, DIAMANTES, PICAS, TREBOLES
    }

    /**
     * Enum para los Rangos, almacena su valor.
     */
    public enum Rango {
        AS(11), DOS(2), TRES(3), CUATRO(4), CINCO(5), SEIS(6), SIETE(7),
        OCHO(8), NUEVE(9), DIEZ(10), JOTA(10), REINA(10), REY(10);

        private final int valor;
        Rango(int valor) { this.valor = valor; }
        public int getValor() { return valor; }
    }

    /**
     * Clase de datos para una Carta individual.
     */
    public static class Carta {
        private final Palo palo;
        private final Rango rango;

        public Carta(Palo palo, Rango rango) {
            this.palo = palo;
            this.rango = rango;
        }
        public Rango getRango() { return rango; }
        public Palo getPalo() { return palo; }
        @Override
        public String toString() { return rango + " de " + palo; }
    }

    /**
     * Clase para la Mano de un participante.
     * Contiene la lógica clave del valor dual del As[cite: 64, 150].
     */
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
            // Lógica del As
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

    /**
     * Clase para la Baraja, usa múltiples mazos[cite: 58].
     */
    public static class Baraja {
        private List<Carta> cartas = new ArrayList<>();

        public Baraja(int numMazos) {
            // TODO: Llenar la lista 'cartas' con 'numMazos'
            // (un bucle por numMazos, bucles anidados por Palo y Rango)
            for (int i = 0; i < numMazos; i++) {
                for (Palo p : Palo.values()) {
                    for (Rango r : Rango.values()) {
                        cartas.add(new Carta(p, r));
                    }
                }
            }
        }
        public void barajar() { Collections.shuffle(cartas); }
        public Carta repartirCarta() { return cartas.remove(0); }
    }

    // --- SPRINT 2: CLASES DE PARTICIPANTES (ANIDADAS) ---

    /**
     * Clase abstracta para comportamiento común[cite: 186].
     */
    public abstract static class Participante {
        protected Mano mano = new Mano();
        public Mano getMano() { return mano; }
        public void recibirCarta(Carta carta) { mano.agregarCarta(carta); }
        public void limpiarMano() { mano.limpiar(); }
        public int getValorMano() { return mano.getValor(); }
        public boolean sePaso() { return mano.sePaso(); }
    }

    /**
     * Clase para el Jugador.
     */
    public static class Jugador extends Participante {
        // TODO SPRINT 3: Añadir 'private int saldo;'
        public Jugador() { super(); }
        // TODO SPRINT 3: Añadir métodos para gestionar saldo
    }

    /**
     * Clase para el Crupier, con su lógica fija[cite: 74, 161].
     */
    public static class Crupier extends Participante {
        public Crupier() { super(); }
        public Carta getCartaVisible() { return mano.getCartas().get(0); }

        public void jugarTurno(Baraja baraja) {
            System.out.println("Turno del Crupier. Mano: " + mano);
            while (getValorMano() < 17) { // Regla: plantarse con 17 o más
                System.out.println("Crupier pide carta...");
                recibirCarta(baraja.repartirCarta());
                System.out.println("Mano del Crupier: " + mano);
            }
            System.out.println("Crupier se planta.");
        }
    }

    // --- SPRINT 4: SISTEMA DE APUESTAS MODULAR (INTERFAZ Y CLASES ANIDADAS) ---

    /**
     * Interfaz para el diseño modular de apuestas[cite: 30].
     */
    public interface Apuesta {
        boolean evaluar(Mano manoJugador, Carta cartaVisibleCrupier, Mano manoCrupierCompleta);
        int calcularPago();
        void setMonto(int monto);
        int getMonto();
    }

    // TODO: Implementar las clases ApuestaSeguro, ApuestaParesPerfectos, Apuesta21mas3
    // Ejemplo de ApuestaSeguro [cite: 85, 86]
    public static class ApuestaSeguro implements Apuesta {
        private int monto;
        @Override
        public boolean evaluar(Mano manoJugador, Carta cartaVisibleCrupier, Mano manoCrupierCompleta) {
            return manoCrupierCompleta.esBlackjack();
        }
        @Override
        public int calcularPago() { return monto * 3; /* Pago 2:1 */ }
        @Override
        public void setMonto(int monto) { this.monto = monto; }
        @Override
        public int getMonto() { return monto; }
    }
    
    // TODO: Implementar ApuestaParesPerfectos [cite: 87, 88]
    // TODO: Implementar Apuesta21mas3 [cite: 89, 90]


    // --- LÓGICA PRINCIPAL DEL JUEGO (Sprints 2 y 3) ---

    public Juego() {
        this.baraja = new Baraja(6); // 6 mazos [cite: 58]
        this.jugador = new Jugador();
        this.crupier = new Crupier();
        this.scanner = new Scanner(System.in);
        this.apuestasActivas = new ArrayList<>(); // Sprint 4
    }

    public void iniciar() {
        baraja.barajar();
        while (true) {
            jugarRonda();
            System.out.println("¿Jugar otra ronda? (s/n)");
            if (scanner.nextLine().equalsIgnoreCase("n")) break;
        }
        System.out.println("Gracias por jugar.");
    }

    private void jugarRonda() {
        jugador.limpiarMano();
        crupier.limpiarMano();
        apuestasActivas.clear();

        // TODO SPRINT 3: Pedir apuesta principal

        // Reparto inicial
        jugador.recibirCarta(baraja.repartirCarta());
        crupier.recibirCarta(baraja.repartirCarta());
        jugador.recibirCarta(baraja.repartirCarta());
        crupier.recibirCarta(baraja.repartirCarta());

        System.out.println("Mano del Jugador: " + jugador.getMano());
        System.out.println("Carta visible del Crupier: " + crupier.getCartaVisible());

        // TODO SPRINT 4: Ofrecer Apuestas Especiales (Seguro, 21+3, Pares)
        // if (crupier.getCartaVisible().getRango() == Rango.AS) { ... ofrecer seguro ... }
        // ... pedir apuesta 21+3 ...
        // ... pedir apuesta Pares Perfectos ...

        // Turno del Jugador
        turnoJugador();

        // Turno del Crupier
        if (!jugador.sePaso()) {
            crupier.jugarTurno(baraja);
        }
        
        // Determinar ganador
        determinarGanador();
    }

    private void turnoJugador() {
        while (true) {
            System.out.println("¿Qué deseas hacer? (1: Pedir, 2: Plantarse)");
            // TODO SPRINT 3: Añadir opciones 3: Doblar, 4: Dividir [cite: 71, 72]
            
            String opcion = scanner.nextLine();
            if (opcion.equals("1")) { // Pedir (Hit) [cite: 69]
                jugador.recibirCarta(baraja.repartirCarta());
                System.out.println("Mano del Jugador: " + jugador.getMano());
                if (jugador.sePaso()) {
                    System.out.println("¡Te has pasado!");
                    break;
                }
            } else if (opcion.equals("2")) { // Plantarse (Stand) [cite: 70]
                System.out.println("Jugador se planta.");
                break;
            } 
        }
    }
    
    private void determinarGanador() {
        System.out.println("Mano final Jugador: " + jugador.getMano());
        System.out.println("Mano final Crupier: " + crupier.getMano());

        // TODO SPRINT 3: Implementar lógica de comparación y pagos (1:1 o 3:2) [cite: 75, 79]
        // TODO SPRINT 4: Evaluar y pagar apuestas especiales
        for (Apuesta apuesta : apuestasActivas) {
            // if (apuesta.evaluar(...)) { ... pagar ... }
        }
    }

    /**
     * Punto de entrada para la aplicación de consola.
     */
    public static void main(String[] args) {
        Juego miJuego = new Juego();
        miJuego.iniciar();
    }
}