<?php
  $con = mysqli_connect("localhost", "user1", "user1!", "android");

  $potName = $_POST["potName"];
  $value = $_POST["value"];

  $statment = mysqli_prepare($con, "UPDATE flower set status=$value where potName = ?");
  mysqli_stmt_bind_param($statment, "s", $potName);
  mysqli_stmt_execute($statment);

  $response = array();
  $response["success"] = true;

  echo json_encode($response);
?>
