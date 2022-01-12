package com.rogerr01;

import java.awt.*;
import java.awt.geom.Point2D;

// Representa un parell de numeros que permeten identificar
// la posició d'un punt en un element de dos dimensions.

// Hereda de la classe Point2D.Double, la qual te un funcionament
// similar, peró li he afegit un metode per comprovar interseccions
public class Coordenada extends Point2D.Double
{

    // El constructor es identic al de la superclasse
    public Coordenada (double x, double y)
    {
        super(x,y);
    }

    // Aquest metode comprova si la coordenada actual i la que se li passa per parametre
    // tenen algun punt en el que es cruen, tenin en compte que cada coordenada ocupa la
    // mida d'una pilota, i per tant s'ha de calcular amb el seu diametre.

    // Per a comprovar aixó, crea un rectangle per a cada coordenada, ja que la classe Rectangle
    // ja te un metode intersects() que podem usar per a realitzar aquesta comprovació.
    public boolean esCreua (Coordenada c)
    {
        // Rectangle de la mida d'una pilota i situat en aquesta coordenada
        Rectangle coordenada1 = new Rectangle((int) x, (int) y, Pilota.DIAMETRE, Pilota.DIAMETRE);

        // Rectangle de la mida d'una pilota i situat en la coordenada que s'indica com a parametre
        Rectangle coordenada2 = new Rectangle((int) c.x, (int) c.y, Pilota.DIAMETRE, Pilota.DIAMETRE);

        // Comprova si algun dels seus punts coincideix
        return coordenada1.intersects(coordenada2);
    }

}
