<?php

require_once 'API.class.php';
class MyAPI extends API
{
    protected $User;

    public function __construct($request, $origin) {
        parent::__construct($request);
    }

    private function initDB() {
        $mysqli = new mysqli("ApPosition.db.12061709.hostedresource.com", "ApPosition", "Fustercluck2!", "ApPosition");
        if (mysqli_connect_errno()) { 
            echo "<br><br>There seems to be a problem with our database. Reload the page or try again later.";
            exit(); 
        }
        return $mysqli;
    }

    /**
     * Example of an Endpoint
     */
     protected function credentials() {
        if ($this->method == 'GET' && isset($_GET['userID'])) {
            $mysqli = $this->initDB();
            
            $userID = $_GET['userID'];

            $sql = "SELECT * FROM User WHERE userID = ".$userID;
            $res = mysqli_query($mysqli, $sql);
            $resultArray = mysqli_fetch_array($res, MYSQLI_ASSOC);
            
            return $resultArray;
        } else if($this->method == 'PUT'){
            parse_str($this->file, $putVars);
            if(!isset($putVars['userID'])) {
                return "Error: Invalid request";
            }

            $mysqli = $this->initDB();

            $userID = $putVars['userID'];

            // This will hold the names of the columns that the caller has given us values for
            $availableColumns = array();
            $rowsResult = mysqli_query($mysqli, "SHOW COLUMNS FROM User");
            while ($row = mysqli_fetch_assoc($rowsResult)) {
                if(isset($putVars[$row['Field']]) && strcmp($row['Field'], "userID") != 0) {
                    array_push($availableColumns, $row['Field']);
                }
            }

            $query = "UPDATE User SET ";

            $numItems = count($availableColumns);
            $i = 0;
            foreach($availableColumns as $columnName) {
                if(++$i == $numItems) $query .= $columnName."='".$putVars[$columnName]."'";
                else $query .= $columnName."='".$putVars[$columnName]."', ";
            }

            $query .= " WHERE userID = ".$userID;

            mysqli_query($mysqli, $query);

                return "Success";
        } else {
            return "Error: Invalid request";
        }
     }

     protected function userID() {
        if ($this->method == 'GET' && isset($_GET['email']) && isset($_GET['password'])) {
            $mysqli = $this->initDB();
            
            $email = $mysqli->escape_string($_GET['email']);
            $password = $mysqli->escape_string($_GET['password']);

            $sql = "SELECT userID FROM User WHERE email = '".$email."' AND password = '".$password."'";
            $res = mysqli_query($mysqli, $sql);
            $resultArray = mysqli_fetch_array($res, MYSQLI_ASSOC);
            return $resultArray;
            
            
        } else {
            return "Error: Invalid request";
        }
     }

     protected function classes() {
        if ($this->method == 'GET' && isset($_GET['userID']) && isset($_GET['day'])) {
            $mysqli = $this->initDB();

            $userID = $_GET['userID'];
            $day = $_GET['day'];

            $sql = "SELECT period, name FROM Period WHERE userID = ".$userID." AND day = ".$day;
            $res = mysqli_query($mysqli, $sql);
            $resultArray = array();
            while($holder = mysqli_fetch_array($res, MYSQLI_ASSOC)) {
                array_push($resultArray, $holder);
            }           
            return $resultArray;
        } else {
            return "Error: Invalid request";
        }
     }
 }

 ?>