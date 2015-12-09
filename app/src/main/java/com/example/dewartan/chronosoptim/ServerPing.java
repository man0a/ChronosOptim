package com.example.dewartan.chronosoptim;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Prakhar on 12/5/2015.
 */

public class ServerPing extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {

    private final String SITE="http://www.cs.brandeis.edu//~prakhar/server/core.php";
    private SyncBuffer syncBuffer;
    private ArrayList<String> actionBuffer;

    public ServerPing(SyncBuffer syncBuffer){
        this.syncBuffer=syncBuffer;
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... data){
        // worker thread
        actionBuffer=new ArrayList<>(data[0]);

        try {
            // hello
            URL url = new URL(SITE);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");

            // talk
            OutputStream out = new BufferedOutputStream(conn.getOutputStream());  // Setup connection
            serverEmit(out); // Sends to server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = serverAbsorb(in); //Reads from server
            ArrayList<String> responses=new ArrayList<>(Arrays.asList(result.split("\\|\\|")));

            // bye
            conn.disconnect();
            out.close();
            in.close();
            return responses;
        }catch(Exception e){
            return null;
        }
    }

    protected void onPostExecute(ArrayList<String> responses){
        // main thread
        syncBuffer.uponSync(responses,actionBuffer);  //set text when finish with server call
    }

    private String serverAbsorb(InputStream in){
        Scanner read=new Scanner(in);
        ArrayList<String> responses=new ArrayList<>();
        String response;
        while(read.hasNextLine()){
            response=read.nextLine();
            responses.add(response);
        }
        if(responses.size()==1){
            return responses.get(0);
        }
        return null;
    }

    private void serverEmit(OutputStream out){
        PrintWriter writer=new PrintWriter(out);
        String str="data=";
        for(String action:actionBuffer){
            str+=action.replace('&','|')+"||";
//            Log.w("pemit", action);
        }
        writer.println(str);
        writer.flush();
        writer.close();
    }
}
