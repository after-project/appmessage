package com.after_project.appmessage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    static String className = MainActivity2.class.getSimpleName();
    private AppMessageReceiver mainActivity2AppMessageReceiver;
    private AppMessage appMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setTitle(this.className);


        //Click Button - App Message To MainActivity1
        {
            Button buttonAppMessageToMainActivity1 = (Button) findViewById(R.id.ButtonAppMessageToMainActivity1);
            buttonAppMessageToMainActivity1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appMessage.sendTo(MainActivity1.className,0,"MainActivty1_Msg"," hi there MainActivity1!");
                }
            });
        }

        //Click Button - App Message To MainActivity1 And CallBack To MainAcitivty2
        {
            Button buttonAppMessageToMainActivity1AndCBToMainAcitivty2 = (Button) findViewById(R.id.ButtonAppMessageToMainActivity1AndCBToMainAcitivty2);
            buttonAppMessageToMainActivity1AndCBToMainAcitivty2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appMessage.sendTo(MainActivity1.className,0,"MainActivty1_CB","MainAcitivty1 cb...");
                }
            });
        }


        {
            //Create AppMessage
            appMessage = new AppMessage(this);

            //Create AppMessageReceiver mainActivity2AppMessageReceiver and that will go use mainActivity2AppMessageReceiverCallback as Receiver Callback.
            mainActivity2AppMessageReceiver = new AppMessageReceiver(mainActivity2AppMessageReceiverCallback);

            // in this example to call registerReceiver we go use the first param "receiverName" with same class name of activity.
            //this garantes that you will go handle the message successfully.
            appMessage.registerReceiver( MainActivity2.className , mainActivity2AppMessageReceiver.receiver );
        }


    }
    // here is the Receiver Callback mainActivity2AppMessageReceiverCallback where you go receive the broadcast messages for MainActivity2.
    private AppMessageReceiver.ReceiverCallback mainActivity2AppMessageReceiverCallback = new AppMessageReceiver.ReceiverCallback() {
        @Override
        public void onReceiveMessage(int param, String event, String data) {
            switch (param) {
                case 0:
                    switch (event) {
                        case "MainActivty2_CB": {
                            Toast.makeText(MainActivity2.this,"MainAcitivty2 received Msg data is: " + data,Toast.LENGTH_LONG).show();
                            break;
                        }
                        case "MainActivty2_Thread": {
                            Toast.makeText(MainActivity2.this,"MainAcitivty2 received Msg data is: " + data,Toast.LENGTH_LONG).show();
                            break;
                        }

                    }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //on Destroy then unregisterReceiver...
        {
            /**
             * Unregister a previously registered BroadcastReceiver.
             * All filters that have been registered for this BroadcastReceiver will be removed.
             */
            appMessage.unregisterReceiver(mainActivity2AppMessageReceiver.receiver);
        }
    }

}