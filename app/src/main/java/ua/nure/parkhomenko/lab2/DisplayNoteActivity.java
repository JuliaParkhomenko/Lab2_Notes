package ua.nure.parkhomenko.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;

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

        if (note.getImagePath()!=null) {
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    // Завершается работа activity. (например, при повороте экрана или при многоконном режиме),
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    //В методе onStop следует особождать используемые ресурсы, которые не нужны пользователю, когда
    // он не взаимодействует с activity. Здесь также можно сохранять данные, например, в БД. При
    // этом во время состояния Stopped activity остается в памяти устройства, сохраняется состояние
    // всех элементов интерфейса.
    @Override
    protected void onStop(){
        super.onStop();
    }

    // В этом методе можно освобождать используемые ресурсы, приостанавливать процессы, например,
    // воспроизведение аудио, анимаций, останавливать работу камеры (если она используется) и т.д.,
    // чтобы они меньше сказывались на производительность системы.
    @Override
    protected void onPause(){
        super.onPause();
    }

    // activity отображается на экране устройства, и пользователь может с ней взаимодействовать.
    // И собственно activity остается в этом состоянии, пока она не потеряет фокус.
    @Override
    protected void onResume(){
        super.onResume();
    }

    // Если после вызова метода onStop пользователь решит вернуться к прежней activity, тогда
    // система вызовет метод onRestart.
    @Override
    protected void onRestart(){
        super.onRestart();
    }
}