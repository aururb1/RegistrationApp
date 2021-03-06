package com.example.urbon.registrationapp.items;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.urbon.registrationapp.R;
import com.example.urbon.registrationapp.models.Owner;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by urbon on 2/17/2018.
 */

public class HeaderItem extends AbstractHeaderItem<HeaderItem.HeaderItemHolder> {

    private Owner owner;
    private String path;

    public HeaderItem(Owner owner, String path) {
        this.owner = owner;
        this.path = path;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getpath() {
        return path;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof HeaderItem) {
            HeaderItem inItem = (HeaderItem) inObject;
            return this.owner.getEmail().equals(inItem.owner.getEmail());
        }
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_header;
    }

    @Override
    public HeaderItemHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new HeaderItemHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, HeaderItemHolder holder, int position, List<Object> payloads) {
        holder.ownerFullName.setText(getOwnerFullName());
        holder.ownerEmail.setText(owner.getEmail());
    }

    class HeaderItemHolder extends FlexibleViewHolder {

        LinearLayout linearLayout;
        TextView ownerFullName;
        TextView ownerEmail;

        HeaderItemHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter, true);
            ownerFullName = view.findViewById(R.id.ownerFullName);
            ownerEmail = view.findViewById(R.id.ownerEmail);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    private String getOwnerFullName() {
        return owner.getName() + " " + owner.getSurname();
    }
}
