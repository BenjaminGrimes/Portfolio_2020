<?php
    session_start();
?>
<?php
    $userID = (int)$_SESSION["uid"];
    $gameID = $_POST["gid"];
    $previousNumOfRows = (int)$_POST["numRows"];
    $player = (int)$_POST["player"];

    $wsdl = $_SESSION['wsdl'];;
    $trace = true;
    $exceptions = true;
    try
    {
        $client = new SoapClient($wsdl, array('trace' => $trace, 'exceptions' => $exceptions));
        
        // get the game board
        $xml_array['gid'] = $gameID;
        $response = $client->getBoard($xml_array);
        $board = $response->return;
        echo $board;
    }
    catch(Exception $e)
    {
        echo $e->getMessage();
    }
?>