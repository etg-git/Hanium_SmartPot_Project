<?php
  $con = mysqli_connect("localhost", "user1", "user1!", "android");

  $potName = $_POST["potName"];
  $auto = $_POST["auto"];
  $limited = $_POST["limited"];
  $pumptime = $_POST["pumptime"];

  $statment = mysqli_prepare($con, "UPDATE flower set auto= $auto,  limited=$limited, pumptime=$pumptime where potName = ?");
  mysqli_stmt_bind_param($statment, "s", $potName);
  mysqli_stmt_execute($statment);

  $response = array();
  $response["success"] = true;

  echo json_encode($response);
?>
