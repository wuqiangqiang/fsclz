package com.zrodo.fsclz.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by grandry.xu on 15-9-18.
 */
public class NetworkUtil {

    public  static  boolean isAvailable(Context context){
        boolean availabe=false;
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager !=null){
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo !=null){
                if(networkInfo.getState()==NetworkInfo.State.CONNECTED){
                    availabe=true;
                }
            }
        }
        return availabe;
    }
}
