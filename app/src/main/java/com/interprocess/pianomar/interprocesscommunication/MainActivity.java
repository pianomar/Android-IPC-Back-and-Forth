package com.interprocess.pianomar.interprocesscommunication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Messenger messenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Service Connection to handle system callbacks
        ServiceConnection sConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                messenger = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // We are conntected to the service
                messenger = new Messenger(service);
            }
        };
        // We bind to the service
        bindService(new Intent(this, MessengerService.class), sConn,
                Context.BIND_AUTO_CREATE);

        String val = "Test";
        Message msg = Message
                .obtain(null, MessengerService.TO_UPPER_CASE);

        msg.replyTo = new Messenger(new ResponseHandler());
        // We pass the value
        Bundle b = new Bundle();
        b.putString("data", val);

        msg.setData(b);

        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // This class handles the Service response
    private class ResponseHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            int respCode = msg.what;

            switch (respCode) {
                case MessengerService.TO_UPPER_CASE_RESPONSE: {
                    String result = msg.getData().getString("respData");
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}
