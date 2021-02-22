package com.pms.a02_rest_json_matriculas;

import android.content.Context;
import android.widget.Toast;

public class Config {

  // dirección IP o URL del servidor
  //private final static String URL_SERVIDOR ="192.168.210.25"; // comprobar IP local que puede cambiar
  public final static String URL_SERVIDOR = "cantabile-chaplains.000webhostapp.com"; // hosting en 000webhost

  // URL del directorio de los scripts php del servidor
  public final static String URL_PHP = "http://" + URL_SERVIDOR + "/rest_con_json/";

  // script de selección de todos los registros
  public final static String PHP_SELECT_ALL = "selectAll.php";

  // script de inserción de un registro
  public final static String PHP_INSERT = "insert.php";

  // script de modificación de un registro
  public final static String PHP_UPDATE = "updateAlu.php";


  public static void tostada(Context context, String mensaje) {
    Toast toast1 = Toast.makeText(context, mensaje, Toast.LENGTH_SHORT);
    toast1.show();
  }
}
