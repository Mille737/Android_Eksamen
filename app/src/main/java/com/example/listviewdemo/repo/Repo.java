package com.example.listviewdemo.repo;

import com.example.listviewdemo.Updatable;
import com.example.listviewdemo.model.Note;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repo {

    private static final Repo repo = new Repo(); // Kan kun køre én gang
    private Updatable activity;
    private final FirebaseFirestore fireDB = FirebaseFirestore.getInstance();
    private static final String NOTES = "notes";
    private static final String TITLE = "title";
    private final List<Note> noteList = new ArrayList<>(); // Gemmer Note objekter. Kan opdateres

    public static Repo r(){
        return repo;
    }

    public void setActivity(Updatable a){  // kaldes fra aktivitet, som skal blive opdateret
        activity = a;
        startListener();
    }

    public void addNote(Note note) {
        DocumentReference ref = fireDB.collection(NOTES).document(note.getId()); // Opretter nyt dokument i Firebase, hvor vi selv angiver document id.
        Map<String, String> map = new HashMap<>();
        map.put(TITLE, note.getTitle()); // tilføj selv flere key-value par efter behov
        ref.set(map).addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                System.out.println("Error i gem: " + task.getException());
            }
        }); // gemmer hele map i aktuelt dokument
    }

    public void startListener() {
        fireDB.collection(NOTES).addSnapshotListener((values, error) -> { // Values indeholder ALLE ting fra firebase så du kan kalde de forskellige til som .getDocuments
            noteList.clear();

            for(DocumentSnapshot snap : values.getDocuments()){
                System.out.println("Test1");
                Note note = new Note(snap.get(TITLE).toString(), snap.getId());
                noteList.add(note);
            }
            activity.update(null); // kaldes efter vi har hentet data fra Firebase
        });

    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void updateDBNote(Note note, String newText) {

        fireDB.collection(NOTES).document(note.getId()).update(TITLE, newText);
    }

}
