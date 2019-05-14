package org.ar.arboard.imageloader;

import android.content.Context;

import org.ar.arboard.weight.ARPinchImageView;


public abstract class ImageLoader implements ImageLoaderInterface<ARPinchImageView> {

    @Override
    public ARPinchImageView createImageView(Context context) {
        ARPinchImageView imageView = new ARPinchImageView(context);
        return imageView;
    }

}
