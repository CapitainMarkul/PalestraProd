package palestra.palestra;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by Dmitry on 03.06.2017.
 */

public class WrapContentLinearLayoutManage extends LinearLayoutManager{


    public WrapContentLinearLayoutManage(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            //Log.e("probe", "meet a IOOBE in RecyclerView");
        }
    }
}
