<?php
    session_start();
?>
<?php
    $x = (int) $_POST["x"];
    $y = (int) $_POST["y"];
    $gID = (int) $_POST["gid"];
    $uID = (int) $_POST["uid"];
    
    $wsdl = $_SESSION['wsdl'];;
    $trace = true;
    $exceptions = true;
    
    try
    {
        $client = new SoapClient($wsdl, array('trace' => $trace, 'exceptions' => $exceptions));
        
        $xml_array['x'] = $x;
        $xml_array['y'] = $y;
        $xml_array['gid'] = $gID;
        $xml_array['pid'] = $uID;
        
        $response = $client->takeSquare($xml_array);
        $takeResult = $response->return;
        
        switch($takeResult)
        {
            case "1":
                echo $takeResult;
                break;
            case "0":
                echo "ERROR: Unsuccessful taking square";
                break;
            case "ERROR-TAKEN":
                echo "ERROR: Square already taken";
                break;
            case "ERROR-DB":
                echo "ERROR: Cannot access DBMS";
                break;
            case "ERROR":
                echo "ERROR: An error has occurred";
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