<?php
  $con = mysqli_connect("localhost", "user1", "user1!", "android");

  $userID = $_POST["userID"];
  $potName = $_POST["potName"];
  $flower1 = $_POST["flower1"];
  $flower2 = $_POST["flower2"];
  $flower3 = $_POST["flower3"];
  $flower4 = $_POST["flower4"];

  $statment = mysqli_prepare($con, "INSERT INTO flower(`userID`, `potname`, `flower1`, `flower2`, `flower3`, `flower4`) VALUES (?, ?, ?, ?, ?, ?)");
  mysqli_stmt_bind_param($statment, "ssssss", $userID, $potName, $flower1, $flower2, $flower3, $flower4);
  mysqli_stmt_execute($statment);

  $response = array();
  $response["success"] = true;

  echo json_encode($response);
?>
