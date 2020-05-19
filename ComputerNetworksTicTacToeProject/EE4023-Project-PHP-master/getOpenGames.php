<?php
	session_start();
?>
<?php

    $wsdl = $_SESSION['wsdl'];;
    $trace = true;
    $exceptions = true;
	
    try
    {
        $client = new SoapClient($wsdl, array('trace' => $trace, 'exceptions' => $exceptions));

        $response = $client->showOpenGames();
        $result = (string) $response->return;
        
        if($result == "ERROR-NOGAMES")
        {
            echo "Error: No games found";
        }
        elseif($result == "ERROR-DB")
        {
            echo "ERROR: Cannot Access the DBMS";
        }
        else
        {
            // split the string
            $rows = explode("\n", $result);
            echo "<table id=\"openGameTable\" class=\"ogGames\" style=\"width: 400px\">
                <tr id=\"openGamesRow\" class=\"openGamesTR\">
                    <th class=\"openGamesTH\">GAME ID</th>
                    <th class=\"openGamesTH\">USERNAME</th>
                    <th class=\"openGamesTH\">TIME STARTED</th>
                </tr><tbody>";
            
            for($i = 0; $i < count($rows); $i++)
            {
                $elements = explode(",", $rows[$i]);
                if($elements[1] !== $_SESSION['username'])
                {
                    echo "<tr class=\"openGamesTR\"><td class=\"openGamesTD\">" . $elements[0] . 
                            "</td><td class=\"openGamesTD\">" . $elements[1] 
                            . "</td><td class=\"openGamesTD\">" . $elements[2] .
                            "</td></tr>";
                }
            }
            echo "</tbody></table>";
        }
    }
    catch(Exception $e)
    {
        echo $e->getMessage();
    }
?>