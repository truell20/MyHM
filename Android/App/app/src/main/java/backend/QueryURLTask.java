package backend;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class QueryURLTask extends AsyncTask<QueryURLParams, Void, String> {

    QueryURLCallback callback;

    public QueryURLTask(QueryURLCallback callback_) {
        callback = callback_;
    }

    @Override
    protected String doInBackground(QueryURLParams... params) {
        return queryURL(params[0].urlText, params[0].method, params[0].arguments);
    }

    @Override
    protected void onPostExecute(String result) {
        if (callback != null) callback.onFinish(result);
    }

    private String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private String urlEncodeUTF8(Map<?,?> map) {
        if(map == null) return "";

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }

    // Returns the contents of the webpage at the url specified. Uses GET to make the query
    private String queryURL(String urlText, HTTPMethod method, HashMap<String, Object> arguments) {
        String argumentString = urlEncodeUTF8(arguments);
        System.out.println("url: " + urlText);

        // Create url based on method
        URL url = null;
        try {
            if(method == HTTPMethod.GET) {
                url = new URL(urlText+"?"+argumentString);
            } else {
                url = new URL(urlText);
            }
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
            return null;
        }

        HttpURLConnection connection = null;
        DataOutputStream dataOutputStream = null;
        try {
            connection = (HttpURLConnection) url.openConnection();

            // If PUT or POST, edit connection and transfer arguments properly
            if(method == HTTPMethod.PUT || method == HTTPMethod.POST) {
                if(method == HTTPMethod.PUT)  connection.setRequestMethod("PUT");
                else  connection.setRequestMethod("POST");

                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(argumentString);
            }

            // Get contents of webpage
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) response.append(inputLine);
            in.close();

            System.out.println("out: " + response.toString());

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // Shut down streams and connection
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.flush();
                    dataOutputStream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
