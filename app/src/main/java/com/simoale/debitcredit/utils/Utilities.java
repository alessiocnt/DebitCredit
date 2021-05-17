package com.simoale.debitcredit.utils;


import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    

    public static Bitmap getImageBitmap(Activity activity, Uri currentPhotoUri) {
        ContentResolver resolver = activity.getApplicationContext()
                .getContentResolver();
        try {
            InputStream stream = resolver.openInputStream(currentPhotoUri);
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Date getDateFromString(String date) {
        Date dateConverted = null;
        try {
            dateConverted = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateConverted;
    }

    public static String getStringFromDate(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }
}