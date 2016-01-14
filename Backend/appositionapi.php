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

	private function sanitizeHTTPParameters() {
		foreach ($_GET as $key => $value) {
			$_GET[$key] = escapeshellcmd($this->mysqli->real_escape_string($value));
		}
		foreach ($_POST as $key => $value) {
			$_POST[$key] = escapeshellcmd($this->mysqli->real_escape_string($value));
		}
	}

	private function encryptPassword($password) {
		return $this->mysqli->real_escape_string(crypt($password, $this->config['salt']));
	}
	
	private function initDB() {
		$this->mysqli = new mysqli($this->config['hostname'], 
			$this->config['username'], 
			$this->config['password'], 
			$this->config['databaseName']);

		if (mysqli_connect_errno()) { 
			echo "<br><br>There seems to be a problem with our database. Reload the page or try again later.";
			exit(); 
		}
	}

	private function select($sql) {
		$res = mysqli_query($this->mysqli, $sql);
		return mysqli_fetch_array($res, MYSQLI_ASSOC);
	}

	private function selectMultiple($sql) {
		$res = mysqli_query($this->mysqli, $sql);
		$finalArray = array();

		while($temp = mysqli_fetch_array($res, MYSQLI_ASSOC)) {
			array_push($finalArray, $temp);
		}

		return $finalArray;
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

	private function removeLocationIfExpired($resultArray) {
		$userID = $resultArray['userID'];

		$lastTimeLocationUpdated = date_create_from_format("Y-m-d H:i:s", $resultArray['lastTimeLocationUpdated']);
		if($lastTimeLocationUpdated == false) return;

		$currentDateTime = new DateTime();

		if($this->isCurrentLocationExpired($currentDateTime, $lastTimeLocationUpdated) == True) {
			mysqli_query($this->mysqli,"UPDATE User SET currentLocation = '' WHERE userID = $userID");
			unset($resultArray['currentLocation']);
		}

		return $resultArray;
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

	//--------------------- API ENDPOINTS ------------------------\\

	// Endpoint associated with a users credentials (everything in the User table; i.e. name, email, firstname, etc.)
	protected function user() {
		if(isset($_GET['userID']) && isset($_GET['password'])) {
			$userID = $_GET['userID'];
			$password = $_GET['password'];

			return $this->removeLocationIfExpired($this->select("SELECT * FROM User WHERE userID = $userID and password = '$password'"));
		} else if(isset($_GET['email']) && isset($_GET['password'])) {
			$email = $this->mysqli->escape_string($_GET['email']);
			$password = $this->mysqli->escape_string($_GET['password']);

			return $this->removeLocationIfExpired($this->select("SELECT * FROM User WHERE email = '$email' AND password = '$password'"));
		} else if(isset($_GET['searchTerm'])) {
			$searchTerm = $_GET['searchTerm'];
			return $this->selectMultiple("SELECT userID, firstName, lastName, email FROM User WHERE 
				LCASE(firstName) LIKE LCASE('%$searchTerm%') 
				OR LCASE(lastName) LIKE LCASE('%$searchTerm%')");
		} else {
			return "Error: Invalid request";
		}
	}


	// Endpoint associated with a User's classes
	protected function classes() {
		if (isset($_GET['userID']) && isset($_GET['day'])) {
			$userID = $_GET['userID'];
			$day = $_GET['day'];

			return $this->selectMultiple("SELECT period, name FROM Period WHERE userID = $userID AND day = $day");
		} else {
			return "Error: Invalid request";
		}
	}

	// Endpoint associated with a User's meetings
	protected function meetings() {
		if(isset($_GET['meetingID'])) {
			$meetingID = $_GET['meetingID'];

			return $this->getMeetingWithID($meetingID);
		} else if(isset($_GET['userID']) && isset($_GET['dayIndex'])) {
			$userID = $_GET['userID'];
			$dayIndex = $_GET['dayIndex'];

			$meetingsForUser = $this->selectMultiple("SELECT meetingID FROM MeetingToUser WHERE userID = $userID");

			$returnMeetings = array();
			foreach($meetingsForUser as $meetingIDArray) {
				$meeting = $this->getMeetingWithID($meetingIDArray['meetingID']);
				if($meeting['dayIndex'] == $dayIndex) array_push($returnMeetings, $meeting);
			}

			return $returnMeetings;
		} else if(isset($_GET['userID']) && isset($_GET['dayIndex']) && isset($_GET['periodIndex'])) {
			$userID = $_GET['userID'];
			$dayIndex = $_GET['dayIndex'];
			$periodIndex = $_GET['periodIndex'];

			$meetingsForUser = $this->selectMultiple("SELECT meetingID FROM MeetingToUser WHERE userID = $userID");

			foreach($meetingsForUser as $meetingIDArray) {
				$meeting = $this->getMeetingWithID($meetingIDArray['meetingID']);
				if($meeting['dayIndex'] == $dayIndex && $meeting['periodIndex'] == $periodIndex) return $meeting;
			}

			return NULL;
		} else if(isset($_POST['name']) && isset($_POST['dayIndex']) && isset($_POST['periodIndex'])) {
			$name = $_POST['name'];
			$dayIndex = $_POST['dayIndex'];
			$periodIndex = $_POST['periodIndex'];

			$this->insert("INSERT INTO Meeting (name, dayIndex, periodIndex) VALUES ('$name', $dayIndex, $periodIndex)");
			$meetingIDArray = $this->select("SELECT meetingID FROM Meeting WHERE name = '$name' AND dayIndex = $dayIndex AND periodIndex = $periodIndex");
			$meetingID = $meetingIDArray['meetingID'];
			var_dump($memberIDs);
			
			$this->insert("INSERT INTO MeetingToUser (meetingID, userID) VALUES ($meetingID, 1)");
		} else {
			return "Error: Invalid request";
		}
	}
 }

 ?>