package cs.ualberta.conditionlog;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class LogArrayAdapter extends ArrayAdapter<PhotoList> {

    private ArrayList<PhotoList> items;
    private Context listContext;

    public LogArrayAdapter(Context context, int textViewResourceId, ArrayList<PhotoList> items) {
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
        PhotoList p = items.get(position);
        if (p != null) {
            TextView tt = (TextView) v.findViewById(R.id.toptext);
            if (tt != null) {
                  tt.setText("Name: "+p.getName());
            }
        }
        return v;
    }
}