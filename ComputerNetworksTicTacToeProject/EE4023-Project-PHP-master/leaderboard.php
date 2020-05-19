<?php session_start(); ?>
<!DOCTYPE html>
<html lang="en-ie">

<head>
    <title>Leaderboard</title>
    <style>
        *{
            font-family: "Century Gothic", CenturyGothic, Geneva, AppleGothic, sans-serif;
        }
        body {
            background-color: lightblue;
        }
        .leaderboardTable, .leaderboardTD, .leaderboardTH {
            border: 1px solid black;
            text-align: center;
        }
        
        .leaderboardTable {
            border-collapse: collapse;
            width: 100%;
        }

        .leaderboardTH, .leaderboardTD {
            padding: 10px;
        }

    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script>

        $.ajaxSetup({
            cache: false
        });

        $(document).ready(function(){

            $.post("getLeaderboard.php", 
                {}, 
                function(data, status) 
                {
                    $("#results").html(data);
                }
            );
        });
    </script>
	
</head>
<body>
<div id="container">
    <div id="header">
    </div>
    <div id="content">
        <form>
            <div id="results"></div>
        </form>
    </div>
</div>
</body>
</html>