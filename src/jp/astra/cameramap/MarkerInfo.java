package jp.astra.cameramap;

import jp.astra.cameramap.helper.StringUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;


@SuppressLint("ResourceAsColor")
class MarkerInfo implements InfoWindowAdapter {
	  LayoutInflater inflater=null;
	  Context context;

	  MarkerInfo(Context context, LayoutInflater inflater) {
	    this.inflater = inflater;
	    this.context = context;
	  }

	  @Override
	  public View getInfoWindow(Marker marker) {
	    return(null);
	  }


	  @Override
	  public View getInfoContents(Marker marker) {
	    View popup=this.inflater.inflate(R.layout.infowindow, null);

	    TextView tv=(TextView)popup.findViewById(R.id.title);

	    if(marker.getTitle().endsWith("jpg")){
	    	tv.setTextColor(this.context.getResources().getColor(R.color.black));
	    }else{
	    	tv.setTextColor(this.context.getResources().getColor(R.color.red));
	    }
	    tv.setText(marker.getTitle());
	    tv=(TextView)popup.findViewById(R.id.snippet);
	    tv.setTextColor(this.context.getResources().getColor(R.color.gray));
	    tv.setText(marker.getSnippet());
	    if(StringUtil.isEmpty(marker.getSnippet()) || marker.getSnippet().startsWith("files:")){
	    	tv.setVisibility(View.GONE);
	    }else{
	    	tv.setVisibility(View.VISIBLE);
	    }

	    return(popup);
	  }
}