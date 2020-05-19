<?php
	session_start();
?>
<?php
    //echo "<p>Hi from getScore!</p>";

    $wins = 0;
    $losses = 0;
    $draws = 0;
	
    function getScore($elements, $uname)
    {
        global $wins, $losses, $draws;
        for($i = 1; $i <= 2; $i++)
        {

            if($elements[$i] == $uname)
            {
                switch($elements[3])
                {
                    case "0":
                        ;
                        break;
                    case "1":
                        if($i == 1)
                                $wins++;
                        else
                                $losses++;
                        break;
                    case "2":
                        if($i == 1)
                                $losses++;
                        else
                                $wins++;
                        break;
                    case "3":
                        $draws++;
                        break;
                    default:
                        ;
                }
            }
        }
    }
	
    $wsdl = $_SESSION['wsdl'];;
    $trace = true;
    $exceptions = true;
	
    try
    {
        $client = new SoapClient($wsdl, array('trace' => $trace, 'exceptions' => $exceptions));

        $response = $client->leagueTable();
        $result = (string) $response->return;

        if($result == "ERROR-NOGAMES")
        {
            ?>
                <script>
                        alert("Error: No games found");
                </script>
            <?php
        }
        elseif($result == "ERROR-DB")
        {
            ?>
                <script>
                        alert("ERROR: Cannot Access the DBMS");
                </script>
            <?php
        }
        else
        {
            // split the string
            $rows = explode("\n", $result);
            // loop through counting no. of wins, losses, draws

            for($i = 0; $i < sizeof($rows); $i++)
            {
                getScore(explode(",",$rows[$i]), $_SESSION["username"]);
            }	

            echo "$wins,$losses,$draws";
        }
    }
    catch(Exception $e)
    {
        echo $e->getMessage();
    }
?>