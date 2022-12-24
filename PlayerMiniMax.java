/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.hb;

import edu.upc.epsevg.prop.othello.CellType;
import edu.upc.epsevg.prop.othello.GameStatus;
import edu.upc.epsevg.prop.othello.IAuto;
import edu.upc.epsevg.prop.othello.IPlayer;
import edu.upc.epsevg.prop.othello.Move;
import edu.upc.epsevg.prop.othello.SearchType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Eric i Alejandro
 */
public class PlayerMiniMax implements IPlayer, IAuto {
    
    private String name;
    /**
     * Profunditat màxima possible
     */
    private int depth_maxima;
    
    /**
     * Número total de nivells de profunditat assolits
     */
    private int calculDepth;
    
    /**
     * Número total de nodes explorats mitjançant el minimax durant un moviment
     */
    private int contabilitatNodes;
    private CellType nostreJugador;
    private CellType OposatJugador;
    private int[][][] HashZobrist = new int[8][8][2];
    //private ArrayList<Point> Hcorners;
    //private ArrayList<Point> Dcorners;
    
    //Taula assigna puntuacio a cada posicio tauler
    //Posicions adjacents a corners en horitzonal o vertical valen -50
    //Posicions adjacents a corners en diagonal valen -100
    //Corners valen 100
    //Posicions inicials valen 1
    /*static final private int[][] puntuacioTauler = {
            { 100,   -70,   20,   20,   20,   20,   -70,  100},
            { -70,   -100,   10,   10,   10,   10,   -100,  -70},
            { 20,   10,   10,   10,   10,   10,   10,  20},
            { 20,   10,   10,   1,   1,   10,   10,  20},
            { 20,   10,   10,   1,   1,   10,   10,  20},
            { 20,   10,   10,   10,   10,   10,   10,  20},
            { -70,   -100,   10,   10,   10,   10,   -100,  -70},
            { 100,   -70,   20,   20,   20,   20,   -70,  100}
    };*/
    static final private int[][] puntuacioTauler = {
            { 4,   -3,   2,   2,   2,   2,   -3,  4},
            { -3,   -4,   -1,   -1,   -1,   -1,   -4,  -3},
            { 2,   -1,   1,   0,   0,   1,   -1,  2},
            { 2,   -1,   0,   1,   1,   0,   -1,  2},
            { 2,   -1,   0,   1,   1,   0,   -1,  2},
            { 2,   -1,   1,   0,   0,   1,   -1,  2},
            { -3,   -4,   -1,   -1,   -1,   -1,   -4,  -3},
            { 4,   -3,   2,   2,   2,   2,   -3,  4}
    };
     
    
    /**
     * Constructor 
     * @param name
     */
    public PlayerMiniMax(String name) {
        //this.Hcorners = new ArrayList<>();
        this.name = name;
        this.calculDepth = 0;
        this.contabilitatNodes = 0;
        Random r = new Random();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                for(int k = 0; k < 2; k++) {
                    this.HashZobrist[i][j][k] = r.nextInt();
                }
            }
        }
        this.depth_maxima = 0;
    }
    
    /**
     * Decideix moviment del jugador donat un tauler
     * @param gs tauler actual
     * @return Move
     */
    @Override
     public Move move(GameStatus gs) {
        Point pos = primerMoviment (gs, 200, Integer.MAX_VALUE, Integer.MIN_VALUE);
        return new Move (pos, contabilitatNodes, calculDepth, SearchType.MINIMAX);
    }
    
    /**
     * Mira moviments possibles, crea tauler amb els moviments  i crida recursivament a funcio MiniMax.
     * @param gs tauler actual 
     * @param depth profunditat 
     * @param alpha amb valor Integer.MAX_VALUE
     * @param beta amb valor Integer.MIN_VALUE); 
     * @return Mira els moviments possibles, Si no n'hi ha cap retorna null, si no retorna el millor moviment
     */
    public Point primerMoviment(GameStatus gs,  int depth, int alpha, int beta) {
       int x;
       this.depth_maxima = depth;
       int millor = Integer.MIN_VALUE;
       ArrayList<Point> moviments = gs.getMoves();
       if(moviments.isEmpty()){
            return null;
       }
       Point onTirar =  moviments.get(0);
       
       for (int i = 0; i < moviments.size(); i++) {
                //Creació tauler buit
                GameStatus child = new GameStatus(gs);
                child.movePiece(moviments.get(i));
                contabilitatNodes++;
                x = MiniMax(child, depth-1, !true, alpha, beta);
                if (x > millor) {
                    millor = x;
                    onTirar = moviments.get(i);
                }
        }
       return onTirar;
    }  
    
    /**
     * Si depth = 0 crida a la funcio que calcula la heuristica. Mira moviments possibles, crea tauler amb els moviments, 
     * fa poda alpha-beta i crida recursivament a funcio MiniMax. 
     * @param gs tauler actual 
     * @param depth profunditat 
     * @param MINorMax boolean decideix si es MIN or MAX dins l'arbre minimax
     * @param alpha amb valor Integer.MAX_VALUE
     * @param beta amb valor Integer.MIN_VALUE); 
     * @return Retorna heuristica del millor moviment trobat. 
     */
    public int MiniMax(GameStatus gs,  int depth, boolean MINorMax, int alpha, int beta) {
        int x;
        int d = this.depth_maxima - depth;
        if (d > this.calculDepth) {
            this.calculDepth = d;
        }
        
        // Si profunditat es 0
        if (depth == 0) {
            return heuristica(gs, gs.getCurrentPlayer());
        }
        
        if (MINorMax) {
            x = Integer.MIN_VALUE;
        }
        else {
            x = Integer.MAX_VALUE;
        }
        contabilitatNodes++;
        ArrayList<Point> moviments = gs.getMoves();
        if (MINorMax == true) {
            for (int i = 0; i < moviments.size(); i++) {
                //Creació tauler buit
                GameStatus child = new GameStatus(gs);
                child.movePiece(moviments.get(i));
                x = MiniMax(child, depth-1, !MINorMax, alpha, beta);
                alpha = Math.max(x, alpha);
                if(alpha >= beta) {
                    break;
                }
                if (x >= beta) {
                    return x; 
                }
            }
        }
        
        else {
            for (int i = 0; i < moviments.size(); i++) {
                //Creació tauler buit
                GameStatus child = new GameStatus(gs);
                child.movePiece(moviments.get(i));
                x = MiniMax(child, depth-1, !MINorMax, alpha, beta);
                beta = Math.min(x, beta);
                if(alpha >= beta) {
                    break;
                }
                if (x <= alpha) {
                    return x;
                }
            }
        }
        return x;
    }
    
    /**Crida a la funcio calculH, suma la puntuacio del jugador actusal i li resta la de oponent.
     * Suma els moviments possibles
     * @param gs tauler actual
     * @param jugadorActual player al que li toca tirar
     * @return Retorna el valor total de la heuristica
     */
    public int heuristica(GameStatus gs, CellType jugadorActual) {
        int h = 0;
        
        CellType oponent = CellType.opposite(jugadorActual); 
        int posmov = gs.getMoves().size();
        //h = posmov;
        
        h += gs.getScore(jugadorActual);
        h -= gs.getScore(oponent);

        h += calculH(gs, jugadorActual); 
        //- calculH(gs, oponent);
        h -= calculH(gs, oponent);
        
        System.out.print(h);
        return h;
        //return 1;
    }
    
    /**Compara el tauler del joc amb la taula puntuacioTauler
     * @param gs tauler actual
     * @param jugadorActual player al que li toca tirar
     * @return Retorna el valor de la heuristica (h)bobtingut de la taula de puntuacions
     */
    public int calculH (GameStatus gs, CellType jugadorActual) {
        int h = 0;
        for (int i = 0; i < puntuacioTauler.length; i++) {
            for (int j = 0; j < puntuacioTauler.length; j++) {
                if (gs.getPos(i, j)== jugadorActual) {
                    h += puntuacioTauler[i][j];
                }
            }
        }
        return h;
    }
    
    /**
     * No fa res
     */
    @Override
    public void timeout() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    @Override
    public String getName() {
        return "MiniMax(" + name + ")";
    }
    
}