package com.example.listviewdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.listviewdemo.global.Global;
import com.example.listviewdemo.model.Note;
import com.example.listviewdemo.repo.Repo;
import java.io.InputStream;

public class CustomListActivity extends AppCompatActivity implements Updatable{

    private MyAdapter myAdapter;
    ImageView myImageView10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        myAdapter = new MyAdapter(this, Repo.r().getNoteList());
        ListView listView = findViewById(R.id.myListView2);

        listView.setAdapter(myAdapter);
        // Printer i consollen
        listView.setOnItemClickListener((_listView, linearLayout, adapterPos, arrPos) -> {
            System.out.println("Klik på række " + arrPos);
            // Intent er en hjælper fra Androids side
            Intent intent = new Intent(this, DetailActivity.class);
            Global.map.put(Global.NOTE_KEY, Repo.r().getNoteList().get((int)arrPos));
             // gemmer text i untent objektet og kan hentes på et andet view
            startActivity(intent);
        });

        Repo.r().setActivity(this);
    }

    public void addNote(View view){
        Note note = new Note("Ny note");
        Repo.r().addNote(note); // Opretter ny Nye + gemmer i Firebase
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void backFromCamera(@Nullable Intent data) {
        try {
            Bitmap currentBitmap = (Bitmap)data.getExtras().get("data");
            myImageView10.setImageBitmap(currentBitmap);
            // skaf en id til dit billede. F.eks. id fra noten
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Object o) {
        System.out.println("Update() kaldet!!!");
        // kald på adapters notidyDatasetChange()
        runOnUiThread(() -> myAdapter.notifyDataSetChanged());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) { // gallery
            backFromGallery(data);
        }
        if(requestCode == 2) { // camera
            backFromCamera(data);
        }
    }

    private void backFromGallery(@Nullable Intent data) {
        Uri uri = data.getData();
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            Bitmap currentBitmap = BitmapFactory.decodeStream(is);
            myImageView10.setImageBitmap(currentBitmap);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void galleryBtnPressed(View view){
        Intent intent = new Intent(Intent.ACTION_PICK); // make an implicit intent, which will allow
        // the user to choose among different services to accomplish this task.
        intent.setType("image/*"); // we need to set the type of content to pick
        startActivityForResult(intent, 1); // start the activity, and in this case
        // expect an answer
    }
}