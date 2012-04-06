package cs.ualberta.conditionlog.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import cs.ualberta.conditionlog.R;
/**
 * An adapter that deals with images for the Gallery view object in the interface Views of ConditionView and ComparisonView
 * @author Jack
 */
public class ImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;
    Bitmap[] bmps;

    /**
     *  Creation of an ImageAdapter requires the context of the app and a list of images to be shown in the gallery
     * @param c		the context of the current application
     * @param bmps	an array of bitmaps that will be used to create the gallery view
     */
    public ImageAdapter(Context c, Bitmap[] bmps) {
        mContext = c;
        this.bmps = bmps;
        TypedArray attr = mContext.obtainStyledAttributes(R.styleable.HelloGallery);
        mGalleryItemBackground = attr.getResourceId(
                R.styleable.HelloGallery_android_galleryItemBackground, 0);
        attr.recycle();
    }

    /**
     * Total number of images in the bitmap array
     * @return the number of images in the gallery view
     */
    public int getCount() {
        return bmps.length;
    }

    /**
     * Used for inner workings of the gallery for image positions
     */
    public Object getItem(int position) {
        return position;
    }
    
    /**
     * Used for inner workings of the gallery for image positions
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Creates and returns a small view containing a specific image for use in the gallery view
     * @return A view of an individual image in the gallery
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);

        imageView.setImageBitmap(bmps[position]);
        imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(mGalleryItemBackground);

        return imageView;
    }
}