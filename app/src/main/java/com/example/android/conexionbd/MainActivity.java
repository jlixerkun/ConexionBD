package com.example.android.conexionbd;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.R.id.edit;

public class MainActivity extends AppCompatActivity {


    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * URL para hacer queries retorna JSONArray de los cursos
     * */
    private static final String SERVICE_URL = "http://192.168.0.4/dbservice/selectall.php";
    /**
     * URL para hacer inserts a la tabla cursos de la base de datos
     * */
    private static final String UP_SERVICE_URL = "http://192.168.0.4/dbservice/registro.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**
         * Campo de texto editable para ingresar el nombre de un nuevo curso.
         * */
        final EditText editText = (EditText) findViewById(R.id.txtCourse);

        /**
         * El campo está con su atributo focusable=false desde el xml_layout
         * con este listener logramos restablecer la funcionalidad
         * al tocar el campo
         * */
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        /**
         * Botón para ejecutar la consulta a la base de datos;
         * */
        Button btnConsultarCursos = (Button) findViewById(R.id.getCourse);



        /**
         * La tarea se ejecuta en em momento de hacer clic en el botón
         */
        btnConsultarCursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Tarea asíncrona para consultar todos los cursos.
                 * No requiere parámetros
                 * */
                CursosAsyncTask task = new CursosAsyncTask();
                task.execute(SERVICE_URL);
            }
        });

        /**
         * Botón para insertar datos en la bd
         * */
        Button btnAgregarCursos = (Button) findViewById(R.id.addCourse);


        btnAgregarCursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cursoNombre = editText.getText().toString();
                if(cursoNombre != "" && cursoNombre != null){
                    String urlString = UP_SERVICE_URL+"?curso="+cursoNombre;
                    new InsertaCursoAsyncTask().execute(urlString);
                } else {
                    Toast.makeText(getApplicationContext(), "Debe especificar un nombre", Toast.LENGTH_SHORT);
                }
            }
        });

    }
    private void updateUi(ArrayList<Curso> listaCursos) {

        CursoAdapter cursosArrayAdapter = new CursoAdapter(this, listaCursos);
        ListView vistaListaCursos = (ListView) findViewById(R.id.listaCursos);

        vistaListaCursos.setAdapter(cursosArrayAdapter);

    }


    private class CursosAsyncTask extends AsyncTask<String, Void, ArrayList<Curso>> {


        @Override
        protected ArrayList<Curso> doInBackground(String... urls) {

            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }


            ArrayList<Curso> cursosArrayList = Utils.fetchCursosData(SERVICE_URL);
            
            return cursosArrayList;
            
        }

        @Override
        protected void onPostExecute(ArrayList<Curso> listaCursos) {
            if (listaCursos == null) {
                return;
            }
            updateUi(listaCursos);
        }


    }


    private class InsertaCursoAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            String cursoRegistrado = Utils.registrarCurso(urls[0]);

            return cursoRegistrado;
        }

        @Override
        protected void onPostExecute(String string) {
            EditText editText = (EditText) findViewById(R.id.txtCourse);
            editText.setText("");
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);


            Toast.makeText(getApplicationContext(), "El curso ha sido insertado con éxito" , Toast.LENGTH_SHORT).show();
        }
    }

}
    
