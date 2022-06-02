package ua.nure.parkhomenko.lab2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;


import androidx.core.content.res.ResourcesCompat;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Note implements Parcelable {
    private String dateTime;

    // картинка
    //private byte[] image = null; //   OLD
    private String imagePath = null;

    private String title;
    private int importance;
    private String description;

    Note(String newTitle, String newDescription, int importance, /*byte[] image*/ String imagePath) {  //   OLD
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"); //"dd.MM.yyyy HH:mm:ss aaa z"
        dateTime = simpleDateFormat.format(calendar.getTime());
        title = newTitle;
        this.importance = importance;
        description = newDescription;
        // картинка
        //if (imagePath != null) {
            this.imagePath = imagePath;
        //}

        //   OLD
        /*if (image != null) {
            this.image = image;
        }*/

    }

    public Note(Parcel in) {
        dateTime = in.readString();
        title = in.readString();
        importance = in.readInt();
        description = in.readString();
        imagePath = in.readString();
        //  КАРТИНКА (ИЗ-ЗА ПРОБЛЕМ С ПАРСЕЛОМ
        /*//if (image != null)
            in.readByteArray(this.image);
         */
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
        //  КАРТИНКА (ИЗ-ЗА ПРОБЛЕМ С ПАРСЕЛОМ
        //parcel.writeByteArray(this.image);
    }


//   OLD
    /*public byte[] getImageInBytes() {
        return image;
    }
    public void setImage(byte[] imageInByte) {
        image = imageInByte;
    }

    public boolean hasUserImage() {
        return image != null;
    }


    public Bitmap getImageBitmap() {
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        return bmp;
        //ImageView image = (ImageView) findViewById(R.id.imageView1);
        //image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(), image.getHeight(), false));
    }
*/

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
