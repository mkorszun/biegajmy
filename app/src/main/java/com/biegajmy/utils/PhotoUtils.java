package com.biegajmy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.FileOutputStream;

public class PhotoUtils {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final int COMPRESSION = 85;
    private static final String EXT = ".jpg";

    public static File scale(Context context, String originlPath) {
        try {
            File outputDir = context.getCacheDir();
            File originalFile = new File(originlPath);
            File outputFile = File.createTempFile(originalFile.getName(), EXT, outputDir);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(originlPath, bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true);
            FileOutputStream stream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION, stream);
            return outputFile;
        } catch (Exception e) {
            return null;
        }
    }

    public static File scale(Context context, Bitmap bitmap) {
        try {
            File outputDir = context.getCacheDir();
            String name = String.valueOf(System.currentTimeMillis());
            File outputFile = File.createTempFile(name, EXT, outputDir);

            bitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true);
            FileOutputStream stream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION, stream);
            return outputFile;
        } catch (Exception e) {
            return null;
        }
    }
}
