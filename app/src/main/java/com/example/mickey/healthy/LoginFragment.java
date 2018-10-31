package com.example.mickey.healthy;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LoginFragment extends Fragment{

    FirebaseAuth fbAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        fbAuth = FirebaseAuth.getInstance();
        if (fbAuth.getCurrentUser() != null)
        {
            Log.d("login", "already logged in go to menu");
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view, new MenuFragment())
                    .commit();
        }
        if (getView() == null) return;
        Button loginButton = getView().findViewById(R.id.login_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = getView().findViewById(R.id.login_username);
                EditText password = getView().findViewById(R.id.login_password);
                final String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                if (usernameStr.isEmpty() || passwordStr.isEmpty())
                {
                    Toast.makeText(getContext(), "กรุณาใส่ user และ password", Toast.LENGTH_SHORT)
                            .show();
                    Log.d("login", "username or password is empty");
                }
                else
                {
                    fbAuth.signInWithEmailAndPassword(usernameStr, passwordStr)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                            {
                                @Override
                                public void onSuccess(AuthResult authResult)
                                {
                                    if (fbAuth.getCurrentUser().isEmailVerified())
                                    {
                                        getActivity().getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.main_view, new MenuFragment())
                                                .commit();
                                        Log.d("login", "login complete go to menu");
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), "email ยังไม่ verify", Toast.LENGTH_SHORT)
                                                .show();
                                        Log.d("login", "email not verify");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(getContext(), "login failed ", Toast.LENGTH_SHORT);
                            Log.d("login", "Error + " + usernameStr + " : " + e.getMessage());
                        }
                    });
                }
            }
        });

        TextView registerButton = getView().findViewById(R.id.login_register_button);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new RegisterFragment())
                        .commit();
                Log.d("login", "go to register");
            }
        });
    }
}
