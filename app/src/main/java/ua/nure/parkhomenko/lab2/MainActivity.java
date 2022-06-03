package ua.nure.parkhomenko.lab2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

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
import java.io.IOException;
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
                                //   OLD
                                /*
                                byte[] image = data.getByteArrayExtra("IMAGE");

                                if(image!=null){
                                    newNote.setImage(image);
                                }
                                    */
                                //String imagePath = data.getStringExtra("IMAGE");
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
                                                File file = new File(folder,/*Environment.getExternalStorageDirectory() + File.separator +*/ notes.get(index_position).getImagePath() + ".jpg");
                                                if (file.exists()) {
                                                    file.delete();
                                                }
                                            }
                                        }
                                        //System.out.println(index_position);
                                        notes.set(index_position, newNote);
                                        noteAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Operation canceled", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            /*
        notes = new ArrayList<Note>();

        notes.add(new Note("My life in several worlds with picture gjlhvjh hjgvjg hvhvl ujgvhgvljgvjh hjvkvjl hvgjyhvl hvbjhyvlu ujgvhlchygv vukjg", "It`s my liiiiife...", 1));
        notes.add(new Note("Monday", "I spent my monday with my friends.", 0));
        notes.add(new Note("Tuesday", "I spent my tuesday with my friends.", 0));
        notes.add(new Note("My life in several worlds with picture gjlhvjh hjgvjg hvhvl ujgvhgvljgvjh hjvkvjl hvgjyhvl hvbjhyvlu ujgvhlchygv vukjg", "It`s my liiiiife...", 1));
        notes.add(new Note("Wednesday", "I spent my wednesday with my friends.", 1));
        notes.add(new Note("Thursday", "I spent my thursday with my friends.", 1));
        notes.add(new Note("Friday", "I spent my friday with my friends.", 2));
        notes.add(new Note("My life in several worlds with picture gjlhvjh hjgvjg hvhvl ujgvhgvljgvjh hjvkvjl hvgjyhvl hvbjhyvlu ujgvhlchygv vukjg", "It`s my liiiiife...", 1));
        notes.add(new Note("Weekends", "I spent my weekends with my friends.", 2
        ));
        notes.add(new Note("My life in several worlds with picture gjlhvjh hjgvjg hvhvl ujgvhgvljgvjh hjvkvjl hvgjyhvl hvbjhyvlu ujgvhlchygv vukjg", "It`s my liiiiife...", 1));

        noteAdapter = new NoteAdapter<Note>(this, R.layout.view_note, notes);
        notesList = findViewById(R.id.notesList);
        notesList.setAdapter(noteAdapter);
            */

        openData();

        notesList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note item = noteAdapter.getItemAtPosition(i);
                //based on item add info to intent
                Intent intent = new Intent(MainActivity.this, DisplayNoteActivity.class);
                intent.putExtra("note", item);
                //  OLD
                //intent.putExtra("IMAGE", item.getImageInBytes());
                startActivity(intent);
            }
        });

        /*notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return true;
            }
        });*/

        //  register our context menu on list view (for setOnItemLongClickListener item`s menu)
        notesList = findViewById(R.id.notesList);
        registerForContextMenu(notesList);
    }

    //  Actionbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //     NEW FOR SEARCH
        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem
                = menu.findItem(R.id.btnSearch);
        SearchView searchView = (SearchView) searchViewItem.getActionView();

        //SearchView searchView = (SearchView) findViewById(R.id.btnSearch); // inititate a search view

        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    // Override onQueryTextSubmit method
                    // which is call
                    // when submitquery is searched

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
                    // This method is overridden to filter
                    // the adapter according to a search query
                    // when the user is typing search
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (newText.isEmpty()) {          //НЕ УВЕРЕНА newText
                            noteAdapter.setSearchCharText("");
                        } else {
                            noteAdapter.setSearchCharText(newText); //НЕ УВЕРЕНА newText

                        }
                        noteAdapter.notifyDataSetChanged();
                        //adapter.getFilter().filter(newText);
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
                //intent = new Intent("android.intent.action.CREATE");
                //startActivity(intent);
                //intent = new Intent(MainActivity.this, EditNoteActivity.class);
                /*startActivityForResult(intent, 0);*/
                activityResultLauncher.launch(intent);
                // startActivity(new Intent(MainActivity.this, EditNoteActivity.class));
                return true;
            /*case R.id.btnSearch:
                SearchView searchView = (SearchView) item.getActionView();

                //SearchView searchView = (SearchView) findViewById(R.id.btnSearch); // inititate a search view

                // attach setOnQueryTextListener
                // to search view defined above
                searchView.setOnQueryTextListener(
                        new SearchView.OnQueryTextListener() {

                            // Override onQueryTextSubmit method
                            // which is call
                            // when submitquery is searched

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

                            // This method is overridden to filter
                            // the adapter according to a search query
                            // when the user is typing search
                            @Override
                            public boolean onQueryTextChange(String newText) {
                                if (newText.isEmpty()) {          //НЕ УВЕРЕНА newText
                                    noteAdapter.setSearchCharText("");
                                } else {
                                    noteAdapter.setSearchCharText(newText); //НЕ УВЕРЕНА newText

                                }
                                noteAdapter.notifyDataSetChanged();
                                //adapter.getFilter().filter(newText);
                                return false;
                            }
                        });

                searchView.setOnCloseListener(() -> {
                    noteAdapter.setSearchCharText("");
                    noteAdapter.notifyDataSetChanged();
                    return false;
                });
                return true;
            */
            case R.id.btnFilterShowAll:

                /*// добавила для фильтра
                MainActivity.this.noteAdapter.getFilter().filter("All");
                // добавила для фильтра
                */

                noteAdapter.setImportanceFilter("All");
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Show all", Toast.LENGTH_LONG).show();
                return true;
            case R.id.btnFilterByMostImportant:
                noteAdapter.setImportanceFilter("MostImportant");
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Most important", Toast.LENGTH_LONG).show();
                return true;
            case R.id.btnFilterByImportant:
                noteAdapter.setImportanceFilter("Important");
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Important", Toast.LENGTH_LONG).show();
                return true;
            case R.id.btnFilterByNotVeryImportant:
                noteAdapter.setImportanceFilter("NotVeryImportant");
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Not very important", Toast.LENGTH_LONG).show();
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
                //  OLD
                //  intent.putExtra("IMAGE", noteForEdit.getImageInBytes());
                activityResultLauncher.launch(intent);
                // edit stuff here
                return true;
            case R.id.btnDeleteNote:
                int pos = info.position;
                notes.remove(pos);
                noteAdapter.notifyDataSetChanged();
                //System.out.println(pos);

                // remove stuff here
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }

    public void saveData(/*View view*/) {

        boolean result = JSONDataFileHandler.exportToJSON(this, notes);
        if (result) {
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Не удалось сохранить данные", Toast.LENGTH_LONG).show();
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    public void openData(/*View view*/) {
        notes = JSONDataFileHandler.importFromJSON(this);
        if (notes != null) {
            noteAdapter = new /*ArrayAdapter<>*/NoteAdapter<Note>(this, R.layout.view_note, notes);
            notesList = findViewById(R.id.notesList);
            notesList.setAdapter(noteAdapter);
            Toast.makeText(this, "Данные восстановлены", Toast.LENGTH_LONG).show();
        } else {
            notes = new ArrayList<Note>();
            noteAdapter = new NoteAdapter<Note>(this, R.layout.view_note, notes);
            notesList = findViewById(R.id.notesList);
            notesList.setAdapter(noteAdapter);
            Toast.makeText(this, "Здоровенькі були! У вас ще немає даних", Toast.LENGTH_LONG).show();
        }
    }
}