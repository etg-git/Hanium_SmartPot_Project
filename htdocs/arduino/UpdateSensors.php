<?php
  $db = mysqli_connect("localhost", "user1", "user1!", "android");

  mysqli_set_charset($db, "utf8");

  if (mysqli_connect_errno()){
    echo "ERROR: 데이타베이스에 연결할 수 없습니다.";
    exit;
  }

  $sensor1 = $_POST["sensor1"];
  $sensor2 = $_POST["sensor2"];
  $sensor3 = $_POST["sensor3"];
  $sensor4 = $_POST["sensor4"];

  $query = "UPDATE flower SET sensor1=$sensor1, sensor2=$sensor2, sensor3=$sensor3, sensor4=$sensor4, update_time=now()
            WHERE id = 1";

  $result = mysqli_query($db, $query);

  if ($result) {
    echo $db->affected_rows." data update into databases.";
  }else{
    echo "ERROR: 자료가 추가되지 않았습니다.";
  }

  mysqli_free_result($result);

  mysqli_close($db);
?>
