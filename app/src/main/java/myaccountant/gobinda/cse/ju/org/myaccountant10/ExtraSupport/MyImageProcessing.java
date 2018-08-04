package myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;

import java.io.IOException;

import myaccountant.gobinda.cse.ju.org.myaccountant10.add_account_feature.CameraPreview;

/**
 * Created by gobinda22 on 8/4/2018.
 */

public class MyImageProcessing {

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        //Log.d("width = ", String.valueOf(bitmap.getWidth()));
        //Log.d("hight = ", String.valueOf(bitmap.getHeight()));

        final RectF rectF = new RectF(rect);
        int pixels = bitmap.getWidth()/2;
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap rotateImage(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        if(CameraPreview.showingFrontCamera) {
            matrix.preScale(1, -1);
        }
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    public static Bitmap processImage(byte[] data) {
        // Determine the width/height of the image
        int IMAGE_SIZE = 300;
        //int width = camera.getParameters().getPictureSize().width;
        //int height = camera.getParameters().getPictureSize().height;
        int width = IMAGE_SIZE, height = IMAGE_SIZE;

        // Load the bitmap from the byte array
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Rotate and crop the image into a square
        int croppedWidth = (width > height) ? height : width;
        int croppedHeight = (width > height) ? height : width;

        Matrix matrix = new Matrix();
        //matrix.postRotate(IMAGE_ORIENTATION);
        Bitmap cropped = Bitmap.createBitmap(bitmap, 0, 0, croppedWidth, croppedHeight, matrix, true);
        bitmap.recycle();

        // Scale down to the output size
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(cropped, IMAGE_SIZE, IMAGE_SIZE, true);
        cropped.recycle();

        return scaledBitmap;
    }
}
