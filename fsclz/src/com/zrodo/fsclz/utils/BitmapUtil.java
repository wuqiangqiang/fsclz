/*
 * Copyright (c) 2013. ������̩��Ϣ�������޹�˾ All rights reserved.
 * For more information,please visit http://www.rektec.com.cn/
 */

package com.zrodo.fsclz.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public final class BitmapUtil {

    /**
     * 将BASE64编码后的图片内容转换成Bitmap对象
     *
     * @param base64Source BASE64图片源
     * @return Bitmap形式的图片
     */
    public static Bitmap fromBase64(String base64Source) {
        byte[] decodedValue = Base64.decode(base64Source, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(decodedValue, 0,
                decodedValue.length);
    }

    /**
     * 将图片压缩至指定大小
     *
     * @param bitmap
     * @param reqSize 大小，单位：KB
     * @return 压缩后的图片
     */
    public static byte[] compress(Bitmap bitmap, int reqSize) {
        //图片的压缩质量
        int quality = 100;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length > reqSize * 1024) {
            baos.reset();
            //每次压缩质量-10
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        return baos.toByteArray();
    }
    
    //添加水印图片
    private Bitmap createWaterMaskImage(Context gContext, Bitmap src, Bitmap watermark)
    {
 
        String tag = "createBitmap";
        Log.d(tag, "create a new bitmap");
        if (src == null)
        {
            return null;
        }       
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, 20, 20, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    /**
     * 将图片缩放至指定的高度和宽度
     *
     * @param pathName  源图片路径
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap scale(String pathName, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        final int height = (options.outHeight > options.outWidth && reqHeight > reqWidth) || (options.outHeight < options.outWidth && reqHeight < reqWidth)
                ? options.outHeight
                : options.outWidth;
        final int width = (options.outHeight > options.outWidth && reqHeight > reqWidth) || (options.outHeight < options.outWidth && reqHeight < reqWidth)
                ? options.outWidth
                : options.outHeight;

        int inSampleSize = 1;  //缩放比例

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }

        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    /**
     * 对图片进行缩放并压缩至指定的大小(KB)
     *
     * @param pathName  源图片路径
     * @param reqWidth  缩放后的宽度
     * @param reqHeight 缩放后的高度
     * @param reqSize   压缩后的图片大小
     * @return 压缩后的图片
     */
    public static byte[] scaleAndCompress(String pathName, int reqWidth, int reqHeight, int reqSize) {
        Bitmap bitmap = scale(pathName, reqWidth, reqHeight);
        byte[] bytes = compress(bitmap, reqSize);

        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }

        return bytes;
    }

    public static String convertToBase64(Bitmap bitmap) {
        String base64 = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        return base64;
    }

    public static Bitmap scaleBitmapSmall(Bitmap bitmap,float width_percent,float height_percent){
        Matrix matrix = new Matrix();
        matrix.postScale(width_percent,height_percent); //长和宽放大缩小的比例(如果要缩放变大，则此值大于1)
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }
    
    
    
    //视频缩略图
    public static Bitmap getVideoThumbnail(String filePath) {  
        Bitmap bitmap = null;  
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
        try {  
            retriever.setDataSource(filePath);  
            bitmap = retriever.getFrameAtTime();  
        }   
        catch(IllegalArgumentException e) {  
            e.printStackTrace();  
        }   
        catch (RuntimeException e) {  
            e.printStackTrace();  
        }   
        finally {  
            try {  
                retriever.release();  
            }   
            catch (RuntimeException e) {  
                e.printStackTrace();  
            }  
        }  
        return bitmap;  
    }
}
