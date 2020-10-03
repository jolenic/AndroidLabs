package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    private ArrayList<Message> messages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Button sendButton = findViewById(R.id.send_button);
        Button receiveButton = findViewById(R.id.receive_button);
        EditText chatText = findViewById(R.id.chat_text);

        ListView messageList = findViewById(R.id.message_list);
        MyListAdapter adapter = new MyListAdapter();
        messageList.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                messages.add(new Message("send", chatText.getText().toString()));
                adapter.notifyDataSetChanged();
                chatText.setText("");}
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                messages.add(new Message("receive", chatText.getText().toString()));
                adapter.notifyDataSetChanged();
                chatText.setText(""); }
        });

        messageList.setOnItemLongClickListener( (p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?")

                    //What is the message:
                    .setMessage("The selected row is: " + pos + ".  The database ID is: " + id)

                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {
                        messages.remove(pos);
                        adapter.notifyDataSetChanged();
                    })
                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> { })


                    //Show the dialog
                    .create().show();
            return true;
        });
    }

    private class Message {

        private String type;
        private String content;

        Message(String type, String content) {
            this.type = type;
            this.content = content;
        }

        //getters and setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String toString() {
            return type + ": " + content;
        }
    }

    private class MyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Message getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = old;
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            if(newView == null) {
                    newView = inflater.inflate(R.layout.send_message_layout, parent, false);
            }

            //set what the text should be for this row:

                TextView tView = newView.findViewById(R.id.sendTextGoesHere);
                tView.setText(getItem(position).toString());
                if (getItem(position).getType() == "receive") {
                    tView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                } else {
                    tView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                }
                //ImageView img = newView.findViewById(R.id.send_image);
                //img.setImageResource(R.drawable.guest_grey);


            return newView;
        }

    }
}

