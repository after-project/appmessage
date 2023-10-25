package com.after_project.appmessage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity1 extends AppCompatActivity {
    static String className = MainActivity1.class.getSimpleName();
    private AppMessageReceiver mainActivity1AppMessageReceiver;
    private AppMessage appMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        setTitle("App Message - "+this.className);

        //Click Button - Open MainActivity2
        Button buttonOpenMainActivty2 = (Button) findViewById(R.id.ButtonOpenMainActivty2);
        {
            buttonOpenMainActivty2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity1.this, MainActivity2.class);
                    startActivity(intent);
                }
            });
        }

        //Click Button - Create Thread and after 3 secounds send App Message to MainActivity1
        {
            Button buttonThreadAppMessageToMainActivity1 = (Button) findViewById(R.id.ButtonThreadAppMessageToMainActivity1);
            buttonThreadAppMessageToMainActivity1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ThreadAppMessageToMainActivity1().execute();
                }
            });
        }

        //Click Button - Create Thread and after 3 secounds send App Message to MainActivity2
        {
            Button buttonThreadAppMessageToMainActivity2 = (Button) findViewById(R.id.ButtonThreadAppMessageToMainActivity2);
            buttonThreadAppMessageToMainActivity2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ThreadAppMessageToMainActivity2().execute();
                    buttonOpenMainActivty2.callOnClick();
                }
            });
        }


        {
            //Create AppMessage
            appMessage = new AppMessage(this);

            //Create AppMessageReceiver mainActivity1AppMessageReceiver and that will go use mainActivity1AppMessageReceiverCallback as Receiver Callback.
            mainActivity1AppMessageReceiver = new AppMessageReceiver(mainActivity1AppMessageReceiverCallback);

            // in this example to call registerReceiver we go use the first param "receiverName" with same class name of activity.
            //this garantes that you will go handle the message successfully.
            appMessage.registerReceiver( MainActivity1.className , mainActivity1AppMessageReceiver.receiver );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //on Destroy then unregisterReceiver...
        {
            /**
             * Unregister a previously registered BroadcastReceiver.
             * All filters that have been registered for this BroadcastReceiver will be removed.
             */
            appMessage.unregisterReceiver(mainActivity1AppMessageReceiver.receiver);
        }
    }

    // here is the Receiver Callback mainActivity1AppMessageReceiverCallback where you go receive the broadcast messages for MainActivity1.
    private AppMessageReceiver.ReceiverCallback mainActivity1AppMessageReceiverCallback = new AppMessageReceiver.ReceiverCallback() {
        @Override
        public void onReceiveMessage ( int param, String event, String data){
            //You can also pass json string, gson string into app message data string

            switch (param) {
                case 0:
                    switch (event) {
                        case "MainActivty1_Msg": {
                            Toast.makeText(MainActivity1.this,"MainAcitivty1 received Msg data is: " + data,Toast.LENGTH_LONG).show();
                            break;
                        }
                        case "MainActivty1_Thread": {
                            Toast.makeText(MainActivity1.this,"MainAcitivty1 received Msg data is: " + data,Toast.LENGTH_LONG).show();
                            break;
                        }
                        case "MainActivty1_CB": {
                            appMessage.sendTo(MainActivity2.className,0,"MainActivty2_CB",data + "to MainAcitivty2!");
                            break;
                        }

                    }
            }
        }
    };

    //Thread - after 3 secounds send App Message to MainActivity1
    private class ThreadAppMessageToMainActivity1 extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String token) {

            appMessage.sendTo(MainActivity1.className,0,"MainActivty1_Thread","I'am MainAcitivty1, its my message from thread!");
        }
        @Override
        protected void onPreExecute() {
        }
    };


    //Thread - after 3 secounds send App Message to MainActivity2
    private class ThreadAppMessageToMainActivity2 extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String token) {
            appMessage.sendTo(MainActivity2.className,0,"MainActivty2_Thread","I'am MainAcitivty1, its my message from thread!");
        }
        @Override
        protected void onPreExecute() {
        }
    };
}