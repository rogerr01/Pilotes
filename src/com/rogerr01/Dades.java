package com.rogerr01;

import static java.util.Arrays.*;

// Gestor de zones i coordenades
public class Dades implements Runnable
{

    // Mida de la tauler
    private static final int x = Tauler.MIDA;
    private static final int y = Tauler.MIDA;

    // Zones de la tauler
    public static Zona[] zones = new Zona[]
            {
                    new Zona(0, 0, x / 2, y / 2, 1),   // ZONA 1
                    new Zona(x / 2, 0, x, y / 2, 2),      // ZONA 2
                    new Zona(0, y / 2, x / 2, y, 3),       // ZONA 3
                    new Zona(y / 2, y / 2, x, y, 4)            // ZONA 4
            };

    // Retorna la zona a la qual pertanyen unes coordenades
    public static Zona getZona (int x, int y)
    {
        // He utilitzat el metode stream.filter de la classe Arrays, que
        // en aquest cas el que fa es recorrer l'array de zones i retornar
        // la primera que compleixi la condició de que x i y hi estan dins,
        // per tant es la forma més rapida de saber a quina zona pertanyen
        // les coordenades, i si no retornará null, tot i que no pot passar
        // ja que totes les coordenades possibles estarán en alguna de les zones
        return stream(zones).filter(z -> z.estaDins(x, y)).findFirst().orElse(null);

    }

    public void start ()
    {
        Funcions.executarFil(this);
    }

    @Override
    public void run ()
    {
        // Al igual que tots els fils, s'executa només quan 'actiu' és true
        while (Tauler.actiu)
        {
            // Pausa la execucció durant 10 milisegons
            Funcions.pausarFil(10);

            // Comprovar els xocs entre pilotes

            // Aquesta part del codi es la que evita que les pilotes ocupin una posició
            // que ja està ocupada per una altra pilota, en cas de que sigui aixi es
            // genera un rebot entre les dos pilotes coincidents

            // El primer bucle recorre les pilotes des del principi
            for (int i = 0; i < Main.tauler.pilotes.size(); i++)
            {
                // El segon bucle comença des de la seguent pilota.
                // Aquest metode evita realitzar dos cops la mateixa comprobació, i fa que sigui més eficient
                for (int j = i + 1; j < Main.tauler.pilotes.size(); j++)
                {
                    // Comprova si la futura posició de les dos pilotes será coincident
                    if (Main.tauler.pilotes.get(i).comprovarXocs(Main.tauler.pilotes.get(j)))
                    {
                        // En cas de que ho sigui, genera un rebot entre elles
                        Main.tauler.pilotes.get(i).rebotar(Main.tauler.pilotes.get(j));
                    }
                }
            }
        }
    }
}