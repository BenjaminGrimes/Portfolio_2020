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
public class RegisterView extends JFrame
{
    private JPanel panel;
    
    private JTextField uname_text;
    private JTextField pw_text;
    private JTextField name_text;
    private JTextField surname_text;
    private JButton registerBtn;
    private JButton backBtn;
    
    public RegisterView()
    {
        panel = new JPanel();
        createView();
    }
    
    public void createView()
    {
        this.setBounds(500, 300, 250, 200);
        
        uname_text = new JTextField();
        pw_text = new JPasswordField();
        name_text = new JTextField();
        surname_text = new JTextField();
        registerBtn = new JButton("Register");
        backBtn = new JButton("Back");
        
        this.setTitle("Register");
        
        panel.setLayout(new GridLayout(5, 2));
        panel.add(new JLabel("Username:"));
        panel.add(uname_text);
        panel.add(new JLabel("Password:"));
        panel.add(pw_text);
        panel.add(new JLabel("Name:"));
        panel.add(name_text);
        panel.add(new JLabel("Surname:"));
        panel.add(surname_text);
        panel.add(backBtn);
        panel.add(registerBtn);
        
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
    
    public String getNameText()
    {
        return name_text.getText();
    }
    
    public String getSurnameText()
    {
        return surname_text.getText();
    }
    
    public void clearFields()
    {
        uname_text.setText("");
        pw_text.setText("");
        name_text.setText("");
        surname_text.setText("");
    }
    
    public void addRegisterBtnListener(ActionListener listener)
    {
        registerBtn.addActionListener(listener);
    }
    
    public void addBackBtnListener(ActionListener listener)
    {
        backBtn.addActionListener(listener);
    }
}
