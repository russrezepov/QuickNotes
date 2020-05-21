package com.russrezepov.mynotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.russrezepov.mynotes.model.Adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView nav_view;
    RecyclerView noteLists;

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference collectionNotes = fStore.collection("notes");
    private NoteAdapter adapter;
    FirestoreRecyclerAdapter<FireNote,NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Query query = collectionNotes; //fStore.collection("notes").orderBy("title",Query.Direction.DESCENDING);
        //executing the query
        FirestoreRecyclerOptions<FireNote> allNotes;
        allNotes = new FirestoreRecyclerOptions.Builder<FireNote>()
                .setQuery(query,FireNote.class)
                .build();


        noteAdapter = new FirestoreRecyclerAdapter<FireNote, NoteViewHolder>(allNotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull final FireNote fireNote) {

                //Binding data from MainActivity, when Adapter object is created, to this View that we have here
                noteViewHolder.noteTitle.setText(fireNote.getTitle());
                noteViewHolder.noteContent.setText(fireNote.getContent());
                final int colorCodes = getRandomColor();
                noteViewHolder.mCardView.setBackgroundColor(noteViewHolder.view.getResources().getColor(colorCodes,null));

                noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //getting current context where we are starting the Activity and passing them to another activity
                        Intent i = new Intent(v.getContext(),TheNoteDetails.class);

                        //When someone clicks on the first item in the RecyclerView it is going to get the position as Zero -> Position
                        //passing the title and description to the NoteDetails
                        i.putExtra("title", fireNote.getTitle());
                        i.putExtra("content", fireNote.getContent());
                        i.putExtra("color", colorCodes);
                        v.getContext().startActivity(i); //Not passing anything yet. Just getting current context and passing current context

                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent, false);
                return new NoteViewHolder(view);
            }
        };



        noteLists = findViewById(R.id.notelist);
        drawerLayout = findViewById(R.id.drawer);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        //Staggered layout expands based on Context Size
        noteLists.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        noteLists.setAdapter(noteAdapter);
        //noteAdapter.startListening();

        FloatingActionButton fab = findViewById(R.id.addNoteFloat);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddNote.class));

            }

        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNote:
                startActivity(new Intent(this, AddNote.class));
                break;

            default:
                Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Toast.makeText(this, "Settings Menu is Clicked!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContent;
        View view;
        CardView mCardView;

        public NoteViewHolder(@NonNull final View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.titles);
            noteContent = itemView.findViewById(R.id.content);
            mCardView = itemView.findViewById(R.id.noteCard);
            view = itemView; //Handles clicks on Recycle View items. Clicks are redirected to the inside of a Note
        }
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.blue);
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.skyBlue);
        colorCode.add(R.color.lightPurple);
        colorCode.add(R.color.lightGreen);
        colorCode.add(R.color.greenlight);
        colorCode.add(R.color.gray);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.red);
        colorCode.add(R.color.notgreen);

        Random randomColor = new Random();
        int numberColor = randomColor.nextInt(colorCode.size());
        return colorCode.get(numberColor);
    }

    //Listening for Data Changes in Firestore


    @Override
    protected void onStart() {
        super.onStart();
    noteAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }

    public class NoteAdapter extends FirestoreRecyclerAdapter<FireNote, NoteAdapter.NoteHolder> {


        public NoteAdapter(@NonNull FirestoreRecyclerOptions<FireNote> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull NoteHolder holder, int i, @NonNull FireNote fireNote) {
            holder.noteTitle.setText(fireNote.getTitle());
            holder.noteContent.setText(fireNote.getContent());
        }


        @NonNull
        @Override
        public NoteAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent,false);
            return new NoteHolder(v);
        }


        public class NoteHolder extends RecyclerView.ViewHolder {
            TextView noteTitle, noteContent;
            View view;
            CardView mCardView;

            public NoteHolder(@NonNull View itemView) {
                super(itemView);
                noteTitle = itemView.findViewById(R.id.titles);
                noteContent = itemView.findViewById(R.id.content);
                mCardView = itemView.findViewById(R.id.noteCard);
                view = itemView; //Handles clicks on Recycle View items. Clicks are redirected to the inside of a Note
            }
        }
    }

}
