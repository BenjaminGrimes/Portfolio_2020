<?php
    session_start();
?>
<?php
    $user = $_POST["uname"];
    $pass = $_POST["pword"];

    $wsdl = $_SESSION['wsdl'];
    $trace = true;
    $exceptions = true;
    try
    {
        $client = new SoapClient($wsdl, array('trace' => $trace, 'exceptions' => $exceptions));

        $xml_array['username'] = $user;
        $xml_array['password'] = $pass;

        $response = $client->login($xml_array);
        $result = (string) $response->return;
        
        switch($result)
        {
            case "-1":
                echo "<p>Error invalid login</p>";
                break;
            default:
                $_SESSION["uid"] = $result;
                $_SESSION["username"] = $user;
                ?>
                    <script>
                        window.location("menu.php");
                    </script>
                <?php
        }
    }
    catch(Exception $e)
    {
        echo $e->getMessage();
    }
?>