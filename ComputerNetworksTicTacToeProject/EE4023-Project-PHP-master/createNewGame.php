<?php
	session_start();
?>
<?php

    $userID = $_SESSION["uid"];
    
    $wsdl = $_SESSION['wsdl'];;
    $trace = true;
    $exceptions = true;
    try
    {
        $client = new SoapClient($wsdl, array('trace' => $trace, 'exceptions' => $exceptions));

        $xml_array['uid'] = $userID;

        $response = $client->newGame($xml_array);
        $result = (string) $response->return;

        switch($result)
        {
            case "ERROR-NOTFOUND":
                echo "ERROR: Cannot find the id of the game just added";
                break;
            case "ERROR-RETRIEVE":
                echo alert("ERROR: Cannot access games table");
                break;
            case "ERROR-INSERT":
                echo "ERROR: Cannot add a new game";
                break;
            case "ERROR-DB":
                echo "ERROR: Cannot access DBMS";
                break;
            default:
                echo $result;
        }
    }
    catch(Exception $e)
    {
        echo $e->getMessage();
    }
?>