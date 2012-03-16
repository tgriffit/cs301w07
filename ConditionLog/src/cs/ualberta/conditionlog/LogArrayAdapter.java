/*
 * author: Andrew Neufeld
 * description: An adapter for item list data of the ListSelectionView class. Uses the Adapter design pattern.
 * date: March 14
 */

package cs.ualberta.conditionlog;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class LogArrayAdapter extends ArrayAdapter<ArrayList<String>> {

    private ArrayList<ArrayList<String>> items;
    private Context listContext;

    public LogArrayAdapter(Context context, int textViewResourceId, ArrayList<ArrayList<String>> items) {
        super(context, textViewResourceId, items);
        listContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) listContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listrow, null);
        }
        ArrayList<String> item = items.get(position);
        if (item != null) {
            TextView tt = (TextView) v.findViewById(R.id.toptext);
            ImageView iv = (ImageView) v.findViewById(R.id.icon);
            String name = item.get(0);
            String imgpath = item.get(0);
            Bitmap bmp = BitmapFactory.decodeFile(imgpath);
            if (tt != null) {
                  tt.setText("Name: " + name);
            }
            if (iv != null) {
            	iv.setImageBitmap(bmp);
            }
        }
        return v;
    }
}