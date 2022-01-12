package com.rogerr01;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.System.*;
import static com.rogerr01.Dades.*;
import static com.rogerr01.Funcions.*;
import static com.rogerr01.Main.*;

// S'executa en un thread diferent i gestiona els temps que la pilota ha estat en cada zona
public class Contador
{

    // Bloc de codi sincronitzat
    private static final ReadWriteLock blocSinc = new ReentrantReadWriteLock();

    // Numero de cops que s'executa la funció per actualitzar el temps
    public static int execuccions = 0;

    // Temps total que s'ha estat executant
    public static double tempsExecuccio = 0;

    // Mitjana de temps que es tarda en executar
    public static double mitjanaTemps = 0;

    // Temps d'inici de la execucció
    public static double tempsInici;

    // Temps fianl de la execucció
    public static double tempsFinal;

    // Temps de cada zona
    private static double temps1 = Double.parseDouble(pantalla.lbZona1.getText().split(" ")[2]);
    private static double temps2 = Double.parseDouble(pantalla.lbZona2.getText().split(" ")[2]);
    private static double temps3 = Double.parseDouble(pantalla.lbZona3.getText().split(" ")[2]);
    private static double temps4 = Double.parseDouble(pantalla.lbZona4.getText().split(" ")[2]);

    // Temps total
    private static double tempsTotal = temps1 + temps2 + temps3 + temps4;

    // Temps d'espera per a tornar-se a executar (en milisegons)
    private static final int ESPERA = 100;

    // Percentatge de temps de cada zona
    private static double percentatge1 = 0.00;
    private static double percentatge2 = 0.00;
    private static double percentatge3 = 0.00;
    private static double percentatge4 = 0.00;


    // Actualitza els marcadors de temps (cada pilota l'executa en el seu propi fil)d
    public static void actualitzarTemps (int x, int y)
    {
        // Es comença a contar el temps d'execucció
        tempsInici = nanoTime();

        // Obtenir el identificador de zona on está la pilora
        int zona = getZona(x, y).getNum();

        // Aquest bloc de codi només es pot executar en un fil a la vegada, els demés esperen el seu torn
       blocSinc.readLock().lock();
            // Augmenta el temps a la zona que correspon
            switch (zona)
            {
            case 1: temps1 += 0.025;
                break;
            case 2: temps2 += 0.025;
                break;
            case 3: temps3 += 0.025;
                break;
            case 4: temps4 += 0.025;
                break;
            }

            // Augmenta sempre el temps total
            tempsTotal += 0.025;

            // Mostra el temps i els percentages
            mostrarTemps();
            actualitzarPercentatges();

        blocSinc.readLock().unlock();

        // Aqui ja s'ha realitzat l'actualització de dades concurrents i es para el temporitzador
        tempsFinal = nanoTime();

        // Per a calcular i mostrar la mitjana de temps també es necessari sincronitzar els fils,
        // ja que sino les dades no serán correctes
        synchronized (new Object())
        {
            calcularMitjana();

        }

    }

    private static void mostrarTemps ()
    {
        // Caracter separador per als decimals dels segons
        char separador = '.';

        // Mostrar els segons en cada zona
        pantalla.lbZona1.setText("Zona 1: " + formatarDecimals(temps1, separador) + " segons");
        pantalla.lbZona2.setText("Zona 2: " + formatarDecimals(temps2, separador) + " segons");
        pantalla.lbZona3.setText("Zona 3: " + formatarDecimals(temps3, separador) + " segons");
        pantalla.lbZona4.setText("Zona 4: " + formatarDecimals(temps4, separador) + " segons");
    }

    // Actualitzar el valor dels percentatges
    private static void actualitzarPercentatges ()
    {

        // El percentatge de cada zona és el temps d'aquella zona entre el total i multiplicat per 100
        percentatge1 = temps1 / tempsTotal * 100;
        percentatge2 = temps2 / tempsTotal * 100;
        percentatge3 = temps3 / tempsTotal * 100;
        percentatge4 = temps4 / tempsTotal * 100;

        // Caracter separador per als decimals del percentatge
        char separador = '.';

        // Mostrar els percentatges de cada zona
        pantalla.lbPrcZona1.setText("Zona 1: " + formatarDecimals(percentatge1, separador) + " %");
        pantalla.lbPrcZona2.setText("Zona 2: " + formatarDecimals(percentatge2, separador) + " %");
        pantalla.lbPrcZona3.setText("Zona 3: " + formatarDecimals(percentatge3, separador) + " %");
        pantalla.lbPrcZona4.setText("Zona 4: " + formatarDecimals(percentatge4, separador) + " %");

    }

}
