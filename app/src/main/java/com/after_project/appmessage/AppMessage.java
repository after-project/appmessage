// Copyright (c) Thiago Schnell.
// Licensed under the MIT License.
package com.after_project.appmessage;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
public class AppMessage {
    private Context mContext;
    AppMessage(Context context){
        mContext = context;
    }
    void unregisterReceiver(BroadcastReceiver receiver){
        //Unregister a previously registered BroadcastReceiver. All filters that have been registered for this BroadcastReceiver will be removed.
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(receiver);
    }
    private String action(String action){
        return String.format("%s.APP_MESSAGE_%s",BuildConfig.APPLICATION_ID,action.toUpperCase());
    }
    void registerReceiver( String receiverName, BroadcastReceiver receiver){
        // Register a receive for any local broadcasts that match the given IntentFilter.
        LocalBroadcastManager.getInstance(mContext)
                .registerReceiver(receiver, new IntentFilter(action(receiverName)));
    }
    private void send(String receiverName, int param, String event, String data, Boolean sync){
        Intent intent = new Intent(action(receiverName));
        intent.putExtra("param", param);
        intent.putExtra("event", event);
        intent.putExtra("data", data);
        if(sync){
            //Like sendBroadcast(Intent), but if there are any receivers for the Intent this function will block and immediately dispatch them before returning.
            LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);
        }else{
            //Broadcast the given intent to all interested BroadcastReceivers.
            // This call is asynchronous;
            // it returns immediately, and you will continue executing while the receivers are run.
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
    }
    protected void sendSync(String receiverName, int param, String event, String data){
        send(receiverName,param,event,data,true);
    }
    /**
     *
     * @param receiverName The name you are used in registerReceiver IntentFilter - must match the same name to receive the messages.
     * @param param id message
     * @param event event of the id message
     * @param data data string of message event
     */
    protected void sendTo(String receiverName, int param, String event, String data){
        send(receiverName,param,event,data,false);
    }
}