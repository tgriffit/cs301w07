package cs.ualberta.conditionlog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
/**
 * 
 * @author Jack
 *	an adapter for images for the gallery
 */
public class ImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;
    Bitmap[] bmps;

    /**
     *  constructor for the image adapter
     * @param c		the context
     * @param bmps	a Bitmap[]
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
     * @return the length of the bitmap[]
     */
    public int getCount() {
        return bmps.length;
    }

    /**
     * @return the position argument
     */
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * returns the imageView
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