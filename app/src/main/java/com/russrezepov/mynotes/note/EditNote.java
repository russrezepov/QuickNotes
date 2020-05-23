package com.russrezepov.mynotes.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.russrezepov.mynotes.MainActivity;
import com.russrezepov.mynotes.R;

import java.util.HashMap;
import java.util.Map;

public class EditNote extends AppCompatActivity {

    Intent data;
    EditText editNoteTtile, editNoteContent;
    FirebaseFirestore fStore;
    ProgressBar progressBarEditNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar editNoteToolbar = findViewById(R.id.editNoteToolbar);
        setSupportActionBar(editNoteToolbar);

        fStore = fStore.getInstance();

        editNoteContent = findViewById(R.id.editNoteContent);
        editNoteTtile = findViewById(R.id.editNoteTitle);
        progressBarEditNote = findViewById(R.id.progressBarEditNote);

        data = getIntent();
        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");

        editNoteTtile.setText(noteTitle);
        editNoteContent.setText(noteContent);

        FloatingActionButton fab = findViewById(R.id.saveEditNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Extracting title and content from FireBase Database
                String nTitle = editNoteTtile.getText().toString();
                String nContent = editNoteContent.getText().toString();

                if (nTitle.isEmpty() || nContent.isEmpty()) {
                    Toast.makeText(EditNote.this, "Notes or Title cannot be Empty", Toast.LENGTH_SHORT).show();
                    return; //returnign to the same activity
                }

                progressBarEditNote.setVisibility(View.VISIBLE);

                //Saving a new note to the Firebase DB
                //Notes is our collection will have multiple number of notes
                //Each note will have its own field - Author, Title, Content, Time Created etc
                //docref is pointing to a blank document that currently has no data

                DocumentReference docref = fStore.collection("notes").document(data.getStringExtra("noteId"));
                //now we can isnert the data inside of this docref document
                Map<String,Object> note = new HashMap<>();
                note.put("title",nTitle);
                note.put("content",nContent);

                docref.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditNote.this, "Note Updated...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditNote.this, "Error, Try Again...", Toast.LENGTH_SHORT).show();
                        progressBarEditNote.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}
