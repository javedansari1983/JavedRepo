package com.agilefaqs.chatapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.confengine.chatapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MainActivity extends Activity implements View.OnClickListener {
    public RecyclerView.Adapter<ViewHolder> messagesAdaptor;
    private RecyclerView messagesList;
    private List<String> messages;
    //************Modified by Javed***************
    private EditText messageInput;
    private Button sendButton;
    //***************//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messages = new ArrayList<>();
        messagesList = (RecyclerView) findViewById(R.id.messagesList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messagesList.setLayoutManager(layoutManager);
        messagesList.addItemDecoration(new DividerItemDecoration(this));
        messagesAdaptor = new RecyclerView.Adapter<ViewHolder>(){
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item_message, parent, false);
                return new ViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                holder.messageTextView.setText(messages.get(position));
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
        };
        //************Modified by Javed***************
        if(messages != null)
        messagesList.setAdapter(messagesAdaptor);
        messageInput = (EditText) findViewById(R.id.messageInput);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);
        //************
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                downloadMessages();
            }
        }, SECONDS.toMillis(1), SECONDS.toMillis(10));
    }

    @Override
    protected void onResume() {
        super.onResume();
        downloadMessages();
    }

    private void downloadMessages() {
        HttpURLConnection urlConnection = null;
        try {
            //************Modified by Javed***************
            //User 10.0.2.2 for emulator and your system Ip for real device
            URL url = new URL("http://172.16.32.231:4567/fetchAllMessages");
            //***************//
            urlConnection = (HttpURLConnection) url.openConnection();
            new AsyncTask<HttpURLConnection, Object, List<String>>() {
                @Override
                protected List<String> doInBackground(HttpURLConnection... params) {

                    List<String> messages = null;
                    try {
                        InputStream in = params[0].getInputStream();
                        InputStreamReader isw = new InputStreamReader(in);
                        StringBuilder buffer = new StringBuilder();
                        int data;
                        data = isw.read();
                        while (data != -1) {
                            char c = (char) data;
                            buffer.append(c);
                            System.out.print(c);
                            data = isw.read();
                        }
                        Type type = new TypeToken<List<String>>() {
                        }.getType();
                        messages = new Gson().fromJson(buffer.toString(), type);
                    } catch (Exception e) {
                        Log.e("", e.getMessage(), e);
                    }
                    return messages;
                }

                @Override
                protected void onPostExecute(List<String> m) {
                    if (messages != null) {
                        messages = m;
                        messagesAdaptor.notifyDataSetChanged();
                        messagesList.scrollToPosition(messagesAdaptor.getItemCount()-1);
                    } else {
                        Toast.makeText(MainActivity.this, "Could not fetch messages", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(urlConnection);
        } catch (Exception e) {
            Log.e("", e.getMessage(), e);
        } finally {
            urlConnection.disconnect();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sendButton){
            HttpURLConnection urlConnection = null;
            try {
                String msg = messageInput.getText().toString();
                messages.add(msg);
                messagesAdaptor.notifyDataSetChanged();
                messagesList.scrollToPosition(messagesAdaptor.getItemCount()-1);
                //************Modified by Javed***************
                //Use 10.0.2.2 for emulator and your system ip for real device
                    URL url = new URL("http://172.16.32.231:4567/send?message=" + msg);
                    urlConnection = (HttpURLConnection) url.openConnection();
                //System.out.println("msg value is: "+msg);
                //urlConnection = connectionOpen("http://172.16.32.231:4567/send?message="+msg);
                //***************//
                new AsyncTask<HttpURLConnection, Object, Boolean>() {
                    @Override
                    protected Boolean doInBackground(HttpURLConnection... params) {

                        try {
                            InputStream in = params[0].getInputStream();
                            InputStreamReader isw = new InputStreamReader(in);
                            StringBuffer buffer = new StringBuffer();
                            int data    ;
                            data = isw.read();
                            while (data != -1) {
                                char c = (char) data;
                                buffer.append(c);
                                System.out.print(c);
                                data = isw.read();
                            }
                            Log.d("", buffer.toString());
                        } catch (Exception e) {
                            Log.e("", e.getMessage(), e);
                            return false;
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean status) {
                        if (status) {
                            messages.add(messageInput.getText().toString());
                            messagesAdaptor.notifyDataSetChanged();
                            messagesList.scrollToPosition(messagesAdaptor.getItemCount()-1);
                            downloadMessages();
                            messageInput.setText("");
                            Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Message send failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute(urlConnection);
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                urlConnection.disconnect();
            }
        }
        }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            messageTextView = (TextView) itemView.findViewById(R.id.message);
        }
    }
//************Modified by Javed***************
//    public HttpURLConnection connectionOpen(String serviceUrl){
//        HttpURLConnection urlConnection = null;
//        try{
//            URL url = new URL(serviceUrl);
//            urlConnection = (HttpURLConnection) url.openConnection();
//        }catch (IOException e){
//            Log.e("", e.getMessage(), e);
//        }
//        return urlConnection;
//    }
    //***************//

}
