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

            $sql = "SELECT firstName, lastName, email, password FROM User WHERE userID = ".$userID;
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