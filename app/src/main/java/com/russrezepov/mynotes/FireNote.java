package com.russrezepov.mynotes;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
@IgnoreExtraProperties
public class FireNote {
    public static String title;
    public static String content;

    public FireNote() {}

    public FireNote(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        FireNote.title = title;
    }

    public static String getContent() {
        return content;
    }

    public void setContent(String content) {
        FireNote.content = content;
    }
}

