package com.example.whatsapp_clone_app;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseACL;

public class ParseSetting extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                
                // change the following to your own server
                .applicationId(getResources().getString(R.string.app_id_parse))
                .clientKey(getResources().getString(R.string.client_key_parse))
                .server(getResources().getString(R.string.server_parse))
                .build()
        
        );
        
        //ParseUser.enableAutomaticUser(); // Allows Guests (Auto Creation?)
        // anon user =/= auto user
        
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
