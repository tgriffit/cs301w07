/**
 * author: Andrew Neufeld
 * description: An adapter for item list data of the ListSelectionView class. Uses the Adapter design pattern.
 * date: March 14
 */

package cs.ualberta.conditionlog;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

class LogArrayAdapter extends ArrayAdapter<ArrayList<String>> {

    private ArrayList<ArrayList<String>> items;
    private Context listContext;

    /**
     * 
     * @param context				the context for the adapter
     * @param textViewResourceId	id for the text view to be passed to super
     * @param items 				an ArrayList<ArrayList<String>>
     */
    public LogArrayAdapter(Context context, int textViewResourceId, ArrayList<ArrayList<String>> items) {
        super(context, textViewResourceId, items);
        listContext = context;
        this.items = items;
    }

    @Override
    /**
     * returns the view
     */
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
            String imgpath = item.get(1);

            //Drawable bmp = Drawable.createFromPath("@drawable/ic_launcher.png");

            if (tt != null) {
                tt.setText("Name: " + name);
            }
            if (iv != null) {
            	iv.setImageBitmap(BitmapFactory.decodeFile(imgpath));
            	//iv.setBackgroundDrawable(bmp);
            } else {
            	Toast toast2 = Toast.makeText(listContext, "Null Drawable!", Toast.LENGTH_LONG);
                toast2.show();
            }
        }
        return v;
    }
}