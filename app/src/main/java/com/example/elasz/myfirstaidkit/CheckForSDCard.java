package com.example.elasz.myfirstaidkit;
import android.os.Environment;
/**
 * Created by elasz on 05.09.2018.
 */

public class CheckForSDCard {
    //Check If SD Card is present or not method
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
