package com.pms.a03_ud6_rest_json_crud;
/**
 * Created by Belal, adaptado y comentado.
 */
public class Config {

    // Host o URL donde están alojados los scripts PHP
    public static final String HOST = "192.168.0.111"; //IP local de tu equipo

    //public static final String HOST = "cantabile-chaplains.000webhostapp.com"; // en 000webhost
    // URL de nuestros scripts PHP
    public static final String URL_ADD = "http://"+HOST+"/CRUD/addEmp.php";
    public static final String URL_GET_ALL = "http://"+HOST+"/CRUD/getAllEmp.php";
    public static final String URL_GET_EMP = "http://"+HOST+"/CRUD/getEmp.php?id=";
    public static final String URL_UPDATE_EMP = "http://"+HOST+"/CRUD/updateEmp.php";
    public static final String URL_DELETE_EMP = "http://"+HOST+"/CRUD/deleteEmp.php?id=";

    //LLaves o Keys que serán usados para acceder a los campos de las tablas de la BD con los scripts PHP
    //El índice de $_POST['indice']
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_NAME = "name";
    public static final String KEY_EMP_DESG = "desg";
    public static final String KEY_EMP_SAL = "salary";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_DESG = "desg";
    public static final String TAG_SAL = "salary";

    //id del empleado para lanzar intent
    public static final String EMP_ID = "emp_id";
}
