package palestra.palestra_v11.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import palestra.palestra_v11.EventInfoRoot;
import palestra.palestra_v11.R;

public class RootViewHolder extends GroupViewHolder {
    private TextView titleEvent;
    private TextView briefInfo;
    private TextView creatorName;
    private TextView ageLimit;
    private TextView categoryName;
    private ImageView creatorAvatar;

    public RootViewHolder(View itemView) {
        super(itemView);

        titleEvent = (TextView) itemView.findViewById(R.id.tv_title_event);
        briefInfo = (TextView)itemView.findViewById(R.id.tv_brief_event);
        creatorName = (TextView) itemView.findViewById(R.id.tv_creator_name);
        ageLimit = (TextView) itemView.findViewById(R.id.tv_age_limit);
        categoryName = (TextView) itemView.findViewById(R.id.tv_category_name);
        creatorName = (TextView) itemView.findViewById(R.id.tv_creator_name);
        creatorAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
    }

    public void setTitleEvent(ExpandableGroup genre) {
        titleEvent.setText(((EventInfoRoot)genre).getTitleEvent());
    }

    public void setBriefInfo(ExpandableGroup genre) {
        briefInfo.setText(((EventInfoRoot)genre).getBriefInfo());
    }
    public void setCreatorName(ExpandableGroup genre) {
        creatorName.setText(((EventInfoRoot)genre).getCreatorName());
    }
    public void setAgeLimit(ExpandableGroup genre) {
        ageLimit.setText(String.valueOf(((EventInfoRoot)genre).getAgeLimit()));
    }
    public void setCategoryName(ExpandableGroup genre) {
        categoryName.setText(((EventInfoRoot)genre).getCategoryName());
    }

    public void setIconRoot(ExpandableGroup genre, Context context) {
        //TODO: Picasso  + creatorAvatar
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
