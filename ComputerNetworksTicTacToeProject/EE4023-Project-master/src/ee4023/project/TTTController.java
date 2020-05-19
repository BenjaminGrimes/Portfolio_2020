/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4023.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import ttt.james.server.TTTWebService;
import ttt.james.server.TTTWebService_Service;

/**
 *
 * @author Benjamin Grimes
 */
public class TTTController implements Runnable
{
    
    private int uid;
    private String username;
    
    private TTTWebService_Service link;
    private TTTWebService proxy;
    
    private MainView mainView;
    private LoginView loginView;
    private RegisterView registerView;
    private MenuView menuView;
    private ScoreView scoreView;
    private LeaderboardView leaderboardView;
    
    public TTTController()
    {
        link = new TTTWebService_Service();
        proxy = link.getTTTWebServicePort();
        
        uid = -1;
        username = "";
        
        createMainView();
        mainView.setVisible(true);
        createLoginView();
        createRegisterView();
        createMenuView();
        createScoreView();
        createLeaderboardView();
    }
    
    @Override
    public void run() 
    {
        while(true)
        {
            if(menuView.isActive())
            {
                menuView.updateOpenGamesTable(proxy.showOpenGames(), username);
            }
            if(scoreView.isActive())
            {
                scoreView.updateScore(proxy.leagueTable(), username);
            }
            if(leaderboardView.isActive())
            {
                leaderboardView.updateLeaderboard(proxy.leagueTable());
            }
            try 
            {
                Thread.sleep(3000);
            } 
            catch (InterruptedException ex)
            {
                Logger.getLogger(TTTController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void createMainView()
    {
        mainView = new MainView();
        mainView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainView.addLoginBtnListener(loginBtnActionListener);
        mainView.addRegisterBtnListener(registerBtnActionListener);
    }
    
    public void createLoginView()
    {
        loginView = new LoginView();
        loginView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginView.addBackBtnListener(loginViewToMainViewActionListener);
        loginView.addLoginBtnListener(loginViewLoginBtnActionListener);
    }
    
    public void createRegisterView()
    {
        registerView = new RegisterView();
        registerView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerView.addBackBtnListener(RegisterViewToMainViewActionListener);
        registerView.addRegisterBtnListener(RegisterViewRegisterBtnActionListener);
    }
    
    public void createMenuView()
    {
        menuView = new MenuView();
        menuView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuView.updateOpenGamesTable(proxy.showOpenGames(), username);
        menuView.addViewScoreBtnListener(ViewScoreActionListener);
        menuView.addViewLeaderboardBtnListener(ViewLeaderboardActionListener);
        menuView.addNewGameBtnListener(CreateNewGameActionListener);
        menuView.addJoinGameBtnListener(JoinSelectedGameActionListener);
    }
    
    public void createScoreView()
    {
        scoreView = new ScoreView();
    }
    
    public void createLeaderboardView()
    {
        leaderboardView = new LeaderboardView();
    }
    
    public void createGame(String result)
    {
        // Open selected game...
        int gid = Integer.parseInt(result);
        TicTacToeGame game = new TicTacToeGame(gid, uid, proxy, 1);
        Thread t = new Thread(game);
        game.addWindowListener(new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                System.out.println("Closing game window...");
                String gameState = proxy.getGameState(gid);
                switch (gameState) 
                {
                    case "ERROR-NOGAME":
                        System.out.println("ERROR: game " + gid + "not found...");
                        break;
                    case "ERROR-DB":
                        System.out.println("ERROR: could not connect to the DBMS");
                        break;
                    case "-1": // Game has not stated
                        deleteGame(proxy.deleteGame(gid, uid), gid);
                        break;
                    default: // gameState == 0, 1, 2, 3
                        break;
                }
            }
        });
        t.start();
    }
    
    private void deleteGame(String deleted, int gid)
    {
        switch (deleted) 
        {
            case "ERROR-GAMESTARTED":
                JOptionPane.showMessageDialog(menuView, "ERROR: Game has started. Cannot delete it.", "ERROR", JOptionPane.ERROR_MESSAGE);
                break;
            case "ERROR-DB":
                JOptionPane.showMessageDialog(menuView, "ERROR: Cannot access the DBMS", "ERROR", JOptionPane.ERROR_MESSAGE);
                break;
            case "1":
                System.out.println("SUCCESS: Game " + gid + " deleted successfully...");
                //game.dispose();
                break;
            default:
                break;
        }
    }
    
    private void registerUser()
    {
        if(registerView.getUsernameText().isEmpty())
        {
            JOptionPane.showMessageDialog(registerView, "Please enter a username", "", JOptionPane.WARNING_MESSAGE);
        }
        else if(registerView.getPasswordText().isEmpty())
        {
            JOptionPane.showMessageDialog(registerView, "Please enter a password", "", JOptionPane.WARNING_MESSAGE);
        }
        else if(registerView.getNameText().isEmpty())
        {
            JOptionPane.showMessageDialog(registerView, "Please enter a name", "", JOptionPane.WARNING_MESSAGE);
        }
        else if(registerView.getSurnameText().isEmpty())
        {
            JOptionPane.showMessageDialog(registerView, "Please enter a surname", "", JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            String registerResult = proxy.register(registerView.getUsernameText(), registerView.getPasswordText(), registerView.getNameText(), registerView.getSurnameText());
            switch (registerResult) 
            {
                case "ERROR-REPEAT":
                    JOptionPane.showMessageDialog(registerView, "Username already exists.", "", JOptionPane.WARNING_MESSAGE);
                    break;
                case "ERROR-DB":
                    JOptionPane.showMessageDialog(registerView, "ERROR: A database error has occurred", "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    // Registered and goto login screen
                    registerView.setVisible(false);
                    registerView.clearFields();
                    loginView.setVisible(true);
                    break;
            }
        }
    }
    
    private void joinGame(int selectedGame)
    {
        String gameInfo = menuView.getSelectedGameInfo(selectedGame);
        String elements[] = gameInfo.split(" ");
        int gid = Integer.parseInt(elements[0]);
        // TODO check gamestate first!!!
        String joined = proxy.joinGame(uid, gid);
        switch (joined) 
        {
            case "0":
                JOptionPane.showMessageDialog(menuView, "ERROR: Unable to join game", "ERROR", JOptionPane.ERROR_MESSAGE);
                break;
            case "ERROR-DB":
                JOptionPane.showMessageDialog(menuView, "ERROR: Cannot access the DBMS", "ERROR", JOptionPane.ERROR_MESSAGE);
                break;
            case "1":
                TicTacToeGame game = new TicTacToeGame(gid, uid, proxy, 2);
                Thread t = new Thread(game);
                game.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we)
                    {
                        ;
                    }
                });
                t.start();
                break;
            default:
                break;
        }
    }
    
    // ----------------- MAIN VIEW LISTENERS -----------------------------------
    private ActionListener loginBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Login Pressed...");
            mainView.setVisible(false);
            loginView.resetFields();
            loginView.setVisible(true);
        }
    };
    
    private ActionListener registerBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Register pressed...");
            mainView.setVisible(false);
            registerView.clearFields();
            registerView.setVisible(true);
        }
    };
    // -------------------------------------------------------------------------

    
    // --------------------- LOGIN VIEW LISTENERS ------------------------------
    private ActionListener loginViewLoginBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = proxy.login(loginView.getUsernameText(), loginView.getPasswordText());
            if(result > 0)
            {
                System.out.println("SUCCESS: User found...");
                uid = result;
                // Display Menu View
                loginView.setVisible(false);
                menuView.setVisible(true);
                menuView.updateOpenGamesTable(proxy.showOpenGames(), username);
                username = loginView.getUsernameText();
            }
            else
            {
                JOptionPane.showMessageDialog(loginView, "User not found.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    };
    
    private ActionListener loginViewToMainViewActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Back to main Pressed...");
            loginView.setVisible(false);
            mainView.setVisible(true);
        }
    };
    // -------------------------------------------------------------------------
    
    
    
    // -------------------- REGISTER VIEW LISTENERS ----------------------------
    private ActionListener RegisterViewRegisterBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Register user pressed...");
            registerUser();
        }
    };
    
    private ActionListener RegisterViewToMainViewActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Back to main pressed");
            registerView.setVisible(false);
            mainView.setVisible(true);
        }
    };
    // -------------------------------------------------------------------------
    
    
    // ------------------ MENU VIEW LISTENERS ----------------------------------
    private ActionListener ViewScoreActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("View Score pressed...");
            scoreView.setVisible(true);
            scoreView.updateScore(proxy.leagueTable(), username);
        }
    };
    
    private ActionListener ViewLeaderboardActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("View Leaderboard pressed...");
            leaderboardView.setVisible(true);
            leaderboardView.updateLeaderboard(proxy.leagueTable());
        }
    };
    
    // TODO open game window when pressed.
    private ActionListener CreateNewGameActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Create new game pressed...");
            String newGameResult = proxy.newGame(uid);
            switch (newGameResult) 
            {
                case "ERROR-NOTFOUND":
                    JOptionPane.showMessageDialog(menuView, "ERROR: Cannot find the id of the game just added", "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                case "ERROR-RETRIEVE":
                    JOptionPane.showMessageDialog(menuView, "ERROR: Cannot access games table", "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                case "ERROR-INSERT":
                    JOptionPane.showMessageDialog(menuView, "ERROR: Cannot add a new game", "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                case "ERROR-DB":
                    JOptionPane.showMessageDialog(menuView, "ERROR: Cannot access DBMS", "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    System.out.println("SUCCESS: Result:" + newGameResult);
                    createGame(newGameResult);
                    break;
            }
        }
    };
    
    private ActionListener JoinSelectedGameActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Join game pressed...");
            int selectedGame  = menuView.getSelectedGame();
            System.out.println("Selected game:" + selectedGame);
            if(selectedGame == -1)
            {
                JOptionPane.showMessageDialog(menuView, "No game selected", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                joinGame(selectedGame);
            }
        }
    };
    // -------------------------------------------------------------------------
}
