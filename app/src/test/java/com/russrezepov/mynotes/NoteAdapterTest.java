package com.russrezepov.mynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.russrezepov.mynotes.model.Note;

public class NoteAdapterTest {

    public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder> {


        public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull NoteHolder holder, int i, @NonNull Note note) {
            holder.noteTitle.setText(note.getTitle());
            holder.noteContent.setText(note.getContent());
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