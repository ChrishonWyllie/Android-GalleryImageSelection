package com.chrishonwyllie.galleryimageselection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get reference to ImageView object in activity_main.xml
        imageView = (ImageView) findViewById(R.id.imageview);

        // Define an action when the ImageView is clicked on
        // NOTE: in activity_main.xml, the ImageView widget is "clickable"
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("Image has been tapped");
                // Check if the user has authorized access
                attemptToOpenGallery();

            }
        });


    }


    public void attemptToOpenGallery() {
        // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE should be a unique int. Although, it can be whatever you choose
        int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        // Check if the user has already authorized access to external storage
        // If not, ask the user for access
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation? (A popup)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                return;
            }
        } else {

            // The user has previously authorized access to external storage.

            System.out.println("user already authorized external storage access. Continue");
            openImageGallery();
        }
    }



    // For if the user has not enabled external storage access prior tapping the imageView
    // Essentially, what happens after the user chooses to grant or refuse permission to access external storage?
    // Random but unique int, same as before
    final int REQUEST_PERMISSION = 1234;
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Permission granted. Continue with image selection

                openImageGallery();

            } else {
                // User refused to grant permission. handle this issue however you wish
            }
        }
    }




    // Same as before, this only has to be a unique int
    final int PHOTO_LIBRARY_REQUEST_CODE_IMAGEVIEW = 1;

    // Create an intent that will open the photo library gallery
    public void openImageGallery() {



        // This will actually open the media gallery

        /*
        * Take note of the second parameter in the 'new Intent(...)' call
        * You can choose to display images or videos
        *
        * Show images:      new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        * Show videoes:     new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        * */

        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), PHOTO_LIBRARY_REQUEST_CODE_IMAGEVIEW);

    }



    // This handles the result of your image selection.
    // Essentially, what happens when the user taps an image/video?
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_LIBRARY_REQUEST_CODE_IMAGEVIEW && resultCode == RESULT_OK && data != null) {

            // Get a uri for the selected image/video (REMEMEBER, WE ARE SELECTING IMAGES IN THIS CASE. NOT VIDEOS
            Uri mediaUri = data.getData();

            System.out.println("Selected media uri: " + mediaUri);


            // Attempt to create a Bitmap object from the selected URI
            try {
                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mediaUri);

                // Set the newly constructed Bitmap as the ImageView's image bitmap
                imageView.setImageBitmap(bitmapImage);

            } catch (IOException e) {

                // In case there is an error in this action....
                e.printStackTrace();
            }
        }
    }
}
