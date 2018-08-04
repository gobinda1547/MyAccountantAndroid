package myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport;


import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_list_feature.ShowAccountListActivity;

/**
 * Created by gobinda22 on 8/4/2018.
 */

public class MyScreenSize extends AppCompatActivity{

    private static int WIDTH;
    private static int HEIGHT;

    public static void initialize(int WIDTH, int HEIGHT){
        MyScreenSize.WIDTH = WIDTH;
        MyScreenSize.HEIGHT = HEIGHT;
    }

    public static int getWidth(){
        return WIDTH;
    }

    public static  int getHeight(){
        return HEIGHT;
    }

}
