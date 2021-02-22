package com.pms.a02_rest_json_matriculas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class WebService_modificar extends AsyncTask<Void, Void, String> {
  //para recibir el contexto de la actividad principal
  private final Activity context;
  private final int posicion;
  private final String URL_PHP;
  private final EditText matricula, alumno, telefono, email;
  private final List<Alumno> listaAlumnos;
  //constructor
  WebService_modificar(Activity context, List<Alumno> listaAlumnos, int posicion,
                       String url_php, EditText matricula, EditText alumno,
                       EditText telefono, EditText email) {
    this.context = context;
    this.listaAlumnos = listaAlumnos;
    this.URL_PHP = url_php;
    this.posicion = posicion;
    this.matricula = matricula;
    this.alumno = alumno;
    this.email = email;
    this.telefono = telefono;
  }

  /**
   * Actualizar el alumno en segundo plano
   */
  @Override
  protected String doInBackground(Void... params) {
    String resultado = "ERROR";
    if (actualizarAlumno())
      // la modificación del alumno ha sido exitosa
      resultado = "OK";
    else
      // ha habido un error al modificar el alumno
      resultado = "ERROR";

    return resultado;
  }//Fin doInBackground


  /**
   * Método para modificar el alumno actual de la BD
   * a través del script => updateAlu.php
   * @return true/false => modificación existosa/error al modificar
   */
  private boolean actualizarAlumno() {
    if (listaAlumnos.isEmpty()) {
      return false;
    } else {
      //obtiene posición en la lista del alumno a modificar
      Alumno alum = listaAlumnos.get(posicion);
      //obtiene el id del alumno a modificar
      String idAlum = String.valueOf(alum.getId());
      boolean resul = false;
      // interfaz para un cliente HTTP
      HttpClient httpclient;
      // define una lista de parámetros ("clave" "valor") que serán enviados por POST al script php
      List<NameValuePair> parametros_POST;
      // define un objeto para realizar una solicitud POST a través de HTTP
      HttpPost httppost;
      // crea el cliente HTTP
      httpclient = new DefaultHttpClient();
      // creamos el objeto httpost para realizar una solicitud POST al script PHP correspondiente
      httppost = new HttpPost(URL_PHP + Config.PHP_UPDATE); // Url del Servidor

      //Añadimos los datos que vamos a enviar por POST al script updateAlu.php
      //** para poder modificar  (y eliminar), debe añadirse el id del alumno a actualizar
      // debe coincidir la clave con índice del $_POST[] indicado en el script updateAlu.php
      parametros_POST = new ArrayList<NameValuePair>(5);
      parametros_POST.add(new BasicNameValuePair("alumno", alumno.getText().toString().trim()));
      parametros_POST.add(new BasicNameValuePair("matricula", matricula.getText().toString().trim()));
      parametros_POST.add(new BasicNameValuePair("telefono", telefono.getText().toString().trim()));
      parametros_POST.add(new BasicNameValuePair("email", email.getText().toString().trim()));
      parametros_POST.add(new BasicNameValuePair("id", idAlum));

      try {
        // establece la entidad => como una lista de pares URL codificada.
        // Esto suele ser útil al enviar una solicitud HTTP POST
        httppost.setEntity(new UrlEncodedFormEntity(parametros_POST));
        // intentamos ejecutar la solicitud HTTP POST
        httpclient.execute(httppost);
        resul = true;
      } catch (UnsupportedEncodingException e) {
        // La codificación de caracteres no es compatible
        resul = false;
        e.printStackTrace();
      } catch (ClientProtocolException e) {
        // Señala un error en el protocolo HTTP
        resul = false;
        e.printStackTrace();
      } catch (IOException e) {
        // Error de Entrada / Salida
        resul = false;
        e.printStackTrace();
      }

      // devuelve el resultado de la inserción
      return resul;

    }
  }//Fin actualizarAlumno()

  /**
   * Muestra un mensaje indicando si se ha modificado o no el alumno.   *
   * @param result
   */
  @Override
  protected void onPostExecute(String result) {

    if (result.equals("OK")) {
      Config.tostada(context, "Alumno actualizado correctamente");
    } else
      Config.tostada(context, "ERROR, Alumno no actualizado!");

  }//Fin onPostExecute
}//fin clase WebService_actualizarAlumno
