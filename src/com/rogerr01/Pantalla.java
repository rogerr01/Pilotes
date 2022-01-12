package com.rogerr01;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class Pantalla extends JFrame
{
    // Constants
    private static final int WIDTH_ELEMENTS = 200;
    private static final int HEIGHT_ELEMENTS = 40;
    private static final int MIDA_PANTALLA = 700;
    private static final int ALTURA_PANTALLA = 650;
    private static final int POSICIO_PANTALLA = 500;
    private static final int POS_INICIAL = 75;
    private static final int SEPARACIO = WIDTH_ELEMENTS + 10;


    // Botons per al control de la execucció
    public JButton btnIniciar = new JButton("INICIAR");
    public JButton btnStop = new JButton("STOP");
    public JButton btnExit = new JButton("SORTIR");

    // Control del numero de pilotes
    public JLabel lbPilotes = new JLabel("1 pilota");
    public JButton btnMesPilotes = new JButton("+");
    public JButton btnMenysPilotes = new JButton("-");

    // Etiquetes per al temps de cada zona
    public JLabel lbZona1 = new JLabel("Zona 1: 0.00 segons");
    public JLabel lbZona2 = new JLabel("Zona 2: 0.00 segons");
    public JLabel lbZona3 = new JLabel("Zona 3: 0.00 segons");
    public JLabel lbZona4 = new JLabel("Zona 4: 0.00 segons");

    // Etiquetes per al percentatge de cada zona
    public JLabel lbPrcZona1 = new JLabel("Zona 1: 00.00 %");
    public JLabel lbPrcZona2 = new JLabel("Zona 2: 00.00 %");
    public JLabel lbPrcZona3 = new JLabel("Zona 3: 00.00 %");
    public JLabel lbPrcZona4 = new JLabel("Zona 4: 00.00 %");

    // Informació sobre els threads
    public JTextArea taInfo = new JTextArea();

    // Accions dels botons
    Accions accions = new Accions();

    // Posicions dels elements
    static int x = POS_INICIAL - 5;
    static int y = 510;

    public Pantalla () throws HeadlessException
    {
        // La mida de la pantalla ha de ser fixa
        setResizable(false);

        // Aqui es defineixen la posició i mides de la pantalla principal
        setBounds(POSICIO_PANTALLA, POSICIO_PANTALLA, MIDA_PANTALLA, ALTURA_PANTALLA);

        // He posat el layout null ja que al haver-hi pocs elements m'hes més facil colocarlos
        // usant posicions absolutes, tot i aixi no és gens recomanable usar un pantalla sense layout
        setLayout(null);

        // Al tancar la finestra s'ha de aturar la execucció de tot el programa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Font per al text dels elements
        Font font = new Font("Arial", Font.BOLD, 14);

        // Aplicar la font a tots els components de la pantalla

        // Crea un stream amb tots els components indicats i en cada un d'ells executa
        // el metode setFont, aixi en lloc d'usar 14 linies de codi es pot fer en 1
        Stream.<JComponent>of(btnIniciar, btnStop, lbZona1, lbZona2, lbZona3, lbZona4, lbPilotes,
                lbPrcZona1, lbPrcZona2, lbPrcZona3, lbPrcZona4
                , btnMesPilotes, btnMenysPilotes, btnExit).forEach(j -> j.setFont(font));

        // El text de la informació no és editable
        taInfo.setEditable(false);

        // Marge de color negre per al quadre de text
        taInfo.setBorder(new LineBorder(Color.BLACK));

        lbPilotes.setFont(lbPilotes.getFont().deriveFont(20f));

        // * * Creació manual del layout usant posicions absolutes * * //

        // Posicionar els botons
        btnIniciar.setBounds(x, y, WIDTH_ELEMENTS, HEIGHT_ELEMENTS);
        x += SEPARACIO;
        btnStop.setBounds(x, y, WIDTH_ELEMENTS, HEIGHT_ELEMENTS);
        x -= SEPARACIO;
        y+= 50;
        btnExit.setBounds(x, y, WIDTH_ELEMENTS * 2 + 10, HEIGHT_ELEMENTS);

        // Posicionar les etiquetes
        x = POS_INICIAL;
        y = 30;
        lbZona1.setBounds(x, y, WIDTH_ELEMENTS, HEIGHT_ELEMENTS);

        x += SEPARACIO;
        lbZona2.setBounds(x, y, WIDTH_ELEMENTS, HEIGHT_ELEMENTS);

        x = POS_INICIAL;
        y = 470;
        lbZona3.setBounds(x, y, WIDTH_ELEMENTS, HEIGHT_ELEMENTS);

        x += SEPARACIO;
        lbZona4.setBounds(x, y, WIDTH_ELEMENTS, HEIGHT_ELEMENTS);

        // Posicionar el panell de numero de pilotes
        x = 500;
        y = 70;
        lbPilotes.setBounds(x, y, WIDTH_ELEMENTS, HEIGHT_ELEMENTS);
        y += 35;
        btnMesPilotes.setBounds(x, y, WIDTH_ELEMENTS / 4, HEIGHT_ELEMENTS);
        x += 55;
        btnMenysPilotes.setBounds(x, y, WIDTH_ELEMENTS / 4, HEIGHT_ELEMENTS);

        // Posicionar el panell de informació
        x -= 50;
        y += 60;
        taInfo.setBounds(x, y, WIDTH_ELEMENTS - 20, HEIGHT_ELEMENTS * 4);

        // Posicionar el panell de percentatges
        y += 160;
        Arrays.asList(lbPrcZona1, lbPrcZona2, lbPrcZona3, lbPrcZona4).forEach( j ->
        { j.setBounds(x, y, WIDTH_ELEMENTS, HEIGHT_ELEMENTS);
                Pantalla.y += j == lbPrcZona4 ? 0 :40;
        });
        
        // Afegir els actionListener als botons i afegir els botons a la pantalla
        // Crea una llista dels botons i els hi aplica el metode addActionListener
        // a través d'un forEach en notació lambda
        Arrays.asList(btnIniciar, btnStop, btnMesPilotes, btnMenysPilotes, btnExit).forEach(b -> b.addActionListener(accions));

        // Afegir tots els elements a la pantalla, usant tambe la interficie Stream
        Arrays.<JComponent>asList(lbPilotes, lbZona1, lbZona2, lbZona3, lbZona4, lbPrcZona1
                , lbPrcZona2, lbPrcZona3, lbPrcZona4, taInfo, btnIniciar, btnStop,
                btnMesPilotes, btnMenysPilotes, btnExit).forEach(this :: add);

        // Al principi el botó de STOP estará desactivat fins que s'inici el moviment
        btnStop.setEnabled(false);

        // Mostra la pantalla
        setVisible(true);
    }
}
