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

import myaccountant.gobinda.cse.ju.org.myaccountant10.add_account_feature.CameraPreview;

public class MyImageProcessing {

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

    public static Bitmap cropSquareImage(Bitmap bitmap){

        int imageSize = Math.min(MyScreenSize.SCREEN_WIDTH, MyScreenSize.SCREEN_HEIGHT);
        int squareLen = Math.min(  MyScreenSize.CAMERA_PICTURE_WIDTH, MyScreenSize.CAMERA_PICTURE_HEIGHT);

        Matrix matrix = new Matrix();
        Bitmap cropped = Bitmap.createBitmap(bitmap, 0, 10, squareLen, squareLen, matrix, true);
        bitmap.recycle();

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(cropped, imageSize, imageSize, true);
        cropped.recycle();

        return scaledBitmap;
    }

}
