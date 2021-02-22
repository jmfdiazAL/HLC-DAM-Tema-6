package com.pms.a02_rest_json_matriculas;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

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

public class WebService_insertar extends AsyncTask<Void, Void, String>{

  private Activity context;

  private final List<Alumno> listaAlumnos;
  private final String URL_PHP;
  private final EditText matricula, alumno, telefono, email;

  //constructor
  WebService_insertar(Activity context, List<Alumno> listaAlumnos, String url_php, EditText
      matricula, EditText alumno, EditText telefono, EditText email) {

    this.context = context;
    this.listaAlumnos = listaAlumnos;
    this.URL_PHP = url_php;
    this.matricula = matricula;
    this.alumno = alumno;
    this.email = email;
    this.telefono = telefono;
  }


  @Override
  protected String doInBackground(Void... params) {

    String resultado = "ERROR";

    if (insertar())
      // la inserción del alumno ha sido exitosa
      resultado = "OK";
    else
      // ha habido un error al insertar el alumno y no se pudo insertar
      resultado = "ERROR";

    return resultado;
  }


  /**
   * Método que intenta insertar los datos en el servidor
   * a través del script => insert.php
   *
   * @return: true/false
   * devuelve true => si la inserción es correcta
   * devuelve false => si hubo un error en la inserción
   */
  private boolean insertar() {
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
    httppost = new HttpPost(URL_PHP + Config.PHP_INSERT); // Url del Servidor

    //Añadimos los datos que vamos a enviar por POST al script insert.php
    // ** =>debe coincidir la clave con índice del $_POST[] indicado en el script insert.php
    parametros_POST = new ArrayList<NameValuePair>(4);
    parametros_POST.add(new BasicNameValuePair("alumno", alumno.getText().toString().trim()));
    parametros_POST.add(new BasicNameValuePair("matricula", matricula.getText().toString().trim()));
    parametros_POST.add(new BasicNameValuePair("telefono", telefono.getText().toString().trim()));
    parametros_POST.add(new BasicNameValuePair("email", email.getText().toString().trim()));

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

  } // fin insertar()

  /**
   * Muestra una tostada de si se pudo o no insertar el alumno
   *
   * @param result
   */
  protected void onPostExecute(String result) {
    if (result.equals("OK")) {
      // inserción correcta
      Config.tostada(context, "Alumno insertado con éxito");
    } else
      Config.tostada(context, "ERROR, no se pudo insertar el alumno");
  } // fin onPostExecute()

} // fin clase WebService_insertar
