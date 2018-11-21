<?php
  $con = mysqli_connect("localhost", "user1", "user1!", "android");

  $userID = $_POST["userID"];
  $userPassword = $_POST["userPassword"];
  $userGender = $_POST["userGender"];
  $userMajor = $_POST["userMajor"];
  $userEmail = $_POST["userEmail"];

  $statment = mysqli_prepare($con, "INSERT INTO USER VALUES (?, ?, ?, ?, ?)");
  mysqli_stmt_bind_param($statment, "sssss", $userID, $userPassword, $userGender, $userMajor, $userEmail);
  mysqli_stmt_execute($statment);

  $response = array();
  $response["success"] = true;

  echo json_encode($response);
?>
