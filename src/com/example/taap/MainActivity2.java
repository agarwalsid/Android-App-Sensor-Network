package com.example.taap;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;  
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.util.Log;

import java.util.ArrayList;  
import java.util.Arrays;  
  

import android.app.Activity;  
import android.os.Bundle;  
import android.widget.ArrayAdapter;  
import android.widget.ListView;  





public class MainActivity2 extends MainActivity {
	
	TextView myLabel3;

	//private ListView mainListView ;
	  //private ArrayAdapter<String> listAdapter ;
	
	public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_main2);
       
        
        myLabel3 = (TextView)findViewById(R.id.label3);
        
        
        myLabel3.setText(MainActivity.log3);
        Log.d("Name: ", MainActivity.log3);
        //mainListView = (ListView) findViewById( R.id.labelnew );  
        //listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, lvList);
        //mainListView.setAdapter( listAdapter );            
        // Create ArrayAdapter using the planet list.  
//        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lvList);
        
        
  //      mainListView.setAdapter( listAdapter ); 
        Button backButton = (Button)findViewById(R.id.back);
        
      
        
    

        backButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
            	Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                
                startActivity(intent);
            	
            	
            }
        });
       
    }
}
