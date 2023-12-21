package com.example.moviefilmapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.moviefilmapp.Activities.LoginActivity;
import com.example.moviefilmapp.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private TextView textViewEmail;
    private boolean settingFragmentVisible = false;
    public AccountFragment() {
        // Required empty public constructor
    }


    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        textViewEmail =view.findViewById(R.id.textViewEmail);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null){
            String userEmail = currentUser.getEmail();
            textViewEmail.setText(userEmail);

        }

        Button logoutButton =view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ConstraintLayout settingBtn = view.findViewById(R.id.setting);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSettingFragment();
            }
        });

        return view;
    }

    private void toggleSettingFragment() {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (!settingFragmentVisible) { 
            // Nếu YourNewFragment không hiển thị, thêm nó vào
            SettingFragment settingFragment = new SettingFragment();
            fragmentTransaction.replace(R.id.fragmentContainer, settingFragment);
            settingFragmentVisible = true;
        } else {
            // Nếu YourNewFragment đang hiển thị, ẩn nó đi
            Fragment yourNewFragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
            if (yourNewFragment != null) {
                fragmentTransaction.remove(yourNewFragment);
                settingFragmentVisible = false;
            }
        }

        fragmentTransaction.commit();
    }
}