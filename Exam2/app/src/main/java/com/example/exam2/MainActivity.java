package com.example.exam2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exam2.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final long BACK_PRESS_INTERVAL_MS = 3000;

    private ActivityMainBinding binding;
    private long mLastBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Email TextWatcher
        binding.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Password TextWatcher
        binding.editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Navigate to ModeActivity on Login click
        binding.buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ModeActivity.class);
            startActivity(intent);
        });

        // Custom double-back-press exit logic with Snackbar
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                long currentTime = System.currentTimeMillis();
                if (currentTime - mLastBackPressedTime < BACK_PRESS_INTERVAL_MS) {
                    finishAffinity();
                } else {
                    mLastBackPressedTime = currentTime;
                    Snackbar.make(
                            binding.getRoot(),
                            R.string.press_back_again_to_exit,
                            Snackbar.LENGTH_LONG
                    ).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLastBackPressedTime = 0;
    }

    private void validateInputs() {
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString();

        boolean isEmailValid = !TextUtils.isEmpty(email)
                && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean isPasswordValid = password.length() >= 6;

        if (!TextUtils.isEmpty(email) && !isEmailValid) {
            binding.editTextEmail.setError(getString(R.string.error_invalid_email));
        } else {
            binding.editTextEmail.setError(null);
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid) {
            binding.editTextPassword.setError(getString(R.string.error_short_password));
        } else {
            binding.editTextPassword.setError(null);
        }

        binding.buttonLogin.setEnabled(isEmailValid && isPasswordValid);
    }
}
