package myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class InternalMemorySupport {

    public static boolean createAppFolder(Context context){
        try{
            File mydir = context.getDir(NameRelatedSupport.APP_FOLDER_NAME, Context.MODE_PRIVATE);
            return mydir.exists() || mydir.mkdirs();
        }catch (Exception e){
            return false;
        }
    }

    public static Bitmap getImageFileFromThisLocation(String imageLocation){
        if(imageLocation == null || imageLocation.length() == 0){
            return null;
        }
        try{
            return  BitmapFactory.decodeFile(imageLocation);
        }catch(Exception e){
            return null;
        }
    }

    public static String writeImageFileInInternalMemory(Context context,Bitmap bitmap){
        int randomNumber = new Random().nextInt(1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = sdf.format(new Date()).concat(String.valueOf(randomNumber)).concat(".png");
        Log.d("filename",fileName);

        File internalStorage = context.getDir(NameRelatedSupport.APP_FOLDER_NAME, Context.MODE_PRIVATE);
        File reportFilePath = new File(internalStorage, fileName);
        String picturePath = reportFilePath.toString();

        try {
            FileOutputStream fos = new FileOutputStream(reportFilePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return picturePath;
    }


    public static boolean deleteImageFileFromInternalMemory(String fileName){
        if (fileName != null && fileName.length() != 0) {
            File reportFilePath = new File(fileName);
            return reportFilePath.delete();
        }
        return false;
    }


}
