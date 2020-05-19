/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4023.project;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Benjamin Grimes
 */
public class MainView extends JFrame 
{
    private JPanel panel;
    private JButton loginBtn;
    private JButton registerBtn;
    
    public MainView()
    {
        panel = new JPanel();
        createView();
    }
    
    public void createView()
    {
        this.setBounds(500, 300, 250, 100);
        
        loginBtn = new JButton("Login");
        registerBtn = new JButton("Register");
        
        this.setTitle("Main View");
        
        panel.setLayout(new GridLayout(2, 1));
        panel.add(loginBtn);
        panel.add(registerBtn);
        
        this.add(panel);
        this.setVisible(false);
    }
    
    public void addLoginBtnListener(ActionListener listener)
    {
        loginBtn.addActionListener(listener);
    }
    
    public void addRegisterBtnListener(ActionListener listener)
    {
        registerBtn.addActionListener(listener);
    }
}
