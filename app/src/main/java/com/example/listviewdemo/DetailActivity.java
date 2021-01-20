package com.example.listviewdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listviewdemo.global.Global;
import com.example.listviewdemo.model.Note;
import com.example.listviewdemo.repo.Repo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class DetailActivity extends AppCompatActivity {

    private Note currentNote;
    ImageView imv;
    StorageReference storageReference;
    FirebaseStorage storage;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView myDetailTextView = findViewById(R.id.myDetailTextView);
        currentNote = (Note) Global.map.get(Global.NOTE_KEY);
        myDetailTextView.setText(currentNote.getTitle());

        //Downloader billedet fra storage
        storageReference = FirebaseStorage.getInstance().getReference(currentNote.getId());
        imv = findViewById(R.id.image2);

        storageReference.getBytes(1024 * 1024).addOnSuccessListener(bytes -> {
            imageBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            //sætter billedet i viewet
            imv.setImageBitmap(imageBitmap);
        });
    }


    public void addUpdatedNote(View view) {
        TextView myDetailTextView = findViewById(R.id.myDetailTextView);
        EditText newDetailText = findViewById(R.id.newText);
        currentNote = (Note) Global.map.get(Global.NOTE_KEY);
        Editable newText = newDetailText.getText();
        myDetailTextView.setText(newText);
        System.out.println("Text has been updated to: " + newText);
        Repo.r().updateDBNote(currentNote, newText.toString()); // Kalder update + gemmer i Firebase

       //uploader til firestorage
        storage  = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child(currentNote.getId());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        storageReference.putBytes(baos.toByteArray()).addOnCompleteListener(snap -> {
            System.out.println("ok uplaodet billedet " + snap);
        }).addOnFailureListener(exception -> {
            System.out.println("failed to upload billedet" + exception);
        });
        finish();
    }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
           assert data != null;
           Bundle extras = data.getExtras();
           imageBitmap = (Bitmap) extras.get("data");

           imv = (ImageView) findViewById(R.id.image2);
           imv.setImageBitmap(imageBitmap);

       }
   }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void cameraBtnPressed(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}