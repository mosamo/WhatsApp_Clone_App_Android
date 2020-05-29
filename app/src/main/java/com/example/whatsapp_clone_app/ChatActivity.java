package com.example.whatsapp_clone_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    
    String mChattingWithUsername;
    
    /**** Very Very Very Important:
     *  To give a view "as much space as it needs" without consuming the space of other views inside a linear layout.
     *  you give that view weight and leave the other ones weight empty:
     *  see chatMessageEditText & sendChatButton
     *** */
    
    /* TODO:
            1. You may want to make chat messages automatically update
              for example Every 5 seconds they are queried in ChatActivity,
              But i don't want to put extra pressure on the server
            .
            2. Make Login and Account Creation case insensitive
     */

    ListView levi;
    ArrayAdapter arrad;
    ArrayList<String> messages = new ArrayList<String>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    
        Intent i = getIntent();
        mChattingWithUsername = i.getStringExtra("username");
        
        TextView otherParty = findViewById(R.id.whoChatting);
                 otherParty.setText("Chatting with " + mChattingWithUsername + ":");
                 
        levi = findViewById(R.id.chatListView);
        arrad = new ArrayAdapter(this, R.layout.white_text_view_alt, messages);
        levi.setAdapter(arrad);
        
        mQueryChats();
    }
    
    public void mSendChat(View view) {
        final EditText chatEditText = findViewById(R.id.chatMessageEditText);
        
        if (!chatEditText.getText().toString().trim().equals("")) {
            
            ParseObject message = new ParseObject("ChatAppMessage");
            message.put("sender", ParseUser.getCurrentUser().getUsername());
            message.put("recipient", mChattingWithUsername);
            message.put("message", chatEditText.getText().toString().trim());
    
            findViewById(R.id.sendChatButton).setEnabled(false);
            chatEditText.setEnabled(false);
    
            message.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_SHORT).show();
                        messages.add(ParseUser.getCurrentUser().getUsername() + ": " + chatEditText.getText().toString());
                        arrad.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to send Message", Toast.LENGTH_SHORT).show();
                    }
                    findViewById(R.id.sendChatButton).setEnabled(true);
                    chatEditText.setEnabled(true);
                    chatEditText.setText("");
                }
            });
        }
    }
    
    public void mQueryChats() {
        /** we want two queries, one where the current user is the sender
         *  and one where the current user is the recipient
         *  and then we combine them into one query(?)
         */
        
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("ChatAppMessage");
        query1.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo("recipient", mChattingWithUsername);
    
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("ChatAppMessage");
        query2.whereEqualTo("sender", mChattingWithUsername);
        query2.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());
    
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);
        
        /** ParseQuery.or() checks if either is true (1 OR 2) */
        ParseQuery<ParseObject> MainQuery = ParseQuery.or(queries);
        MainQuery.orderByAscending("createdAt");
        MainQuery.setLimit(10);
        MainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        
                        messages.clear();
                        
                        for (ParseObject o : objects) {
                            messages.add(o.getString("sender") + ": " + o.getString("message"));
                        }
                        
                        arrad.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
