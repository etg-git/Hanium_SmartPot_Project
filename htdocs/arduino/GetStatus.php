<?php
  $con = mysqli_connect("localhost", "user1", "user1!", "android");
  $result = mysqli_query($con, "SELECT status, auto, limited, pumptime FROM flower where id = 1");

  while ($row = mysqli_fetch_array($result)) {
    echo '?'. $row[0]. $row[1]. $row[2]. $row[3];
  }
  mysqli_close($con);

?>
