<?php
  $con = mysqli_connect("localhost", "user1", "user1!", "android");

  $userID = $_POST["userID"];
  $potName = $_POST["potName"];

  $result = mysqli_query($con
  , "SELECT potname,flower1,flower2,flower3,flower4,sensor1, sensor2, sensor3, sensor4,
   startday, update_time, auto, limited, pumptime FROM flower WHERE userID='$userID' AND potName='$potName'");
  $response = array();

  while ($row = mysqli_fetch_array($result)) {
    array_push($response, array("potname"=>$row[0], "flower1"=>$row[1], "flower2"=>$row[2], "flower3"=>$row[3]
  , "flower4"=>$row[4], "sensor1"=>$row[5], "sensor2"=>$row[6], "sensor3"=>$row[7], "sensor4"=>$row[8],
   "startday"=>$row[9], "update_time"=>$row[10], "auto"=>$row[11], "limited"=>$row[12], "pumptime"=>$row[13] ));
  }

  echo json_encode(array("response"=>$response), JSON_UNESCAPED_UNICODE);
  mysqli_close($con);
?>
