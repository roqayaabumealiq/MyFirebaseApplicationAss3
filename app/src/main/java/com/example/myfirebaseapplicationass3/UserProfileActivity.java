package com.example.myfirebaseapplicationass3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome ,textViewFullName ,textViewEmail, textViewDoB, textViewGender, textViewMobile;
    private ProgressBar progressBar;
    private String fullName, email, doB, gender, mobile;

    private ImageView imageView;
    private FirebaseAuth authProfile;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("Home");
        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDoB = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(UserProfileActivity.this, "Something went wrong! User's details are not available at the momer" ,Toast.LENGTH_LONG).show();
        } else {
        progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }
    private void showUserProfile (FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userDetails:snapshot.getChildren()){
                    ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                    if (readUserDetails != null) {
                        fullName = firebaseUser.getDisplayName();
                        email = firebaseUser.getEmail();
                        doB = readUserDetails.doB;
                        gender = readUserDetails.gender;
                        mobile = readUserDetails.mobile;

                        textViewWelcome.setText("Welcome, " + fullName + "!");
                        textViewFullName.setText (fullName);
                        textViewEmail.setText (email);
                        textViewDoB.setText(doB);
                        textViewGender.setText (gender);
                        textViewMobile.setText (mobile);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(  UserProfileActivity.this, "Something went wrong!" ,Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id == R.id.menu_update_profile) {
            Intent intent = new Intent( UserProfileActivity.this, UpdateProfileActivity.class);
            startActivity (intent);
        }
     else if (id == R.id.menu_logout) {
        authProfile.signOut();
        Toast.makeText( UserProfileActivity. this,  "Logged Out", Toast.LENGTH_LONG).show();
        Intent intent = new Intent (UserProfileActivity. this, MainActivity.class);
        //Clear stack to prevent user coming back to UserProfileActivity on pressing back button after Logging
        intent. setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity (intent);
        finish(); //Close UserProfileActivity
    } else {
        Toast.makeText(UserProfileActivity. this, "Something went wrong!", Toast.LENGTH_LONG).show();
    }

        return super.onOptionsItemSelected(item);
    }
}