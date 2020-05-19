/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4023.project;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Benjamin Grimes
 */
public class LeaderboardView extends JFrame
{
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTable leaderboardTable;
    private DefaultTableModel model;
    String [][] data;
    String [] colNames;
    
    private ArrayList<Player> players;
    
    public LeaderboardView()
    {
        players = new ArrayList<Player>();
        panel = new JPanel();
        createView();
    }
    
    public void createView()
    {
        this.setBounds(500, 300, 400, 300);
        
        colNames = null;
        data = null;
        
        model = createTableModel();
        
        leaderboardTable = new JTable(model);
        leaderboardTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
        scrollPane = new JScrollPane(leaderboardTable);
        
        this.setTitle("Leaderboard");
        
        panel.setLayout(new GridLayout(1, 1));
        panel.add(scrollPane);
        
        this.add(panel);
        this.setVisible(false);
    }
    
    public void updateLeaderboard(String leagueTable)
    {
        if(leagueTable.equals("ERROR-NOGAMES"))
        {
            System.out.println("ERROR: No games found");
            colNames = new String[1];
            colNames[0] = "NO OPEN GAMES";
            data = null;
        }
        else if(leagueTable.equals("ERROR-DB"))
        {
            System.out.println("ERROR: Cannot access the DBMS");
            colNames = new String[1];
            colNames[0] = "ERROR CONNECTING TO THE DBMS";
            data = null;
        }
        else
        {
            // Reset wins, losses, draws for players
            resetPlayers();
            
            // Have the string of leage table
            // formate "gID,p1UID,p2UID,gameState,startTime\n"
            // Split into league string into rows
            String [] rows = leagueTable.split("\n");
            for(int i = 0; i < rows.length; i++)
            {
                validateScore(rows[i].split(","));
            }
            
            Collections.sort(players, Collections.reverseOrder());
            
            colNames = new String[4];
            colNames[0] = "USERNAME";
            colNames[1] = "WINS";
            colNames[2] = "LOSSES";
            colNames[3] = "DRAWS";
            
            data = new String[players.size()][colNames.length];
            for(int i = 0; i < data.length; i++)
            {
                String [] row = players.get(i).toString().split(",");
                for(int j = 0; j < data[i].length; j++)
                {
                    data[i][j] = row[j];
                }
            }
        }
        model = createTableModel();

        leaderboardTable.setModel(model);
        leaderboardTable.revalidate();
    }
    
    private void resetPlayers()
    {
        for(Player player : players)
        {
            player.resetWins();
            player.resetLosses();
            player.resetDraws();
        }
    }
    
    private void validateScore(String [] elements)
    {
        Player p;
        
        for(int i = 1; i <= 2; i++)
        {
            p = new Player(elements[i]);
            if(!players.contains(p))
            {
                players.add(p);
            }
            
            for(Player player : players)
            {
                if(player.equals(p))
                {
                    switch(elements[3])
                    {
                        case "0":
                            ;
                            break;
                        case "1":
                            if(i == 1 )
                                player.addWin();
                            else
                                player.addLoss();
                            break;
                        case "2":
                            if(i == 1)
                                player.addLoss();
                            else
                                player.addWin();
                            break;
                        case "3":
                            player.addDraw();
                            break;
                        default :
                            ;
                    }
                }
            }
        }
    }
    
    private DefaultTableModel createTableModel()
    {
        DefaultTableModel m = new DefaultTableModel(data, colNames)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        return m;
    }
}
