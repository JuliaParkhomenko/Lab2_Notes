package ua.nure.parkhomenko.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DisplayNoteActivity extends AppCompatActivity {
    ImageView iv_notePicture, iv_importance;
    TextView tv_title, tv_description, tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);

        iv_notePicture = findViewById(R.id.iv_notePicture);
        iv_importance = findViewById(R.id.iv_importance);
        tv_date = findViewById(R.id.tv_date);
        tv_description = findViewById(R.id.tv_description);
        tv_title = findViewById(R.id.tv_title);

        Note note = getIntent().getParcelableExtra("note");
        //  OLD
        //  byte[] image = getIntent().getByteArrayExtra("IMAGE");

        if (note.getImagePath()!=null) {
            //iv_notePicture.setImageURI(note.getImageURI());

            //OLD
            /* note.setImage(image);
            iv_notePicture.setImageBitmap(note.getImageBitmap());
             */

            Bitmap myBitmap = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator + note.getImagePath()+".jpg"); // OR  https://stackoverflow.com/questions/4181774/show-image-view-from-file-path

            iv_notePicture.setImageBitmap(myBitmap);
        } else {
            iv_notePicture.setImageResource(R.drawable.note_default_img);
        }
        tv_title.setText(note.getTitle());
        tv_description.setText(note.getDescription());
        tv_date.setText(note.getDateTime());

        iv_importance.setImageResource(note.getImportanceImage(note.getImportance()));
    }
}