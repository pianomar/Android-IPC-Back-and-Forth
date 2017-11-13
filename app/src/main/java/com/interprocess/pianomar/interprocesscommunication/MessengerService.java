package com.interprocess.pianomar.interprocesscommunication;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by Pianomar on 11/12/2017.
 */

public class MessengerService extends Service {
    public static final int TO_UPPER_CASE = 1;
    public static final int TO_UPPER_CASE_RESPONSE = 1;
    private Messenger messengerContainingHandler = new Messenger(new ConvertHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messengerContainingHandler.getBinder();
    }

    class ConvertHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // This is the action
            int msgType = msg.what;

            switch (msgType) {
                case TO_UPPER_CASE: {
                    try {
                        // Incoming data
                        String data = msg.getData().getString("data");
                        Message message = Message.obtain(null, TO_UPPER_CASE_RESPONSE);
                        Bundle bundle = new Bundle();
                        bundle.putString("respData", data.toUpperCase());
                        message.setData(bundle);

                        msg.replyTo.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                default:
                    super.handleMessage(msg);
            }
        }
    }
}