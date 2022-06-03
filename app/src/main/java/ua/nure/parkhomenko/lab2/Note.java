package ua.nure.parkhomenko.lab2;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Note implements Parcelable {
    private String dateTime;
    private String imagePath = null;
    private String title;
    private int importance;
    private String description;

    Note(String newTitle, String newDescription, int importance, String imagePath) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"); //"dd.MM.yyyy HH:mm:ss aaa z"
        dateTime = simpleDateFormat.format(calendar.getTime());
        title = newTitle;
        this.importance = importance;
        description = newDescription;
        this.imagePath = imagePath;
    }

    public Note(Parcel in) {
        dateTime = in.readString();
        title = in.readString();
        importance = in.readInt();
        description = in.readString();
        imagePath = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getImportanceImage(int index) {
        switch (index) {
            case 0:
                return R.drawable.red;
            case 1:
                return R.drawable.yellow;
            case 2:
                return R.drawable.green;
            default:
                return R.drawable.green;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"); //"dd.MM.yyyy HH:mm:ss aaa z"
        dateTime = simpleDateFormat.format(calendar.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dateTime);
        parcel.writeString(title);
        parcel.writeInt(importance);
        parcel.writeString(description);
        parcel.writeString(imagePath);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
