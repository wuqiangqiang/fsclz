package com.zrodo.fsclz.utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by grandry.xu on 15-9-18.
 */
public class JSONUtil {
        public static JSONObject toJSONObject(String json){
            JSONObject obj=null;
            try{
                obj=new JSONObject(json);
            }catch (Exception e){
                throw new RuntimeException("json parser error");
            }
            return obj;
        }

        public static JSONArray toJSONArray(String json){
            JSONArray array=null;
            try{
                array=new JSONArray(json);
            }catch (Exception e){
                throw  new RuntimeException("json array parser error");
            }
            return array;
        }
}
