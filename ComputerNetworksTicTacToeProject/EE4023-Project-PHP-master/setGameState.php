<?php
    session_start();
?>
<?php

    $gameID = $_POST["gID"];
    $state = $_POST["gstate"];
    
    $wsdl = $_SESSION['wsdl']; 
    $trace = true;
    $exceptions = true;
    try
    {
        $client = new SoapClient($wsdl, array('trace' => $trace, 'exceptions' => $exceptions));
        
        $xml_array['gid'] = $gameID;
        $xml_array['gstate'] = $state;
        
        $response = $client->setGameState($xml_array);
        $setStateResult = (string) $response->return;
        
        switch($setStateResult)
        {
            case "1":
                //echo "1";
                break;
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