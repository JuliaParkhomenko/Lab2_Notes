package ua.nure.parkhomenko.lab2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Note> notes;
    ListView notesList;
    NoteAdapter<Note> noteAdapter;

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int result = activityResult.getResultCode();

                            if (result == RESULT_OK) {
                                Intent data = activityResult.getData();
                                Bundle extras = data.getExtras();
                                String action = extras.getString("action");
                                Note newNote = data.getParcelableExtra("note");
                                String imagePath = newNote.getImagePath();
                                if (action.equals("create")) {
                                    notes.add(newNote);
                                    noteAdapter.notifyDataSetChanged();
                                } else if (action.equals("edit")) {
                                    String index_position_string = data.getStringExtra("INDEX_POSITION");
                                    int index_position = Integer.parseInt(index_position_string);
                                    if (imagePath != null) {
                                        if (!notes.get(index_position).getTitle().equals(newNote.getTitle())) {
                                            if (notes.get(index_position).getImagePath() != null) {
                                                // Accessing the saved data from the downloads folder
                                                File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                                                // myData represent the file data that is saved publicly
                                                File file = new File(folder, notes.get(index_position).getImagePath() + ".jpg");
                                                if (file.exists()) {
                                                    file.delete();
                                                }
                                            }
                                        }
                                        notes.set(index_position, newNote);
                                        noteAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    //Toast.makeText(MainActivity.this, "Operation canceled", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openData();

        notesList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note item = noteAdapter.getItemAtPosition(i);
                //based on item add info to intent
                Intent intent = new Intent(MainActivity.this, DisplayNoteActivity.class);
                intent.putExtra("note", item);
                startActivity(intent);
            }
        });

        //  register our context menu on list view (for setOnItemLongClickListener item`s menu)
        notesList = findViewById(R.id.notesList);
        registerForContextMenu(notesList);
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
        saveData();
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

    //  Actionbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //     NEW FOR SEARCH
        // Initialise menu item search bar with id and take its object
        MenuItem searchViewItem
                = menu.findItem(R.id.btnSearch);
        SearchView searchView = (SearchView) searchViewItem.getActionView();

        // attach setOnQueryTextListener to search view defined above
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    // Override onQueryTextSubmit method which is call when submit query is searched

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (query.isEmpty()) {
                            noteAdapter.setSearchCharText("");
                        } else {
                            noteAdapter.setSearchCharText(query);
                        }
                        noteAdapter.notifyDataSetChanged();
                        return false;
                    }
                    // This method is overridden to filter the adapter according to a search query
                    // when the user is typing search
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (newText.isEmpty()) {          //НЕ УВЕРЕНА newText
                            noteAdapter.setSearchCharText("");
                        } else {
                            noteAdapter.setSearchCharText(newText); //НЕ УВЕРЕНА newText
                        }
                        noteAdapter.notifyDataSetChanged();
                        return false;
                    }
                });

        searchView.setOnCloseListener(() -> {
            noteAdapter.setSearchCharText("");
            noteAdapter.notifyDataSetChanged();
            return false;
        });

        return true;
    }

    // Interaction with Actionbar menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.btnAddNote:
                intent = new Intent(this, EditNoteActivity.class);
                intent.setAction("android.intent.action.CREATE");
                activityResultLauncher.launch(intent);
                return true;
            case R.id.btnFilterShowAll:
                noteAdapter.setImportanceFilter("All");
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(this, R.string.filterShowAll, Toast.LENGTH_LONG).show();
                return true;
            case R.id.btnFilterByMostImportant:
                noteAdapter.setImportanceFilter("MostImportant");
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(this, R.string.filterByMostImportant_noteBtn, Toast.LENGTH_LONG).show();
                return true;
            case R.id.btnFilterByImportant:
                noteAdapter.setImportanceFilter("Important");
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(this, R.string.filterByImportant_noteBtn, Toast.LENGTH_LONG).show();
                return true;
            case R.id.btnFilterByNotVeryImportant:
                noteAdapter.setImportanceFilter("NotVeryImportant");
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(this, R.string.filterByNotVeryImportant_noteBtn, Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //  ListView Item menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.notesList) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    // Interaction with ListView`s Item menu items
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent;
        switch (item.getItemId()) {
            case R.id.btnEditNote:
                Note noteForEdit = noteAdapter.getItemAtPosition(info.position);
                //intent = new Intent("android.intent.action.EDIT");
                intent = new Intent(this, EditNoteActivity.class);
                intent.setAction("android.intent.action.EDIT");
                intent.putExtra("INDEX_POSITION", Integer.toString(info.position));
                intent.putExtra("noteForEdit", noteForEdit);
                activityResultLauncher.launch(intent);
                return true;
            case R.id.btnDeleteNote:
                int pos = info.position;
                notes.remove(pos);
                noteAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void saveData() {

        boolean result = JSONDataFileHandler.exportToJSON(this, notes);
        if (result) {
            Toast.makeText(this, R.string.dataSaved, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.dataNotSaved, Toast.LENGTH_LONG).show();
        }
    }

    public void openData() {
        notes = JSONDataFileHandler.importFromJSON(this);
        if (notes != null) {
            noteAdapter = new NoteAdapter<Note>(this, R.layout.view_note, notes);
            notesList = findViewById(R.id.notesList);
            notesList.setAdapter(noteAdapter);
            Toast.makeText(this, R.string.dataRecovered, Toast.LENGTH_LONG).show();
        } else {
            notes = new ArrayList<Note>();
            noteAdapter = new NoteAdapter<Note>(this, R.layout.view_note, notes);
            notesList = findViewById(R.id.notesList);
            notesList.setAdapter(noteAdapter);
            Toast.makeText(this, R.string.noDataYet, Toast.LENGTH_LONG).show();
        }
    }
}