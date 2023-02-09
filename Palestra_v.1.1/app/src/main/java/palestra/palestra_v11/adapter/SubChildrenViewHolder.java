package palestra.palestra_v11.adapter;

import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import palestra.palestra_v11.R;

public class SubChildrenViewHolder extends ChildViewHolder {

    private TextView dateTime;
    private TextView countReview;
    private TextView countParticipant;
    private MapView googleMap;

    public SubChildrenViewHolder(View itemView) {
        super(itemView);
        dateTime = (TextView) itemView.findViewById(R.id.tv_date_event);
        countReview = (TextView) itemView.findViewById(R.id.tv_count_review);
        countParticipant = (TextView) itemView.findViewById(R.id.tv_count_participant);
        googleMap = (MapView) itemView.findViewById(R.id.mv_place_event);
    }

    public void setDateTime(String dateTime) {
        this.dateTime.setText(dateTime);
    }

    public void setCountReview(String countReview) {
        this.countReview.setText(countReview);
    }

    public void setCountParticipant(String countParticipant) {
        this.countParticipant.setText(countParticipant);
    }
    //TODO: GoogleMap + lantitude + longitude (как их вытаскивать? возможно переписать RootObject)
}
