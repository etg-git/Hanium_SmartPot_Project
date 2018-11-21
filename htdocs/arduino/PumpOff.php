  <?php
  $conn = mysqli_connect("localhost", "user1", "user1!", "android");

  mysqli_query($conn, "UPDATE flower set status=0 where id = 1");

  $conn->close();
?>
