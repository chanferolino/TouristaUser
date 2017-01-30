package com.touristadev.tourista.utils;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A utility class that handles all Http related calls and services.
 */
public class HttpUtils {

    private static final String LOG_TAG = HttpUtils.class.getSimpleName();

    /**
     * Retrieves the response data in String format from the specifed URL.
     *
     * @param sUrl the well-formed URL to retrieve the data from
     * @return the response data in String format.
     */
    public static String GET(String sUrl) {
        if (TextUtils.isEmpty(sUrl)) {
            throw new RuntimeException("Passed URL is either null or empty.");
        }

        String requestMethod = "GET";

        if (TextUtils.isEmpty(requestMethod)) {
            throw new RuntimeException("Request Method is null or empty");
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(sUrl);

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.connect();

            // Read the input stream and convert into a response string
            InputStream inputStream = urlConnection.getInputStream();

            // This should not be changed to StringBuilder as this method must be invoked on a
            // background thread.
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line);
                buffer.append("\n");
            }

            if (buffer.length() == 0) {
                // The Stream was empty. No point in pasing
                return null;
            }

            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error retrieving data from server", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    /**
     * Retrieves the response data in String format from the specifed URI.
     *
     * @param uri the built uri to retrieve data from.
     * @return the response data in String format.
     */
    public static String GET(Uri uri) {
        return GET(uri.toString());
    }

    /**
     * Posts a JSON Request to the server endpoint.
     *
     * @param uri  the built uri to post data to
     * @param json the data to be posted in JSON format
     * @return the JSON response data in String format
     */
    public static String POST(Uri uri, String json) {
        return PLACE(uri.toString(), "POST", json);
    }

    /**
     * Posts a JSON Request to the server endpoint.
     *
     * @param url  the built url to post data to
     * @param json the data to be posted in JSON format
     * @return the JSON response data in String format
     */
    public static String POST(String url, String json) {
        return PLACE(url, "POST", json);
    }
    public static String POST(String url, JSONObject json) {
        return PLACE(url, "POST", json);
    }
    public static String POST(String url, JSONArray json) {
        return PLACE(url, "POST", json);
    }

    /**
     * Posts an update to a server resource by posting JSON-formatted request.
     *
     * @param uri  the built uri to put data to
     * @param json the data to be posted in JSON format
     * @return the JSON response data in String format
     */
    public static String PUT(Uri uri, String json) {
        return PLACE(uri.toString(), "PUT", json);
    }

    /**
     * Posts an update to a server resource by posting JSON-formatted request.
     *
     * @param url  the built url to put data to
     * @param json the data to be posted in JSON format
     * @return the JSON response data in String format
     */
    public static String PUT(String url, String json) {
        return PLACE(url, "PUT", json);
    }

    /**
     * Posts an update to a server resource by posting JSON-formatted request.
     *
     * @param uri  the built uri to put data to
     * @param json the data to be posted in JSON format
     * @return the JSON response data in String format
     */
    public static String PATCH(Uri uri, String json) {
        return PLACE(uri.toString(), "PATCH", json);
    }

    /**
     * Posts an update to a server resource by posting JSON-formatted request.
     *
     * @param url  the built url to put data to
     * @param json the data to be posted in JSON format
     * @return the JSON response data in String format
     */
    public static String PATCH(String url, String json) {
        return PLACE(url, "PATCH", json);
    }

    /**
     * Removes a server resource.
     *
     * @param uri the built uri to command the delete operation
     * @return the response code from the server after performing the operation
     */
    public static int DELETE(Uri uri) {
        return DELETE(uri.toString());
    }

    /**
     * Posts a JSON Request to the server endpoint.
     *
     * @param sUrl          the built url to post data to
     * @param requestMethod the method to use when requesting a POST, PUT, PATCH operations
     * @param json          the data to be posted in JSON format
     * @return the JSON response data in String format
     */
    private static String PLACE(String sUrl, String requestMethod, String json) {
        HttpURLConnection urlConnection;

        try {
            // Connect
            URL url = new URL(sUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.addRequestProperty("Content-Type", "application/json");
            urlConnection.addRequestProperty("Authorization", "key="+"AAAAzfXo2LM:APA91bFZ6Adgvob0lEKkcv1NxEfDtZIhenSAYnmtqpADx_sJKxeYBSgygy_pYP03Pi643cVjHZsGq5SjGz26TOdqKsoI5SqKmN9vv96udPrV97TyVdKUHCCadOdqmaXmuvgf8OsV11gdtqQb_E9go_QZaXuLfuteMg");
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.connect();

            // Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(json);
            writer.close();
            outputStream.close();

            // Read
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            StringBuffer buffer = new StringBuffer();

            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }

            bufferedReader.close();
            inputStream.close();

            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    private static String PLACE(String sUrl, String requestMethod, JSONObject json) {
        HttpURLConnection urlConnection;

        try {
            // Connect
            URL url = new URL(sUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.addRequestProperty("Content-Type", "application/json");
            urlConnection.addRequestProperty("Authorization", "key="+"AAAAzfXo2LM:APA91bFZ6Adgvob0lEKkcv1NxEfDtZIhenSAYnmtqpADx_sJKxeYBSgygy_pYP03Pi643cVjHZsGq5SjGz26TOdqKsoI5SqKmN9vv96udPrV97TyVdKUHCCadOdqmaXmuvgf8OsV11gdtqQb_E9go_QZaXuLfuteMg");
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.connect();

            // Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(json.toString());
            writer.close();
            outputStream.close();

            // Read
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            StringBuffer buffer = new StringBuffer();

            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }

            bufferedReader.close();
            inputStream.close();

            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    private static String PLACE(String sUrl, String requestMethod, JSONArray json) {
        HttpURLConnection urlConnection;

        try {
            // Connect
            URL url = new URL(sUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.addRequestProperty("Content-Type", "application/json");
            urlConnection.addRequestProperty("Authorization", "key="+"AAAAzfXo2LM:APA91bFZ6Adgvob0lEKkcv1NxEfDtZIhenSAYnmtqpADx_sJKxeYBSgygy_pYP03Pi643cVjHZsGq5SjGz26TOdqKsoI5SqKmN9vv96udPrV97TyVdKUHCCadOdqmaXmuvgf8OsV11gdtqQb_E9go_QZaXuLfuteMg");
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.connect();

            // Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(json.toString());
            writer.close();
            outputStream.close();

            // Read
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            StringBuffer buffer = new StringBuffer();

            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }

            bufferedReader.close();
            inputStream.close();

            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Removes a server resource.
     *
     * @param sUrl the built url to command the delete operation
     * @return the response code from the server after performing the operation
     */
    public static int DELETE(String sUrl) {
        HttpURLConnection urlConnection;

        try {
            // Connect
            URL url = new URL(sUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("DELETE");
            urlConnection.connect();

            return urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 400; // Bad Request
    }
}