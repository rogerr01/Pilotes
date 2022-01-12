package com.rogerr01;

import javax.swing.*;
import java.awt.*;

import static com.rogerr01.Contador.actualitzarTemps;
import static com.rogerr01.Funcions.*;
import static com.rogerr01.Tauler.actiu;

public class Pilota extends JComponent implements Runnable
{

    // CONSTANTS
    public final static int DIAMETRE = 30;
    public final Color COLOR;

    // COORDENADES
    public final Coordenada posicio;
    private final Coordenada velocitat;

    // VARIABLES
    private boolean filActiu = true;

    public Pilota (double x, double y)
    {
        // Parametres propis de cada pilota
        posicio = new Coordenada(x, y);
        velocitat = new Coordenada(5, 6);
        COLOR = generarColor();
    }

    // Cada pilota tindrá el seu propi thread en que executará aquest metode,
    // el qual mou la pilota i actualitza el contador de temps de la zona en que está
    public void run ()
    {
        // S'executa si tant el fil de la pilota com el general han d'estar actius
        while (actiu && filActiu)
        {

            // Atura el fil 25 milisegons
            pausarFil(25);

            // Mou la pilota a una nova posició segons la seva velocitat actual
            moure();

            try
            {
                // Actualitza el temps del comptador que correspongui (de forma sincronitzada)
                actualitzarTemps((int) posicio.x, (int) posicio.y);
            }

            // Hi haurá una excepció si la pilota s'ha sortit del tauler i no está en cap zona
            catch (NullPointerException e)
            {
                // En aquest cas es coloca la pilota en la posició inicial
                posicio.setLocation(0, 0);
            }

        }
    }


    public void stop ()
    {
        // Atura el fil de la pilota sense afectar als demés fils
        filActiu = false;
    }

    // Modifica els valors de la velocitat
    public void canviarVelocitat (double x, double y)
    {
        velocitat.setLocation(x, y);
    }

    // Mou la pilota a la posició que li correspon segons la seva velocitat
    public void moure ()
    {

        // Calcula les noves posicions
        var novaX = posicio.x + velocitat.x;
        var novaY = posicio.y + velocitat.y;

        // Si xoca amb els marges laterals, inverteix la velocitat horitzontal
        if (novaX >= Main.tauler.getWidth() - DIAMETRE && velocitat.x > 0 || novaX <= 0 && velocitat.x < 0)
        {
            canviarVelocitat(-velocitat.x, velocitat.y);
        }

        // Si xoca amb els marges superior o inferior, inverteix la velocitat vertical
        else if (novaY >= Main.tauler.getHeight() - DIAMETRE && velocitat.y > 0 || novaY <= 0 && velocitat.y < 0)
        {
            canviarVelocitat(velocitat.x, -velocitat.y);
        }

        // Coloca la pilota en la nova posició
        posicio.setLocation(posicio.x + velocitat.x, posicio.y + velocitat.y);
    }

    // Dibuixa la pilota
    public void draw (Graphics2D g2d)
    {
        dibuixarPilota(g2d, this);
    }


    // Comprova si la pilota xocará amb una altra pilota
    public boolean comprovarXocs (Pilota p)
    {
        // Només realitza la comprovació si les pilotes no son la mateixa
        if (p != this)
        {
            // Comprova si les posicions de les dos pilotes provocaran un xoc
            if (posicio.x + DIAMETRE > p.posicio.x
                && posicio.x < p.posicio.x + DIAMETRE
                && posicio.y + DIAMETRE > p.posicio.y
                && posicio.y < p.posicio.y + DIAMETRE)
            {
                // Hi ha un xoc si la distancia entre les pilotes és més gran que el doble de diametre elevat al quadrat
                return calcularDistancia(this, p) < Math.pow(DIAMETRE * 2 , 2 );
            }
        }

        // Si la pilota es ella mateixa o no hi ha hagut cap xoc, retorna fals
        return false;
    }

    // Crea un rebot més o menys realista i evita que les pilotes es quedin engantxades.
    // El rebot no consisteix simplement en canviar les direccions de les dos pilotes sino
    // que depenen del angle del xoc i de la velocitat que tenen els elements es produeix
    // un intercanvi d'energia en el qual una de les pilotes li dona part de la seva
    // velocitat a l'altra, per tant la velocitat de les pilotes va variant cada cop que
    // es produeix una colisió, pero la velocitat total de les pilotes sempre será la mateixa,
    // ja que s'aplica el principi de conservació de l'energia.
    public void rebotar (Pilota p)
    {
        // Distancia entre les dos pilotes
        double distanciaX = posicio.x - p.posicio.x;
        double distanciaY = posicio.y - p.posicio.y;

        // Diferencia de velocitat entre les dos pilotes
        double velocitatX = p.velocitat.x - velocitat.x;
        double velocitatY = p.velocitat.y - velocitat.y;

        // El producte escalar dels vectors permet determinar si una pilota s'está movent cap a l'altra
        double prodEscalar = (distanciaX * velocitatX) + (distanciaY * velocitatY);

        // En cas de que sigui aixi, es produeix la colisió entre les dos pilotes
        if (prodEscalar > 0)
        {
            // He usat part d'aquest codi com a referencia -> https://gamedev.stackexchange.com/a/20525¡
            double escalaXoc = prodEscalar / calcularDistancia(this, p);

            // Calcula el xoc entre les pilotes
            double xocX = distanciaX * escalaXoc;
            double xocY = distanciaY * escalaXoc;

            // Calcula les noves velocitats per a la pilota actual
            double nouVx1 = velocitat.x + (xocX);
            double nouVy1 = velocitat.y + (xocY);

            // Calcula les noves velocitats per a la pilota amb la que ha xocat
            double nouVx2 = p.velocitat.x - (xocX);
            double nouVy2 = p.velocitat.y - (xocY);

            // S'actualitzen les velocitats de les pilotes
            canviarVelocitat(nouVx1, nouVy1);
            p.canviarVelocitat(nouVx2, nouVy2);
        }
    }
}
