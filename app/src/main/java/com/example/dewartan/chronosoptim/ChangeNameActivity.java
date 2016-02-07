package com.example.dewartan.chronosoptim;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by Prakhar on 12/4/2015.
 */

public class ChangeNameActivity extends ClientDevice{
    TextView name;
    EditText newNamer;
    SyncBuffer syncBuffer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.username_display);

        name=(TextView)findViewById(R.id.name);
        name.setText(getLocalAlias());
        newNamer=(EditText)findViewById(R.id.new_name);

        syncBuffer=new SyncBuffer(this);
    }

    public void uponSync(String response){
        if(response.startsWith(":)")){
            String newName=response.substring(2);
            setLocalAlias(newName);
            name.setText(newName);
        }
    }
    public void coverActions(LinkedList<String> actions){}
    public LinkedList<String> recoverActions(){return new LinkedList<>();}

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save(View view){
        String newName=newNamer.getText().toString();
        syncBuffer.send("&x=changeU&name="+newName);
    }
}