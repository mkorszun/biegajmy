package com.biegajmy.events.form.dialogs;

import android.content.Context;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.biegajmy.R;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.IntArrayRes;
import org.androidannotations.annotations.res.StringRes;

@EBean public class DistanceDialog {

    @RootContext Context context;
    @IntArrayRes(R.array.distances) int[] DISTANCES;
    @StringRes(R.string.distance_unit) String UNIT;

    private MaterialSimpleListAdapter adapter;
    private MaterialDialog.ListCallback listener;

    @AfterInject public void setup() {
        adapter = new MaterialSimpleListAdapter(context);
        for (int i : DISTANCES) {
            adapter.add(new MaterialSimpleListItem.Builder(context).content(String.format("%d %s", i, UNIT))
                .icon(R.drawable.oval_icon)
                .build());
        }
    }

    public void show() {
        new MaterialDialog.Builder(context).title(R.string.distance_title).adapter(adapter, listener).show();
    }

    public void setSelectionListener(MaterialDialog.ListCallback listener) {
        this.listener = listener;
    }

    public String getSelection(int i) {
        return String.valueOf(DISTANCES[i]);
    }
}
