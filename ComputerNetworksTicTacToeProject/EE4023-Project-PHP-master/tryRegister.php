<?php
    session_start();
?>
<?php
    $user = $_POST["uname"];
    $pass = $_POST["pword"];
    $name = $_POST["name"];
    $surname = $_POST["surname"];

    $wsdl = $_SESSION['wsdl'];
    $trace = true;
    $exceptions = true;

    if(strlen($user) == 0)
    {
        echo "Please enter a username";
    }
    elseif(strlen($pass) == 0)
    {
        echo "Please enter a password";
    }
    elseif(strlen($name) == 0)
    {
        echo "Please enter a name";
    }
    elseif(strlen($surname) == 0)
    {
        echo "Please enter a surname";
    }
    else
    {
        try
        {
            $client = new SoapClient($wsdl, array('trace' => $trace, 'exceptions' => $exceptions));

            $xml_array["username"] = $user;
            $xml_array["password"] = $pass;
            $xml_array["name"] = $name;
            $xml_array["surname"] = $surname;

            $response = $client->register($xml_array);
            $result = (string) $response->return;

            switch($result)
            {
                case "ERROR-REPEAT":
                    echo "Username already exists";
                    break;
                case "ERROR-DB":
                    echo "ERROR: A database error has occurred";
                    break;
                default:
                    ?>
                        <script>
                            alert("You have been registered!");
                            window.location("login.php");
                        </script>
                    <?php
            }
        }
        catch(Exception $e)
        {
                echo $e->getMessage();
        }
    }
?>