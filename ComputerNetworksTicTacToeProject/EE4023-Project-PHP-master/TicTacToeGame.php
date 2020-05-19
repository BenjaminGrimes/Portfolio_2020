<?php session_start(); ?>
<!DOCTYPE html>
<html lang="en-ie">
<head>
    <title>Tic-Tac-Toe Game</title>
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
        .boardSquare {
            width: 100px;
            height: 100px;
            font-size: 50px;
        }
    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script>
        var uid = "<?php echo $_SESSION["uid"]; ?>";
        var gid = "<?php echo $_GET["gid"]; ?>";
        var turn = 1;
        var player = "<?php echo $_GET["p"]; ?>";
        player.trim();
        var gamestate = "-1";
        var previousNumOfRows = 0;
        var timeout = null;
        
        function updateGame() 
        {
            do
            {
                $.post("updateGame.php",
                    {
                        gid: gid,
                        numRows: previousNumOfRows,
                        player: player
                    },
                    function(data, status)
                    {
                        var board = data.toString();
                        // get the gamestate
                        $.post("getGameState.php",
                        {
                            gid: gid
                        },function(data, status){
                            if(data.indexOf("ERROR") > -1)
                            {
                                alert(data);
                            }
                            else
                            {
                                gamestate = data.toString();
                            }
                        });
                        if(board.indexOf("ERROR-DB") > -1)
                        {
                            alert(data);
                        }
                        else if(board.indexOf("ERROR-NOMOVES") > -1)
                        {
                            if(gamestate === "0")
                            {
                                document.getElementById('turnText').innerHTML = "TURN: PLAYER 1";
                            }
                        }
                        else
                        {
                            var rows = board.split("\n");
                            if(previousNumOfRows < rows.length)
                            {
                                previousNumOfRows = rows.length;
                                // Check board
                                checkBoard(rows);
                            }
                        }
                    }
                );
                if(gamestate === "-1" || gamestate === "0")
                {
                    timeout = setTimeout("updateGame()", 1000);
                }
                else
                {
                    switch(gamestate)
                    {
                        case "1":
                            document.getElementById('turnText').innerHTML = "PLAYER 1 WINS!";
                            break;
                        case "2":
                            document.getElementById('turnText').innerHTML = "PLAYER 2 WINS!";
                            break;
                        case "3":
                            document.getElementById('turnText').innerHTML = "IT'S A DRAW!";
                            break;
                        default :
                            break;
                    }
                }
            } while(gamestae === "-1" || gamestate === "0");
        }
        
        function tryTakeSquare(button)
        {
            
            if(turn === Number(player) && gamestate === "0")
            {
            var text = button.id.split(",");
            var x = Number(text[0]);
            var y = Number(text[1]);
            $.post("checkSquare.php", {
                x: x,
                y: y,
                gid: gid
            }, function(data, status)
            {
                //$("#results").append(data+"<br>");
                if(data.indexOf("ERROR") > -1)
                {
                    alert(data);
                }
                else if(Number(data) === 1)
                {
                    alert("Square taken already");
                }
                else if(Number(data) === 0)
                {
                    // try take empty square
                    $.post("tryTakeSquare.php", 
                    {
                        x: x,
                        y: y,
                        gid: gid,
                        uid: uid
                    },
                    function(data, status)
                    {
                        if(data.indexOf("ERROR") > -1)
                        {
                            alert(data);
                        }
                        else
                        {
                            // update ui
                            if(player === "1")
                            {
                                button.value = "X";
                            }
                            else if(player === "2")
                            {
                                button.value = "O";
                            }
                            // Check for win
                            
                            $.post("checkForWin.php",
                            {
                                gID: gid
                            },
                            function(data, status)
                            {
                                if(data.indexOf("ERROR") > -1)
                                {
                                    alert(data);
                                }
                                else if(data === "0")
                                {
                                    // game still playing
                                }
                                else
                                {
                                    switch(data)
                                    {
                                        case "1":
                                            gamestate = 1;
                                            setGameState(1);
                                            break;
                                        case "2":
                                            gamestate = 2;
                                            setGameState(2);
                                            break;
                                        case "3":
                                            gamestate = 3;
                                            setGameState(3);
                                            break;
                                        default :
                                            break;
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
        }
        
        function setGameState(state)
        {
            $.post("setGameState.php",
            {
                gID: gid,
                gstate: state
            },
            function(data, status)
            {
                if(data.indexOf("ERROR") > -1)
                {
                    alert(data);
                }
                else
                {
                    // game state has been set
                }
            });
        }
        
        function checkBoard(rows)
        {
            var lastMove = rows[rows.length-1];
            var elements = lastMove.split(",");
            var playerID = elements[0].toString();
            var x = Number(elements[1]);
            var y = Number(elements[2]);
            
            if(rows.length % 2 === 0 && gamestate === "0")
            {
                turn = 1;
                document.getElementById('turnText').innerHTML = "TURN: PLAYER 1";
            }
            else if(rows.length % 2 === 1 && gamestate === "0")
            {
                turn = 2;
                document.getElementById('turnText').innerHTML = "TURN: PLAYER 2";
            }
            
            if(uid !== playerID && player === "1")
            {
                // update player 1s UI
                var name = x.toString()+","+y.toString();
                document.getElementById(name).value = "O";
            }
            else if(uid !== playerID && player === "2")
            {
                // Update player 2s UI
                var name = x.toString()+","+y.toString();
                document.getElementById(name).value = "X";
            }
        }
        
        
        $.ajaxSetup({
            cache: false
        });
        
        $(document).ready(function(){
            updateGame();
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
                        <input id="0,0" class="button boardSquare" type="button" onclick="tryTakeSquare(this)" value="" tabindex="1" /> 
                    </td>
                    <td colspan="2">
                        <input id="0,1" class="button boardSquare" type="button" onclick="tryTakeSquare(this)" value="" tabindex="1" /> 
                    </td>
                    <td colspan="2">
                        <input id="0,2" class="button boardSquare" type="button" onclick="tryTakeSquare(this)" value="" tabindex="1" /> 
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input id="1,0" class="button boardSquare" type="button" onclick="tryTakeSquare(this)" value="" tabindex="1" /> 
                    </td>
                    <td colspan="2">
                        <input id="1,1" class="button boardSquare" type="button" onclick="tryTakeSquare(this)" value="" tabindex="1" /> 
                    </td>
                    <td colspan="2">
                        <input id="1,2" class="button boardSquare" type="button" onclick="tryTakeSquare(this)" value="" tabindex="1" /> 
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input id="2,0" class="button boardSquare" type="button" onclick="tryTakeSquare(this)" value="" tabindex="1" /> 
                    </td>
                    <td colspan="2">
                        <input id="2,1" class="button boardSquare" type="button" onclick="tryTakeSquare(this)" value="" tabindex="1" /> 
                    </td>
                    <td colspan="2">
                        <input id="2,2" class="button boardSquare" type="button" onclick="tryTakeSquare(this)" value="" tabindex="1" /> 
                    </td>
                </tr>
            </table>
            <div id="turnText">
                <label id="turnText">Waiting for second Player...</label>
                </div>
            <div id="results"></div>
        </form>
    </div>
</div>
</body>
</html>