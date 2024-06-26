package edu.birzeit.www;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class AddCarActivity extends AppCompatActivity {
    private static final String TAG = "AddCarActivity";
    private LinearLayout carInfoContent, rentalInfoContent, specificationsInfoContent;
    private Spinner carModelSpinner, yearSpinner, fuelTypeSpinner, numberOfSeatsSpinner, transmissionSpinner;
    private ImageView selectImageButton;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ArrayAdapter<String> carModelAdapter;

    private ArrayAdapter<String> seatsAdapter;

    private static final String add_URL = "http://10.0.2.2:80/project_android/AddNewCar.php";
    private EditText descriptionEditText, vinEditText, RentalPriceEditText, colorEditText, topSpeedEditText;

    private Uri selectedImageUri; // Variable to store the selected image URI
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton imageButton;
    private Menu menu;
    TextView textViewUsername, textViewEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2add);

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationView);
        imageButton = findViewById(R.id.buttonDrawer);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu = navigationView.getMenu();
                onCreateOptionsMenu(menu);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        //--------------------------** SHAHD EDIT **-----------------------------
        // to show name & email on tool bar..
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);


//
        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);  // This gets the header view from the NavigationView

        TextView textViewUsername = headerView.findViewById(R.id.textViewUsername);
        TextView textViewEmail = headerView.findViewById(R.id.textViewEmail);

        // Access SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "example@example.com");  // Use default value if not found

        textViewEmail.setText(savedEmail);


        // Make HTTP request to fetch user data
        String getUserUrl = "http://10.0.2.2:80/project_android/get_users.php?email=" + savedEmail;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, getUserUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        JSONObject user = response.getJSONObject(0); //  there's only one user returned
                        String fetchedUsername = user.getString("UserName");

                        // Autofill EditTexts
                        textViewUsername.setText(fetchedUsername);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, error -> {
            // Handle error
            Toast.makeText(AddCarActivity.this, "Failed to Fetch", Toast.LENGTH_SHORT).show();
        });
        queue.add(jsonArrayRequest);
//--------------------------** SHAHD EDIT **-----------------------------

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.AdminSettingOption) {
                    Toast.makeText(AddCarActivity.this, "Account Setting Option", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCarActivity.this, manageAdminAcc_Activity.class);
                    startActivity(intent);
                }
                if (itemId == R.id.addCarOption) {
                    Toast.makeText(AddCarActivity.this, "Add Car Page", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCarActivity.this, AddCarActivity.class);
                    startActivity(intent);
                }
                if (itemId == R.id.reportOption) {
                    Toast.makeText(AddCarActivity.this, "Report Page", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCarActivity.this, ReportActivity.class);
                    startActivity(intent);
                }
                if (itemId == R.id.orders) {
                    Toast.makeText(AddCarActivity.this, "Admin Orders Page", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCarActivity.this, AdminOrders.class);
                    startActivity(intent);
                }
                if (itemId == R.id.reservations) {
                    Toast.makeText(AddCarActivity.this, "Reservations Page", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCarActivity.this, UserReservations.class);
                    startActivity(intent);
                }
                if (itemId == R.id.ContactUsOption) {
                    Toast.makeText(AddCarActivity.this, "Contact Us Page", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCarActivity.this, ContacUsActivity.class);
                    startActivity(intent);
                }

                if (itemId == R.id.logout) {
                    Toast.makeText(AddCarActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                    getSharedPreferences("loginPrefs", MODE_PRIVATE).edit()
                            .clear()
                            .apply();

                    Intent intent = new Intent(AddCarActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (itemId == R.id.home) {
                    Toast.makeText(AddCarActivity.this, "Home Page", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCarActivity.this, MainActivity2.class);
                    startActivity(intent);
                }
                if (itemId == R.id.navaboutUs) {
                    Toast.makeText(AddCarActivity.this, "About Us", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCarActivity.this, AboutUs.class);
                    startActivity(intent);
                }
                drawerLayout.close();
                return false;
            }


        });



        carInfoContent = findViewById(R.id.car_info_content);
        rentalInfoContent = findViewById(R.id.rental_info_content);
        specificationsInfoContent = findViewById(R.id.specifications_info_content);
        selectImageButton = findViewById(R.id.selectImageButton);

        TextView carInfoHeader = findViewById(R.id.car_info_header);
        TextView rentalInfoHeader = findViewById(R.id.rental_info_header);
        TextView specificationsInfoHeader = findViewById(R.id.specifications_info_header);

        carModelSpinner = findViewById(R.id.modelSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        fuelTypeSpinner = findViewById(R.id.fuelTypeSpinner);
        numberOfSeatsSpinner = findViewById(R.id.numberOfSeatsSpinner);
        transmissionSpinner = findViewById(R.id.transmissionSpinner);


        descriptionEditText = findViewById(R.id.descriptionEditText);
        vinEditText = findViewById(R.id.vinEditText);
        RentalPriceEditText = findViewById(R.id.RentalPriceEditText);
        colorEditText = findViewById(R.id.colorEditText);
        topSpeedEditText = findViewById(R.id.topSpeedEditText);
        //--
        ArrayList<String> carModels = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.car_models)));
        carModelAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, carModels);
        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayList<String> carSeats = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.seatsNumber)));
        seatsAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, carSeats);
        seatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //--
        ArrayAdapter<CharSequence> carYearAdapter = ArrayAdapter.createFromResource(this, R.array.years, R.layout.spinner_layout);
        carYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> fuelAdapter = ArrayAdapter.createFromResource(this, R.array.fuelType, R.layout.spinner_layout);
        fuelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        ArrayAdapter<CharSequence> seatsAdapter = ArrayAdapter.createFromResource(this, R.array.seatsNumber, R.layout.spinner_layout);
