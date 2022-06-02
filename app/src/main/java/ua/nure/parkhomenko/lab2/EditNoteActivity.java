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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditNoteActivity extends AppCompatActivity {

    Note note;
    //  OLD
    //byte[] imageInBytes = null;
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
                                /*Bitmap bitmap = ((BitmapDrawable) iv_photo.getDrawable()).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);*/
                                //  OLD
                                /*  byte[] imageInByte = baos.toByteArray();
                                imageInBytes = imageInByte;
                                */
                                //Toast.makeText(MainActivity.this, "Title modified", Toast.LENGTH_LONG).show();
                            } else {
                                //Toast.makeText(MainActivity.this, "Operation canceled", Toast.LENGTH_LONG).show();
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
                index_position = inputIntent.getStringExtra("INDEX_POSITION");  // РАБОТАЕТ!
            }

            //String index_position = inputIntent.getStringExtra("indexPosition"); //НЕ РАБОТАЕТ!
            //  НЕ ЗАБЫТЬ ЗА КАРТИНКУ

            TextInputEditText et_title = findViewById(R.id.et_title);
            TextInputEditText et_description = findViewById(R.id.et_description);
            ImageView iv_image = findViewById(R.id.iv_photo);
            //  OLD
            /*
            byte[] image = inputIntent.getByteArrayExtra("IMAGE");
            if(image!=null){
                note.setImage(image);
            }*/
            if (note.getImagePath()!=null) { //   OLD
                //iv_image.setImageURI(note.getImageURI());

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
                //note.setImage(bytes.toByteArray());

                //TODO: resolve img conflict
                //iv_image.setImageBitmap(note.getImageBitmap());
            } else {
                iv_image.setImageResource(R.drawable.note_default_img);
            }
            Spinner spinner = findViewById(R.id.spinner);
            et_description.setText(note.getDescription());
            et_title.setText(note.getTitle());
            spinner.setSelection(note.getImportance());        //ЯК ПРАЦЮЄ


            //Note newNote = new Note("TestCreate", "I write it 30.05.2022 in 1:38", 0);
            //Intent data = new Intent();
            //inputIntent.putExtra("id", title);
            //setResult(RESULT_OK, data);
            outputIntent.putExtra("action", "edit");
            outputIntent.putExtra("INDEX_POSITION", index_position);
            //inputIntent.putExtra("indexPosition", index_position);
            //outputIntent.putExtra("note", note);
            // outputIntent.setData(newNote);
        } else if (action.equals("android.intent.action.CREATE")) {
            ImageView img = (ImageView) findViewById(R.id.iv_photo);
            img.setImageResource(R.drawable.note_default_img);
            //note = new Note("TestCreate", "I write it 30.05.2022 in 1:38", 0);
            note = new Note("", "", 0, null);
            //inputIntent.putExtra("id", title);
            //setResult(RESULT_OK, data);
            outputIntent.putExtra("action", "create");
            //outputIntent.putExtra("note", note);

            //Note note = getIntent().getParcelableExtra("note");
        }


/*   -------------- Адаптер для спинера ------------- не нужен, т.к. он нужен для мгновенного реагирования на выбор пользователя
// Получаем экземпляр элемента Spinner
        Spinner spinner = findViewById(R.id.spinner);

// Настраиваем адаптер
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.importance,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Вызываем адаптер
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                //selection.setText(item);
                note.setImportance(spinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

*/
    }


    public void uploadImage(View view) {
        //ImageView iv_photo = findViewById(R.id.iv_photo);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
        //startActivityForResult(intent, 3);
    }

    public void saveNote(View view) {
        TextInputEditText et_title = findViewById(R.id.et_title);
        TextInputEditText et_description = findViewById(R.id.et_description);
        // проверка на заполненность всех полей
        if (!TextUtils.isEmpty(et_description.getText()) && !TextUtils.isEmpty(et_title.getText())) {


            //  НЕ ЗАБЫТЬ СОХРАНИТЬ КАРТИНКУ
            ImageView iv_photo = findViewById(R.id.iv_photo);
            if (!iv_photo.getDrawable().getConstantState().equals(getDrawable(R.drawable.note_default_img).getConstantState()) /*&& imageInBytes != null*/) { //  OLD
                /*Bitmap bitmap = ((BitmapDrawable) iv_photo.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageInByte = baos.toByteArray();*/

                //  OLD
                //note.setImage(imageInBytes);

                Bitmap bitmap = ((BitmapDrawable) iv_photo.getDrawable()).getBitmap();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                //Задаете путь и имя
                String imagePath = et_title.getText().toString();

                // Requesting Permission to access External Storage
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        23);

                // getExternalStoragePublicDirectory() represents root of external storage, we are using DOWNLOADS
                // We can use following directories: MUSIC, PODCASTS, ALARMS, RINGTONES, NOTIFICATIONS, PICTURES, MOVIES
                File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                // Storing the data in file with name as noteTitle.jpg
                File file = new File(folder,/*Environment.getExternalStorageDirectory() + File.separator + */imagePath+".jpg");
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
                    //outputIntent.putExtra("IMAGE", imagePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //outputIntent.putExtra("IMAGE", imageInBytes);
            }
            Spinner spinner = findViewById(R.id.spinner);
            // Получаем выбранный объект
            //int item = spinner.getSelectedItemPosition();
            //selection.setText(item);

            note.setImportance(spinner.getSelectedItemPosition());
            note.setDescription(et_description.getText().toString());
            note.setTitle(et_title.getText().toString());
            note.setDateTime();
            outputIntent.putExtra("note", note); //     ВОЗМОЖНО НЕ РАБОТАЕТ (НЕ ПЕРЕДАЕТ ИНТЕНТ)
            //inputIntent.putExtra("note", note);
            setResult(RESULT_OK, outputIntent);
            finish();
        } else {
            Toast.makeText(this, R.string.error_fillAllFields, Toast.LENGTH_LONG).show();
        }
    }
}