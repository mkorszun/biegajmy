package com.biegajmy.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PhotoUtils {

    private static final String TAG = PhotoUtils.class.getName();

    private static final int HEIGHT = 400;
    private static final int COMPRESSION = 85;

    private static final String EXT = ".jpg";
    private static final String IMAGE = "image/*";

    public static final int CAMERA_REQUEST = 1888;
    public static final int SELECT_PICTURE = 1;

    public static File scale(Context context, Bitmap bitmap, int orientation) {
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            File outputFile = getFileForImage(context);

            float aspectRatio = ((float) bitmap.getHeight() / (float) bitmap.getWidth());
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (HEIGHT / aspectRatio), HEIGHT, true);
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
        int orientation = getOrientationFromExif(image.getPath());
        if (orientation <= 0) {
            orientation = getOrientationFromMediaStore(context, image);
        }

        return orientation;
    }

    public static File getFileForImage(Context context) throws IOException {
        String name = String.valueOf(System.currentTimeMillis());
        return SystemUtils.createTMPFile(context, name, EXT);
    }

    private static int getOrientationFromExif(String imagePath) {
        int orientation = -1;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                    orientation = 0;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to get image exif orientation", e);
        }

        return orientation;
    }

    private static int getOrientationFromMediaStore(Context context, Uri imageUri) {
        if (imageUri == null) return -1;

        String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
        Cursor cursor = context.getContentResolver().query(imageUri, projection, null, null, null);

        int orientation = -1;

        if (cursor != null && cursor.moveToFirst()) {
            orientation = cursor.getInt(0);
            cursor.close();
        }

        return orientation;
    }
}
