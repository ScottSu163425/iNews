package com.su.scott.inews.iamge;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Administrator on 2016/1/20.
 */
public class LruBitmapCache {
    private static LruCache<String, Bitmap> lruCache;

    private static LruBitmapCache instance = null;

    private LruBitmapCache() {
        int maxSize = 20 * 1024 * 1024;// 20M

        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public static LruBitmapCache getInstance() {
        if (null == instance) {
            instance = new LruBitmapCache();
        }
        return instance;
    }

    public Bitmap get(String url) {
        return lruCache.get(url);
    }

    public void put(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
    }

    public void clearCache() {
        if (null != lruCache) {
            lruCache.evictAll();
        }
    }

}
