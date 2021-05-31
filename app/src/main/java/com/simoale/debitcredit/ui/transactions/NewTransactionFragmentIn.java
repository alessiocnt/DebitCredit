package com.simoale.debitcredit.ui.transactions;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.android.volley.RequestQueue;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.simoale.debitcredit.R;
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
import com.simoale.debitcredit.utils.LocationUtils;
import com.simoale.debitcredit.utils.Utilities;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import static com.simoale.debitcredit.utils.Utilities.REQUEST_IMAGE_CAPTURE;

public class NewTransactionFragmentIn extends Fragment {

    private Activity activity;
    private final TransactionType transactionType;

    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;
    private PayeeViewModel payeeViewModel;
    private WalletViewModel walletViewModel;
    private TagViewModel tagViewModel;
    private TransactionTagViewModel transactionTagViewModel;

    private TextInputLayout amountEditText;
    private TextInputLayout descriptionEditText;
    String categorySelected;
    String payeeSelected;
    String walletSelected;
    private List<String> tagSelected;
    private TextInputLayout noteEditText;
    private TextView dateDisplay;
    private String dateSelected;
    private TextView locationText;
    private Switch locationSwitch;
    private Button captureBtn;
    private ImageView imageView;
    private Button saveBtn;
    private Button cancelBtn;

    // Location variables
    LocationUtils locationUtils;
    private boolean requestingLocationUpdates = false;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private RequestQueue requestQueue;
    private final static String OSM_REQUEST_TAG = "OSM_REQUEST";

