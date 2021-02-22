package com.pms.a02_rest_json_matriculas;

// código adaptado del código original de la página web:
// http://picarcodigo.blogspot.com.es

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/*
 recuerda que:
1)debes añadir el permiso de internet al manifiesto para poder acceder a
 los scripts de php y mandar información por POST mediante HTTP
 <uses-permission android:name="android.permission.INTERNET" />
 y gestionar la petición de permisos, si fuese necesario, para API >= 23
 2) Para API >=23 ya no está soportado HttpClient directamente por Android
 por lo que hay que indicar que se utiliza esta librería en el gradle:
 useLibrary 'org.apache.http.legacy'
*/
public class MainActivity extends AppCompatActivity {

  // atributos
  public EditText matricula;
  public EditText alumno;
  public EditText telefono;
  public EditText email;

  private Button insertar;
  private Button modificar;
  private Button mostrar;
  private ImageButton mas;
  private ImageButton menos;

  private int posicion; //posición dentro de listaAlumnos
  private List<Alumno> listaAlumnos; // Lista de alumnos obtenidos de la BD


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    // referencia a los EditText
    matricula = (EditText) findViewById(R.id.matricula);
    alumno = (EditText) findViewById(R.id.nombre);
    telefono = (EditText) findViewById(R.id.telefono);
    email = (EditText) findViewById(R.id.email);

    // crea la lista de alumnos
    listaAlumnos = new ArrayList<Alumno>();

    // acceder al BOTÓN MOSTRAR
    mostrar = (Button) findViewById(R.id.mostrar);
    //Define la acción del botón Mostrar
    mostrar.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        //1. carga listaAlumnos con los datos de la BD mediante la petición POST
        //2. muestra el primero de ellos en pnatalla
        new WebService_cargar(MainActivity.this, listaAlumnos,
            Config.URL_PHP, matricula, alumno, telefono, email).execute();
      }
    });

    // acceder al botón MAS => +
    mas = (ImageButton) findViewById(R.id.mas);
    // Define la acción del botón + => Se mueve por el ArrayList mostrando el alumno siguiente
    mas.setOnClickListener(new View.OnClickListener() {

      // la lista de alumnos va desde la posición 0 hasta el tamaño-1 => size()-1

      @Override
      public void onClick(View v) {
        // Comprobar si la lista de alumnos no está vacía
        if (!listaAlumnos.isEmpty()) {
          if (posicion < listaAlumnos.size() - 1) {
            // no se ha alcanzando o superado el final de lista => avanzar
            posicion++;
            // mostrar el alumno de la lista situado en posición
            mostrarAlumno();
          } else {
            //Config.tostada(getApplicationContext(), "Último de la lista");
          }
        }
      }

    });

    // acceder al botón MENOS => -
    menos = (ImageButton) findViewById(R.id.menos);
    // Definir la acción del botón - => Se mueve por el ArrayList mostrando el alumno anterior
    menos.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // Comprobar si la lista de alumnos no está vacía
        if (!listaAlumnos.isEmpty()) {
          if (posicion > 0) {
            // no se ha alcanzando el principio de lista => retroceder
            posicion--;
            // mostrar el alumno de la lista situado en posición
            mostrarAlumno();
          } else {
            // se ha alcanzando el principio de lista
            //Config.tostada(getApplicationContext(), "Primero de la lista");
          }
        }
      }
    });

    // acceder al BOTÓN INSERTAR
    insertar = (Button) findViewById(R.id.insertar);
    //Define la acción del botón Insertar => Insertamos los datos del alumno
    insertar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //controla que la información no esté en blanco
        if (!matricula.getText().toString().trim().equalsIgnoreCase("") ||
            !alumno.getText().toString().trim().equalsIgnoreCase("") ||
            !telefono.getText().toString().trim().equalsIgnoreCase("") ||
            !email.getText().toString().trim().equalsIgnoreCase(""))

          // 1. inserta los datos del alumno en la BD mediante petición POST
          new WebService_insertar(MainActivity.this, listaAlumnos, Config.URL_PHP, matricula,
              alumno, telefono, email).execute();
        else
          Config.tostada(getApplicationContext(), "Hay información por rellenar");
      }
    });

    //Acceder al BOTÓN MODIFICAR
    modificar = (Button) findViewById(R.id.modificar);
    //Define la acción del botón Modificar => modifica los datos del alumno
    modificar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //controla que la información no esté en blanco
        if (!matricula.getText().toString().trim().equalsIgnoreCase("") ||
            !alumno.getText().toString().trim().equalsIgnoreCase("") ||
            !telefono.getText().toString().trim().equalsIgnoreCase("") ||
            !email.getText().toString().trim().equalsIgnoreCase(""))

          // intenta modificar los datos del alumno que se muestra en pantalla
          new WebService_modificar(MainActivity.this, listaAlumnos, posicion,
              Config.URL_PHP, matricula, alumno, telefono, email).execute();
        else
          Config.tostada(getApplicationContext(), "Hay información por rellenar");
      }
    });


  } // fin onCreate()

  /**
   * Método que muestra un alumno almacenado en el ArrayList listaAlumnos
   *
   */
  public void mostrarAlumno() {

    // recoge en alumno la información del alumno ubicado en posicion de listaAlumnnos
    Alumno alumno = listaAlumnos.get(posicion);
    // poner la información del alumno en los EditText
    this.alumno.setText(alumno.getAlumno());
    matricula.setText("" + alumno.getMatricula());
    telefono.setText("" + alumno.getTelefono());
    email.setText(alumno.getEmail());

  } // fin mostrarAlumno()

}
