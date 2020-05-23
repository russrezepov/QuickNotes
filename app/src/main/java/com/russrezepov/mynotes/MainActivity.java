package com.russrezepov.mynotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.russrezepov.mynotes.model.Adapter;
import com.russrezepov.mynotes.model.Note;
import com.russrezepov.mynotes.model.NoteAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView nav_view;
    RecyclerView noteList;
    FloatingActionButton fab;

    private FirebaseFirestore fStore;
    //private Adapter adapter;
    //private NoteAdapter noteAdapter;
    final static String TAG = "Firebase Not Reading";
    final static String TAGF = "Firebase CONNECTED";


    FirestoreRecyclerAdapter<Note,NoteViewHolder> noteAdapter;

    //>>>ON CREATE <<<
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i(TAGF,"onCreate");

        drawerLayout = findViewById(R.id.drawer);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        noteList = findViewById(R.id.noteList);
        fab = findViewById(R.id.addNoteFloat);

        setUpRecyclerView();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddNote.class));

            }

        });

    }

    private void setUpRecyclerView() {

        fStore = FirebaseFirestore.getInstance();
        Query query = fStore.collection("notes").orderBy("title",Query.Direction.DESCENDING);
        //executing the query
        FirestoreRecyclerOptions<Note> allNotes;
        allNotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

//        fStore.collection("notes")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            Log.i(TAGF,"Firebase Connected Successfully");
//                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//                                //titles.add(document.getString("title"));
//                                //content.add(document.getString("content"));
//                                Log.i(TAGF,document.getString("title"));
//                                Log.i(TAGF,document.getString("content"));
//                                Log.i(TAGF,"Firebase Connected Successfully");
//                            }
//                        } else {
//                            Log.w(TAG,"Error getting documents.", task.getException());
//                        }
//                    }
//                });

        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder> (allNotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull final Note note) {

                //Binding data from MainActivity, when Adapter object is created, to this View that we have here
                noteViewHolder.noteTitle.setText(note.getTitle());
                noteViewHolder.noteContent.setText(note.getContent());
                final int colorCodes = getRandomColor();
                noteViewHolder.mCardView.setBackgroundColor(noteViewHolder.view.getResources().getColor(colorCodes,null));

                noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //getting current context where we are starting the Activity and passing them to another activity
                        Intent i = new Intent(v.getContext(),TheNoteDetails.class);

                        //When someone clicks on the first item in the RecyclerView it is going to get the position as Zero -> Position
                        //passing the title and description to the NoteDetails
                        i.putExtra("title", note.getTitle());
                        i.putExtra("content", note.getContent());
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

        //Staggered layout expands based on Context Size
        noteList.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        noteList.setAdapter(noteAdapter);
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

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
            noteAdapter.stopListening();

    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContent;
        View view;
        CardView mCardView;

        private NoteViewHolder(@NonNull final View itemView) {
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


}
