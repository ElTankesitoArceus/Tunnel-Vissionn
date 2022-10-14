package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    String host="jellytoto.lostgumball.com";
    String user="toto";
    String password="69420nice";
    String command1="curl http://127.0.0.1:7878/api/v3/diskspace";
    String apiKey = "?apikey=9405ac8e98184fcfade3dae84dcd4fb9";
    TextView textView;

    boolean open = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);

            JSch jsch = new JSch();
            Session session = jsch.getSession( user, host);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            open = session.isConnected();

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command1 + apiKey);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] temp = new byte[1024];

            while (open) {
                while (in.available() > 0) {
                    int i = in.read(temp, 0, 1024);
                    if (i< 0) break;
                    textView.setText(new String(temp, 0, i));
                }
                if(channel.isClosed()){
                    System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }
                Thread.sleep(1000);
            }
            channel.disconnect();
            session.disconnect();
            System.out.println("DONE");


        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        open = false;
    }
}