package ua.nure.parkhomenko.lab2;

import java.util.ArrayList;
import android.content.Context;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JSONDataFileHandler {
    private static final String FILE_NAME = "data";

    static boolean exportToJSON(Context context, ArrayList<Note> dataList) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            DataItems dataItems = new DataItems();
            dataItems.setNotes(dataList);
            String jsonString = gson.toJson(dataItems);

            fileOutputStream.write(jsonString.getBytes());
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static ArrayList<Note> importFromJSON(Context context) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = context.openFileInput(FILE_NAME);
            InputStreamReader streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(streamReader, DataItems.class);
            return  dataItems.getNotes();
        }catch (IOException ex){
            ex.printStackTrace();
        }

        return null;
    }

    private static class DataItems {
        private ArrayList<Note> notes;

        ArrayList<Note> getNotes() {
            return notes;
        }
        void setNotes(ArrayList<Note> notes) {
            this.notes = notes;
        }
    }
}
