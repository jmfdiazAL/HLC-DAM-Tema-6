<?php
	//insert.php
	// alojamiento en 000webhost => 	

	// 000webhost usa una versión de php más avanzada => Extensión MySQL mejorada
	// han quedado algunas funciones obsoletas o deprecated => mysql_connect => obsoleta
	// http://php.net/manual/es/book.mysqli.php

	$hostname ="localhost";  //nuestro servidor de BD
	$database ="alumnos"; //Nombre de nuestra base de datos
	$username ="root";       //Nombre de usuario de nuestra base de datos 
	$password ="";   //Contraseña de nuestra base de datos	

	// intentar conectar al servidor con el usuario y contraseña anteriores
	$mysqli = new mysqli($hostname,$username,$password, $database);

	/*
	 * This is the "official" OO way to do it,
	 * BUT $connect_error was broken until PHP 5.2.9 and 5.3.0.
	 */
	if ($mysqli->connect_error) {
		die('Connect Error (' . $mysqli->connect_errno . ') '
			    . $mysqli->connect_error);
	}

	// variables que almacenan los valores que enviamos por nuestra app por POST o a través de un formulario
	//el índice de $_POST['indice'] coincide con el 'name' de los input de insert.html 
	// observar que se llaman igual en nuestra app y aquí, esto es:
	// el índice de $_POST['indice'] con la clave en App: parametros_POST = new ArrayList<NameValuePair>(4))
	$alumno=$_POST['alumno'];
	$matricula=$_POST['matricula'];
	$telefono=$_POST['telefono'];
	$email=$_POST['email'];

	// realizar la inserción de los datos del alumno en la tabla matriculas
	$insertar = "insert into matriculas(nombre,matricula,telefono,email) values ('".$alumno."','".$matricula."','".$telefono."','".$email."')";
	
	// lanzar la consulta
	$query_exec = $mysqli->query($insertar);
	if (!$query_exec) {
		echo "Falló la inserción de la tabla matriculas: (" . $mysqli->errno . ") " . $mysqli->error;
	}	 

?>
