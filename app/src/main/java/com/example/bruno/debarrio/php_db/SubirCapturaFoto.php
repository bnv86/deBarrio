<?php
	$con = mysqli_connect("localhost", "id5094472_bnv", "Fray1234", "id5094472_debarrio_db");
    
    $foto = $_POST["foto"];
    $nombre = $_POST["nombre"];
    $statement = mysqli_prepare($con, "INSERT INTO imagen (foto, nombre) VALUES (?, ?)");
    mysqli_stmt_bind_param($statement, "ssis", $foto, $nombre);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>