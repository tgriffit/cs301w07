

package cs.ualberta.conditionlog.controller;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cs.ualberta.conditionlog.R;
/**
 * An adapter for item list data of the ListSelectionView class. Uses the Adapter design pattern.
 * date: March 14
 * @author Andrew Neufeld
 */
public class ListArrayAdapter extends ArrayAdapter<ArrayList<String>> {

    private ArrayList<ArrayList<String>> items;
    private Context listContext;

    /**
     * Constructor for ListArrayAdapter that saves the app context and saves the passed list of lists of strings
     * @param context				the context for the adapter
     * @param textViewResourceId	id for the text view to be passed to super
     * @param items 				an ArrayList<ArrayList<String>>
     */
    public ListArrayAdapter(Context context, int textViewResourceId, ArrayList<ArrayList<String>> items) {
        super(context, textViewResourceId, items);
        listContext = context;
        this.items = items;
    }
    /**
     * Creates and returns a list item with a thumbnail image and a text portion. This method is only used internally.
     * @param position Position in the list of items in the ListView
     * @param convertView View that ensures the list item is valid
     * @param parent A ViewGroup for the parent list 
     * @return View A specific view for use in the list
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) listContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // get one list item layout to populate
            v = vi.inflate(R.layout.listrow, null);
        }
        ArrayList<String> item = items.get(position);
        if (item != null) {
            TextView tt = (TextView) v.findViewById(R.id.toptext);
            ImageView iv = (ImageView) v.findViewById(R.id.icon);
            // get the name and image filepath from the data object
            String name = item.get(0);
            //String imgpath = item.get(1);

            if (tt != null) {
            	// set the name text on the current list item layout
                tt.setText("Name: " + name);
            }
            if (iv != null) {
            	// set the icon image on the current lsit item layout
            	//iv.setImageBitmap(BitmapFactory.decodeFile(imgpath));
            } else {
            	Toast toast = Toast.makeText(listContext, "Null Drawable!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        return v;
    }
}