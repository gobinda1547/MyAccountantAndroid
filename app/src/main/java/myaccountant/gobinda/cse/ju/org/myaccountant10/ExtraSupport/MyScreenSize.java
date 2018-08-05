package myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport;

public class MyScreenSize{

    public static int SCREEN_WIDTH;
    public  static int SCREEN_HEIGHT;

    static int CAMERA_PICTURE_WIDTH;
    static int CAMERA_PICTURE_HEIGHT;

    public static void initializeScreenSize(int WIDTH, int HEIGHT){
        MyScreenSize.SCREEN_WIDTH = WIDTH;
        MyScreenSize.SCREEN_HEIGHT = HEIGHT;
    }

    public static void initializeCameraPictureSize(int WIDTH, int HEIGHT){
        MyScreenSize.CAMERA_PICTURE_WIDTH = WIDTH;
        MyScreenSize.CAMERA_PICTURE_HEIGHT = HEIGHT;
    }



}