//        seatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<CharSequence> transmissionAdapter = ArrayAdapter.createFromResource(this, R.array.transType, R.layout.spinner_layout);
        transmissionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        carModelSpinner.setAdapter(carModelAdapter);
        transmissionSpinner.setAdapter(transmissionAdapter);
        yearSpinner.setAdapter(carYearAdapter);
        fuelTypeSpinner.setAdapter(fuelAdapter);
        numberOfSeatsSpinner.setAdapter(seatsAdapter);
//**************************************************************************
        Button addButton = findViewById(R.id.AddCarBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCar();
//                Toast.makeText(AddCarActivity.this, "Car Added S",Toast.LENGTH_SHORT).show();

            }
        });
//**************************************************************************
        carInfoHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(carInfoContent);
            }
        });

        rentalInfoHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(rentalInfoContent);
            }
        });

        specificationsInfoHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(specificationsInfoContent);
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE_REQUEST);
            }
        });
///////////////////////////////////---DIALOG FOR MODEL & # OF SEATS--/////////////////////////////////////
        //---------------------------------MODEL DIALOG--------------------------------------------
        carModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("New Model")) {
                    showNewModelDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        }); //CAR MODEL LISTENER ENDS

        //---------------------------------# OF SEATS DIALOG--------------------------------------------
        numberOfSeatsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Other")) {
                    showNewSeatDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });//New # OF SEATS LISTENER ENDS


    }//OnCreate ENDS


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    //---------------------------------------show New Model Dialog--------------------------------------
    private void showNewModelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        builder.setTitle("Enter New Model");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newModel = input.getText().toString();
                Log.d(TAG, "New model entered: " + newModel);
                addNewModel(newModel);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                if (positiveButton != null && negativeButton != null) {
                    positiveButton.setTextColor(getResources().getColor(R.color.darkgray));
                    negativeButton.setTextColor(getResources().getColor(R.color.darkgray));
                }
            }
        });

        dialog.show();
    }//MODEL DIALOG SHOW ENDS

    /////////////
    private void addNewModel(String newModel) {
        try {
            if (carModelSpinner.getAdapter() instanceof ArrayAdapter) {
                carModelAdapter.add(newModel);
                carModelAdapter.notifyDataSetChanged();
                carModelSpinner.setSelection(carModelAdapter.getPosition(newModel));
                Log.d(TAG, "New model added to spinner: " + newModel);
            } else {
                Log.e(TAG, "Adapter is not an instance of ArrayAdapter");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding new model: ", e);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////

    //---------------------------------------show New Seat Dialog--------------------------------------
    private void showNewSeatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        builder.setTitle("Enter Number of Seats");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String numberOfSeats = input.getText().toString();
                Log.d(TAG, "Number of seats entered: " + numberOfSeats);
                addNewNumberOfSeats(numberOfSeats);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                if (positiveButton != null && negativeButton != null) {
                    positiveButton.setTextColor(getResources().getColor(R.color.darkgray));
                    negativeButton.setTextColor(getResources().getColor(R.color.darkgray));
                }
            }
        });

        dialog.show();
    }

    // Method to handle adding the number of seats
    private void addNewNumberOfSeats(String numberOfSeats) {
        try {
            if (numberOfSeatsSpinner.getAdapter() instanceof ArrayAdapter) {
                seatsAdapter.add(numberOfSeats);
                seatsAdapter.notifyDataSetChanged();
                numberOfSeatsSpinner.setSelection(seatsAdapter.getPosition(numberOfSeats));
                Log.d(TAG, "New number Of Seats added to spinner: " + numberOfSeats);
            } else {
                Log.e(TAG, "Adapter is not an instance of ArrayAdapter");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding new model: ", e);
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            String fileName = getFileName(selectedImageUri);
            Log.e(TAG, fileName + " Uploaded Successfully ");

            TextView fileNameTextView = findViewById(R.id.fileNameTextView);
            fileNameTextView.setVisibility(View.VISIBLE);
            fileNameTextView.setText(fileName);
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex >= 0) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result != null ? result.lastIndexOf('/') : -1;
            if (cut != -1 && result != null) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    //*********************************00 ADD CAR METHOD 00***************************************
    private void addCar() {
        final String model = carModelSpinner.getSelectedItem().toString();
        final String description = descriptionEditText.getText().toString();
        final String vin = vinEditText.getText().toString();
        final String fuelType = fuelTypeSpinner.getSelectedItem().toString();
        final String transmission = transmissionSpinner.getSelectedItem().toString();
        final String numberOfSeats = numberOfSeatsSpinner.getSelectedItem().toString();
        final String rentPrice = RentalPriceEditText.getText().toString();
        final String color = colorEditText.getText().toString();
        final String year = yearSpinner.getSelectedItem().toString();
        final String topSpeed = topSpeedEditText.getText().toString();

        // Check if any field is empty
        if (model.isEmpty() || description.isEmpty() || vin.isEmpty() || fuelType.isEmpty() ||
                transmission.isEmpty() || numberOfSeats.isEmpty() || rentPrice.isEmpty() ||
                color.isEmpty() || year.isEmpty() || topSpeed.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if an image is selected
        if (selectedImageUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                inputStream.close();

                // Convert the byte array to a base64 string
                String imageBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);

                RequestQueue queue = Volley.newRequestQueue(this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, add_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            Log.d(TAG, "Response: " + message);
                            if (message.equals("Car added successfully")) {
                                Toast.makeText(AddCarActivity.this, "Car Added Successfully!", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Error: VIN already exists")) {
                                Toast.makeText(AddCarActivity.this, "Error: VIN already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddCarActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("model", model);
                        params.put("description", description);
                        params.put("vin", vin);
                        params.put("fuelType", fuelType);
                        params.put("transmission", transmission);
                        params.put("numberOfSeats", numberOfSeats);
                        params.put("rentPrice", rentPrice);
                        params.put("color", color);
                        params.put("year", year);
                        params.put("topSpeed", topSpeed);
                        params.put("image", imageBase64); // Add the base64 image string to the parameters
                        return params;
                    }
                };
                queue.add(stringRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }}

//*************************************************************************************
public boolean onCreateOptionsMenu(Menu menu) {
    menu.clear();

    getMenuInflater().inflate(R.menu.drawer_items, menu);

    MenuItem addCarItem = menu.findItem(R.id.addCarOption);
    addCarItem.setVisible(login.isAdmin); // Hide/show add car menu item based on isAdmin value

    MenuItem ordersItem = menu.findItem(R.id.orders);
    ordersItem.setVisible(login.isAdmin); // Hide/show orders menu item based on isAdmin value

    MenuItem reportItem = menu.findItem(R.id.reportOption);
    reportItem.setVisible(login.isAdmin); // Hide/show report menu item based on isAdmin value

    MenuItem reservItem = menu.findItem(R.id.reservations);
    reservItem.setVisible(!(login.isAdmin));
    MenuItem contactItem = menu.findItem(R.id.ContactUsOption);
    contactItem.setVisible(!(login.isAdmin));
    return true;
}
}

