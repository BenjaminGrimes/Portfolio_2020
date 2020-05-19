<?php
    session_start();
?>
<?php
    $gameID = $_POST["gID"];
    $userID = $_SESSION["uid"];
    
    $wsdl = $_SESSION['wsdl']; 
    $trace = true;
    $exceptions = true;
    try
    {
        $client = new SoapClient($wsdl, array('trace' => $trace, 'exceptions' => $exceptions));
        
        $xml_array['uid'] = $userID;
        $xml_array['gid'] = $gameID;
        
        $response = $client->joinGame($xml_array);
        //echo var_dump($response);
        $result = (string) $response->return;
        //echo $result;
        switch($result)
        {
            case "0":
                ?>
                    <script>
                        alert("ERROR: Unable to join game");
                    </script>
                <?php
                break;
            case "ERROR-DB":
                ?>
                    <script>
                        alert("ERROR: Cannot access the DBMS");
                    </script>
                <?php
                break;
            case "1":
                // Go to game board.
                echo $gameID;
            default:
                ;
                break;
        }
    }
    catch(Exception $e)
    {
        echo $e->getMessage();
    }
?>