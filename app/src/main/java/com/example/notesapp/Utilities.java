package com.example.notesapp;

import android.content.Context;
import android.widget.Toast;

public class Utilities {
    static void createToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
