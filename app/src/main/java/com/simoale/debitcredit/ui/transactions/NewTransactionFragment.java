package com.simoale.debitcredit.ui.transactions;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.material.snackbar.Snackbar;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.simoale.debitcredit.utils.Utilities.REQUEST_IMAGE_CAPTURE;

public class NewTransactionFragment extends Fragment {

    private Activity activity;
    private final TransactionType transactionType;

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
    private Map<Integer, Chip> tagSelected;
    private TextInputLayout noteEditText;
    private TextView dateDisplay;
    private String dateSelected;
    private TextView locationText;
    private Switch locationSwitch;
    private Button captureBtn;
    private ImageView imageView;
    private Button saveBtn;
    private Button cancelBtn;

    LocationUtils locationUtils;
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

    public NewTransactionFragment(TransactionType type) {
        this.transactionType = type;
    }

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

            this.locationUtils = new LocationUtils(activity);
            requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean result) {
                            if (result) {
                                locationUtils.startLocationUpdates();
                                Log.d("LAB", "PERMISSION GRANTED");
                            } else {
                                Log.d("LAB", "PERMISSION NOT GRANTED");
                                locationUtils.showDialog();
                            }
                        }
                    });
            locationUtils.initializeLocation(locationText);
            locationUtils.setupNetwork();

            setupDatePicker();
            setupChips();
            setupImageCapture();
            setupLocation();

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // Retrive data
                        Integer amount = transactionType.getType() * Math.abs(Integer.parseInt(amountEditText.getEditText().getText().toString()));
                        String description = descriptionEditText.getEditText().getText().toString();
                        String category = categoryEditText.getEditText().getText().toString();
                        String payee = payeeEditText.getEditText().getText().toString();
                        String wallet = walletEditText.getEditText().getText().toString();
                        List<Chip> tagChips = new ArrayList<>();
                        tagChips.addAll(tagSelected.values());
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

                        AtomicInteger walletId = new AtomicInteger();
                        walletViewModel.getWalletFromName(wallet).observe((LifecycleOwner) activity, w -> {
                            walletId.set(w.getId());
                            if (Utilities.checkDataValid(amount.toString(), category, dateSelected, wallet)) {
                                transactionViewModel.addTransaction(new Transaction(amount,
                                        description, category, payee, dateSelected, walletId.intValue(), walletId.intValue(), location, note, imageUriString));
                                Navigation.findNavController(v).navigate(R.id.action_newTransactionTabFragment2_to_nav_home2);
                            } else {
                                Toast.makeText(activity.getBaseContext(), "Every field must be filled", Toast.LENGTH_LONG).show();
                            }
                        });

                        transactionViewModel.setImageBitmpap(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            this.cancelBtn.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_newTransactionTabFragment2_to_nav_home2);
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
                imageView.setImageBitmap(bitmap);
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
        this.tagSelected = new HashMap<>();
        tagViewModel.getTagList().observe((LifecycleOwner) activity, new Observer<List<Tag>>() {
            @Override
            public void onChanged(List<Tag> tag) {
                tagChipGroup.removeAllViews();
                for (Tag t : tag) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, tagChipGroup, false);
                    chip.setId(View.generateViewId());
                    chip.setText(t.getName());

                    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                tagSelected.putIfAbsent(chip.getId(), chip);
                            } else {
                                tagSelected.remove(chip.getId());
                            }
                        }
                    });
                    tagChipGroup.addView(chip);
                }
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
        AtomicInteger year = new AtomicInteger(Calendar.getInstance().get(Calendar.YEAR));
        AtomicInteger month = new AtomicInteger(Calendar.getInstance().get(Calendar.MONTH));
        AtomicInteger day = new AtomicInteger(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        calendar.setOnClickListener(v -> {
            DatePicker newFragment = new DatePicker(year.get(), month.get(), day.get());
            newFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
            newFragment.getDataReady().observe(getActivity(), value -> {
                if (value) {
                    year.set(newFragment.getYear());
                    month.set(newFragment.getMonth());
                    day.set(newFragment.getDay());
                    dateSelected = String.format("%04d%02d%02d", newFragment.getYear(), newFragment.getMonth(), newFragment.getDay());
                    this.dateDisplay.setText(String.format("Date: %02d/%02d/%04d", newFragment.getDay(), newFragment.getMonth() + 1, newFragment.getYear()));
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

}