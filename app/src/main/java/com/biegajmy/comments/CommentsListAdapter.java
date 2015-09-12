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
import com.biegajmy.utils.TimeUtils;
import com.squareup.picasso.Picasso;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommentsListAdapter extends ArrayAdapter<Comment> {

    private LayoutInflater inflater;
    private List<Comment> comments;

    public CommentsListAdapter(Context context, List<Comment> comments) {
        super(context, R.layout.comment_list_item, comments);
        this.comments = comments;
    }

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public View getView(int position, View convertView, ViewGroup parent) {

        Comment item = getItem(position);
        Date current = Calendar.getInstance().getTime();
        Date commentDate = new Date(item.timestamp);

        View view = convertView == null ? inflate(getContext(), parent) : convertView;
        ((TextView) view.findViewById(R.id.comment_msg)).setText(item.userName + " " + item.msg);
        ((TextView) view.findViewById(R.id.comment_date)).setText(TimeUtils.getDiff(commentDate, current).toString());

        ImageView userPhoto = (ImageView) view.findViewById(R.id.event_comment_user_photo);
        Picasso.with(this.getContext()).load(item.photoURL).into(userPhoto);

        return view;
    }

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    public List<Comment> getComments() {
        return comments;
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

    //********************************************************************************************//
    //********************************************************************************************//
}
