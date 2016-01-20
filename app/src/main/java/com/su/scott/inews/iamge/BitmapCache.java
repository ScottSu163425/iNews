package com.su.scott.inews.iamge;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2015/12/24.
 */
public class BitmapCache implements ImageLoader.ImageCache {
    private LruBitmapCache mCache;

    public BitmapCache() {
        mCache = LruBitmapCache.getInstance();
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }

    //图片本地缓存
   /* @Override
    public Bitmap getBitmap(String url) {
        return ImageCacheFileUtils.getInstance().getImage(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        ImageCacheFileUtils.getInstance().saveBitmap(bitmap, url);
    }*/

}
