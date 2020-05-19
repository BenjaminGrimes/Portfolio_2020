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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Benjamin Grimes
 */
public class LoginView extends JFrame
{
    private JPanel panel;
    private JButton backBtn;
    private JButton loginBtn;
    private JTextField pw_text;
    private JTextField uname_text;

    public LoginView()
    {
        panel = new JPanel();
        createView();
    }
    
    public void createView()
    {
        this.setBounds(500, 300, 250, 100);
        
        JLabel uname_label = new JLabel("Username:");
        uname_text = new JTextField();
        
        JLabel pw_label = new JLabel("Password:");
        pw_text = new JPasswordField();
        
        backBtn = new JButton("Back");
        loginBtn = new JButton("Login");
        
        this.setTitle("Login");
        
        panel.setLayout(new GridLayout(3, 2));
        panel.add(uname_label);
        panel.add(uname_text);
        panel.add(pw_label);
        panel.add(pw_text);
        panel.add(backBtn);
        panel.add(loginBtn);
        
        this.add(panel);
        this.setVisible(false);
    }
    
    public String getUsernameText()
    {
        return uname_text.getText();
    }
    
    public String getPasswordText()
    {
        return pw_text.getText();
    }
    
    public void resetFields()
    {
        uname_text.setText("");
        pw_text.setText("");
    }
    
    public void addLoginBtnListener(ActionListener listener)
    {
        loginBtn.addActionListener(listener);
    }
    
    public void addBackBtnListener(ActionListener listener)
    {
        backBtn.addActionListener(listener);
    }
}
