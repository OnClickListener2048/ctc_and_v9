package com.tohier.cartercoin.loader;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import com.tohier.cartercoin.columnview.LoadingView;
import com.youth.banner.loader.ImageLoader;


public class GlideImageLoader extends ImageLoader {
    private LoadingView gif;

    public GlideImageLoader(LoadingView gif) {
        this.gif = gif;
    }

    @Override
    public void displayImage(final Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        if(Util.isOnMainThread()) {
            Glide.with(context)
                    .load(path)
                    .listener(new RequestListener<Object, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Object o, Target<GlideDrawable> target, boolean b) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable glideDrawable, Object o, Target<GlideDrawable> target, boolean b, boolean b1) {
                            if (gif != null) {
                                gif.setVisibility(View.GONE);
                            }

                            return false;
                        }
                    })
                    .crossFade()
                    .into(imageView);
        }

    }
}
