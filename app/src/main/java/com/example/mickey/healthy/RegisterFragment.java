package com.example.mickey.healthy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment {
    FirebaseAuth fbAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        fbAuth = FirebaseAuth.getInstance();
        Button registerButton = getView().findViewById(R.id.register_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = getView().findViewById(R.id.register_email);
                EditText password = getView().findViewById(R.id.register_password);
                EditText confirmPassword = getView().findViewById(R.id.register_confirm_password);
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                String confirmPasswordStr = confirmPassword.getText().toString();

                if (usernameStr.isEmpty() || passwordStr.isEmpty() || confirmPasswordStr.isEmpty())
                {
                    Toast.makeText(getContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                    Log.d("register", "some field is empty");
                }
                else if (passwordStr.length() < 6)
                {
                    Toast.makeText(getContext(), "password ต้องยาวอย่างน้อย 6 ตัวอักษร", Toast.LENGTH_SHORT).show();
                    Log.d("register", "password length too short");
                }
                else if (!passwordStr.equals(confirmPasswordStr))
                {
                    Toast.makeText(getContext(), "confirm password ผิด", Toast.LENGTH_SHORT).show();
                    Log.d("register", "confirm password not equal to password");
                }
                else if (usernameStr.equals("admin"))
                {
                    Toast.makeText(getContext(), "user นี้มีอยู่ในระบบแล้ว", Toast.LENGTH_SHORT).show();
                    Log.d("register", "username already existed");
                }
                else
                {
                    fbAuth.createUserWithEmailAndPassword(usernameStr, passwordStr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            sendVerifyEmail(authResult.getUser());
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_view, new LoginFragment())
                                    .commit();
                            Log.d("register", "register success");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("register", "register failed.Error :" + e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void sendVerifyEmail(FirebaseUser user)
    {
        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("register", "send verify email success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("register", "send verify email failed : " + e.getMessage());
            }
        });
    }
}
