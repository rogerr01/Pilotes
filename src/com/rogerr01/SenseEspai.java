package com.rogerr01;

// Aquesta excepció indica que actualment no hi ha espai lliure per a colocar cap altra pilota.

// Aixó és degut a que al colocar una nova pilota es busca una posició aleatoria en la qual
// cap dels pixels que ocupa estiguin ocupats per una altra pilota, per tant depenen de la colocació
// de les pilotes en cada moment pot variar la quantiat d'espais disponibles, i no es podrá colocar
// una nova pilota fins que es moguin de tal manera que s'alliberi l'espai necessari.

public class SenseEspai extends Exception
{
    public SenseEspai ()
    {
        super("Actualment no hi ha espai per a més pilotes");
    }
}
