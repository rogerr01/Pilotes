package com.rogerr01;

public class Zona
{
    // Coordeandes de la zona
    private final int x1; // Primera posició horitzontal
    private final int y1; // Primera posició vertical
    private final int x2; // Segona posició horitzontal
    private final int y2; // Segona posició vertical

    // Numero de la zona
    int num;

    public Zona (int x1, int y1, int x2, int y2, int num)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.num = num;
    }

    // Retorna la posició X inicial
    public int getX ()
    {
        return x1;
    }

    // Retorna la posició Y inicial
    public int getY ()
    {
        return y1;
    }

    // Retorna el numero de zona
    public int getNum ()
    {
        return num;
    }

    // Comprova si unes coordenades estan dins de la zona indicada
    public boolean estaDins (int x, int y)
    {
        return x >= x1 & x <= x2 & y >= y1 & y <= y2;
    }

    // Retorna el centre de la zona (suma de les coordenades entre dos)
    public Coordenada getCentre ()
    {
        // El centre de cada zona és la mitad de la suma dels dos punts,
        // per tant la formula es (x1+x2) / 2

        // Ja que aquesta operació es realitzara 4 vegades cada 10 milisegons al fer el repaint
        // del tauler, he usat com a metode de divisió entre 2 un intercanvi de bits, que el que
        // fa és desplaçar els bits 1 cop cap a la dreta, i per tant el valor resultant será la
        // meitat del valor original, el qual és el mateix que dividir entre dos pero pot resultar
        // una mica més rapid de processar i aixi ser més optim si la operació es repetix moltes vegades.
        final var x = x1 + x2 >> 1;
        final var y = y1 + y2 >> 1;

        // Retorna una coordenada amb els valors del centre de la zona
        return new Coordenada(x,y);
    }
}
