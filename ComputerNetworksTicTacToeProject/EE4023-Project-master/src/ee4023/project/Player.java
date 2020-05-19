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
public class Player implements Comparable
{
    private String username;
    private int wins;
    private int losses;
    private int draws;
    
    public Player(String username)
    {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }
    
    public void addWin()
    {
        wins++;
    }
    
    public void addLoss()
    {
        losses++;
    }
    
    public void addDraw()
    {
        draws++;
    }
    
    public void resetWins()
    {
        wins = 0;
    }
    
    public void resetLosses()
    {
        losses = 0;
    }
    
    public void resetDraws()
    {
        draws = 0;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return false;
        if(o instanceof Player)
        {
            Player p = (Player) o;
            return p.username.equals(this.username);
        }
        return false;
    }
    
    @Override
    public String toString()
    {
        return username + "," + wins + "," + losses + "," + draws;
    }

    @Override
    public int compareTo(Object o) 
    {
        return Integer.compare(this.wins, ((Player)o).wins);
    }
    
}