    public NewTransactionFragmentIn(TransactionType type) {
        this.transactionType = type;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_transaction_in, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();

        if (activity != null) {
            // Getting the views
            setupUi();
            // Creation of needed viewMoldel to retrive data
            this.transactionViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TransactionViewModel.class);
            this.categoryViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CategoryViewModel.class);
            this.payeeViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(PayeeViewModel.class);
            this.walletViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(WalletViewModel.class);
            this.tagViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TagViewModel.class);
            this.transactionTagViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TransactionTagViewModel.class);

            this.locationUtils = new LocationUtils(activity);
            requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    result -> {
                        if (result) {
                            locationUtils.startLocationUpdates();
                            Log.d("LAB", "PERMISSION GRANTED");
                        } else {
                            Log.d("LAB", "PERMISSION NOT GRANTED");
                            locationUtils.showDialog();
                        }
                    });
            locationUtils.initializeLocation(locationText, requestPermissionLauncher);
            locationUtils.setupNetwork();

            setupDatePicker();
            setupChips();
            setupImageCapture();
            setupLocation();


            saveBtn.setOnClickListener(v -> {
                try {
                    // Retrive data
                    String amountString = amountEditText.getEditText().getText().toString();
                    String description = descriptionEditText.getEditText().getText().toString();
                    String location = locationText.getText().toString();
                    String note = noteEditText.getEditText().getText().toString();
                    Bitmap bitmap = transactionViewModel.getBitmap().getValue();
                    String imageUriString;
                    if (bitmap != null) {
                        //method to save the image in the gallery of the device
                        imageUriString = String.valueOf(saveImage(bitmap, activity));
                        //Toast.makeText(activity,"Image Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        imageUriString = "ic_launcher_foreground";
                    }
                    // Insert data
                    Wallet currentWallet = walletViewModel.getWalletFromName(walletSelected).getValue();
                    if (Utilities.checkDataValid(amountString, categorySelected, dateSelected, walletSelected)) {
                        Float amount = transactionType.getType() * Math.abs(Float.parseFloat(amountString.replace(',', '.')));
                        transactionViewModel.addTransaction(new Transaction(amount,
                                description, categorySelected, payeeSelected, dateSelected, currentWallet.getId(), currentWallet.getId(), location, note, imageUriString), tagSelected);
//                            walletViewModel.updateBalance(currentWallet.getId(), amount);
                        Navigation.findNavController(v).navigate(R.id.action_newTransactionTabFragment_to_nav_home);
                    } else {
                        Toast.makeText(activity.getBaseContext(), "All fields are required", Toast.LENGTH_LONG).show();
                    }
                    transactionViewModel.setImageBitmpap(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.cancelBtn.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_newTransactionTabFragment_to_nav_home);
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null && requestingLocationUpdates) {
            locationUtils.registerNetworkCallback();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (requestingLocationUpdates && getActivity() != null) {
            locationUtils.startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        locationUtils.stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(OSM_REQUEST_TAG);
        }
        locationUtils.unRegisterNetworkCallback();
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
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
                transactionViewModel.getBitmap().removeObservers(getViewLifecycleOwner());
            }
        });
    }

    private void setupLocation() {
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    locationUtils.setRequestingLocationUpdates(true);
                    locationUtils.registerNetworkCallback();
                    locationUtils.startLocationUpdates();
                }
            }
        });
    }

    private void setupChips() {
        // Category chip group
        ChipGroup categoryChipGroup = getView().findViewById(R.id.transaction_in_category_chip_group);
        setupCategoryChips(categoryChipGroup);
        // Payee chip group
        ChipGroup payeeChipGroup = getView().findViewById(R.id.transaction_in_payee_chip_group);
        setupPayeeChips(payeeChipGroup);
        // Wallet chip group
        ChipGroup walletChipGroup = getView().findViewById(R.id.transaction_in_wallet_chip_group);
        setupWalletChips(walletChipGroup);
        // Tag chip group
        ChipGroup tagChipGroup = getView().findViewById(R.id.transaction_in_tag_chip_group);
        setupTagChips(tagChipGroup);
    }

    private void setupPayeeChips(ChipGroup payeeChipGroup) {
        payeeViewModel.getPayeeList().observe((LifecycleOwner) activity, payee -> {
            payeeChipGroup.removeAllViews();
            for (Payee p : payee) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, payeeChipGroup, false);
                chip.setId(View.generateViewId());
                chip.setText(p.getName());
                payeeChipGroup.addView(chip);
            }

        });
        payeeChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                // Set the chosen payee
                payeeSelected = chip.getText().toString();
            }
        });
        ImageButton add = getView().findViewById(R.id.transaction_in_add_payee);
        add.setOnClickListener(v -> {
            // Generate dialog for insertion
            View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_add, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity).setView(dialogView);
            EditText editText = (EditText) dialogView.findViewById(R.id.dialog_add_InputEditText);
            TextInputLayout layout = dialogView.findViewById(R.id.dialog_add_TextInput);
            layout.setHint("New payee name");
            dialogBuilder.setMessage("Remember that an item must be unique!")
                    .setCancelable(false) //Sets whether this dialog is cancelable with the BACK key.
                    .setPositiveButton("Save", (dialog, id) -> {
                        if (Utilities.checkDataValid(editText.getText().toString())) {
                            payeeViewModel.addPayee(new Payee(editText.getText().toString()));
                        } else {
                            Toast.makeText(activity.getBaseContext(), "Insert a payee name to create a new one", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });
    }

    private void setupCategoryChips(ChipGroup categoryChipGroup) {
        categoryViewModel.getCategoryList().observe((LifecycleOwner) activity, category -> {
            categoryChipGroup.removeAllViews();
            for (Category cat : category) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, categoryChipGroup, false);
                chip.setId(View.generateViewId());
                chip.setText(cat.getName());
                categoryChipGroup.addView(chip);
            }

        });
        categoryChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                // Set the chosen category
                categorySelected = chip.getText().toString();
            }
        });
        ImageButton add = getView().findViewById(R.id.transaction_in_add_category);
        add.setOnClickListener(v -> {
            View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_add, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity).setView(dialogView);
            EditText editText = (EditText) dialogView.findViewById(R.id.dialog_add_InputEditText);
            TextInputLayout layout = dialogView.findViewById(R.id.dialog_add_TextInput);
            layout.setHint("New category name");
            dialogBuilder.setMessage("Remember that an item must be unique!")
                    .setCancelable(false) //Sets whether this dialog is cancelable with the BACK key.
                    .setPositiveButton("Save", (dialog, id) -> {
                        if (Utilities.checkDataValid(editText.getText().toString())) {
                            categoryViewModel.addCategory(new Category(editText.getText().toString()));
                        } else {
                            Toast.makeText(activity.getBaseContext(), "Insert a category name to create a new one", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });
    }

    private void setupWalletChips(ChipGroup walletChipGroup) {
        walletViewModel.getWalletList().observe((LifecycleOwner) activity, wallet -> {
            walletChipGroup.removeAllViews();
            for (Wallet w : wallet) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, walletChipGroup, false);
                chip.setId(View.generateViewId());
                chip.setText(w.getName());
                walletChipGroup.addView(chip);
            }

        });
        walletChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                // Set the chosen category
                walletSelected = chip.getText().toString();
            }
        });
    }

    private void setupTagChips(ChipGroup tagChipGroup) {
        this.tagSelected = new ArrayList<>();
        tagViewModel.getTagList().observe((LifecycleOwner) activity, tag -> {
            tagChipGroup.removeAllViews();
            for (Tag t : tag) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, tagChipGroup, false);
                chip.setId(View.generateViewId());
                chip.setText(t.getName());
                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            tagSelected.add(chip.getText().toString());
                        } else {
                            tagSelected.remove(chip.getText().toString());
                        }
                    }
                });
                tagChipGroup.addView(chip);

            }
        });
        ImageButton add = getView().findViewById(R.id.transaction_in_add_tag);
        add.setOnClickListener(v -> {
            View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_add, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity).setView(dialogView);
            EditText editText = (EditText) dialogView.findViewById(R.id.dialog_add_InputEditText);
            TextInputLayout layout = dialogView.findViewById(R.id.dialog_add_TextInput);
            layout.setHint("New tag name");
            dialogBuilder.setMessage("Remember that an item must be unique!")
                    .setCancelable(false) //Sets whether this dialog is cancelable with the BACK key.
                    .setPositiveButton("Save", (dialog, id) -> {
                        if (Utilities.checkDataValid(editText.getText().toString())) {
                            tagViewModel.addTag(new Tag(editText.getText().toString()));
                        } else {
                            Toast.makeText(activity.getBaseContext(), "Insert a tag name to create a new one", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });
    }

    private void setupDatePicker() {
        ImageButton calendar = getView().findViewById(R.id.transaction_in_calendar);
        AtomicInteger year = new AtomicInteger(Calendar.getInstance().get(Calendar.YEAR));
        AtomicInteger month = new AtomicInteger(Calendar.getInstance().get(Calendar.MONTH));
        AtomicInteger day = new AtomicInteger(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        calendar.setOnClickListener(v -> {
            DatePicker datePicker = new DatePicker(year.get(), month.get(), day.get());
            datePicker.show(requireActivity().getSupportFragmentManager(), "datePicker");
            datePicker.getDataReady().observe(getActivity(), value -> {
                if (value) {
                    year.set(datePicker.getYear());
                    month.set(datePicker.getMonth());
                    day.set(datePicker.getDay());
                    dateSelected = String.format("%04d%02d%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDay());
                    this.dateDisplay.setText(String.format("Date: %02d/%02d/%04d", datePicker.getDay(), datePicker.getMonth() + 1, datePicker.getYear()));
                }
            });
        });
    }

    /**
     * Method called to save the image taken as a file in the gallery
     *
     * @param bitmap the image taken
     * @return the Uri of the image saved
     * @throws IOException if there are some issue with the creation of the image file
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

    public void setupUi() {
        this.amountEditText = activity.findViewById(R.id.transaction_in_amount_TextInput);
        this.descriptionEditText = activity.findViewById(R.id.transaction_in_description_TextInput);
        this.dateDisplay = activity.findViewById(R.id.transaction_in_date_display);
        this.noteEditText = activity.findViewById(R.id.transaction_in_note_TextInput);
        this.locationText = activity.findViewById(R.id.transaction_in_location_text);
        this.locationSwitch = activity.findViewById(R.id.transaction_in_switch_location);
        this.captureBtn = activity.findViewById(R.id.transaction_in_capture_button);
        this.imageView = activity.findViewById(R.id.transaction_in_imageView);
        this.saveBtn = getView().findViewById(R.id.transaction_in_save_button);
        this.cancelBtn = getView().findViewById(R.id.transaction_in_cancel_button);
    }

    @Override
    // Needs to ensure that the current Fragment in detached from the FragmentManager
    public void onDestroy() {
        super.onDestroy();
        categoryViewModel.getCategoryList().removeObservers((LifecycleOwner) activity);
        payeeViewModel.getPayeeList().removeObservers((LifecycleOwner) activity);
        walletViewModel.getWalletList().removeObservers((LifecycleOwner) activity);
        tagViewModel.getTagList().removeObservers((LifecycleOwner) activity);
    }
}