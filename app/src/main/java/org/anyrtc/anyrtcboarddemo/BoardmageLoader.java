package org.anyrtc.anyrtcboarddemo;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.anyrtc.whiteboard.imageloader.ImageLoader;
import org.anyrtc.whiteboard.weight.PinchImageView;

/**
 * Created by liuxiaozhong on 2018/9/30.
 */
public class BoardmageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, PinchImageView imageView) {
        RequestManager requestManager= Glide.with(context);
        requestManager.load(path).
                into(imageView);
    }
}
