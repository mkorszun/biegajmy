package com.biegajmy.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PhotoUtils {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 400;
    private static final int COMPRESSION = 85;
    private static final String EXT = ".jpg";
    private static final String IMAGE = "image/*";

    public static final int CAMERA_REQUEST = 1888;
    public static final int SELECT_PICTURE = 1;

    public static File scale(Context context, Bitmap bitmap, int orientation) {
        try {
            File outputFile = getFileForImage(context);
            bitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true);

            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            FileOutputStream stream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION, stream);
            return outputFile;
        } catch (Exception e) {
            return null;
        }
    }

    public static File scale(Context context, Uri uri, int orientation) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream is = contentResolver.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return scale(context, bitmap, orientation);
        } catch (Exception e) {
            return null;
        }
    }

    public static void fromGallery(Fragment fragment) {
        Intent intent = new Intent();
        intent.setType(IMAGE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fragment.startActivityForResult(Intent.createChooser(intent, ""), SELECT_PICTURE);
    }

    public static void fromCamera(Fragment fragment) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        fragment.startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public static void set(String url, Context context, ImageView view) {
        if (!url.isEmpty()) Picasso.with(context).load(url).into(view);
    }

    public static int getOrientation(Context context, Uri image) {
        int orientation = 0;
        String[] orientationColumn = { MediaStore.Images.Media.ORIENTATION };
        Cursor cur = context.getContentResolver().query(image, orientationColumn, null, null, null);
        if (cur != null && cur.moveToFirst()) orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        return orientation;
    }

    public static File getFileForImage(Context context) throws IOException {
        String name = String.valueOf(System.currentTimeMillis());
        return SystemUtils.createTMPFile(context, name, EXT);
    }
}
