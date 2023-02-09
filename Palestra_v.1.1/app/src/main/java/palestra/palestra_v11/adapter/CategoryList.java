package palestra.palestra_v11.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import palestra.palestra_v11.R;

/**
 * Created by Dmitry on 26.07.2017.
 */

public class CategoryList extends RecyclerView.Adapter<CategoryList.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_age_limit;
        private TextView tv_category_name;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_age_limit = (TextView) itemView.findViewById(R.id.tv_age_limit);
            tv_category_name = (TextView) itemView.findViewById(R.id.tv_category_name);
        }
    }
}
