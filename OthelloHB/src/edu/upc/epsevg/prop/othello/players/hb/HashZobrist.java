/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.hb;

import edu.upc.epsevg.prop.othello.CellType;
import static edu.upc.epsevg.prop.othello.CellType.EMPTY;
import static edu.upc.epsevg.prop.othello.CellType.PLAYER1;
import java.util.Random;

/**
 *
 * @author Eric
 */
public class HashZobrist {
    /**
     * tauler que consta de valors aleatoris per a realitzar el hash
     */
    private final int[][][] tauler; 
    
    /**
     * constructor
     * @param size
     */
    public HashZobrist(int size){
        
        //this.InfoTabla = new HashMap<>();
        this.tauler = new int[size][size][2];
        Random r = new Random();
        
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                for(int k = 0; k < 2; k++) {
                    this.tauler[i][j][k] = r.nextInt();
                }
            }
        }
        
        //this.whiteMove = rand.nextInt();
        
    }
    
    /**
     * Pasa de un celltype a un int per assignar-ho al tercer camp del tauler
     * @param player El CellType corresponent
     * @return si és EMPTY: 0, si és PLAYER1 (negre): 1 i si és PLAYER2 (blanques): 2
     */
    private int quinJugador(CellType player) {
        if (player == EMPTY) {
            return 0;
        }
        if (player == PLAYER1) {
            return 1;
        }
        else {
            return 2;
        }
    }
}
