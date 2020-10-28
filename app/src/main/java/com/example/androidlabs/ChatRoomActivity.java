package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {
    private ArrayList<Message> messages = new ArrayList<>();
    protected MyOpener opener = new MyOpener(this);
    protected SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Boolean isTablet = (findViewById(R.id.frame) != null);

        Button sendButton = findViewById(R.id.send_button);
        Button receiveButton = findViewById(R.id.receive_button);
        EditText chatText = findViewById(R.id.chat_text);

        ListView messageList = findViewById(R.id.message_list);

        loadDataFromDatabase(opener);

        MyListAdapter adapter = new MyListAdapter();
        messageList.setAdapter(adapter);


        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int type = 0;
                String content = chatText.getText().toString();

                //add to db and get new ID
                ContentValues newRowValues = new ContentValues();
                newRowValues.put(opener.COL_TYPE, type);
                newRowValues.put(opener.COL_CONTENT, content);
                //insert into db and get id value
                long newId = db.insert(opener.TABLE_NAME, null, newRowValues);
                messages.add(new Message(type, content, newId));
                adapter.notifyDataSetChanged();
                chatText.setText("");}
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int type = 1;
                String content = chatText.getText().toString();

                //add to db and get new ID
                ContentValues newRowValues = new ContentValues();
                newRowValues.put(opener.COL_TYPE, type);
                newRowValues.put(opener.COL_CONTENT, content);
                //insert into db and get id value
                long newId = db.insert(opener.TABLE_NAME, null, newRowValues);

                messages.add(new Message(type, content, newId));
                adapter.notifyDataSetChanged();
                chatText.setText("");}
        });

        messageList.setOnItemLongClickListener( (list, item, position, id) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putString("MESSAGE_CONTENT", messages.get(position).getContent());
            dataToPass.putInt("MESSAGE_POSITION", position);
            dataToPass.putLong("MESSAGE_ID", id);

            if(isTablet)
            {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
            return true;
        });

//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//            alertDialogBuilder.setTitle("Do you want to delete this?")
//
//                    //What is the message:
//                    .setMessage("The selected row is: " + pos + ".  The database ID is: " + id)
//
//                    //what the Yes button does:
//                    .setPositiveButton("Yes", (click, arg) -> {
//                        deleteContact(opener, messages.get(pos));
//                        messages.remove(pos);
//                        adapter.notifyDataSetChanged();
//                    })
//                    //What the No button does:
//                    .setNegativeButton("No", (click, arg) -> { })
//
//
//                    //Show the dialog
//                    .create().show();
//            return true;
    } //end method onCreate

    private void loadDataFromDatabase(MyOpener opener)
    {
        //get a database connection:
        MyOpener dbOpener = opener;
        db = dbOpener.getWritableDatabase();


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {dbOpener.COL_ID, dbOpener.COL_TYPE, dbOpener.COL_CONTENT};
        //query all the results from the database:
        Cursor results = db.query(false, dbOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        printCursor(results, db.getVersion());

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int typeColIndex = results.getColumnIndex(dbOpener.COL_TYPE);
        int contentColIndex = results.getColumnIndex(dbOpener.COL_CONTENT);
        int idColIndex = results.getColumnIndex(dbOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        results.moveToFirst();
        while(!results.isAfterLast())
        {
            int type = results.getInt(typeColIndex);
            String content = results.getString(contentColIndex);
            long id = results.getLong(idColIndex);

            //add the new message to the array list:
            messages.add(new Message(type, content, id));
            results.moveToNext();
        }

        //At this point, the messages array has loaded every row from the cursor.
    } //end method loadDataFromDatabase

    private void printCursor(Cursor c, int version) {
        Log.d("Version #: ", String.valueOf(version));
        Log.d("# of Columns: ", String.valueOf(c.getColumnCount()));
        Log.d("Names of Columns: ", Arrays.toString(c.getColumnNames()));
        Log.d("# of Results: ", String.valueOf(c.getCount()));

        int typeColIndex = c.getColumnIndex(opener.COL_TYPE);
        int contentColIndex = c.getColumnIndex(opener.COL_CONTENT);
        int idColIndex = c.getColumnIndex(opener.COL_ID);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            int type = c.getInt(typeColIndex);
            String content = c.getString(contentColIndex);
            long id = c.getLong(idColIndex);
            Log.d("ROW", "type: " + type + ", content: " + content + ", id:" + id);
            c.moveToNext();
        }

    } //end method printCursor

    protected void deleteContact(MyOpener opener, Message m)
    {
        db = opener.getWritableDatabase();
        db.delete(opener.TABLE_NAME, opener.COL_ID + "= ?", new String[] {Long.toString(m.getId())});
    }

    private class Message {

        private int type;
        private String content;
        private long id;

        Message(int type, String content, long id) {
            this.type = type;
            this.content = content;
            this.id = id;
        }

        //getters and setters
        public int getType() {
            return type;
        }

        public void setType(int type) {
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


            if (getItem(position).getType() == 1) {
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

