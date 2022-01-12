package com.rogerr01;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.awt.Color.*;
import static com.rogerr01.Funcions.*;

class Tauler extends JPanel
{

    // CONSTANTS
    public static int MIDA = 401;
    public static int POSICIO = 75;
    public static Color COLOR = WHITE;
    public static ExecutorService exe = Executors.newCachedThreadPool();

    // VARIABLES
    public static volatile boolean actiu = false;
    public List<Pilota> pilotes = new ArrayList<>();
    public int numPilotes = 0;

    public Tauler ()
    {
        // Posicio i mida del tauler
        setBounds(POSICIO, POSICIO, MIDA, MIDA);

        setOpaque(true);

        // Color de fons
        setBackground(COLOR);

        // Crea i afegeix la primera pilota
        afegirPilota();

        // Mostra el tauler
        setVisible(true);

        new Thread(this :: run).start();
    }

    // Dibuixar els elements grafics
    public void paint (Graphics g)
    {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        dibuixarLinies(g2);
        colocarNums(g2);

        pilotes.forEach(p -> p.draw(g2));
    }


    public void afegirPilota ()
    {

        // La coordenada inicial per a la primera pilota
        Coordenada c = new Coordenada(5, 5);

        // Si no és la primera pilota, s'intenta generar-li una coordenada aleatoria que esitgui disponible
        try
        {
            if (numPilotes != 0)
            {
                c = generarCoordenada();
            }
        }

        // Si no es pot, significará que ja están totes ocupades i per tant s'haurá d'esperar a que
        // quedin espais lliures per poder afegir una altra pilota
        catch (SenseEspai exeption)
        {
            // Es mostra el avis i es finalitza l'execucció de la funció
            JOptionPane.showMessageDialog(null, exeption.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Es crea una nova pilota amb les coordenades indicades
        Pilota pilota = new Pilota(c.x, c.y);

        // S'afegeix a la llsta i s'inicia el thread
        pilotes.add(pilota);

        exe.execute(pilota);

        // S'augmenta el contador de numero de pilotes
        numPilotes++;

    }

    // Elimina totalment l'existencia de l'ultima pilota de la llista
    public void eliminarPilota ()
    {

        // Si hi ha 0 pilotes no s'ha d'executar aquesta funció
        if (numPilotes < 1) return;

        // Busca quina pilota s'ha d'eliminar
        Pilota pilotaEliminada = pilotes.get(pilotes.size() - 1);

        // S'atura el seu fil d'execucció propi
        pilotaEliminada.stop();

        // S'elimina de la llista, per tant deixará de dibuixar-se en cada repaint
        pilotes.remove(pilotaEliminada);

        // Es redueix el numero en el marcador de pilotes
        numPilotes--;
    }


    // Activa l'execucció de tots els fils
    public void start ()
    {
        // Indicar que s'han d'estar executant tots els threads
        actiu = true;

        // Iniciar els de cada pilota
        pilotes.forEach(p -> exe.execute(p));

    }

    public void run ()
    {
        while (isVisible())
        {
            pausarFil(25);
            repaint();
        }
    }

    // Atura l'execucció de tots els fils existents
    public void stop ()
    {
        actiu = false;
    }
}
