<?php
  $con = mysqli_connect("localhost", "user1", "user1!", "android");

  $potName = $_POST["potName"];

  $statment = mysqli_prepare($con, "DELETE FROM flower WHERE potname = ?");
  mysqli_stmt_bind_param($statment, "s", $potName);
  mysqli_stmt_execute($statment);

  $response = array();
  $response["success"] = true;

  echo json_encode($response);
?>
