package com.simoale.debitcredit.ui.transactions;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.simoale.debitcredit.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.simoale.debitcredit.model.Category;
import com.simoale.debitcredit.model.Payee;
import com.simoale.debitcredit.model.Tag;
import com.simoale.debitcredit.model.Transaction;
import com.simoale.debitcredit.model.Wallet;
import com.simoale.debitcredit.ui.category.CategoryViewModel;
import com.simoale.debitcredit.ui.payee.PayeeViewModel;
import com.simoale.debitcredit.ui.tag.TagViewModel;
import com.simoale.debitcredit.ui.wallet.WalletViewModel;
import com.simoale.debitcredit.utils.DatePicker;
import com.simoale.debitcredit.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.simoale.debitcredit.utils.Utilities.REQUEST_IMAGE_CAPTURE;

public class NewTransactionFragment extends Fragment {

    private Activity activity;

    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;
    private PayeeViewModel payeeViewModel;
    private WalletViewModel walletViewModel;
    private TagViewModel tagViewModel;

    private TextInputLayout amountEditText;
    private TextInputLayout descriptionEditText;
    private TextInputLayout categoryEditText;
    private TextInputLayout payeeEditText;
    private TextInputLayout walletEditText;
    private TextInputLayout tagEditText;
    private TextInputLayout noteEditText;
    private TextView dateDisplay;
    private TextView dateSelected;
    private TextView locationText;
    private Switch locationSwitch;
    private Button captureBtn;
    private ImageView imageView;
    private Button saveBtn;
    private Button cancelBtn;

    // Location variables
    private boolean requestingLocationUpdates = false;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    //callback that keep monitored the internet connection
    private ConnectivityManager.NetworkCallback networkCallback;
    private Boolean isNetworkConnected = false;
    private Snackbar snackbar;

    private RequestQueue requestQueue;
    private final static String OSM_REQUEST_TAG = "OSM_REQUEST";

    //public NewTransactionFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_transaction, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();

