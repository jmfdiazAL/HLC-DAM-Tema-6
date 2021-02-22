<?php
	// REST CON JSON 000webhost --- selectAll.php

	// alojamiento en 000webhost => 

	// 000webhost usa una versión de php más avanzada => Extensión MySQL mejorada
	// han quedado algunas funciones obsoletas o deprecated => mysql_connect => obsoleta
	// http://php.net/manual/es/book.mysqli.php

	$hostname ="localhost";  //nuestro servidor de BD
	$database ="id5529146_alum"; //Nombre de nuestra base de datos
	$username ="id5529146_isa";       //Nombre de usuario de nuestra base de datos 
	$password ="123456";   //Contraseña de nuestra base de datos	
	
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
	
 	
	// realizar una consulta que selecciona todas las matriculas de los alumnos
	// ordenadas por la matricula
	$query_search = "select * from matriculas order by id";

	// crear un array para almacenar el json
	$json = array();

	// lanzar la consulta
	$query_exec = $mysqli->query($query_search);
	if (!$query_exec) {
		echo "Falló la consulta de la tabla matriculas: (" . $mysqli->errno . ") " . $mysqli->error;
	}
	else {		
		while($row = $query_exec->fetch_array(MYSQLI_ASSOC)){
			// almacena cada fila de la consulta en el array $json
			$json['alumnos'][]=$row;
		}		
		
	}		

	// cerrar la conexión con la BD mysql
	$mysqli->close();	

	// mostrar por pantalla y codificar el vector $json como JSON
	echo json_encode($json);	
	
?>


