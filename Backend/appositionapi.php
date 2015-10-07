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
            if (mysqli_connect_errno()) { // If something went wrong
                print("error"); // print error
                exit(); // exit out of the program
            }
            $userID = $_GET['userID'];

            $sql = "SELECT firstName, lastName, email, password FROM User WHERE userID = ".$userID;
            $res = mysqli_query($mysqli, $sql);
            $resultArray = mysqli_fetch_array($res, MYSQLI_ASSOC);
            
            return $resultArray;
        } else {
            return "Error: Only accepts GET requests with userID argument";
        }
     }
 }

 ?>