package ua.nure.parkhomenko.lab2;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditNoteActivity extends AppCompatActivity {

    Note note;
    Intent outputIntent = new Intent();
    Intent inputIntent;

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int result = activityResult.getResultCode();
                            Intent data = activityResult.getData();

                            if (result == RESULT_OK && data != null) {
                                Uri selectedImage = data.getData();
                                ImageView iv_photo = findViewById(R.id.iv_photo);
                                iv_photo.setImageURI(selectedImage);
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // получаем Intent, который вызывал это Activity
        inputIntent = getIntent();
        // читаем из него action
        String action = inputIntent.getAction();
        String index_position = null;

        // в зависимости от action заполняем переменные
        if (action.equals("android.intent.action.EDIT")) {
            note = inputIntent.getParcelableExtra("noteForEdit");
            Bundle extras = inputIntent.getExtras();
            if (extras != null) {
                index_position = inputIntent.getStringExtra("INDEX_POSITION");
            }

            TextInputEditText et_title = findViewById(R.id.et_title);
            TextInputEditText et_description = findViewById(R.id.et_description);
            ImageView iv_image = findViewById(R.id.iv_photo);

            if (note.getImagePath()!=null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                File bitmapFile = new File(Environment.getExternalStorageDirectory() + File.separator + note.getImagePath()+".jpg");
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(bitmapFile);
                    fileInputStream.read(bytes.toByteArray());
                    fileInputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap myBitmap = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator + note.getImagePath()+".jpg");
                iv_image.setImageBitmap(myBitmap);
            } else {
                iv_image.setImageResource(R.drawable.note_default_img);
            }
            Spinner spinner = findViewById(R.id.spinner);
            et_description.setText(note.getDescription());
            et_title.setText(note.getTitle());
            spinner.setSelection(note.getImportance());

            outputIntent.putExtra("action", "edit");
            outputIntent.putExtra("INDEX_POSITION", index_position);
        } else if (action.equals("android.intent.action.CREATE")) {
            ImageView img = (ImageView) findViewById(R.id.iv_photo);
            img.setImageResource(R.drawable.note_default_img);
            note = new Note("", "", 0, null);
            outputIntent.putExtra("action", "create");
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ImageView imageView = (ImageView)findViewById(R.id.iv_photo);
        if (!imageView.getDrawable().getConstantState().equals(getDrawable(R.drawable.note_default_img).getConstantState()) ) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(folder,"temp.jpg");
            try {
                if(!file.exists())
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
//Запись
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes.toByteArray());
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outState.putString("IMAGE", "temp.jpg");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.containsKey("IMAGE")){
            ImageView imageView = findViewById(R.id.iv_photo);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            File bitmapFile = new File(Environment.getExternalStorageDirectory() + File.separator + savedInstanceState.getString("IMAGE"));
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(bitmapFile);
                fileInputStream.read(bytes.toByteArray());
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap myBitmap = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator + savedInstanceState.getString("IMAGE"));
            imageView.setImageBitmap(myBitmap);

        }
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

    public void uploadImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }

    public void saveNote(View view) {
        TextInputEditText et_title = findViewById(R.id.et_title);
        TextInputEditText et_description = findViewById(R.id.et_description);
        // проверка на заполненность всех полей
        if (!TextUtils.isEmpty(et_description.getText()) && !TextUtils.isEmpty(et_title.getText())) {

            ImageView iv_photo = findViewById(R.id.iv_photo);
            if (!iv_photo.getDrawable().getConstantState().equals(getDrawable(R.drawable.note_default_img).getConstantState()) ) {

                Bitmap bitmap = ((BitmapDrawable) iv_photo.getDrawable()).getBitmap();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                //Задаете путь и имя
                String imagePath = et_title.getText().toString();

                /*ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        23);*/
                // getExternalStoragePublicDirectory() represents root of external storage, we are using DOWNLOADS
                // We can use following directories: MUSIC, PODCASTS, ALARMS, RINGTONES, NOTIFICATIONS, PICTURES, MOVIES
                File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                // Storing the data in file with name as noteTitle.jpg
                File file = new File(folder,imagePath+".jpg");
                try {
                    if(file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//Запись
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(bytes.toByteArray());
                    fileOutputStream.close();
                    note.setImagePath(imagePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Spinner spinner = findViewById(R.id.spinner);
            note.setImportance(spinner.getSelectedItemPosition());
            note.setDescription(et_description.getText().toString());
            note.setTitle(et_title.getText().toString());
            note.setDateTime();
            outputIntent.putExtra("note", note); //
            setResult(RESULT_OK, outputIntent);
            finish();
        } else {
            Toast.makeText(this, R.string.error_fillAllFields, Toast.LENGTH_LONG).show();
        }
    }
}