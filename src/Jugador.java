import java.util.Random;

import javax.swing.JPanel;

public class Jugador {

    private int TOTAL_CARTAS = 10;
    private int MARGEN = 10;
    private int DISTANCIA = 40;

    private Carta[] cartas = new Carta[TOTAL_CARTAS];

    private Random r = new Random(); // la suerte del jugador

    public void repartir() {
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int posicion = MARGEN + (TOTAL_CARTAS - 1) * DISTANCIA;
        for (Carta carta : cartas) {
            carta.mostrar(pnl, posicion, MARGEN);
            posicion -= DISTANCIA;
        }
        pnl.repaint();
    }

    public String getGrupos() {
        String mensaje = "No se encontraron figuras";
        int[] contadores = new int[NombreCarta.values().length];
        for (Carta c : cartas) {
            contadores[c.getNombre().ordinal()]++;
        }

        boolean hayGrupos = false;
        for (int contador : contadores) {
            if (contador > 1) {
                hayGrupos = true;
                break;
            }
        }

        if (hayGrupos) {
            mensaje = "Se encontraron los siguientes grupos:\n";
            int fila = 0;
            for (int contador : contadores) {
                if (contador > 1) {
                    mensaje += Grupo.values()[contador] + " de " + NombreCarta.values()[fila] + "\n";
                }
                fila++;
            }
        }

        return mensaje;
    }

    public String getEscaleras() {
        boolean[][] matriz = new boolean[Pinta.values().length][NombreCarta.values().length];

        // Marcar en la matriz las cartas que tiene el jugador
        for (Carta carta : cartas) {
            int fila = carta.getPinta().ordinal();
            int col = carta.getNombre().ordinal();
            matriz[fila][col] = true;
        }

        String mensaje = "No se encontraron escaleras";

        for (int pinta = 0; pinta < matriz.length; pinta++) {
            int inicio = 0;
            while (inicio < matriz[pinta].length) {
                int longitud = 0;

                // Buscar secuencias consecutivas
                while (inicio + longitud < matriz[pinta].length && matriz[pinta][inicio + longitud]) {
                    longitud++;
                }

                if (longitud >= 3) {
                    if (mensaje.equals("No se encontraron escaleras")) {
                        mensaje = "Se encontraron escaleras:\n";
                    }

                    mensaje += Grupo.values()[longitud] + " de " + Pinta.values()[pinta] + ": ";

                    for (int i = inicio; i < inicio + longitud; i++) {
                        mensaje += NombreCarta.values()[i];
                        if (i < inicio + longitud - 1) {
                            mensaje += ", ";
                        }
                    }
                    mensaje += "\n";
                }

                inicio = inicio + Math.max(1, longitud);
            }
        }

        return mensaje;
    }

    public int getPuntaje() {
        boolean[][] matriz = new boolean[Pinta.values().length][NombreCarta.values().length];

        // Marcar en la matriz las cartas que tiene el jugador
        for (Carta carta : cartas) {
            int fila = carta.getPinta().ordinal();
            int col = carta.getNombre().ordinal();
            matriz[fila][col] = true;
        }

        boolean[][] usadas = new boolean[Pinta.values().length][NombreCarta.values().length];

        // Marcar como "usadas" las cartas que hacen parte de escaleras
        for (int pinta = 0; pinta < matriz.length; pinta++) {
            int inicio = 0;
            while (inicio < matriz[pinta].length) {
                int longitud = 0;
                while (inicio + longitud < matriz[pinta].length && matriz[pinta][inicio + longitud]) {
                    longitud++;
                }

                if (longitud >= 3) {
                    for (int i = inicio; i < inicio + longitud; i++) {
                        usadas[pinta][i] = true;
                    }
                }

                inicio = inicio + Math.max(1, longitud);
            }
        }
                int[] contadores = new int[NombreCarta.values().length];
        for (Carta c : cartas) {
            contadores[c.getNombre().ordinal()]++;
        }

        for (int i = 0; i < contadores.length; i++) {
            if (contadores[i] > 1) {
                for (Carta c : cartas) {
                    if (c.getNombre().ordinal() == i) {
                        int fila = c.getPinta().ordinal();
                        int col = c.getNombre().ordinal();
                        usadas[fila][col] = true;
                    }
                }
            }
        }

        int puntaje = 0;
        for (Carta carta : cartas) {
            int fila = carta.getPinta().ordinal();
            int col = carta.getNombre().ordinal();
            if (!usadas[fila][col]) {
                // Valor de la carta
                switch (carta.getNombre()) {
                    case JACK:
                    case QUEEN:
                    case KING:
                    case AS:
                        puntaje += 10;
                        break;
                    default:
                        puntaje += carta.getNombre().ordinal() + 1;
                }
            }
        }

        return puntaje;
    }
}
