package palestra.palestra_v11.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;

import palestra.palestra_v11.EventInfoRoot;
import palestra.palestra_v11.R;

public class RootAdapter extends ExpandableRecyclerViewAdapter<RootViewHolder, SubChildrenViewHolder> {
    private Context context;

    public RootAdapter(ArrayList<? extends ExpandableGroup> groups, Context context) {
        super(groups);
        this.context = context;
    }

    @Override
    public RootViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_root, parent, false);
        return new RootViewHolder(view);
    }

    @Override
    public SubChildrenViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_children, parent, false);
        return new SubChildrenViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(SubChildrenViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {
        final EventInfoRoot.EventInfoChildren eventInfoChildren =
                ((EventInfoRoot) group).getItems().get(childIndex);
        holder.setDateTime(eventInfoChildren.getDateOf());
        holder.setCountReview(String.valueOf(eventInfoChildren.getCountReview()));
        holder.setCountParticipant(String.valueOf(eventInfoChildren.getCountParticipant()));
    }

    @Override
    public void onBindGroupViewHolder(RootViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {
        holder.setTitleEvent(group);
        holder.setAgeLimit(group);
        holder.setBriefInfo(group);
        holder.setCategoryName(group);
        holder.setCreatorName(group);
    }
}
