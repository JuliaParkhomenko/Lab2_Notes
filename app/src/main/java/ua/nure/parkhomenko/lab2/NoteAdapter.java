package ua.nure.parkhomenko.lab2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter<N> extends ArrayAdapter<Note> implements Filterable {
    private LayoutInflater inflater;
    private int layout;
    private List<Note> notes;

    // добавила для фильтра
    private List<Note> filterednotes;
    // добавила для фильтра

    String importanceFilter="All";

    // invoke the suitable constructor of the ArrayAdapter class
    public NoteAdapter(/*@NonNull */Context context, int resource, List<Note> notes)
    {
        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, resource, notes);
        this.notes = notes;

        // добавила для фильтра
        filterednotes = notes;
        // добавила для фильтра

        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }


    // добавила для фильтра
    /*//For this helper method, return based on filteredData
    public int getCount()
    {
        return filterednotes.size();
    }

    //This should return a data object, not an int
    public Note getItem(int position)
    {
        return filterednotes.get(position);
    }*/
    // добавила для фильтра

    public Note getItemAtPosition(int position){
        return notes.get(position);
    }

    //@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        //if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.view_note, parent, false);
            //currentItemView=inflater.inflate(R.layout.view_note, parent, false); // ТАК В МЕТАНІТІ. ТРЕБА БУДЕ СПРОБУВАТИ
        //}

        // get the position of the view from the ArrayAdapter
        Note note = getItem(position);
        boolean isHide=true;
        if(importanceFilter.equals("All")){
            isHide = false;
        }else{
            switch (note.getImportance()) {
                case 0: {
                    if(importanceFilter.equals("MostImportant"))
                        isHide=false;
                    break;
                }
                case 1: {
                    if(importanceFilter.equals("Important"))
                        isHide=false;
                    break;
                }
                case 2: {
                    if(importanceFilter.equals("NotVeryImportant"))
                        isHide=false;
                    break;
                }
                default:
                    break;
            }
        /*switch (importanceFilter){
            case R.array.importance:
            currentNumberPosition.getImportance()
        })*/
        }

        if(isHide){
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.view_hide_note, parent, false);
            return currentItemView;
        }

        // then according to the position of the view assign the desired image for the same
//        ImageView noteImage = currentItemView.findViewById(R.id.iv_notePicture);
//        assert currentNumberPosition != null;
//        noteImage.setImageResource(currentNumberPosition.getNumbersImageId());

        ImageView ivImage = currentItemView.findViewById(R.id.iv_notePicture);
        if(note.getImagePath()!=null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator + note.getImagePath()+".jpg");
            ivImage.setImageBitmap(myBitmap);
            //ivImage.setImageURI(currentNumberPosition.getImageURI());
        }
        else {
            ivImage.setImageResource(R.drawable.note_default_img);
        }
        TextView tvTitle = currentItemView.findViewById(R.id.tv_title);
        tvTitle.setText(note.getTitle());

        TextView tvDateTime = currentItemView.findViewById(R.id.tv_dateTime);
        tvDateTime.setText(note.getDateTime());

        ImageView importanceImage = currentItemView.findViewById(R.id.iv_importance);
        switch (note.getImportance()) {
            case 0: {
                importanceImage.setImageResource(R.drawable.red);
                break;
            }
            case 1: {
                importanceImage.setImageResource(R.drawable.yellow);
                break;
            }
            case 2: {
                importanceImage.setImageResource(R.drawable.green);
                break;
            }
            default:
                break;
        }

        return currentItemView;
    }

    public void setImportanceFilter(String importanceFilter) {
        this.importanceFilter=importanceFilter;
    }
}