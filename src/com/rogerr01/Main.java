package com.rogerr01;

public class Main
{
    // El tauler és on estan els elements dibuixats
    public static Tauler tauler = new Tauler();

    // La pantalla és el JFrame principal amb tots els botons, etiquetes i el tauler
    public static Pantalla pantalla = new Pantalla();


    public static void main (String[] args)
    {
        // Afegeix el tauler a la pantalla
          pantalla.add(tauler);
    }
}