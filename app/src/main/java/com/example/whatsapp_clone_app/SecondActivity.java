package com.example.whatsapp_clone_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    
    ArrayList<String>users = new ArrayList<String>();
    ArrayAdapter arrad;
    ListView levi;
    
    /** instead of a scroll view to get a scrolling list  *
     *  you must use linearLayout                         */
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        
        arrad = new ArrayAdapter(this, R.layout.white_text_view, users);
    
        levi = findViewById(R.id.levi);
        levi.setAdapter(arrad);
        
        mQueryForParseUsers();
    }
    
    private void mQueryForParseUsers() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseUser user : objects) {
                            users.add(user.getUsername());
                        }
                        
                        levi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                                i.putExtra("username", users.get(position));
                                startActivity(i);
                            }
                        });
                        arrad.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    
    public void mLogoutFromTalk(View view) {
        ParseUser.logOut();
        
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
