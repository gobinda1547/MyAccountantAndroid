package myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport;

public class SizeRelatedSupport {

    public static boolean screenSizeInitialized;

    public static int SCREEN_WIDTH;
    public  static int SCREEN_HEIGHT;

    static int CAMERA_PICTURE_WIDTH;
    static int CAMERA_PICTURE_HEIGHT;

    public static void initializeScreenSize(int WIDTH, int HEIGHT){
        SizeRelatedSupport.SCREEN_WIDTH = WIDTH;
        SizeRelatedSupport.SCREEN_HEIGHT = HEIGHT;
        screenSizeInitialized = true;
    }

    public static void initializeCameraPictureSize(int WIDTH, int HEIGHT){
        SizeRelatedSupport.CAMERA_PICTURE_WIDTH = WIDTH;
        SizeRelatedSupport.CAMERA_PICTURE_HEIGHT = HEIGHT;
    }



}
