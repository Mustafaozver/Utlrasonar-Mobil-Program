package com.example.sonatest.ults100;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class Bluetooth extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private static final String TAG = "sametsMessage";

    private static int adetB = 0;
    public static int k = 0;
    public static byte[] tempBuffer = new byte[16200];

    private Button btnOnOff;
    private Button btnFindUnpairedDevices;
    private ListView lvNewDevices;
    private Button btnStartConnection;
    public ArrayList<BluetoothDevice> mBTDevices;
    public DeviceListAdapter mDeviceListAdapter;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBTDevice;
    private ConnectThread mConnectThread;
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static ConnectedThread mConnectedThread;
    public static final int MESSAGE_READ = 1;

    private static Handler mHandler = new Handler();

    public static void setHandler(Handler handler){
        mHandler = handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        btnOnOff = (Button)findViewById(R.id.btnOnOff);
        btnFindUnpairedDevices = (Button)findViewById(R.id.btnFindUnpairedDevices);
        lvNewDevices = (ListView)findViewById(R.id.lvNewDevices);
        mBTDevices = new ArrayList<>();
        btnStartConnection = (Button)findViewById(R.id.btnStartConnection);

        //Broadcasts when bond state changes(ie: pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver2, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnOnOff: enabling/disabling bluetooth");
                enableDisableBT();
            }
        });

        lvNewDevices.setOnItemClickListener(Bluetooth.this);

        btnFindUnpairedDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnDiscover: Looking for unpaired devices.");
                if(mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                    Log.d(TAG, "btnDiscover: Canceling discovery.");

                    //in manifest
                    checkBTPermissions();

                    mBluetoothAdapter.startDiscovery();
                    IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mBroadcastReceiver1, discoverDevicesIntent);
                }
                if(!mBluetoothAdapter.isDiscovering()) {

                    checkBTPermissions();

                    mBluetoothAdapter.startDiscovery();
                    IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mBroadcastReceiver1, discoverDevicesIntent);
                }
            }
        });

        btnStartConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    startBTConnection(mBTDevice);
                }catch (Exception e) {
                    Log.e(TAG,"btnStartConnection: a device was not choosen");
                    Toast.makeText(getApplicationContext(),"Önce listeden bir cihaza dokunun",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        try {
            Log.d(TAG, "Bluetooth: onDestroy: called.");
            super.onDestroy();
            unregisterReceiver(mBroadcastReceiver1);
            unregisterReceiver(mBroadcastReceiver2);

        }catch (Exception e) {
            Log.e(TAG, "Bluetooth: onDestroy: " +e.getMessage());
        }
    }

    public void enableDisableBT() {
        if(mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Cihazın Bluetooth Özelliğine Sahip Değil",Toast.LENGTH_SHORT).show();
        }
        else {
            if(!mBluetoothAdapter.isEnabled()) {
                Log.d(TAG,"enableDiasableBT: enabling BT.");
                Intent enableBTintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBTintent);
            }
            if(mBluetoothAdapter.isEnabled()) {
                Log.d(TAG,"enableDiasableBT: disabling BT.");
                mBluetoothAdapter.disable();
            }
        }
    }

    //Then ConnectThread starts and attempts to make a connection with other devices AcceptThread
    public void startBTConnection(BluetoothDevice device) {
        Log.d(TAG,"startBTConnection: Initializing RFCOMM Bluetooth Connection.");

        mConnectThread = new ConnectThread(device);
        mConnectThread.start();

    }

    //Ignore these errors, they are not important
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION");
            if(permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1001); //Any number
            }
        }
        else {
            Log.d(TAG,"checkBTPermissions: No need to check permissions. SDK < LOLLIPOP.");
        }
    }

    //listing devices that are not paired
    //Executed by btnDiscover()
    private BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION_FOUND.");

            if(action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(!mBTDevices.contains(device)) {
                    mBTDevices.add(device);
                    Log.d(TAG, "onReceive: "+device.getName()+": "+device.getAddress());
                    mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                    lvNewDevices.setAdapter(mDeviceListAdapter);
                }
            }
        }
    };

    //detects bond state changes (Pairing status changes)
    private BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases
                //case1: bonded already
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    //inside mBroadcastReceiver2
                    mBTDevice = mDevice;
                }
                //case2: creating bond
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");

                }
                //case3: breaking a bond
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");

                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //first cancel discovery because its very memory intensive
        mBluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You clicked on a device");
        String deviceName = mBTDevices.get(position).getName();
        String deviceAddress = mBTDevices.get(position).getAddress();

        Log.d(TAG, "onItemClick: deviceName = "+ deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = "+ deviceAddress);

        //create the bond.
        //NOTE: Requires API 17+? I think is JellyBean
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if(mBTDevices.get(position).getBondState() == BluetoothDevice.BOND_NONE) {
                Log.d(TAG, "Trying to pair with "+ deviceName);
                mBTDevices.get(position).createBond();
            }
            else if(mBTDevices.get(position).getBondState() == BluetoothDevice.BOND_BONDED) {
                Log.d(TAG, "Choosing "+ deviceName+" connection");
            }

            mBTDevice = mBTDevices.get(position);
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                Log.d(TAG, "ConnectThread: Trying to create RFcommSocket using UUID"+ MY_UUID);
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG,"ConnectThread: Could not create RFcommSocket " + e.getMessage());
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();

                Log.d(TAG, "run: ConnectThread connected");
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.d(TAG,"ConnectThread: run: Closed socket ");
                } catch (IOException closeException) {
                    Log.e(TAG,"ConnectThread: run: Unable to close connection in socket "+closeException.getMessage());
                }
                Log.d(TAG,"run: ConnectThread: Could not connect to UUID: "+MY_UUID);
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connected(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                Log.d(TAG, "ConnectThread: cancel: Closing Client Socket");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: cancel: close() of mmSocket in ConnectThread failed"+ e.getMessage());
            }
        }
    }

    private void connected(BluetoothSocket mmSocket) {
        //Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG,"ConnectedThread: Starting");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {

                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);

                    for(int i=0; i<numBytes; i++) {
                        tempBuffer[k] = mmBuffer[i];
                        k++;
                    }

                    if(k>=2) {

                        adetB = unsignedToBytes(tempBuffer[1])*256+unsignedToBytes(tempBuffer[2]);
                        Log.d(TAG,"ConnectedThread: k: "+ k);
                        if(k >= adetB + 3) {
                            if(tempBuffer[0] == (byte)0xff) {
                                mHandler.obtainMessage(MESSAGE_READ, k, -1, tempBuffer).sendToTarget();
                                String tempMsg2 = new String(tempBuffer,0,k);
                                Log.d(TAG,"ConnectedThread: tempBuffer: "+ tempMsg2);
                                Log.d(TAG,"k: "+k);
                                k=0;
                                tempBuffer[0] = 0;
                            }
                            else {
                                k=0;
                                tempBuffer[0] = 0;
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG,"ConnectedThread: read: Error reading inputstream. "+e.getMessage());
                    break;
                }
            }
        }

        //write method
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);                //write bytes over BT connection via outstream
                String writeMessage = new String (bytes,0,bytes.length);
                Log.d(TAG,"ConnectedThread: write: " +writeMessage);

            } catch (IOException e) {
                //if you cannot write, close the application
                Log.d(TAG,"ConnectedThread: write: Connection Failure");

            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "ConnectedThread: cancel: Could not close the connect socket", e);
            }
        }
    }

    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }
}
