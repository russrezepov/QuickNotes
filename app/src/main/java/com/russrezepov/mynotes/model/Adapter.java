package com.russrezepov.mynotes.model;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.russrezepov.mynotes.R;
import com.russrezepov.mynotes.TheNoteDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<String> titles;
    private List<String> content;

    public Adapter(List<String> title, List<String> content) {
        this.titles = title;
        this.content = content;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Binding data from MainActivity, when Adapter object is created, to this View that we have here
        holder.noteTitle.setText(titles.get(position));
        holder.noteContent.setText(content.get(position));
        final int colorCodes = getRandomColor();
        holder.mCardView.setBackgroundColor(holder.view.getResources().getColor(colorCodes,null));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting current context where we are starting the Activity and passing them to another activity
                Intent i = new Intent(v.getContext(),TheNoteDetails.class);

                //When someone clicks on the first item in the RecyclerView it is going to get the position as Zero -> Position
                //passing the title and description to the NoteDetails
                i.putExtra("title", titles.get(position));
                i.putExtra("content", content.get(position));
                i.putExtra("color", colorCodes);
                v.getContext().startActivity(i); //Not passing anything yet. Just getting current context and passing current context

            }
        });

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

    @Override
    public int getItemCount() {
        return titles.size(); //returning total amount of notes saved
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContent;
        View view;
        CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.titles);
            noteContent = itemView.findViewById(R.id.content);
            mCardView = itemView.findViewById(R.id.noteCard);
            view = itemView; //Handles clicks on Recycle View items. Clicks are redirected to the inside of a Note
        }
    }
}
