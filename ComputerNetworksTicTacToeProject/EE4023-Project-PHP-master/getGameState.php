<?php
    session_start();
?>
<?php
    $gID = (int) $_POST["gid"];
    
    $wsdl = $_SESSION['wsdl'];;
    $trace = true;
    $exceptions = true;
    
    try
    {
        $client = new SoapClient($wsdl, array('trace' => $trace, 'exceptions' => $exceptions));
        // get the game state
        $xml_array['gid'] = $gID;
        
        $response = $client->getGameState($xml_array);
        $result = $response->return;
        
        switch($result)
        {
            case "ERROR-NOGAME":
                echo "ERROR: Cannot find game with the supplied gid";
                break;
            case "ERROR-DB":
                echo "ERROR: Cannot access DBMS";
                break;
            default:
                echo $result;
                break;
        }
    }
    catch(Exception $e)
    {
        echo $e->getMessage();
    }
?>