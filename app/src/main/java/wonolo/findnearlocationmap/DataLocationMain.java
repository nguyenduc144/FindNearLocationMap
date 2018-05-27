package wonolo.findnearlocationmap;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DataLocationMain extends ArrayAdapter<NameLocation> {
    private PlayerListener mListener;
    private PlaceFinderListener mPlaceFinderListener;
    private Intent mIntent;
    public static ProgressDialog sProgressDialog;

    public DataLocationMain(Context context, int resource, ArrayList<NameLocation> list, PlayerListener listener, PlaceFinderListener listener_place) {
        super(context, resource,list);
        this.mListener = listener;
        this.mPlaceFinderListener = listener_place;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder ;
        if (convertView==null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.location_main_item,null);
            viewHolder = new ViewHolder();
            viewHolder.mName = (TextView)convertView.findViewById(R.id.tv_name_location);
            viewHolder.mMap = (Button) convertView.findViewById(R.id.btn_map);
            viewHolder.mList = (Button) convertView.findViewById(R.id.btn_list);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final NameLocation location = getItem(position);
        viewHolder.mName.setText(location.getmNameLocation());
        viewHolder.mList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getContext(),ListLocationActivity.class);
                mIntent.putExtra("LOCATION",location.getmNameLocation().toLowerCase());
                mIntent.putExtra("CHOOSE","list");
                sProgressDialog = ProgressDialog.show(getContext(),"Please wait...","Finding near place..!",true);
                mListener.onClickPlayer(position,mIntent);
            }
        });
        viewHolder.mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getContext(),MapActivity.class);
                mIntent.putExtra("LOCATION",location.getmNameLocation().toLowerCase());
                mIntent.putExtra("CHOOSE","map");
                mListener.onClickPlayer(position,mIntent);
            }
        });
        return convertView;
    }
    class ViewHolder {
        private TextView mName;
        private Button mMap;
        private Button mList;
    }
}
