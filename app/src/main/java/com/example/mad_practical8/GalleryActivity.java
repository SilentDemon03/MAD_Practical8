package com.example.mad_practical8;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Size;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    //private final int MY_PERMISSIONS_REQUEST_READ_MEDIA_IMAGES = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Check for external storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Permission already granted, proceed with loading images
            loadImages();
        }
    }

    private void loadImages() {
        List<Image> imageList = getPhotos();
        ImageView IVThumb1 = findViewById(R.id.IVThumb1);
        ImageView IVThumb2 = findViewById(R.id.IVThumb2);
        ImageView IVThumb3 = findViewById(R.id.IVThumb3);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            try {
                Bitmap thumbnail1 = getApplicationContext().getContentResolver().loadThumbnail(
                        imageList.get(0).uri, new Size(640, 480), null);
                IVThumb1.setImageBitmap(thumbnail1);
                Bitmap thumbnail2 = getApplicationContext().getContentResolver().loadThumbnail(
                        imageList.get(1).uri, new Size(640, 480), null);
                IVThumb2.setImageBitmap(thumbnail2);
                Bitmap thumbnail3 = getApplicationContext().getContentResolver().loadThumbnail(
                        imageList.get(2).uri, new Size(640, 480), null);
                IVThumb3.setImageBitmap(thumbnail3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with loading images
                loadImages();
            } else {

            }
        }
    }


    public static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    class Image {
        private final Uri uri;
        private final String name;
        private final String date_taken;

        public Image(Uri uri, String name, String date_taken) {
            this.uri = uri;
            this.name = name;
            this.date_taken = date_taken;
        }
    }

    protected List<Image> getPhotos() {
        List<Image> ImageList = new ArrayList<Image>();
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
        };
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " ASC";

        try (Cursor cursor = getApplicationContext().getContentResolver().query(
                collection,
                projection,
                "",
                null,
                sortOrder)) {

            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int datetakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                String dateadded = cursor.getString(datetakenColumn);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                // Stores column values and the contentUri in a local object
                // that represents the media file.
                ImageList.add(new Image(contentUri, name, dateadded));
            }
        }
        return ImageList;
    }
}
