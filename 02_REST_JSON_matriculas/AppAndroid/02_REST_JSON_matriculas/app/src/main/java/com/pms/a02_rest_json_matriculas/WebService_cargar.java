package com.pms.a02_rest_json_matriculas;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class WebService_cargar extends AsyncTask<Void, Void, String> {

  //contexto de la actividad principal
  private final Activity context;

  private final String URL_PHP;
  private final EditText matricula, alumno, telefono, email;
  private final List<Alumno> listaAlumnos;
  private int posicion;

  /**
   * constructor
   *
   * @param context
   * @param listaAlumnos
   * @param url_php
   * @param matricula
   * @param alumno
   * @param telefono
   * @param email
   */
  WebService_cargar(Activity context, List<Alumno> listaAlumnos, String url_php,
                    EditText matricula, EditText alumno, EditText telefono, EditText email) {
    this.context = context;
    this.listaAlumnos = listaAlumnos;
    this.URL_PHP = url_php;
    this.matricula = matricula;
    this.alumno = alumno;
    this.email = email;
    this.telefono = telefono;
  }

  /**
   * tarea en segundo plano para obtener los registros de la base de datos a cargar en pantalla
   *
   * @param params
   * @return
   */
  @Override
  protected String doInBackground(Void... params) {
    String resultado = "ERROR";

    if (filtrarDatos()) {
      // hay un alumno que cargar
      resultado = "OK";
    } else
      // no hay alumno que cargar
      resultado = "ERROR";

    return resultado;
  }

  /**
   * Método que crea un objeto Alumno con los datos (String)recibidos del servidor y lo almacena en
   * nuestro ArrayList listaAlumnos
   *
   * @return: true /false
   * devuelve true => si hay algún alumno que cargar
   * false => en caso contrario
   */
  private boolean filtrarDatos() {
    boolean resul = false;
    listaAlumnos.clear();
    Alumno alumno = null;

    boolean error_json = false; // para detectar un error al transformar a JSON

    // 1.** rellena listaAlumnos mediante la petición POST **
    String respuesta = cargar();

    // Compara respuesta ignorando mayúsculas y minúsculas con la cadena ""
    if (!respuesta.equalsIgnoreCase("")) { //si hay datos
      JSONObject json; // define un objeto JSON
      try {
        // crea el objeto JSON en base al String respuesta
        json = new JSONObject(respuesta);

        // devuelve un array json si existe el índice de nombre "alumnos"**
        // ***($json['alumnos'][]=$row; -- => archivo.php=>selectAll.php )
        JSONArray jsonArray = json.optJSONArray("alumnos");
        for (int i = 0; i < jsonArray.length(); i++) {
          alumno = new Alumno();
          // obtener el objeto JSON de la posición i
          JSONObject jsonArrayChild = jsonArray.getJSONObject(i);

          // guarda el alumno jsonArrayChild en el objeto alumno
          // **(el índice debe ser el nombre de la columna de la tabla=>matriculas)
          alumno.setAlumno(jsonArrayChild.optString("nombre"));
          alumno.setMatricula(jsonArrayChild.optInt("matricula"));
          alumno.setTelefono(jsonArrayChild.optInt("telefono"));
          alumno.setEmail(jsonArrayChild.optString("email"));
          alumno.setId(jsonArrayChild.optInt("id"));

          // añade el alumno a la lista de alumnos
          listaAlumnos.add(alumno);
        }

      } catch (JSONException e) {
        // Error al convertir a JSON
        // => esto sucede porque no hay alumnos en la consulta y el array json está vacío
        // o por cualquier otro motivo
        e.printStackTrace();
        error_json = true;
      }

      if (error_json)
        resul = false;
      else
        resul = true;
    } else // no hay datos
      resul = false;

    return resul;
  } // fin filtrarDatos()

  /**
   * Método que realiza una consulta a la BD de todas las matriculas de los alumnos
   * a través del script => selectAll.php
   *
   * @return: Devuelve los datos del servidor en forma de String
   */
  private String cargar() {

    // almacenará la respuesta del servidor BD en un String
    String resultado = "";
    // crea el cliente HTTP por defecto
    HttpClient httpclient = new DefaultHttpClient();
    // crea el objeto httpost para realizar la solicitud POST del script PHP correspondiente
    HttpPost httppost = new HttpPost(URL_PHP + Config.PHP_SELECT_ALL); // Url del Servidor
    // para la respuesta de la solicitud al servidor
    HttpResponse response;

    try {
      //ejecuta petición enviando datos por POST y obtiene respuesta
      response = httpclient.execute(httppost);
      // obtiene la entidad del mensaje de respuesta HTTP
      HttpEntity entity = response.getEntity();
      // crea un nuevo flujo de entrada tipo InputStream => instream
      // => con la entidad HTTP => entity
      InputStream instream = entity.getContent();
      // convierte la respuesta del servidor => instream =>
      // a formato cadena (String) => resultado
      resultado = convertStreamToString(instream);

    } catch (ClientProtocolException e) {
      // error en el protocolo HTTP
      e.printStackTrace();
    } catch (IOException e) {
      // error de E/S
      e.printStackTrace();
    }

    return resultado;

  } // fin cargar()

  /**
   * Método que convierte la respuesta del servidor => is
   * => a formato cadena (String) => y la devuelve
   *
   * @param is: respuesta del servidor
   * @throws IOException
   * @return: respuesta en formato String
   */
  public static String convertStreamToString(InputStream is) throws IOException {

    String resul = ""; // resultado a devolver
    BufferedReader reader = null;

    //Convierte respuesta a String
    try {
      // crear un flujo de entrada de tipo BufferedReader en base
      // a un flujo de entrada InputStreamReader con un juego de caracteres de tipo "UTF-8"
      // el tamaño del buffer es de 8 caracteres
      reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);

      // quién no sepa que es un StringBuilder =>
      // http://picandocodigo.net/2010/java-stringbuilder-stringbuffer/

      // crea una cadena de caracteres modificable => StringBuilder
      StringBuilder sb = new StringBuilder();

      // lee todas las líneas del fichero a través del flujo de entrada reader
      String line = null;
      while ((line = reader.readLine()) != null)
        // añade cada línea leída del fichero con un salto de línea => "\n"
        sb.append(line + "\n");

      // guardamos el resultado de la respuesta en el String => resul
      resul = sb.toString();

      // Log.e("getpostresponse", " resul= " + sb.toString());

    } catch (IOException e) {
      e.printStackTrace();
      Log.e("log_tag", "Error E/S al convertir el resultado " + e.toString());

    } finally {
      // la clausula finally siempre se ejecuta => salten excepciones o no
      // por eso es conveniente intentar cerrar aquí los flujos => por si hay un error
      // en la lectura del flujo por ejemplo de tipo E/S => IOException
      try {
        if (is != null)
          is.close(); // cerrar el flujo de entrada is
        if (reader != null)
          reader.close(); // cerrar el flujo de entrada reader
      } catch (IOException e) {
        e.printStackTrace();
        Log.e("log_tag", "Error E/S al cerrar los flujos de entrada " + e.toString());
      }

    }

    return resul;

  } // fin convertStreamToString()

  /**
   * Muestra en pantalla el primer alumno de la lista o un mensaje de error
   * si no se han podido cargar
   * @param result
   */
  protected void onPostExecute(String result) {
    if (result.equals("OK")) {
      // muestra el alumno indicado
      ((MainActivity)context).mostrarAlumno();
    } else
      Config.tostada(this.context, "ERROR, no hay más alumnos que cargar");
  } // fin onPostExecute()

} // fin clase WebService_cargar
