package myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport;


import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import java.io.ByteArrayOutputStream;

import myaccountant.gobinda.cse.ju.org.myaccountant10.take_image_feature.CameraPreview;

public class ImageProcessingSupport {

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth()/2;

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

    public static Bitmap convertIntoBitmap(byte[] imageArray){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length, options);
    }

    public static byte[] convertIntoByteArray(Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    public static Bitmap cropSquareImage(Bitmap bitmap){

        int squareLen = Math.min(  SizeRelatedSupport.CAMERA_PICTURE_WIDTH, SizeRelatedSupport.CAMERA_PICTURE_HEIGHT);
        int imageSize = Math.min(SizeRelatedSupport.SCREEN_WIDTH, SizeRelatedSupport.SCREEN_HEIGHT);

        Matrix matrix = new Matrix();
        Bitmap cropped = Bitmap.createBitmap(bitmap, 0, 10, squareLen, squareLen, matrix, true);
        bitmap.recycle();

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(cropped, imageSize, imageSize, true);
        cropped.recycle();

        return scaledBitmap;
    }

    public static Bitmap getDefaultAccountImage(Context context){
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.b);
    }

}
