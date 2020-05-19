<?php session_start(); ?>
<!DOCTYPE html>
<html lang="en-ie">

<head>
    <title>Menu</title>
    <style>
        *{
            font-family: "Century Gothic", CenturyGothic, Geneva, AppleGothic, sans-serif;
        }
        body {
            background-color: lightblue;
        }
        .center {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }
        
        .openGamesTable, .openGamesTD, .openGamesTH {
            border: 1px solid black;
            text-align: center;
        }
        
        .openGamesTable {
            border-collapse: collapse;
            width: 100%;
        }

        .openGamesTH, .openGamesTD {
            padding: 10px;
        }
        
        .button {
            background-color: #4CAF50; /* Green */
            border: none;
            color: white;
            width: 400px;
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
        .menubutton {
            background-color: white;
            color: black;
            border: 2px solid #555555;
        }
        .menubutton:hover {
            background-color: #555555;
            color: white;
        }
        input[type=text]{
            width: 50%;
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
        
        var timeout = null;
        
        function updateOpenGamesTable() {
            $.post("getOpenGames.php",
                {},
                function(data, status)
                {
                    $("#ogTable").html(data);
                }
            );
            timeout = setTimeout("updateOpenGamesTable()", 3000);
        }
        
        $.ajaxSetup({
            cache: false
        });
        
        $(document).ready(function(){
            updateOpenGamesTable();
            
            $(document).on('click', '#viewScoreBtn', function()
            {
                //window.location("index.php");
                //$("#results").html("<p>View Score pressed<p>");
                window.open("score.php");
            });

            $(document).on('click', '#viewLeaderboardBtn', function()
            {
                //window.location("index.php");
                //$("#results").html("<p>View Leaderboard pressed<p>");
                window.open("leaderboard.php");
            });

            $(document).on('click', '#createNewGameBtn', function()
            {
                //window.location("index.php");
                $.post("createNewGame.php", 
                    {
                        
                    }, 
                    function(data, status) 
                    {
                        if(data.indexOf("ERROR") > -1)
                        {
                            alert(data);
                        }
                        else
                        {
                            //$("#results").append("SUCCESS");
                            window.open("TicTacToeGame.php?gid="+data+"&p=1");
                        }
                    }
                );
            });

            $(document).on('click', '#joinGameBtn', function()
            {
                //window.location("index.php");
                //$("#results").html("<p>Join pressed<p>");
                $.post("tryJoin.php",
                {
                    gID: $("#selectGameText").val()
                },
                function(data, status)
                {
                    //$("#results").html(data);
                    if(data.indexOf("ERROR") > -1)
                    {
                        // display error
                    }
                    else
                    {
                        window.open("TicTacToeGame.php?gid="+data+"&p=2");
                    }
                });
            });
            
            // Code taken for only allowing numberic input from here:
            // ----- https://stackoverflow.com/questions/469357/html-text-input-allows-only-numeric-input -----
            $("#selectGameText").keydown(function (e) {
                // Allow: backspace, delete, tab, escape, enter and .
                if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
                     // Allow: Ctrl/cmd+A
                    (e.keyCode == 65 && (e.ctrlKey === true || e.metaKey === true)) ||
                     // Allow: Ctrl/cmd+C
                    (e.keyCode == 67 && (e.ctrlKey === true || e.metaKey === true)) ||
                     // Allow: Ctrl/cmd+X
                    (e.keyCode == 88 && (e.ctrlKey === true || e.metaKey === true)) ||
                     // Allow: home, end, left, right
                    (e.keyCode >= 35 && e.keyCode <= 39)) {
                         // let it happen, don't do anything
                         return;
                }
                // Ensure that it is a number and stop the keypress
                if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
                    e.preventDefault();
                }
            });
            // -------------------------------------------------------------------------------------------------
        });

    </script>
	
</head>
<body>
<div id="container">
    <div id="header">
    </div>
    <div id="content" class="center">
        <form>
            <table class="menu_elements">
                <!-- View Score -->
                <tr>
                    <td colspan="2">
                        <input id="viewScoreBtn" class="button menubutton" type="button" name="view_score" value="View Score" tabindex="5" /> 
                    </td>
                </tr>

                <!-- View Leaderboard -->
                <tr>
                    <td colspan="2">
                        <input id="viewLeaderboardBtn" class="button menubutton" type="button" name="view_leaderboards" value="Leaderboards" tabindex="6"/>
                    </td>
                </tr>

                <!-- Create new game -->
                <tr>
                    <td colspan="2">
                        <input id="createNewGameBtn" class="button menubutton" type="button" name="create_game" value="Create New Game" tabindex="7"/>
                    </td>
                </tr>

                <!-- Open games table -->
                <tr>
                    <td>
                        <div id="ogTable" class="ogGames" style="overflow-y: scroll; height:200px; width: 420px;"></div>
                    </td>
                </tr>
                
                <!-- Join Game -->
                <tr>
                    <td>
                        <br>
                    <label for="selectGameText">Enter a Game ID:</label>
                        <input id="selectGameText" type="text" tabindex="8" placeholder="Enter a Game ID" />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input id="joinGameBtn" class="button menubutton" type="button" name="join" value="Join Game" tabindex="9"/>
                    </td>
                </tr>
            </table>
            <div id="results"></div>
        </form>
    </div>
</div>
</body>
</html>