package org.ar.anyrtcboarddemo;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.ar.arboard.imageloader.ImageLoader;
import org.ar.arboard.weight.ARPinchImageView;

/**
 * Created by liuxiaozhong on 2018/9/30.
 */
public class BoardmageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ARPinchImageView imageView) {
        RequestManager requestManager= Glide.with(context);
        requestManager.load(path).
                into(imageView);
    }
}
