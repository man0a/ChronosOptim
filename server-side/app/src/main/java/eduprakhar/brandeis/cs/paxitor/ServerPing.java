package eduprakhar.brandeis.cs.paxitor;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.Override;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Prakhar on 10/27/2015.
 */
public class ServerPing extends AsyncTask<String, Void, String> {

    private static final String TAG=MyActivity.class.getSimpleName();
    private TextView output;
    private String data;

    public ServerPing(TextView output,String data){
        // main thread
        this.output=output;
        this.data=data;
    }
@Override
    protected String doInBackground(String... urls){
        // worker thread
        try {
            // hello
            URL url = new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");

            // talk
            OutputStream out = new BufferedOutputStream(conn.getOutputStream());  // Setup connection
            serverEmit(out); // Sends to server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = serverAbsorb(in); //Reads from server

            // bye
            conn.disconnect();
            out.close();
            in.close();
            return result;
        }catch(Exception e){
            return null;
        }
    }
    @Override

    protected void onPostExecute(String siteContent){
        // main thread
        output.setText(siteContent);  //set text when finish with server call
    }

    private String serverAbsorb(InputStream in){
        Scanner read=new Scanner(in);
        String str="";
        Log.w(TAG, "" + read.hasNext());
        for(int i=0;i<5 && read.hasNext();i++){
            str+="\n"+read.next();
        }
        return str+"\n";
    }

    private void serverEmit(OutputStream out){
        PrintWriter writer=new PrintWriter(out);
        String data2="poop="+data;
        Log.w(TAG,data2);
        writer.println(data2);
        writer.flush();
        writer.close();
    }
}
