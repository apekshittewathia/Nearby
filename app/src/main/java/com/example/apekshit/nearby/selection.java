package com.example.apekshit.nearby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class selection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
public void srch(View v){
    Intent i=new Intent(this,MainActivity.class);
    String sr ="&keyword="+((EditText)findViewById(R.id.editText)).getText().toString();
    sr=sr.replaceAll(" ","%20");
//    String src=space(sr);
    i.putExtra("EXTRA",sr);
    Log.e("SR", sr);
    startActivity(i);
}

    public void res(View v){
        Intent i=new Intent(this,MainActivity.class);
        i.putExtra("EXTRA", "&type=restaurant");
        startActivity(i);
    }
    public void ban(View v){
        Intent i=new Intent(this,MainActivity.class);
        i.putExtra("EXTRA", "&name=bank");
        startActivity(i);
    }
    public void hot(View v){
        Intent i=new Intent(this,MainActivity.class);
        i.putExtra("EXTRA","&name=hotel");
        startActivity(i);
    }
    public void ret(View v){
        Intent i=new Intent(this,MainActivity.class);
        i.putExtra("EXTRA","&name=retail%20store");
        startActivity(i);
    }
public String space(ArrayList a){

    for(int i=0;i<a.size();i++){
        if(a.get(i)==" "){
            a.remove(i);
            a.add(i,"%");
            a.add(i+1,"2");
            a.add(i+2,"0");
        }
    }
    return a.toString();
}

}
//Clean coder