        if (activity != null) {
            // Getting the views
            this.amountEditText = activity.findViewById(R.id.transaction_amount_TextInput);
            this.descriptionEditText = activity.findViewById(R.id.transaction_description_TextInput);
            this.categoryEditText = activity.findViewById(R.id.transaction_category_TextInput);
            this.payeeEditText = activity.findViewById(R.id.transaction_payee_TextInput);
            this.walletEditText = activity.findViewById(R.id.transaction_wallet_TextInput);
            this.tagEditText = activity.findViewById(R.id.transaction_tag_TextInput);
            this.dateDisplay = activity.findViewById(R.id.date_display);
            this.dateSelected = activity.findViewById(R.id.date_selected);
            this.noteEditText = activity.findViewById(R.id.transaction_note_TextInput);
            this.locationText = activity.findViewById(R.id.location_text);
            this.locationSwitch = activity.findViewById(R.id.switch_location);
            this.captureBtn = activity.findViewById(R.id.capture_button);
            this.imageView = activity.findViewById(R.id.imageView);
            this.saveBtn = getView().findViewById(R.id.transaction_save_button);
            this.cancelBtn = getView().findViewById(R.id.transaction_cancel_button);
            // Creation of needed viewMoldel to retrive data
            this.transactionViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TransactionViewModel.class);
            this.categoryViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CategoryViewModel.class);
            this.payeeViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(PayeeViewModel.class);
            this.walletViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(WalletViewModel.class);
            this.tagViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TagViewModel.class);



            requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean result) {
                            if (result) {
                                startLocationUpdates(activity);
                                Log.d("LAB", "PERMISSION GRANTED");
                            } else {
                                Log.d("LAB", "PERMISSION NOT GRANTED");
                                showDialog(activity);
                            }
                        }
                    });

            initializeLocation(activity);
            setupNetwork();

            setupDatePicker();
            setupChips();
            setupImageCapture();
            setupLocation();

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Bitmap bitmap = transactionViewModel.getBitmap().getValue();
                        String imageUriString;
                        if (bitmap != null) {
                            //method to save the image in the gallery of the device
                            imageUriString = String.valueOf(saveImage(bitmap, activity));
                            //Toast.makeText(activity,"Image Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            imageUriString = "ic_launcher_foreground";
                        }

                        /*addViewModel.addCardItem(new CardItem(imageUriString,
                                placeTextInputEditText.getText().toString(),
                                descriptionTextInputEditText.getText().toString(),
                                dateTextInputEditText.getText().toString()));

                        addViewModel.setImageBitmpap(null);*/



                        /*if (Utilities.checkDataValid(walletName, walletAmount, walletDescription)) {
                            this.walletViewModel.addWallet(new Wallet(walletName, walletDescription, Integer.parseInt(walletAmount), selectedColor.toString()));
                            Navigation.findNavController(v).navigate(R.id.action_new_wallet_to_nav_wallet);
                        } else {
                            Toast.makeText(activity.getBaseContext(), "Every field must be filled", Toast.LENGTH_LONG).show();
                        }*/

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            
            /*this.saveBtn.setOnClickListener(v -> {
                retriveData();

                TextInputLayout walletNameEditText = activity.findViewById(R.id.wallet_name_TextInput);
                String walletName = walletNameEditText.getEditText().getText().toString();
                TextInputLayout walletAmountEditText = activity.findViewById(R.id.wallet_initial_value);
                String walletAmount = walletAmountEditText.getEditText().getText().toString();
                this.walletViewModel.addWallet(new Wallet(walletName, "nice", Integer.parseInt(walletAmount), "null"));
            });*/

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null && requestingLocationUpdates) {
            registerNetworkCallback(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (requestingLocationUpdates && getActivity() != null) {
            startLocationUpdates(getActivity());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(OSM_REQUEST_TAG);
        }
        unRegisterNetworkCallback();
    }

    private void initializeLocation(Activity activity) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        locationRequest = LocationRequest.create();
        // Set the desired interval for active location updates, in milliseconds.
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Update UI with location data
                Location location = locationResult.getLastLocation();
                if (isNetworkConnected) {
                    //if internet connection is available, I can make the request
                    sendVolleyRequest(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

                    requestingLocationUpdates = false;
                    stopLocationUpdates();
                } else {
                    //if internet connection is not available, I'll show the user a snackbar
                    snackbar.show();
                }
            }
        };
    }

    private void setupNetwork() {
        // TODO idk if it works
        snackbar = Snackbar.make(activity.findViewById(R.id.nav_host_fragment),
                "No Internet Available",
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_WIRELESS_SETTINGS);
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        if (intent.resolveActivity(activity.getPackageManager()) != null) {
                            activity.startActivity(intent);
                        }
                    }
                });
        networkCallback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                isNetworkConnected = true;
                snackbar.dismiss();
                if (requestingLocationUpdates){
                    startLocationUpdates(activity);
                }
            }
            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                isNetworkConnected = false;
                snackbar.show();
            }
        };
        requestQueue = Volley.newRequestQueue(activity);
    }

    private void setupImageCapture() {
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there is a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        transactionViewModel.getBitmap().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });

    }

    private void setupLocation() {
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    requestingLocationUpdates = true;
                    registerNetworkCallback(activity);
                    startLocationUpdates(activity);
                }
            }
        });
    }

    private void setupChips() {
        // Category chip group
        ChipGroup categoryChipGroup = getView().findViewById(R.id.transaction_category_chip_group);
        setupCategoryChips(categoryChipGroup);
        // Payee chip group
        ChipGroup payeeChipGroup = getView().findViewById(R.id.transaction_payee_chip_group);
        setupPayeeChips(payeeChipGroup);
        // Wallet chip group
        ChipGroup walletChipGroup = getView().findViewById(R.id.transaction_wallet_chip_group);
        setupWalletChips(walletChipGroup);
        // Tag chip group
        ChipGroup tagChipGroup = getView().findViewById(R.id.transaction_tag_chip_group);
        setupTagChips(tagChipGroup);
    }

    private void setupPayeeChips(ChipGroup payeeChipGroup) {
        payeeViewModel.getPayeeList().observe((LifecycleOwner) activity, new Observer<List<Payee>>() {
            @Override
            public void onChanged(List<Payee> payee) {
                payeeChipGroup.removeAllViews();
                for (Payee p : payee) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, payeeChipGroup, false);
                    chip.setId(View.generateViewId());
                    chip.setText(p.getName());
                    payeeChipGroup.addView(chip);
                }
            }
        });
        payeeChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                // Set the chosen payee
                payeeEditText.getEditText().setText(chip.getText());
            }
        });
        ImageButton add = getView().findViewById(R.id.add_payee);
        add.setOnClickListener(v -> {
            if (Utilities.checkDataValid(payeeEditText.getEditText().getText().toString())) {
                payeeViewModel.addPayee(new Payee(payeeEditText.getEditText().getText().toString()));
            } else {
                Toast.makeText(activity.getBaseContext(), "Insert a payee name to create a new one", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupCategoryChips(ChipGroup categoryChipGroup) {
        categoryViewModel.getCategoryList().observe((LifecycleOwner) activity, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> category) {
                categoryChipGroup.removeAllViews();
                for (Category cat : category) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, categoryChipGroup, false);
                    chip.setId(View.generateViewId());
                    chip.setText(cat.getName());
                    categoryChipGroup.addView(chip);
                }
            }
        });
        categoryChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                // Set the chosen category
                categoryEditText.getEditText().setText(chip.getText());
            }
        });
        ImageButton add = getView().findViewById(R.id.add_category);
        add.setOnClickListener(v -> {
            if (Utilities.checkDataValid(categoryEditText.getEditText().getText().toString())) {
                categoryViewModel.addCategory(new Category(categoryEditText.getEditText().getText().toString()));
            } else {
                Toast.makeText(activity.getBaseContext(), "Insert a category name to create a new one", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupWalletChips(ChipGroup walletChipGroup) {
        walletViewModel.getWalletList().observe((LifecycleOwner) activity, new Observer<List<Wallet>>() {
            @Override
            public void onChanged(List<Wallet> wallet) {
                walletChipGroup.removeAllViews();
                for (Wallet w : wallet) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, walletChipGroup, false);
                    chip.setId(View.generateViewId());
                    chip.setText(w.getName());
                    walletChipGroup.addView(chip);
                }
            }
        });
        walletChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                // Set the chosen category
                walletEditText.getEditText().setText(chip.getText());
            }
        });
    }

    private void setupTagChips(ChipGroup tagChipGroup) {
        tagViewModel.getTagList().observe((LifecycleOwner) activity, new Observer<List<Tag>>() {
            @Override
            public void onChanged(List<Tag> tag) {
                tagChipGroup.removeAllViews();
                for (Tag t : tag) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, tagChipGroup, false);
                    chip.setId(View.generateViewId());
                    chip.setText(t.getName());
                    tagChipGroup.addView(chip);
                }
            }
        });
        // TODO List<Chips> per avere quelle cliccate (forse meglio con un map id-chip cosi posso toglierle e metterle velocemente)
        tagChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                // Set the chosen category
                tagEditText.getEditText().setText(chip.getText());
            }
        });
        ImageButton add = getView().findViewById(R.id.add_tag);
        add.setOnClickListener(v -> {
            if (Utilities.checkDataValid(tagEditText.getEditText().getText().toString())) {
                tagViewModel.addTag(new Tag(tagEditText.getEditText().getText().toString()));
            } else {
                Toast.makeText(activity.getBaseContext(), "Insert a TAG to create a new one", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupDatePicker() {
        ImageButton calendar = getView().findViewById(R.id.calendar);
        calendar.setOnClickListener(v -> {
            DatePicker newFragment = new DatePicker(getActivity().findViewById(R.id.date_display));
            newFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
            newFragment.getDataReady().observe(getActivity(), value -> {
                if (value) {
                    Log.e("date", "" + newFragment.getYear() + " " + newFragment.getMonth() + " " + newFragment.getDay());
                }
            });
        });
    }

    /**
     * Method called to save the image taken as a file in the gallery
     * @param bitmap the image taken
     * @throws IOException if there are some issue with the creation of the image file
     * @return the Uri of the image saved
     */
    private Uri saveImage(Bitmap bitmap, Activity activity) throws IOException {
        // Create an image file name
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ITALY).format(new Date());
        String name = "JPEG_" + timeStamp + "_.png";
        ContentResolver resolver = activity.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpg");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Log.d("LAB-AddFragment", String.valueOf(imageUri));
        OutputStream fos = resolver.openOutputStream(imageUri);
        //for the jpeg quality, it goes from 0 to 100
        //for the png, the quality is ignored
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        if (fos != null) {
            fos.close();
        }
        return imageUri;
    }

    private Transaction retriveData() {
        String amount = amountEditText.getEditText().getText().toString();
        String description = descriptionEditText.getEditText().getText().toString();
        String category = categoryEditText.getEditText().getText().toString();
        String payee = payeeEditText.getEditText().getText().toString();
        String date = dateSelected.getText().toString();
        String wallet = walletEditText.getEditText().getText().toString();
        //List<Chips>aaas;

        return null;
    }

    /**
     * Method called to start requesting the updates for the Location
     * It checks also the permission fo the Manifest.permission.ACCESS_FINE_LOCATION
     *
     * @param activity the current Activity
     */
    private void startLocationUpdates(Activity activity) {
        final String PERMISSION_REQUESTED = Manifest.permission.ACCESS_FINE_LOCATION;
        //permission granted
        if (ActivityCompat.checkSelfPermission(activity, PERMISSION_REQUESTED) == PackageManager.PERMISSION_GRANTED) {
            statusGPSCheck(activity);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_REQUESTED)) {
            //if the permission was denied before
            showDialog(activity);
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(PERMISSION_REQUESTED);
        }
    }

    /**
     * Method called to stop the updates for the Location
     */
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    /**
     * Method called to register the NetworkCallback in the ConnectivityManager (SDK >= N) or
     * to get info about the network with NetworkInfo (Android 6)
     *
     * @param activity the current Activity
     */
    private void registerNetworkCallback(Activity activity) {
        Log.d("LAB","registerNetworkCallback");
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(networkCallback);
            } else {
                //Class deprecated since API 29 (android 10) but used for android 6
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                isNetworkConnected = networkInfo != null && networkInfo.isConnected();
            }
        } else {
            isNetworkConnected = false;
        }
    }

    /**
     * Method called to unregister the NetworkCallback (SDK >= N) or
     * to dismiss the snackbar in Android 6 (it works only if the snackbar is still visible)
     *
     */
    private void unRegisterNetworkCallback() {
        if (getActivity() != null) {
            Log.d("LAB", "unRegisterNetworkCallback");
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        connectivityManager.unregisterNetworkCallback(networkCallback);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    snackbar.dismiss();
                }
            }
        }
    }

    /**
     * Method called to check the status of the GPS (on or off)
     * If the GPS is off, a dialog will be displayed to the user to turn it on
     *
     * @param activity the current Activity
     */
    private void statusGPSCheck(Activity activity) {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //if gps is off, show the alert message
            new AlertDialog.Builder(activity)
                    .setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> activity.startActivity(
                            new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                    .create()
                    .show();
        }
    }

    /**
     *Method called to create a new Dialog to check the user for previously denied permission
     *
     * @param activity the current Activity
     */
    private void showDialog(Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage("Permission was denied, but is needed for the gps functionality.")
                .setCancelable(false) //Sets whether this dialog is cancelable with the BACK key.
                .setPositiveButton("OK", (dialog, id) -> activity.startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    /**
     * Method called to query the OpenStreetMap API
     * @param latitude latitude of the device
     * @param longitude longitude of the device
     */
    private void sendVolleyRequest(String latitude, String longitude){
        String url ="https://nominatim.openstreetmap.org/reverse?lat="+latitude+
                "&lon="+longitude+"&format=jsonv2&limit=1";

        // Request a jsonObject response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    locationText.setText(response.get("name").toString());
                    unRegisterNetworkCallback();
                } catch (JSONException e) {
                    locationText.setText("Error finding your current location");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LAB", error.toString());
            }
        });

        jsonObjectRequest.setTag(OSM_REQUEST_TAG);
        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);
    }
}