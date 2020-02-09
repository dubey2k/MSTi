package com.example.msti.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.msti.R;
import com.example.msti.model.userData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SwipeCard_Adapter extends ArrayAdapter<userData> {

    Context context;

    public SwipeCard_Adapter(@NonNull Context context, int resource, List<userData> dataList) {
        super(context, resource, dataList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        userData user = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.swipe_card_item, parent, false);

        ConstraintLayout user_container = convertView.findViewById(R.id.user_container);
        ImageView profile_pic = convertView.findViewById(R.id.show_profilePic);
        TextView profile_name = convertView.findViewById(R.id.show_userName);
        TextView profile_description = convertView.findViewById(R.id.user_description);

        profile_name.setText(user.getName() + ", " + user.getYear()+" - "+user.getCollege());
        profile_description.setText(user.getDescription());
        if (!user.getProfilePic().isEmpty())
            Picasso.get().load(user.getProfilePic()).into(profile_pic);

        user_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });


        return convertView;
    }
}