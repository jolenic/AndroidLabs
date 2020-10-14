package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Button sendButton = findViewById(R.id.send_button);
        Button receiveButton = findViewById(R.id.receive_button);
        EditText chatText = findViewById(R.id.chat_text);

        ListView messageList = findViewById(R.id.message_list);

        loadDataFromDatabase();

        MyListAdapter adapter = new MyListAdapter();
        messageList.setAdapter(adapter);


        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String type = "send";
                String content = chatText.getText().toString();

                //add to db and get new ID
                ContentValues newRowValues = new ContentValues();
                newRowValues.put(MyOpener.COL_TYPE, type);
                newRowValues.put(MyOpener.COL_CONTENT, content);
                //insert into db and get id value
                long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
                messages.add(new Message(type, content, newId));
                adapter.notifyDataSetChanged();
                chatText.setText("");}
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String type = "receive";
                String content = chatText.getText().toString();

                //add to db and get new ID
                ContentValues newRowValues = new ContentValues();
                newRowValues.put(MyOpener.COL_TYPE, type);
                newRowValues.put(MyOpener.COL_CONTENT, content);
                //insert into db and get id value
                long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

                messages.add(new Message(type, content, newId));
                adapter.notifyDataSetChanged();
                chatText.setText("");}
        });

        messageList.setOnItemLongClickListener( (p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?")

                    //What is the message:
                    .setMessage("The selected row is: " + pos + ".  The database ID is: " + id)

                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {
                        deleteContact(messages.get(pos));
                        messages.remove(pos);
                        adapter.notifyDataSetChanged();
                    })
                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> { })


                    //Show the dialog
                    .create().show();
            return true;
        });
    } //end method onCreate

    private void loadDataFromDatabase()
    {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {MyOpener.COL_ID, MyOpener.COL_TYPE, MyOpener.COL_CONTENT};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int typeColIndex = results.getColumnIndex(MyOpener.COL_TYPE);
        int contentColIndex = results.getColumnIndex(MyOpener.COL_CONTENT);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String type = results.getString(typeColIndex);
            String content = results.getString(contentColIndex);
            long id = results.getLong(idColIndex);

            //add the new message to the array list:
            messages.add(new Message(type, content, id));
        }

        //At this point, the messages array has loaded every row from the cursor.
    } //end method loadDataFromDatabase

    protected void deleteContact(Message m)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(m.getId())});
    }

    private class Message {

        private String type;
        private String content;
        private long id;

        Message(String type, String content, long id) {
            this.type = type;
            this.content = content;
            this.id = id;
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

        public long getId() { return id;}

        public void setId(long id) { this.id = id; }

        public String toString() { return "  " + content + "  "; }
    } //end sub-class Message

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
            return messages.get(position).getId();
        }

        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = old;
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            if(newView == null) {
                    newView = inflater.inflate(R.layout.message_layout, parent, false);
            }

            //set what the text should be for this row:

                TextView tView = newView.findViewById(R.id.messageTextGoesHere);
                tView.setText(getItem(position).toString());
                ImageView receive_pic = newView.findViewById(R.id.receive_pic);
                ImageView send_pic = newView.findViewById(R.id.send_pic);


            if (getItem(position).getType() == "receive") {
                    tView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    receive_pic.setImageResource(R.drawable.row_receive);
                    send_pic.setImageResource(0);
                } else {
                    tView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    send_pic.setImageResource(R.drawable.row_send);
                    receive_pic.setImageResource(0);
                }

            return newView;
        }

    } //end sub-class MyListAdapter
} //end class ChatRoomActivity

