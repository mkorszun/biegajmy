package com.biegajmy.events.form.dialogs;

import android.content.Context;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.biegajmy.R;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;

@EBean public class PaceDialog {

    @RootContext Context context;
    @StringArrayRes(R.array.pace) String[] PACE;
    @StringRes(R.string.pace_unit) String UNIT;

    private MaterialSimpleListAdapter adapter;
    private MaterialDialog.ListCallback listener;

    @AfterInject public void setup() {
        adapter = new MaterialSimpleListAdapter(context);
        for (String i : PACE) {
            adapter.add(new MaterialSimpleListItem.Builder(context).content(String.format("%s %s", i, UNIT))
                .icon(R.drawable.oval_icon)
                .build());
        }
    }

    public void show() {
        new MaterialDialog.Builder(context).title(R.string.pace_title).adapter(adapter, listener).show();
    }

    public void setSelectionListener(MaterialDialog.ListCallback listener) {
        this.listener = listener;
    }

    public String getSelection(int i) {
        return String.valueOf(PACE[i]);
    }
}
