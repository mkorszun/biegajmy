package com.biegajmy.comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.biegajmy.R;
import com.biegajmy.model.Comment;
import com.squareup.picasso.Picasso;
import java.util.Date;
import java.util.List;

public class CommentsListAdapter extends ArrayAdapter<Comment> {

    private LayoutInflater inflater;

    public CommentsListAdapter(Context context, List<Comment> comments) {
        super(context, R.layout.comment_list_item, comments);
    }

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public View getView(int position, View convertView, ViewGroup parent) {

        Comment item = getItem(position);
        View view = convertView == null ? inflate(getContext(), parent) : convertView;
        ((TextView) view.findViewById(R.id.comment_msg)).setText(item.msg);
        ((TextView) view.findViewById(R.id.comment_date)).setText(new Date(item.timestamp).toString());
        ImageView userPhoto = (ImageView) view.findViewById(R.id.event_comment_user_photo);
        Picasso.with(this.getContext()).load(item.photoURL).into(userPhoto);

        return view;
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private View inflate(Context context, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        return inflater.inflate(R.layout.comment_list_item, parent, false);
    }
}
