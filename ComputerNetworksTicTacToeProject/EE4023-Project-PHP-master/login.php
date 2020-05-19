<?php session_start(); ?>
<!doctype html>
<html lang="en-ie">
<head>
    <title>Login</title>
    <style>
        body {
            background-color: lightblue;
        }
        .center {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }
        .button {
            background-color: #4CAF50; /* Green */
            width: 100px;
            border: none;
            color: white;
            padding: 16px 32px;
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
        .loginbutton {
            background-color: white; 
            color: black; 
            border: 2px solid #4CAF50;
        }
        .loginbutton:hover {
            background-color: #4CAF50;
            color: white;
        }
        .backbutton {
            background-color: white; 
            color: black; 
            border: 2px solid #f44336;
        }
        .backbutton:hover {
            background-color: #f44336;
            color: white;
        }
        h1 {
            color: black;
            text-align: center;
            font-family: "Century Gothic", CenturyGothic, Geneva, AppleGothic, sans-serif;
        }
        label {
            font-family: "Century Gothic", CenturyGothic, Geneva, AppleGothic, sans-serif;
        }
        input[type=text], input[type=password]{
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script>

        $.ajaxSetup({
            cache: false
        });

        $(document).ready(function(){

            $(document).on('click', '#loginBtn', function()
            {
                val1 = $("#username_text").val();
                val2 = $("#password_text").val();
                $.post("tryLogin.php", 
                    {
                            uname: val1,
                            pword: val2 
                    }, 
                    function(data, status) 
                    {
                        $("#results").html(data);
                    }
                );
            });
            
            $(document).on('click', '#backBtn', function()
            {
                    window.location("index.php");
            });
        });
    </script>
</head>
<body>
<div id="container">
    <div id="header">
    </div>
    <div id="content" class="center">
        <h1>Login</h1>
        <form>
            <table>
                <tr>
                <label for="username_text">Username:</label>
                    <input id="username_text" type="text" name="uname" id="uname" tabindex="1" placeholder="Enter your username"/>
                </tr>
                <tr>
                <label for="password_text">Password:</label>
                    <input id="password_text" type="password" name="pword" id="pword" tabindex="2" placeholder="Enter your password"/></
                </tr>
                <tr>
                    <td>
                        <input id="loginBtn" class="button loginbutton" type="button" name="login" value="Login" tabindex="3" />
                    </td>
                    <td>
                        <input id="backBtn" class="button backbutton" type="button" name="back" value="Back" tabindex="4" />
                    </td>
                </tr>
            </table>
        </form>

        <div id="results"></div>
    </div>
    <div id="sidebar">
    </div>
    <div id="footer">
    </div>
    <div class="clear"></div>
</div>
</body>
</html>