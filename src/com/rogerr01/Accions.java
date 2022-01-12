package com.rogerr01;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.rogerr01.Funcions.*;
import static com.rogerr01.Funcions.iniciar;
import static com.rogerr01.Funcions.stop;

// ActionListener per als botons
public class Accions implements ActionListener
{

    // Accions dels botons
    public void actionPerformed (ActionEvent e)
    {

        // Boto d'iniciar
        if (e.getSource().equals(Main.pantalla.btnIniciar))
        {
            // Acció per a iniciar els dos threads
            iniciar();

            // Desactiva el botó d'iniciar i activa el botó d'aturar
            Main.pantalla.btnIniciar.setEnabled(false);
            Main.pantalla.btnStop.setEnabled(true);
        }

        // Botó de parar
        else if (e.getSource().equals(Main.pantalla.btnStop))
        {

            // Acció per a posar en pausa els threads
            stop();

            // Activa el botó d'iniciar i des activa el botó d'aturar
            Main.pantalla.btnIniciar.setEnabled(true);
            Main.pantalla.btnStop.setEnabled(false);
        }

        // Botó de sortir
        else if (e.getSource().equals(Main.pantalla.btnExit))
        {

            // Demana confirmació
            if (JOptionPane.showConfirmDialog(null,"Segur que vols tancar l'aplicació?") != 0)
            {
                // Si s'ha seleccionat que no, s'acaba aqui la funció
                return;
            }

            // Finalitza l'execucció de tots els fils i tanca la aplicació
            tancarAplicacio();
        }

        // Botó per aumentar el numero de pilotes
        else if (e.getSource().equals(Main.pantalla.btnMesPilotes))
        {
            Main.tauler.afegirPilota();
            Main.pantalla.lbPilotes.setText(Main.tauler.numPilotes + (Main.tauler.numPilotes == 1 ? " pilota" : " pilotes"));

            // Reinicia els valors en el calcul del temps d'actualització de dades concurrents
            reiniciarCalculVelocitat();
        }

        // Botó per reduir el numero de pilotes
        else if (e.getSource().equals(Main.pantalla.btnMenysPilotes))
        {
            Main.tauler.eliminarPilota();
            Main.pantalla.lbPilotes.setText(Main.tauler.numPilotes + (Main.tauler.numPilotes == 1 ? " pilota" : " pilotes"));

            // Reinicia els valors en el calcul del temps d'actualització de dades concurrents
            reiniciarCalculVelocitat();
        }
    }
}
