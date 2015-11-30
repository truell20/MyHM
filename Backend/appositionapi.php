<?php

require_once 'API.class.php';
class MyAPI extends API
{
	// Holds the end times of each period in string form
	public $periodEndings = array("09:15:00", "13:15:00");

	// The database
	private $mysqli = NULL;

	public function __construct($request, $origin) {
		$this->initDB();
		parent::__construct($request);
	}

	// Initializes and returns a mysqli object that represents our mysql database
	private function initDB() {
		$this->mysqli = new mysqli("ApPosition.db.12061709.hostedresource.com", 
			"ApPosition", 
			"Fustercluck2!", 
			"ApPosition");
		
		if (mysqli_connect_errno()) { 
			echo "<br><br>There seems to be a problem with our database. Reload the page or try again later.";
			exit(); 
		}
	}

	private function select($sql) {
		$res = mysqli_query($this->mysqli, $sql);
		return mysqli_fetch_array($res, MYSQLI_ASSOC);
	}

	private function insert($sql) {
		mysqli_query($this->mysqli, $sql);
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


	private function isKeyValid($key, $userID) {
		$sql = "SELECT password FROM User WHERE userID = $userID";
		$resultArray = $this->select($sql);

		if(strcmp($resultArray['password'], $key) == 0) return True;
		else return False;
	}

	private function removeLocationIfExpired(&$resultArray) {
		$userID = $resultArray['userID'];

		$lastTimeLocationUpdated = date_create_from_format("Y-m-d H:i:s", $resultArray['lastTimeLocationUpdated']);
		if($lastTimeLocationUpdated == false) return;

		$currentDateTime = new DateTime();

		if($this->isCurrentLocationExpired($currentDateTime, $lastTimeLocationUpdated) == True) {
			mysqli_query($this->mysqli,"UPDATE User SET currentLocation = '' WHERE userID = $userID");
			unset($resultArray['currentLocation']);
		}
	}

	/// Returns an associated array with a meetings credentials
	private function getMeetingWithID($meetingID) {
		$sql = "SELECT * FROM Meeting WHERE meetingID = $meetingID";
		$meetingResult = mysqli_query($this->mysqli, $sql);
		$resultArray = mysqli_fetch_array($meetingResult, MYSQLI_ASSOC);

		/// Get members of meeting
		$sql = "SELECT * FROM MeetingToUser WHERE meetingID = $meetingID";
		$userIDRes = mysqli_query($this->mysqli, $sql);
		$userIDs = array();
		while($userIDArray = mysqli_fetch_array($userIDRes, MYSQLI_ASSOC)) {
			array_push($userIDs, $userIDArray['userID']);
		}
		$resultArray['members'] = $userIDs;

		return $resultArray;
	}

	// API ENDPOINTS

	// Endpoint associated with a users credentials (everything in the User table; i.e. name, email, firstname, etc.)
	protected function credentials() {
		if ($this->method == 'GET') {
			$resultArray = NULL;

			if(isset($_GET['userID'])) {
				$userID = $_GET['userID'];

				$sql = "SELECT * FROM User WHERE userID = $userID";
				$res = mysqli_query($this->mysqli, $sql);
				$resultArray = mysqli_fetch_array($res, MYSQLI_ASSOC);

				$this->removeLocationIfExpired($resultArray);

				return $resultArray;
			} else if(isset($_GET['email']) && isset($_GET['password'])) {
				$email = $this->mysqli->escape_string($_GET['email']);
				$password = $this->mysqli->escape_string($_GET['password']);

				$sql = "SELECT * FROM User WHERE email = '$email' AND password = '$password'";
				$res = mysqli_query($this->mysqli, $sql);
				$resultArray = mysqli_fetch_array($res, MYSQLI_ASSOC);

				$this->removeLocationIfExpired($resultArray);

				return $resultArray;
			} else if(isset($_GET['searchTerm'])) {
				$searchTerm = $_GET['searchTerm'];

				$sql = "SELECT userID, firstname, lastname, email FROM User WHERE name LIKE '$searchTerm'";
				$res = mysqli_query($this->mysqli, $sql);

				$returnArray = array();

				while($resultArray = mysqli_fetch_array($res, MYSQLI_ASSOC)) {
					$this->removeLocationIfExpired($resultArray);
					array_push($returnArray, $resultArray);
				}

				return $returnArray;
			} else {
				return "Error: Invalid request";
			}
		} else if($this->method == 'PUT'){
			// Sleep for one second to stop brute force attacks
			sleep(1);

			parse_str($this->file, $putVars);
			
			// If there is no userID
			if(!isset($putVars['userID'])) {
				return "Error: Invalid request";
			}

			$userID = $putVars['userID'];
			$key = $putVars['key'];

			if($this->isKeyValid($key, $userID) == False) {
				return "Invalid key";
			}

			// This will hold the names of the columns that the caller has given us values for
			$availableColumns = array();
			$rowsResult = mysqli_query($this->mysqli, "SHOW COLUMNS FROM User");
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

			mysqli_query($this->mysqli, $query);

			return "Success";
		} else {
			return "Error: Invalid request";
		}
	}


	// Endpoint associated with a User's classes
	protected function classes() {
		if ($this->method == 'GET' && isset($_GET['userID']) && isset($_GET['day'])) {
			$userID = $_GET['userID'];
			$day = $_GET['day'];

			$sql = "SELECT period, name FROM Period WHERE userID = $userID AND day = $day";
			$res = mysqli_query($this->mysqli, $sql);
			$resultArray = array();
			while($holder = mysqli_fetch_array($res, MYSQLI_ASSOC)) {
				array_push($resultArray, $holder);
			}           
			return $resultArray;
		} else {
			return "Error: Invalid request";
		}
	}

	// Endpoint associated with a User's meetings
	protected function meetings() {
		// Get meetings with date and userID
		if ($this->method == 'GET' && isset($_GET['userID']) && isset($_GET['date'])) {
			$userID = $_GET['userID'];
			$date = $_GET['date'];

			$sql = "SELECT meetingID FROM MeetingToUser WHERE userID = $userID";
			$meetingIDResult = mysqli_query($this->mysqli, $sql);

			$returnArray = array();

			while($holder = mysqli_fetch_array($meetingIDResult, MYSQLI_ASSOC)) {
				$meetingID = $holder['meetingID'];
				$meeting = $this->getMeetingWithID($meetingID);

				/// Include the meeting if it is on the specified date
				$beginningDateTime = date_create_from_format("Y-m-d H:i:s", $resultArray['beginning']);
				if(strcmp($date, $beginningDateTime->format("Y-m-d")) == 0) {
					array_push($returnArray, $resultArray);
				}
			}  

			return $returnArray;
		} else if($this->method == 'GET' && isset($_GET['meetingID'])) {
			$meetingID = $_GET['meetingID'];

			return $this->getMeetingWithID($meetingID);
		} else {
			return "Error: Invalid request";
		}
	}
 }

 ?>