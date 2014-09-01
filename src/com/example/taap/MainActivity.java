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
import android.widget.TextView;
import android.widget.ListView;
import android.widget.EditText;  
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.util.Log;
import android.app.ListActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;




public class MainActivity extends Activity {
	
	TextView myLabel;
	TextView myLabel2;

	TextView mytimedisp;
	TextView mycondidisp;
	TextView mytemperdisp;
    EditText myTextbox;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    String condidata2;
    String temperadata2;                           
    String temperbdata2;
    String currentDateTimeString2;
    String log;
    public static String log2;
    public static String log3;
    
    String[] lv;
    ArrayList<String> lvList = new ArrayList<String>();
    
    
    DatabaseHandler db = new DatabaseHandler(this);
    
   
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        
        
        Button openButton = (Button)findViewById(R.id.open);
        //Button sendButton = (Button)findViewById(R.id.send);
        Button closeButton = (Button)findViewById(R.id.close);
        Button onButton = (Button)findViewById(R.id.button1);
        Button offButton = (Button)findViewById(R.id.button3);
        Button blinkButton = (Button)findViewById(R.id.button2);
        Button recButton = (Button)findViewById(R.id.rec);
        
        
        
        myLabel = (TextView)findViewById(R.id.label);
        myLabel2 = (TextView)findViewById(R.id.label2);
        
        
        mytimedisp = (TextView)findViewById(R.id.timedisp);
        mycondidisp = (TextView)findViewById(R.id.condidisp);
        mytemperdisp = (TextView)findViewById(R.id.temperdisp);
        
      //Open Button
        openButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try 
                {
                    findBT();
                    openBT();
                }
                catch (IOException ex) { }
            }
        });
        
      //Send Button
        //sendButton.setOnClickListener(new View.OnClickListener()
        //{
            //public void onClick(View v)
            //{
                //try 
                //{
                  //  sendData();
                //}
                //catch (IOException ex) { }
            //}
        //});
        
        //Close button
        closeButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try 
                {
                    closeBT();
                    

                   
                    
                               
                 // Reading all contacts
                    Log.d("Reading: ", "Reading all data.."); 
                    List<Data> taapdata = db.getAllData();       
                    log3="Date                                         " + "| Temp " + "| Condi ";
                    // Writing Contacts to log
                    
                    for (Data cn : taapdata) {
             
                        log = "Id "+cn.getID()+"| Date " + cn.getDt() + "| Temper " + cn.getTemper() + "| Condi "+ cn.getCondi();
                            // Writing Contacts to log
                        log2 = cn.getDt() +  " | " + cn.getTemper() + " | " + cn.getCondi();
                        // Writing Contacts to log
                    Log.d("Name: ", log);
                    lvList.add(log);
                    log3=log3+"\n"+log2;
                    
                    }
                   
                    
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);                 
                    
                    
                    }     
                    
                
                catch (IOException ex) { }
            }
        });
        
      //On button
        onButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try 
                {
                    onLED();
                }
                catch (IOException ex) { }
            }
        });
        
      //Off button
        offButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try 
                {
                    offLED();
                }
                catch (IOException ex) { }
            }
        });
        
      //Blink button
        blinkButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try 
                {
                    blinkLED();
                    
                    
                }
                catch (IOException ex) { }
            }
        });
        
      //Receive button
        recButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try 
                {
                	blinkLED();
                    
                    beginListenForData2();
                    
                              
                    
                }
                catch (IOException ex) { }
            }
        });
        
        
         
      
            
    }
    
    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            myLabel.setText("No bluetooth adapter available");
        }
        
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
        
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("linvor")) 
                {
                    mmDevice = device;
                    myLabel.setText("linvor Found");
                    break;
                }
                else 
                {
                	myLabel.setText("linvor Not Found");
                }
            }
            	
        }
        
    }
    
    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);        
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        
        
        myLabel.setText("Bluetooth Opened");
    }
    
    
    void beginListenForData()
    {
    	
        final Handler handler = new Handler(); 
        final byte delimiter = 10; //This is the ASCII code for a newline character
        
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {              
            	myLabel.setText("Running");
                
               while(!Thread.currentThread().isInterrupted() && !stopWorker)
               {
                    try 
                    {
                        int bytesAvailable = mmInputStream.available();                        
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    
                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            myLabel2.setText(data);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } 
                    catch (IOException ex) 
                    {
                        stopWorker = true;
                    }
               }
            }
        });

        workerThread.start();
    }


    
    
    void beginListenForData2()
    {
    	
        final Handler handler = new Handler(); 
        final byte delimiter = 10; //This is the ASCII code for a newline character
        
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {              
            	myLabel.setText("Running");
                
               while(!Thread.currentThread().isInterrupted() && !stopWorker)
               {
                    try 
                    {
                        int bytesAvailable = mmInputStream.available();                        
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                                  
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    final String condidata = data.substring(0,1);  
                                    final String temperadata = data.substring(1,3);                                   
                                    final String temperbdata = data.substring(3,5);
                                    final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                                    condidata2 = condidata;
                                    temperadata2 = temperadata;
                                    temperbdata2 = temperbdata;
                                    currentDateTimeString2 = currentDateTimeString;
                                    
                                    
                                    Log.d("Insert: ", "Inserting .."); 
                                    db.addData(new Data(currentDateTimeString2, temperadata2+"."+temperbdata2, condidata2));        
                                    
                                    
                                    readBufferPosition = 0;
                                    
                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                        	myLabel2.setText(data);
                                        	if(condidata2.equals("2"))
                                        	{
                                            mycondidisp.setText("Light");
                                            condidata2="Light";
                                        	}
                                        	else
                                        	{
                                            mycondidisp.setText("Dark");
                                            condidata2="Dark";
                                        	}
                                            mytemperdisp.setText(temperadata2+"."+temperbdata2);
                                            mytimedisp.setText(currentDateTimeString2);
                                          
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } 
                    catch (IOException ex) 
                    {
                        stopWorker = true;
                    }
               }
            }
        });

        workerThread.start();
    }

    
    
    
    
    void recData() throws IOException
    {
    	String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        mytimedisp.setText(currentDateTimeString);
       
    }
    
    
    
    
    void sendData() throws IOException
    {
        String msg = "abcdefghijklm";
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        myLabel.setText("Data Sent");
       
    }
    
    void onLED() throws IOException
    {
        String msg = "1";
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        myLabel.setText("LED On");
       
    }
    
    void offLED() throws IOException
    {
        String msg = "2";
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        myLabel.setText("LED Off");
       
    }
    
    void blinkLED() throws IOException
    {
        String msg = "3";
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        myLabel.setText("LED Blink");
       
    }
    
    
    void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        myLabel.setText("Bluetooth Closed");
    }
    
    
}
