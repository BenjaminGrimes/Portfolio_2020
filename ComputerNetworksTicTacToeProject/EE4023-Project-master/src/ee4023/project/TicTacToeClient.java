/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4023.project;

/**
 *
 * @author Benjamin Grimes
 */
public class TicTacToeClient
{
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        Thread mainThread = new Thread(new TTTController());
        mainThread.start();
    }
}
