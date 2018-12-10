package com.example.mickey.healthy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.INTERNET}, 0);
        }
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 0);
        }

        ImageView testPicture = getView().findViewById(R.id.login_test_picture);
        Glide.with(getContext()).load("https://66.media.tumblr.com/a707f9ecab5f6dc186f260899ad28ee7/tumblr_ogra32BDou1vjoybyo3_250.png")
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("test", "load error : " + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.e("test", "load complete");
                        return false;
                    }
                })
                .into(testPicture);

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
