<?php
$nombre=$_GET["nombre"];
$apellido=$_GET["apellido"];
$direccion=$_GET["direccion"];
$telefono=$_GET["telefono"];
$correo=$_GET["correo"];
$sexo=$_GET["sexo"];
$fecha=$_GET["fecha"];

$cont=0;
include("cn.php");

$q=mysqli_query($c,"INSERT INTO usuarios(id, nombre, apellido, direccion, telefono, correo, sexo, fecha) VALUES ('NULL','$nombre', '$apellido', '$direccion', '$telefono', '$correo', '$sexo', '$fecha')");

if($q){
    echo "Registro insertado exitoso";
}
else{
    echo "Fallo en el registro";
}
?>