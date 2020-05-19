<?php session_start(); ?>
<!DOCTYPE html>
<html lang="en-ie">

<head>
    <title>Welcome To TIC-TAC-TOE</title>
    <style>
        * {
            font-family: "Century Gothic", CenturyGothic, Geneva, AppleGothic, sans-serif;
        }
        body {
            background-color: lightblue;
        }
        h1 {
            color: black;
            text-align: center;
        }
        .center {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }
        .button {
            background-color: #4CAF50; /* Green */
            border: none;
            color: white;
            padding: 16px 32px;
            width: 200px;
            align-self: center;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            font-family: "Century Gothic", CenturyGothic, Geneva, AppleGothic, sans-serif;
            margin: 4px 2px;
            -webkit-transition-duration: 0.4s; /* Safari */
            transition-duration: 0.4s;
            cursor: pointer;
        }
        .loginbutton{
            background-color: white; 
            color: black; 
            border: 2px solid #4CAF50;
        }
        .loginbutton:hover {
            background-color: #4CAF50;
            color: white;
        }
        .registerbutton {
            background-color: white; 
            color: black; 
            border: 2px solid #f44336;
        }
        .registerbutton:hover {
            background-color: #f44336;
            color: white;
        }
    </style>
</head>
<body>

<div id="container">
    <div id="header">
    </div>
    <div id="content" class="center">
        <?php
        $_SESSION['wsdl'] = "http://localhost:8080/TTTWebApplication/TTTWebService?WSDL";
        //echo "VERSION: " . phpversion();
            if(isset($_POST['login']))
            {
                // Login was clicked
                ?>
                    <script>
                        window.location("login.php");
                    </script>
                <?php
            }
            if(isset($_POST['register']))
            {
                // Register was clicked
                ?>
                    <script>
                        window.location("register.php");
                    </script>
                <?php
            }
            ?>
            <h1>Welcome To Tic-Tac-Toe</h1>
            <form action="" method="POST">
                <table>
                    <tr>
                        <td>
                            <input class="button loginbutton" type="submit" name="login" value="Login" tabindex="1">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input class="button registerbutton" type="submit" name="register" value="Register" tabindex="2">
                        </td>
                    </tr>
                </table>
            </form>		
            <?php
        ?>
    </div>
</div>
</body>
</html>