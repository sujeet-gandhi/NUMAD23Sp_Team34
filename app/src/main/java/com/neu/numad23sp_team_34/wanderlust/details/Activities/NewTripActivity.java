package com.neu.numad23sp_team_34.wanderlust.details.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.project.ConnectingLineItemDecoration;
import com.neu.numad23sp_team_34.project.ImageAdapter;
import com.neu.numad23sp_team_34.project.ItineraryAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class NewTripActivity extends AppCompatActivity {



    private RecyclerView imageRecyclerView;
    private ImageAdapter imageAdapter;
    private List<Bitmap> images = new ArrayList<>();
    private static final int AUTOCOMPLETE_REQUEST_CODE = 2;


    private EditText editKeywords;
    private RecyclerView itineraryRecyclerView;
    private ItineraryAdapter itineraryAdapter;
    private List<String> itineraryItems = new ArrayList<>();


    private ImageView returnToHomeImageView;
    private ImageView openCameraImageView;
    private ImageView openGalleryImageView;
    private ImageView uploadedPhotoImageView;

    private String pathToFile;
    private String photoURL;

    private StorageReference storageReference;
    private DatabaseReference databaseTripReference;

    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int GALLERY_REQUEST_CODE = 102;

    private EditText tripNameEditText;
    private EditText destinationEditText;

    private RadioGroup tripTypeRadioGroup;

    private TextView showPriceEUR;
    private TextView startDateTextView;
    private TextView endDateTextView;

    private SeekBar priceSeekBar;

    private RatingBar ratingBarTrip;

    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);


        Places.initialize(getApplicationContext(), "AIzaSyC0YKtZG9Gq0bA8slXRbBbvRlaw3IxsI8c");


        openCameraImageView = findViewById(R.id.openCameraImageView);
        openGalleryImageView = findViewById(R.id.openGalleryImageView);
        uploadedPhotoImageView = findViewById(R.id.uploadedPhotoImageView);
        tripNameEditText = findViewById(R.id.tripNameEditText);
        destinationEditText = findViewById(R.id.destinationEditText);
        tripTypeRadioGroup = (RadioGroup) findViewById(R.id.tripTypeRadioGroup);
        showPriceEUR = findViewById(R.id.showPriceEUR);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        priceSeekBar = (SeekBar) findViewById(R.id.priceSeekBar);
        ratingBarTrip = (RatingBar) findViewById(R.id.ratingBarTrip);
        editKeywords = findViewById(R.id.keywords);


        Button buttonAddLocation = findViewById(R.id.buttonAddLocation);

        itineraryRecyclerView = findViewById(R.id.itineraryRecyclerView);

        itineraryAdapter = new ItineraryAdapter( itineraryItems, () -> {
            buttonAddLocation.setVisibility(View.INVISIBLE);
            buttonAddLocation.callOnClick();
            itineraryAdapter.notifyDataSetChanged();
        });

        LinearLayoutManager layoutManagerItinerary = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itineraryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itineraryRecyclerView.setAdapter(itineraryAdapter);
        itineraryRecyclerView.addItemDecoration(new ConnectingLineItemDecoration(Color.GRAY, 5));

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseTripReference = FirebaseDatabase.getInstance().getReference("trips");


        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPlacesAutocomplete();
            }
        });

        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showPriceEUR.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        openCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        openGalleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });
    }


    private void launchPlacesAutocomplete() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(pathToFile);
                uploadedPhotoImageView.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Photo URL: " + Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                uploadImageToFirebase(f.getName(), contentUri);
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "TravelJournal_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "Gallery URI:  " + imageFileName);
                uploadedPhotoImageView.setImageURI(contentUri);
                uploadImageToFirebase(imageFileName, contentUri);
            }
        }
    }


    public void onClickReturnToHome(View view) {
        returnToHomeImageView = (ImageView) findViewById(R.id.previousMenuImageView);
        returnToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
            }
        });
    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "Uploaded Photo URL: " + uri.toString());
                        photoURL = uri.toString();
                    }
                });
                Toast.makeText(NewTripActivity.this, "Photo Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewTripActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TravelJournal_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        pathToFile = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.neu,numad23sp_team_34", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    public void addTripOnClick(View view) {
        String tripName = tripNameEditText.getText().toString().trim();
        String destination = destinationEditText.getText().toString().trim();

        int selectedIdRadioGroup = tripTypeRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButtonSelectedOption = (RadioButton) findViewById(selectedIdRadioGroup);
        String radioButtonToStringTripType = (String) radioButtonSelectedOption.getText();
        List<String> keywords = Arrays.asList(editKeywords.getText().toString().split(","));


        int priceOfTrip = priceSeekBar.getProgress();
        float tripRating = ratingBarTrip.getRating();

        String startDate = startDateTextView.getText().toString().trim();
        String endDate = endDateTextView.getText().toString().trim();

        String url = photoURL;


        if (!TextUtils.isEmpty(tripName)) {
            String id = databaseTripReference.push().getKey();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            Trip trip = new Trip(id, tripName, destination, radioButtonToStringTripType, priceOfTrip, tripRating, startDate, endDate, url, false, keywords, itineraryItems, userId);
            trip.setTimestamp(System.currentTimeMillis());


            databaseTripReference.child(id).setValue(trip);

            Toast.makeText(this, "Trip added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter Trip Name", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickPickStartDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.getTime();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startDateTextView.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void onClickPickEndDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.getTime();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDateTextView.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}