package com.example.whatsapp_clone_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    
    
    EditText usernameInput;
    EditText passwordInput;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
    
        // Initializing Views
        usernameInput = findViewById(R.id.usernameEditText);
        passwordInput = findViewById(R.id.passwordEditText);
        
        immediatelyRedirect();
    }
    
    public void mLoginButton(View view) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
    
        PreventButtonUse(true);
    
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    immediatelyRedirect();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                    passwordInput.setText("");
                }
            
                PreventButtonUse(false);
            }
        });
    }
    
    public void mSignupButton(View view) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
    
        ParseUser newUser = new ParseUser();
    
        newUser.setUsername(username);
        newUser.setPassword(password);
    
        if (CheckForCorrectInput(username, password)) {
        
            PreventButtonUse(true);
        
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                    
                        Toast.makeText(getApplicationContext(), "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                    
                        immediatelyRedirect();
                    
                    } else {
                        // Tell User the Error! -- We might not want to do that
                        // Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_SHORT).show();
                    }
                
                    PreventButtonUse(false);
                }
            });
        } else {
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show();
        }
    }
    
    public void immediatelyRedirect() {
        if (ParseUser.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
            startActivity(intent);
            finish();
        }
    }
    
    public void PreventButtonUse(boolean state) {
        findViewById(R.id.Login).setEnabled(!state);
        findViewById(R.id.Signup).setEnabled(!state);
    }
    
    public boolean CheckForCorrectInput(String u, String p) {
        
        // we are not trimming password : to allow spaces in it
        if (u.trim().length() == 0 || p.equals("")) {
            return false;
        }
        
        return true;
    }
}
