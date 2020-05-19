<?php
	session_start();
?>
<?php
    class Player
    {
        public $username = "";
        public $wins = 0;
        public $losses = 0;
        public  $draws = 0;

        function Player($username)
        {
            $this->username = $username;
            $this->wins = 0;
            $this->losses= 0;
            $this->draws = 0;
        }

        public function getUsername()
        {
            return $this->username;
        }

        public function getWins()
        {
            return $this->wins;
        }

        public function getLosses()
        {
            return $this->losses;
        }

        public function getDraws()
        {
            return $this->draws;
        }

        public function addWin()
        {
            $this->wins++;
        }

        public function addLoss()
        {
            $this->losses++;
        }

        public function addDraw()
        {
            $this->draws++;
        }

        public function __toString()
        {
            try
            {
                return (string) "$this->username,$this->wins,$this->losses,$this->draws";
            }
            catch(Exception $e)
            {
                return $e->getMessage();
            }
        }
    }
	
    $players = array();
	
    function validateScore($elements, &$players)
    {
        for($i = 1; $i <= 2; $i++)
        {
            $p = new Player($elements[$i]);
            if(!isset($players[$p->getUsername()]))
            {
                $players[$p->getUsername()] = $p;
            }
            $x = 3;
            foreach($players as $player)
            {
                if($player->getUsername() == $p->getUsername())
                {
                    switch($elements[$x])
                    {
                        case "0":
                            ;
                            break;
                        case "1":
                            if ($i == 1) {
                            $players[$player->getUsername()]->addWin();
                        } else {
                            $players[$player->getUsername()]->addLoss();
                        }
                        break;
                        case "2":
                            if ($i == 1) {
                            $players[$player->getUsername()]->addLoss();
                        } else {
                            $players[$player->getUsername()]->addWin();
                        }
                        break;
                        case "3":
                            $players[$player->getUsername()]->addDraw();
                            break;
                        default:                            
                    }
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

            for($i = 0; $i < count($rows) ; $i++)
            {
                validateScore(explode(",",$rows[$i]), $players);
            }

            // Sort the array
            //sort($players);
            function cmp($a, $b)
            {
                return $a->wins < $b->wins;
            }
            usort($players, "cmp");
            
            echo "<table class=\"leaderboardTable\">
                <tr class=\"leaderboardTR\">
                    <th class=\"leaderboardTH\">USERNAME</th>
                    <th class=\"leaderboardTH\">WINS</th>
                    <th class=\"leaderboardTH\">LOSSES</th>
                    <th class=\"leaderboardTH\">DRAWS</th>
                </tr>";
            
            foreach($players as $player)
            {
                //echo "$pp"
                $col_1 = $player->getUsername();
                $col_2 = $player->getWins();
                $col_3 = $player->getLosses();
                $col_4 = $player->getDraws();
                echo "<tr class=\"leaderboardTR\"><td class=\"leaderboardTD\">" . $col_1 . 
                        "</td><td class=\"leaderboardTD\">" . $col_2 
                        . "</td><td class=\"leaderboardTD\">" . $col_3 . 
                        "</td><td class=\"leaderboardTD\">" . $col_4 . "</td></tr>";
            }
            echo "</table>";
        }
    }
    catch(Exception $e)
    {
        echo $e->getMessage();
    }
?>