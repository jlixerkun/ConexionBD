package com.example.android.conexionbd;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by jlixerkun on 12/03/17.
 */

public final class Utils {

    /** Tag for the log messages */
    public static final String LOG_TAG = Utils.class.getSimpleName();


    public static String registrarCurso(String registroCursoUrl) {
        /**
         * Crear URL de registro
         * */
        URL urlRegistro = crearUrl(registroCursoUrl);

        /**
         * Hacer request a la URL
         */
        String respuesta = null;

        try {
            respuesta = armarHttpRequest(urlRegistro);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(LOG_TAG, respuesta);
        return respuesta;

    }



    public static ArrayList<Curso> fetchCursosData(String requestUrl) {
        // Create URL object
        URL url = crearUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = armarHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Curso> cursosLista = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return cursosLista;
    }

    /**
     * Retorna objeto URL de una string URL.
     */
    private static URL crearUrl(String stringUrl){
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error en la url");

            e.printStackTrace();
        }

        return url;
    }


    private static String armarHttpRequest(URL url) throws IOException {

        String jsonResponse ="";

        if (url == null) {
            return jsonResponse;
        } else {
            Log.i(LOG_TAG, "url is not null " + url.toString() );
        }


        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }


        } catch (IOException e){
            Log.e(LOG_TAG, "Problema con los resultados JSON.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static ArrayList<Curso> extractFeatureFromJson(String cursosJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(cursosJson)) {
            return null;
        } else {
            Log.i(LOG_TAG, cursosJson);
        }

        ArrayList<Curso> responseArray = new ArrayList<>();

        try {
            JSONArray baseJsonResponse = new JSONArray(cursosJson);

//            if(baseJsonResponse.length() > 0) {
//                Log.i(LOG_TAG, "YEAHHH " + baseJsonResponse) ;
//            }
            for (int i = 0 ; i < baseJsonResponse.length(); i++) {
                JSONArray cursoActual = baseJsonResponse.getJSONArray(i);
                String nombreCursoActual = cursoActual.getString(1);
                responseArray.add(new Curso(nombreCursoActual));
            }

            return responseArray;

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the cursos JSON results", e);
        }
        return null;
    }


}
