package com.example.myfirebaseapplicationass3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterDoB, editTextRegisterMobile,
    editTextRegisterPwd, editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");
        Toast.makeText(RegisterActivity. this,  "You can register now", Toast.LENGTH_LONG).show();
        progressBar = findViewById(R.id.progressBar);
        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);



        //RadioButton for Gender
        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();
        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);
                        //Obtain the entered data

                String textFullName = editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDoB = editTextRegisterDoB.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textGender; //Can't obtain the value before verifying if any button was selected or not


                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(  RegisterActivity. this,  "Please enter your full name", Toast.LENGTH_LONG).show();
                    editTextRegisterFullName.setError("Full Name is required");
                    editTextRegisterFullName.requestFocus();
                } else if (TextUtils.isEmpty (textEmail)) {
                    Toast.makeText(  RegisterActivity. this,"Please enter your email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher (textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Valid email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty (textDoB)) {
                    Toast.makeText( RegisterActivity. this, "Please your date of birth", Toast.LENGTH_LONG).show();
                    editTextRegisterDoB.setError("Date of Birth is required");
                    editTextRegisterDoB.requestFocus();
                } else if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(  RegisterActivity. this,  "Please select your gender", Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("Gender is required");
                    radioButtonRegisterGenderSelected.requestFocus();
                } else if (TextUtils.isEmpty (textMobile)) {
                    Toast.makeText( RegisterActivity. this,  "Please enter your mobile no.", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile No. is required");
                    editTextRegisterMobile.requestFocus();
                } else if (textMobile.length() != 10) {
                    Toast.makeText( RegisterActivity.this, "Please re-enter your mobile no.", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile No. should be 10 digits");
                    editTextRegisterMobile.requestFocus();
                } else if (TextUtils.isEmpty (textPwd)) {
                    Toast.makeText(  RegisterActivity. this,  "Please enter your password", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password is required");
                    editTextRegisterPwd.requestFocus();
                } else if (textPwd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password too weak");
                    editTextRegisterPwd.requestFocus();
                } else {
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility (View.VISIBLE);
                    registerUser(textFullName, textEmail, textDoB, textGender, textMobile, textPwd);
                }

            }
        });



    }

    private void registerUser(String textFullName, String textEmail, String textDoB, String textGender, String textMobile, String textPwd) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            Toast.makeText(  RegisterActivity.this,  "User registered successfully", Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                                //Send Verification Email
                          /*  firebaseUser.sendEmailVerification();
                               //Open User Profile after successful registration
                            Intent intent = new Intent ( RegisterActivity. this, UserProfileActivity.class);
                              //To Prevent User from returning back to Register Activity on pressing back button after registration
                            intent. setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity (intent);
                            finish(); //to close Register Activity */
                        }
//                        else {
//                            try {
//                                throw task.getException();
//                            } catch (FirebaseAuthWeakPasswordException e) {
//                                editTextRegisterPwd.setError("Your password is too weak. Kindly use a mix of alphabets, numbers");
//                            }
//                        }







                    }
                });

    }
}

