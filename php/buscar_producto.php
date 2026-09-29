<?php
    include 'cn.php';

    $id = $_GET['id'];

    $consulta = "SELECT * FROM  usuarios WHERE id= '$id'";
    $resultado = $conexion ->query($consulta);

    while($fila = $resultado->fetch_array()){
        $usuario[] = array_map('utf8_encode',$fila);
    }

    echo json_encode($usuario);
    $resultado->close();
?>