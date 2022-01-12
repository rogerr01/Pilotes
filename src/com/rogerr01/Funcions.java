package com.rogerr01;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.stream.IntStream;

import static java.awt.Color.*;
import static java.awt.Font.*;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.lang.Math.*;
import static java.lang.String.valueOf;
import static java.lang.Thread.*;
import static java.math.RoundingMode.*;
import static java.text.MessageFormat.*;
import static java.util.Arrays.stream;
import static com.rogerr01.Dades.zones;

public class Funcions
{
    static Random rnd = new Random();

    // Genera un color aleatori
    public static Color generarColor()
    {
        // Valors RGB aleatoris
        final var R = rnd.nextInt(0x100);
        final var G = rnd.nextInt(0x100);
        final var B = rnd.nextInt(0x100);

        // Genera un color amb els valors RGB anteriors
        return new Color(R,G,B);
    }

    // Genera una coordenada aleatoria dins del tauler, que estigui disponible
    public static Coordenada generarCoordenada() throws SenseEspai
    {

        // El bucle s'executará com a maxim 20.000 vegades si no troba cap coordenada lliure
        for (int i = 0; i < 20000; i++)
        {
            // El rang es calcula de forma dinamica segons les mides del tauler i de la pilota
            int x = rnd.nextInt(Tauler.MIDA - (Pilota.DIAMETRE + 5));
            int y = rnd.nextInt(Tauler.MIDA - (Pilota.DIAMETRE + 5));

            // Genera la coordenada
            Coordenada c = new Coordenada(x, y);

            // Aquesta part del codi comprova que la coordenada creada no estigui
            // ocupant espai d'una pilota ja existent, per a fer-ho genera un stream
            // de la llista de pilotes actuals, i comprova si alguna de les pilotes
            // ja está ocupant part d'aquella zona, en cas de que sigui aixi es torna
            // a repetir el bucle for superior i per tant es genera una nova coordenada
            // que tornara a passar aquest procés de comprobació.

            // En cas de que s'arribi al final del stream (s'ha comprovat tota la llista)
            // i no s'hagi trobat cap coincidencia, es retornara la coordenada generada
            // ja que significa que está disponible per a colocar-hi una nova pilota.
            if (Main.tauler.pilotes.stream().takeWhile(
                    p -> !p.posicio.esCreua(c)).anyMatch(
                    p -> p.equals(Main.tauler.pilotes.get(Main.tauler.pilotes.size() - 1))
            )) return c;

        }

        // Si s'executa el bucle més de 10.000 vegades i encara no s'ha trobat una posició lliure,
        // significa que están totes ocupades, ja que com a maxim en un tauler de 600x600 on cada pilota
        // ocupa 60px podrien haver-hi 100 posicions disponibles, i la possibilitat de generar 10.000
        // posicions aleatories i que cap coincideixi amb una de les disponibles es casi inexistent
        throw new SenseEspai();
    }


    // Aquest metode dibuixa una pilota en un component Graphics
    public static void dibuixarPilota(Graphics g, Pilota p)
    {
        // Converteix el element Graphics en un Graphics2D, ja que
        // té metodes més avançats com per exemple setRenderingHint,
        // que permet donar una millor qualitat al dibuix
        final Graphics2D g2d = (Graphics2D) g;

        // Converteix en numeros enters les coordenades de la pilota
        final var x = (int) p.posicio.x;
        final var y = (int) p.posicio.y;

        // Dibuixa una rodona 2D del color de la pilota i amb les seves dimensions i coordenades
        g2d.setColor(p.COLOR);
        g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g2d.fillOval(x, y, Pilota.DIAMETRE, Pilota.DIAMETRE);

        // Dibuixa un marge de color negre al voltant de la pilota,
        // ja que si el color és molt clar pot ser dificil de veure
        g2d.setColor(BLACK);
        g2d.drawOval(x, y, Pilota.DIAMETRE, Pilota.DIAMETRE);
    }

    // Calcula la distancia entre dos pilotes
    public static double calcularDistancia (Pilota p1, Pilota p2)
    {
        // Diferencia de distancia horitzontal
        final var xDist = p1.posicio.x -  p2.posicio.x;

        // Diferencia de distancia vertical
        final var yDist = p1.posicio.y -  p2.posicio.y;

        // Suma de les dos diferencies elevades al quadrat
        return pow(xDist, 2) + pow(yDist, 2);
    }

