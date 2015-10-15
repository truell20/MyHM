<?php

require_once 'API.class.php';
class MyAPI extends API
{
	// Holds the end times of each period in string form
	public $periodEndings = array("09:15:00", "13:15:00");

	public function __construct($request, $origin) {
		parent::__construct($request);
	}

	// Initializes and returns a mysqli object that represents our mysql database
	private function initDB() {
		$mysqli = new mysqli("ApPosition.db.12061709.hostedresource.com", "ApPosition", "Fustercluck2!", "ApPosition");
		if (mysqli_connect_errno()) { 
			echo "<br><br>There seems to be a problem with our database. Reload the page or try again later.";
			exit(); 
		}
		return $mysqli;
	}

	// Determines if a User's current location has expired. Is true if the current location was updated during a different period
	private function isCurrentLocationExpired($currentDateTime, $lastTimeLocationUpdated) {
		if($currentDateTime->getTimestamp() - $lastTimeLocationUpdated->getTimestamp() >= 60*45) return True;
		if($currentDateTime->diff($lastTimeLocationUpdated)->days > 0) return True;

		$isExpired = False;
		foreach($this->periodEndings as $periodEnd) {
			$periodEndDateTime = date_create_from_format("Y-m-d H:i:s", $currentDateTime->format('Y-m-d')." ".$periodEnd);
			
			if($currentDateTime->getTimestamp() - $periodEndDateTime->getTimestamp() > 0 
				&& $lastTimeLocationUpdated->getTimestamp() - $periodEndDateTime->getTimestamp() < 0) {
				
				$isExpired = True;
				break;
			}
		}

		return $isExpired;
	}

	// API ENDPOINTS

	// Endpoint associated with a users credentials (everything in the User table; i.e. name, email, firstname, etc.)
	protected function credentials() {
		if ($this->method == 'GET') {
			$resultArray = NULL;

			if(isset($_GET['userID'])) {
				$mysqli = $this->initDB();
				$userID = $_GET['userID'];

				$sql = "SELECT * FROM User WHERE userID = $userID";
				$res = mysqli_query($mysqli, $sql);
				$resultArray = mysqli_fetch_array($res, MYSQLI_ASSOC);
			} else if(isset($_GET['email']) && isset($_GET['password'])) {
				$mysqli = $this->initDB();
				$email = $mysqli->escape_string($_GET['email']);
				$password = $mysqli->escape_string($_GET['password']);

				$sql = "SELECT * FROM User WHERE email = '$email' AND password = '$password'";
				$res = mysqli_query($mysqli, $sql);
				$resultArray = mysqli_fetch_array($res, MYSQLI_ASSOC);
			} else {
				return "Error: Invalid request";
			}

			// Remove the currentLocation field if it was updated during a different period
			$currentDateTime = new DateTime();
			$lastTimeLocationUpdated = date_create_from_format("Y-m-d H:i:s", $resultArray['lastTimeLocationUpdated']);

			if($this->isCurrentLocationExpired($currentDateTime, $lastTimeLocationUpdated) == True) {
				mysqli_query($mysqli,"UPDATE User SET currentLocation = '' WHERE userID = $userID");
				$resultArray['currentLocation'] = NULL;
			}

			return $resultArray;
		} else if($this->method == 'PUT'){
			parse_str($this->file, $putVars);
			
			// If there is no userID
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

			// Building the query based on the items the caller has given us
			$query = "UPDATE User SET ";
			foreach($availableColumns as $columnName) $query .= "$columnName = '$putVars[$columnName]', ";
			
			// Add update the lastTimeLocationUpdated column
			$query .= " lastTimeLocationUpdated = '".date('Y-m-d H:i:s')."'";
			
			// Add where condition to query
			$query .= " WHERE userID = $userID";

			mysqli_query($mysqli, $query);

			return "Success";
		} else {
			return "Error: Invalid request";
		}
	}


	// Endpoint associated with a User's classes
	protected function classes() {
		if ($this->method == 'GET' && isset($_GET['userID']) && isset($_GET['day'])) {
			$mysqli = $this->initDB();
			$userID = $_GET['userID'];
			$day = $_GET['day'];

			$sql = "SELECT period, name FROM Period WHERE userID = $userID AND day = $day";
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