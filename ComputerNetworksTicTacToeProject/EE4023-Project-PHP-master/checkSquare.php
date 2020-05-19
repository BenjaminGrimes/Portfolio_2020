<?php
    session_start();
?>
<?php
    $x = (int) $_POST["x"];
    $y = (int) $_POST["y"];
    $gID = (int) $_POST["gid"];
    
    $wsdl = $_SESSION['wsdl'];
    $trace = true;
    $exceptions = true;
    
    try
    {
        $client = new SoapClient($wsdl, array('trace' => $trace, 'exceptions' => $exceptions));
        
        // get the game state
        $xml_array['x'] = $x;
        $xml_array['y'] = $y;
        $xml_array['gid'] = $gID;
        
        $response = $client->checkSquare($xml_array);
        $checkResult = $response->return;
        switch($checkResult)
        {
            case "ERROR-DB":
                echo "ERROR: Cannot connect to DBMS";
                break;
            default:
                echo $checkResult;
        }
    }
    catch(Exception $e)
    {
        echo $e->getMessage();
    }
?>