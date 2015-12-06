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
    protected ArrayList<String> doInBackground(ArrayList<String>... data){ //.. Gives a string seperated by commas, no max parameter
        // worker thread
        actionBuffer=data[0];

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
            ArrayList<String> result = serverAbsorb(in); //Reads from server

            // bye
            conn.disconnect();
            out.close();
            in.close();
            return result;
        }catch(Exception e){
            Log.w("hm",e.toString());
            return null;
        }
    }

    protected void onPostExecute(ArrayList<String> response){
        // main thread
        syncBuffer.uponSync(response,actionBuffer);  //set text when finish with server call
    }

    private ArrayList<String> serverAbsorb(InputStream in){
        Scanner read=new Scanner(in);
        ArrayList<String> responses=new ArrayList<>();
        String response;
        while(read.hasNextLine()){
            response=read.nextLine();
            responses.add(response);
        }
        return responses;
    }

    private void serverEmit(OutputStream out){
        PrintWriter writer=new PrintWriter(out);
        for(String action:actionBuffer){
            writer.println(action);
            Log.w("emit", action);
        }
        writer.flush();
        writer.close();
    }
}
