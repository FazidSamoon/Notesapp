package com.example.notesapp;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utilities {
    static void createToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    static CollectionReference getCollectionReference() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore .getInstance().collection("notes").document(currentUser.getUid()).collection("my notes");
    }

    static String timeStampToString(Timestamp timestamp){
        return new SimpleDateFormat("mm/dd/yy").format(timestamp.toDate());
    }
}
