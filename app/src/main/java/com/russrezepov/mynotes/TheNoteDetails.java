package com.russrezepov.mynotes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

        Intent data = getIntent(); //Getting data from anotehr activity - When someone clicks on the RecyclerView Note

        TextView title = findViewById(R.id.noteDetailsTitle);
        TextView content = findViewById(R.id.noteDetailsContent);
        //Adding Scrolling feature to the content view
        content.setMovementMethod(new ScrollingMovementMethod());

        content.setText(data.getStringExtra("content")); //getting content from another activity and settign it to noteDetailsContent textview
        content.setBackgroundColor(getResources().getColor(data.getIntExtra("color",0),null)); //Setting the same color of a note when clicking on it
        title.setText(data.getStringExtra("title")); //getting content from another activity and settign it to noteDetailsContent textview

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
