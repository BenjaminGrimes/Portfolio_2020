/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4023.project;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Benjamin Grimes
 */
public class ScoreView extends JFrame
{
    private JPanel panel;
    private JLabel userWinsLabel;
    private JLabel userLossesLabel;
    private JLabel userDrawsLabel;
    
    private int wins, losses, draws;
    
    public ScoreView()
    {
        panel = new JPanel();
        createView();
    }
    
    public void createView()
    {
        System.out.println("Creating score view...");
        
        this.setBounds(500, 300, 300, 300);
        
        this.setTitle("Score");
        
        userWinsLabel = new JLabel("X");
        userLossesLabel = new JLabel("X");
        userDrawsLabel = new JLabel("X");
        
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Your wins:"));
        panel.add(userWinsLabel);
        panel.add(new JLabel("Your Losses:"));
        panel.add(userLossesLabel);
        panel.add(new JLabel("Your Draws:"));
        panel.add(userDrawsLabel);
        
        this.add(panel);
        this.setVisible(false);
    }
    
    public void updateScore(String listOfMyGames, String username)
    {
        if(listOfMyGames.equals("ERROR-NOGAMES"))
        {
            JOptionPane.showMessageDialog(this, "ERROR: No games found", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if(listOfMyGames.equals("ERROR-DB"))
        {
            JOptionPane.showMessageDialog(this, "ERROR: Cannot Access DBMS", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            // Found games
            String [] rows = listOfMyGames.split("\n");
            
            wins = 0;
            losses = 0;
            draws = 0;

            for(int i = 0; i < rows.length; i++)
            {
                getScore(rows[i].split(","), username);
            }
            userWinsLabel.setText(""+ wins);
            userLossesLabel.setText("" + losses);
            userDrawsLabel.setText("" + draws);
        }
    }
    
    private void getScore(String [] elements, String username)
    {
        for(int i = 1; i <= 2; i++)
        {
            if(elements[i].equals(username))
            {
                switch(elements[3])
                {
                    case "0":
                        ;
                        break;
                    case "1":
                        if(i == 1)
                            wins++;
                        else
                            losses++;
                        break;
                    case "2":
                        if(i == 1)
                            losses++;
                        else
                            wins++;
                        break;
                    case "3":
                        draws++;
                        break;
                    default :
                        ;
                }
            }
        }
    }
}
