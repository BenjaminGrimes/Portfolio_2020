/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4023.project;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Benjamin Grimes
 */
public class MenuView extends JFrame
{
    private JPanel panel;
    
    private JButton viewScoreBtn;
    private JButton viewLeaderboardsBtn;
    private JButton newGameBtn;
    private JScrollPane scrollPane;
    private JTable openGamesTable;
    private JButton joinGameBtn;
    
    private DefaultTableModel model;
    
    private String [][] data;
    private String [] colNames;
    
    
    public MenuView()
    {
        panel = new JPanel();
        createMenuView();
    }
    
    public void createMenuView()
    {
        this.setBounds(500, 300, 500, 500);
        
        viewScoreBtn = new JButton("View Score");
        viewLeaderboardsBtn = new JButton("Leaderboards");
        newGameBtn = new JButton("Create New Game");
        
        colNames = null;
        data = null;
        
        model = createTableModel();
        openGamesTable = new JTable(model);
        openGamesTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        scrollPane = new JScrollPane(openGamesTable);
        
        joinGameBtn = new JButton("Join Game");
        
        this.setTitle("Menu Screen");
        panel.setLayout(new GridLayout(5, 1));
        panel.add(viewScoreBtn);
        panel.add(viewLeaderboardsBtn);
        panel.add(newGameBtn);
        panel.add(scrollPane);
        panel.add(joinGameBtn);
        
        this.add(panel);
        //this.pack();
        this.setVisible(false);
    }
    
    public void updateOpenGamesTable(String openGames, String username)
    {
        if(openGames.equals("ERROR-NOGAMES"))
        {
            colNames = new String [1];
            colNames[0] = "NO OPEN GAMES";
            
            data = null;
        }
        else if(openGames.equals("ERROR-DB"))
        {
            JOptionPane.showMessageDialog(this, "ERROR: Cannot access the DBMS");
            colNames = new String [1];
            colNames[0] = "ERROR CONNECTING TO DATABASE";
            data = null;
        }
        else
        {
            List<String> openGamesRows = new ArrayList<String>(Arrays.asList(openGames.split("\n")));
            Iterator<String> itr = openGamesRows.listIterator();
            while (itr.hasNext()) 
            {
                String next = itr.next();
                if(next.contains(username))
                {
                    itr.remove();
                }
            }
            if(openGamesRows.size() > 0)
            {
                colNames = new String [3];
                colNames[0] = "GAME ID";
                colNames[1] = "USERNAME";
                colNames[2] = "TIME STARTED";
                data = new String [openGamesRows.size()][colNames.length];
                for(int i = 0; i < data.length; i++)
                {
                    String row [] = openGamesRows.get(i).split(",");

                    for(int j = 0; j < data[i].length; j++)
                    {
                        data[i][j] = row[j];
                    }
                }
            }
            else
            {
                colNames = new String [1];
                colNames[0] = "NO OPEN GAMES";

                data = null;
            }
        }
        
        int selected = openGamesTable.getSelectedRow();
        
        model = createTableModel();
        openGamesTable.setModel(model);
        openGamesTable.revalidate();
        
        // try keep selected row selected
        if(selected != -1)
        {
            try
            {
                openGamesTable.setRowSelectionInterval(selected, selected);
            }
            catch(IllegalArgumentException ex)
            {
                System.out.println("ERROR: Could not select row.");
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
    
    public int getSelectedGame() 
    {
        return openGamesTable.getSelectedRow();
    }
    
    public String getSelectedGameInfo(int selectedRow)
    {
        return data[selectedRow][0] + " " + data[selectedRow][1]; /*+ " " + data[selectedRow][2];*/
    }
    
    public void addViewScoreBtnListener(ActionListener listener)
    {
        viewScoreBtn.addActionListener(listener);
    }
    
    public void addViewLeaderboardBtnListener(ActionListener listener)
    {
        viewLeaderboardsBtn.addActionListener(listener);
    }
    
    public void addNewGameBtnListener(ActionListener listener)
    {
        newGameBtn.addActionListener(listener);
    }
    
    public void addJoinGameBtnListener(ActionListener listener)
    {
        joinGameBtn.addActionListener(listener);
    }
}
