package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText title, content;
    ImageButton save;
    TextView titleHeading, deleteNoteButton;
    String titleIntent, docIdIntent, contentIntent;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        //accessing the fields in the xml file
        title = findViewById(R.id.note_title);
        content = findViewById(R.id.note_content);
        save = findViewById(R.id.saveNoteButton);
        titleHeading = findViewById(R.id.title);
        deleteNoteButton = findViewById(R.id.delete_note_button);

        //receive data from intent
        titleIntent = getIntent().getStringExtra("title");
        docIdIntent = getIntent().getStringExtra("docId");
        contentIntent = getIntent().getStringExtra("content");

        //if edit function set content data into relevant fields
        if (docIdIntent != null && !docIdIntent.isEmpty()) {
            isEditMode = true;
        }

        title.setText(titleIntent);
        content.setText(contentIntent);

        if (isEditMode) {
            titleHeading.setText("Edit note");
            deleteNoteButton.setVisibility(View.VISIBLE);
        }




        //save notes if save button clicked
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteTitle = title.getText().toString();
                String noteContent = content.getText().toString();

                if (noteTitle.isEmpty()) {
                    title.setError("Title is required");
                    return;
                }


                //save details in a note object
                Note note = new Note();
                note.setTitle(noteTitle);
                note.setContent(noteContent);
                note.setTimestamp(Timestamp.now());

                saveNoteToFirebase(note);
            }
        });


        //delete notes if delete note button clicked
        deleteNoteButton.setOnClickListener((v) -> deleteNoteFromFirebase());
    }

    //save notes in firebase
    public void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;

        //if edit mode is true get the specific document id and edit
        //otherwise create a new document
        if (isEditMode) {
            //update the note
            documentReference = Utilities.getCollectionReference().document(docIdIntent);
        } else {
            //create note
            documentReference = Utilities.getCollectionReference().document();
        }


        //display a toast message based on the success status of the note save
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utilities.createToast(getApplicationContext(), "Note added successfully");
                    finish();
                } else {
                    Utilities.createToast(getApplicationContext(), "Failed");
                }
            }
        });
    }


    //delete note function
    public void deleteNoteFromFirebase() {
        DocumentReference documentReference;
        documentReference = Utilities.getCollectionReference().document(docIdIntent);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utilities.createToast(getApplicationContext(), "Note deleted successfully");
                    finish();
                } else {
                    Utilities.createToast(getApplicationContext(), "Failed while deleting");
                }
            }
        });
    }
}