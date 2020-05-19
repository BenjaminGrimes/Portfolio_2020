<?php
    session_start();
?>
<?php
    $gameID = $_POST["gID"];
    
    $wsdl = $_SESSION['wsdl']; 
    $trace = true;
    $exceptions = true;
    try
    {
        $client = new SoapClient($wsdl, array('trace' => $trace, 'exceptions' => $exceptions));
        
        $xml_array['gid'] = $gameID;
        
        $response = $client->checkWin($xml_array);
        $checkWinResult = (string) $response->return;
        
        switch($checkWinResult)
        {
            case "0":
                // dont do anything, game still playing..
                echo "0";
                break;
            case "1":
                // set state to 1
                echo "1";
                //setState($gameID, 1);
                break;
            case "2":
                // set state to 2
                echo "2";
                break;
            case "3":
                // set state to 3
                echo "3";
                break;
            case "ERROR-RETRIEVE":
                echo "ERROR: Issue getting details about the game.";
                break;
            case "ERROR-NOGAME":
                echo "ERROR: No game can be found mathing the game ID: " + $gameID;
                break;
            case "ERROR-DB":
                echo "ERROR: Problem accessing the DBMS";
                break;
            default:
                break;
                
        }
    }
    catch(Exception $e)
    {
        echo $e->getMessage();
    }
?>