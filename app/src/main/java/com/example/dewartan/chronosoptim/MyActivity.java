package com.example.dewartan.chronosoptim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MyActivity extends AppCompatActivity {

    public static final String TAG=MyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    public void add(View view){
        write(a() + b());
    }
    public void sub(View view){
        write(a() - b());
    }
    public void mult(View view){
        write(a()*b());
    }
    public void div(View view){
        write(a()/b());
    }
    public void mod(View view){
        write(a()%b());
    }
    public void exp(View view){
        write(Math.pow(a(), b()));
    }
    public void log(View view){
        write(Math.log(b()) / Math.log(a()));
    }

    public void web(View view){
        TextView tw = (TextView)findViewById(R.id.output);
        //
        Event test1 = new Event("Vertica", "Friday, Nov 20", "5:00pm", "\"Meeting Today\"", "MAD Project Meeting");

        ServerPing ping=new ServerPing(tw,test1.toJSON()); //json on the grubs
        ping.execute("http://www.cs.brandeis.edu//~prakhar/hello.php"); // execute is part of async task

        /*
            <?php
                echo $_POST["poop"];
            ?>
        */

    }

    private double a(){
        EditText et=(EditText) findViewById(R.id.inp1);
        return Integer.parseInt(et.getText().toString());
    }
    private double b(){
        EditText et=(EditText) findViewById(R.id.inp2);
        return Integer.parseInt(et.getText().toString());
    }
    private void write(double s){
        TextView tw=(TextView) findViewById(R.id.output);
        tw.setText(""+s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