    // Acció d'iniciar el moviment i els contadors de temps
    public static void iniciar ()
    {
        // Inicia la execucció d'un thread per a moure la pilota
        Main.tauler.start();

        // Gestor de coordenades
        new Dades().start();
    }

    // Acció d'aturar el thread de la pilota
    public static void stop ()
    {
        Main.tauler.stop();
    }

    public static void reiniciarCalculVelocitat()
    {
        // Reiniciar el calcul de velocitat en zones
        Contador.execuccions = 0;
        Contador.tempsExecuccio = 0;
    }

    public static void tancarAplicacio()
    {
        // Acció per a posar en pausa els threads
        stop();

        // Aturar el executor
        Tauler.exe.shutdown();

        // Deixa de mostrar la pantalla
        Main.pantalla.dispose();

        // Finalitza l'execucció del programa
        System.exit(0);
    }

    // Formatar un numero per a que mostri només dos valors decimals separats per un caracter separador
    public static String formatarDecimals (double valor, char separador)
    {
        // Definir el format
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(FLOOR);

        // Aplicar el format al valor que s'ens ha passat com a parametre
        String resultat = df.format(valor);

        // Modificar la coma per el caracter separador escollit
        resultat = resultat.replace(",", String.valueOf(separador));

        // Retornar una String amb el numero decimal formatat
        return resultat;
    }

    public static void executarFil(Runnable r)
    {
        // Nou fil
        Thread thread = new Thread(r);

        // Prioritat predeterminada
        thread.setPriority(NORM_PRIORITY);

        // Iniciar
        thread.start();
    }

    // Dibuixar les linies de divisió de les 4 zones
    public static void dibuixarLinies(Graphics2D g)
    {
        stream(zones).forEach(z -> g.drawRect(z.getX(), z.getY(), Tauler.MIDA/2 - 1, Tauler.MIDA/2 - 1));
    }

    // Colocar els numeros de zona
    public static void colocarNums (Graphics2D g)
    {
        // Tipus de lletra i color
        g.setFont(new Font("Arial", BOLD, 32));
        g.setColor(BLACK);

        // Crea un IntStream que recorre totes les zones i coloca
        // el numero de zona en el centre del quadrat.

        // He usat un IntStream en lloc del stream normal ja que
        // necessito saber el numero de cada iteració (0..3) per
        // a colocar-lo en el text que escriu
        IntStream.range(0, zones.length).forEach(i ->
        {
            // Busca les coordenades del centre de la zona
            Coordenada c = zones[i].getCentre();

            // Escriu el numero de la zona, que será el valor de
            // la variable iterativa més 1, ja que ha començat per 0
            g.drawString(valueOf(i + 1), (int) c.x, (int) c.y);

        });
    }


    public static void calcularMitjana()
    {
        // Es suma el temps al total per a calcular la mitjana
        Contador.tempsExecuccio += (Contador.tempsFinal - Contador.tempsInici);

        // S'indica que hi ha hagut una nova execucció del metode
        Contador.execuccions++;

        // Mitjana de temps (temps total entre numero d'execuccions)
        Contador.mitjanaTemps = (Contador.tempsExecuccio / Contador.execuccions);

        // Aqui es crea el format en el que es mostrara el missatge,
        // el metode usat pertany a la classe MessageFormat, que permet
        // generar una string amb un patró i uns arguments determinats
        String info = format(
                "\n N° Pilotes: {0} \n\n " +
                        "Temps d''actualització: \n\n " +
                        "{1} nanosegons \n\n " +
                        "( {2} milisegons )",
                        Main.tauler.numPilotes, Contador.mitjanaTemps, Contador.mitjanaTemps / 1000000);

        // Mostra el text en el quadre de text de la pantalla
        Main.pantalla.taInfo.setText(info);
    }

    // Pausa el fil actual durant la quantitat de temps indicada
    public static void pausarFil(long milisegons)
    {
        try
        {
            // Metode de la classe Thread per a pausar la execucció
            sleep(milisegons);
        }
        // Excepció en cas de que s'interrompi el fil
        catch (InterruptedException e)
        {
            // Marca el fil com a interromput
           currentThread().interrupt();
        }
    }
}
