package com.russrezepov.mynotes.note;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.russrezepov.mynotes.R;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TheNoteDetails extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_note_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Getting data from anotehr activity - When someone clicks on the RecyclerView Note
        final Intent data = getIntent();

        TextView title = findViewById(R.id.noteDetailsTitle);
        TextView content = findViewById(R.id.noteDetailsContent);
        //Adding Scrolling feature to the content view
        content.setMovementMethod(new ScrollingMovementMethod());

        //getting content from another activity and settign it to noteDetailsContent textview
        content.setText(data.getStringExtra("content"));
        //getting content from another activity and settign it to noteDetailsContent textview
        title.setText(data.getStringExtra("title"));
        content.setBackgroundColor(getResources().getColor(data.getIntExtra("color",0),null)); //Setting the same color of a note when clicking on it

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditNote.class);
                i.putExtra("title",data.getStringExtra("title"));
                i.putExtra("content",data.getStringExtra("content"));
                i.putExtra("noteId",data.getStringExtra("noteId"));
                startActivity(i);
            }
        });
    }

    //Sending the user back where the user came from.
    // Implementing optionsItemSelect instead of including the Parent Activity in the  Manifest file in our case


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
