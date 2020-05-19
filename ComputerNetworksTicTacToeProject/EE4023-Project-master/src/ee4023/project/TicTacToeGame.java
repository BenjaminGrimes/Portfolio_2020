/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4023.project;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import ttt.james.server.TTTWebService;

/**
 *
 * @author Benjamin Grimes
 */
public class TicTacToeGame extends JFrame implements Runnable
{
    private final int gID;
    private final int uID;
    private final int player;
    private int turn;
    private String gameState;
    private final TTTWebService proxy;
    
    private JPanel mainPanel;
    private JPanel gamePanel;
    private JButton [][] cells;
    private JLabel info;
    
    public TicTacToeGame(int gID, int uID, TTTWebService proxy, int player)
    {
        this.proxy = proxy;
        this.gID = gID;
        this.uID = uID;
        this.player = player;
        mainPanel = new JPanel();
        gamePanel = new JPanel();
        cells = new JButton[3][3];
        turn = 1;
        createView();
        addListeners();
        this.setVisible(true);
    }
    
    @Override
    public void run() 
    {
        // Get the game state
        gameState = proxy.getGameState(gID);
        System.out.println("GameState: " + gameState);
        int previousNumOfRows = 0;
        while(gameState.equals("-1") || gameState.equals("0"))
        {
            // get board
            String board = proxy.getBoard(gID);
            // Gamestate up here to allow board to update for the last time.
            // TODO ERROR CHECK FOR GAMESTATE
            gameState = proxy.getGameState(gID);
            switch (board) 
            {
                case "ERROR-NOMOVES":
                    // No moves have been taken. Player 1s turn
                    System.out.println("ERROR: No moves have yet been taken.");
                    if(gameState.equals("0"))
                        info.setText("TURN: PLAYER 1");
                    break;
                case "ERROR-DB":
                    System.out.println("ERROR: cannot access the DBMS");
                    JOptionPane.showMessageDialog(this, "ERROR: Cannot access the DBMS", "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    String [] rows = board.split("\n");
                    if(previousNumOfRows < rows.length)
                    {
                        previousNumOfRows = rows.length;
                        checkBoard(rows);
                    }
                    break;
            }
            
            try 
            {    
                Thread.sleep(1000);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(TicTacToeGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        switch (gameState) 
        {
            case "1":
                info.setText("PLAYER 1 WON!");
                JOptionPane.showMessageDialog(this, "PLAYER 1 WON!", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "2":
                info.setText("PLAYER 2 WON!");
                JOptionPane.showMessageDialog(this, "PLAYER 2 WON!", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "3":
                info.setText("IT'S A DRAW!");
                JOptionPane.showMessageDialog(this, "IT'S A DRAW!", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                break;
        }
    }
    
    public void createView()
    {
        this.setBounds(500, 300, 500, 500);
        this.setTitle("Tic-Tac-Toe");
        
        gamePanel.setLayout(new GridLayout(3, 3));
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[i].length; j++)
            {
                cells[i][j] = new JButton("");
                gamePanel.add(cells[i][j]);
            }
        }
        
        info = new JLabel("");
        if(player == 1)
            info.setText("WAITING FOR A SECOND PLAYER");
        
        mainPanel.setLayout(new GridLayout(2, 1));
        
        mainPanel.add(gamePanel);
        mainPanel.add(info);
        
        this.add(mainPanel);
        this.setVisible(false);
    }
    
    public void addListeners()
    {
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[i].length; j++)
            {
                cells[i][j].addActionListener(new TTTListener(i, j));
            }
        }
    }
    
    private void checkBoard(String [] rows)
    {
        // get the rows and count no of turns taken
        //String [] rows = board.split("\n");
        // Check if any moves have been taken since last cycle
        // if there have been then update the board
        System.out.println("CHECK BOARD FOR UPDATE");
        String lastMove = rows[rows.length-1];
        String [] elements = lastMove.split(",");
        int playerID = Integer.parseInt(elements[0]);
        int x = Integer.parseInt(elements[1]);
        int y = Integer.parseInt(elements[2]);

        if(uID != playerID && player == 1)
        {
            // Update player 1s UI
            System.out.println("UPDATING BOARD FOR P1");
            cells[x][y].setText("O");
        }
        else if(uID != playerID && player == 2)
        {
            // Update player 2s UI
            System.out.println("UPDATING BOARD FOR P2");
            cells[x][y].setText("X");
        }

        // Moves have been taken already
        // String format "pID,x,y"
        // First move is first row. etc...
        // no. of turns = no. of rows.
        if(rows.length % 2 == 0 && gameState.equals("0"))
        {
            // if even no of turns then it is player 1s turn 
            turn = 1;
            info.setText("TURN: PLAYER 1");
        }
        else if(rows.length % 2 == 1 && gameState.equals("0"))
        {
            // else if odd no of turns then it is player 2s turn
            turn = 2;
            info.setText("TURN: PLAYER 2");
        }
    }
    
    private class TTTListener implements ActionListener
    {
        private final int x;
        private final int y;
        
        public TTTListener(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(turn == player) // If players turn
            {
                JButton buttonClicked = (JButton) e.getSource();
                System.out.print("Game ID:" + gID + " User ID:" + uID);
                System.out.println(" x:" + x + " y:" + y);

                // Check if turn in run
                String gameState = proxy.getGameState(gID);
                switch (gameState) 
                {
                    case "ERROR-NOGAME":
                        System.out.println("ERROR: cannot find game " + gID);
                        JOptionPane.showMessageDialog(TicTacToeGame.this, "ERROR: cannot find game " + gID, "ERROR", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "ERROR-DB":
                        System.out.println("ERROR: cannot access the DBMS");
                        JOptionPane.showMessageDialog(TicTacToeGame.this, "ERROR: cannot access the DBMS", "ERROR", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "-1":
                        System.out.println("WAITING FOR SECOND PLAYER...");
                        break;
                    case "0":
                        System.out.println("Take move...");
                        checkSquare();
                        break;
                    case "1":
                        System.out.println("PLAYER ONE HAS WON");
                        break;
                    case "2":
                        System.out.println("PLAYER TWO HAS WON");
                        break;
                    case "3":
                        System.out.println("ITS A DRAW");
                        break;
                    default:
                        break;
                }
            }
        }
        
        public void checkSquare()
        {
            String check = proxy.checkSquare(x, y, gID);
            System.out.println("Check = " + check);
            switch (check) {
                case "ERROR-DB":
                    System.out.println("ERROR: cannot connect to DBMS");
                    JOptionPane.showMessageDialog(TicTacToeGame.this, "ERROR: cannot connect to DBMS", "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                case "1":
                    System.out.println("CANNOT TAKE SQUARE, IT HAS ALREADY BEEN TAKEN");
                    break;
                case "0":
                    takeSquare();
                    break;
                default:
                    break;
            }
        }
        
        public void takeSquare()
        {
            // Try take the square
            String takeResult = proxy.takeSquare(x, y, gID, uID);
            System.out.println("TakeResult:" + takeResult);
            switch (takeResult) {
                case "1":
                    // Successfully taken square. Check board for a win.
                    if(player == 1)
                        cells[x][y].setText("X");
                    else if(player == 2)
                        cells[x][y].setText("O");
                    checkForWin();
                    break;
                case "0": 
                    System.out.println("Unsuccessful taking square");
                    break;
                case "ERROR-TAKEN":
                    System.out.println("ERROR: Square already taken");
                    break;
                case "ERROR-DB":
                    System.out.println("ERROR: cannot access DBMS");
                    JOptionPane.showMessageDialog(TicTacToeGame.this, "ERROR: cannot access DBMS", "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                case "ERROR":
                    System.out.println("ERROR: an error has occurred");
                    JOptionPane.showMessageDialog(TicTacToeGame.this, "ERROR: an error has occurred", "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    break;
            }
        }
        
        public void checkForWin()
        {
            String checkWinResult = proxy.checkWin(gID);
            System.out.println("CHECK WIN RESULT: " + checkWinResult);
            switch (checkWinResult) 
            {
                case "0":
                    System.out.println("NO WIN YET - Continue playing...");
                    break;
                case "1":
                    setState(1);
                    break;
                case "2":
                    setState(2);
                    break;
                case "3":
                    setState(3);
                    break;
                case "ERROR-RETRIEVE":
                    System.out.println("ERROR: Issue getting details about the game");
                    JOptionPane.showMessageDialog(TicTacToeGame.this, "ERROR: Issue getting details about the game", "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                case "ERROR-NOGAME":
                    System.out.println("ERROR: No game can be found matching the gID:" + gID);
                    JOptionPane.showMessageDialog(TicTacToeGame.this, "ERROR: No game can be found matching the gID:" + gID, "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                case "ERROR-DB":
                    System.out.println("ERROR: Problem accessing the DBMS");
                    JOptionPane.showMessageDialog(TicTacToeGame.this, "ERROR: Problem accessing the DBMS", "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    break;
            }
        }
        
        private void setState(int state)
        {
            String setStateResult = proxy.setGameState(gID, state);
            switch (setStateResult) 
            {
                case "1":
                    System.out.println("SUCCESS: Game state set to " + state);
                    break;
                case "ERROR-NOGAME":
                    System.out.println("ERROR: Unable to find a game with the supplied gID");
                    JOptionPane.showMessageDialog(TicTacToeGame.this, "ERROR: Unable to find a game with the supplied gID" + gID, "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                case "ERROR-DB":
                    System.out.println("ERROR: Unable to access the DBMS");
                    JOptionPane.showMessageDialog(TicTacToeGame.this, "ERROR: Unable to access the DBMS", "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    break;
            }
        }
    }
}
