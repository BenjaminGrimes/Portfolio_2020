<?php session_start(); ?>
<!DOCTYPE html>
<html lang="en-ie">

<head>
    <title>Score</title>
    <style>
        *{
            font-family: "Century Gothic", CenturyGothic, Geneva, AppleGothic, sans-serif;
        }
        body {
            background-color: lightblue;
        }
        label{
            font-size: 150%;
            text-align: center;
        }
        .scoreTable, .scoreTD, .scoreTH {
            border: 1px solid black;
            text-align: center;
        }
        
        .scoreTable {
            border-collapse: collapse;
            width: 100%;
        }

        .scoreTH, .scoreTD {
            padding: 10px;
        }
    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script>
        $.ajaxSetup({
            cache: false
        });

        $(document).ready(function()
        {
            $.post("getScore.php", 
                {}, 
                function(data, status) 
                {
                    //$("#results").append(data);
                    var arr = data.split(",");
                    var wCell = document.getElementById("winsCell");
                    wCell.innerHTML = arr[0];

                    var lCell = document.getElementById("lossesCell");
                    lCell.innerHTML = arr[1];

                    var dCell = document.getElementById("drawsCell");
                    dCell.innerHTML = arr[2];
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
            <label for="sTable">Your Score</label>
            <table id="sTable" class="scoreTable">
                <tr class="scoreTable">
                    <th class="scoreTH">Your Wins</th>
                    <th class="scoreTH">Your Losses</th>
                    <th class="scoreTH">Your Draws</th>
                </tr>
                <tr class="scoreTable">
                    <th id="winsCell" class="scoreTD"></th>
                    <th id="lossesCell" class="scoreTD"></th>
                    <th id="drawsCell" class="scoreTD"></th>
                </tr>
                <div id="results"></div>
            </table>
        </form>
    </div>
</div>
</body>
</html>