package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class DetailsFragment extends Fragment {

    static final String MESSAGE_CONTENT = "MESSAGE_CONTENT";
    static final String MESSAGE_TYPE = "MESSAGE_TYPE";
    static final String MESSAGE_ID = "MESSAGE_ID";
    private Bundle dataFromActivity;
    private AppCompatActivity parentActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        String message = dataFromActivity.getString(MESSAGE_CONTENT);
        int type = dataFromActivity.getInt(MESSAGE_TYPE);
        long id = dataFromActivity.getLong(MESSAGE_ID);

        View newView = inflater.inflate(R.layout.fragment_details, container, false);

        TextView messageContent = newView.findViewById(R.id.messageContent);
        messageContent.setText(message);
        TextView messageID = newView.findViewById(R.id.messageID);
        messageID.setText(String.valueOf(id));
        CheckBox isSendCheck = newView.findViewById(R.id.isSendCheck);
        if (type==0) {
            isSendCheck.setChecked(true);
        } else{
            isSendCheck.setChecked(false);
        }

        Button hideButton = (Button)newView.findViewById(R.id.hideButton);
        hideButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return newView;
    } //end onCreateView()


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    } //end onAttach()

} //end class DetailsFragment