package com.example.urbon.registrationapp.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.urbon.registrationapp.R;
import com.example.urbon.registrationapp.models.Pet;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by urbon on 2/17/2018.
 */

public class SectionItem extends AbstractSectionableItem<SectionItem.ViewHolder, HeaderItem> {
    private Pet pet;

    public SectionItem(HeaderItem header, Pet pet) {
        super(header);
        this.header = header;
        this.pet = pet;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof SectionItem) {
            SectionItem inItem = (SectionItem) inObject;
            return this.pet.getName().equals(inItem.pet.getName());
        }
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_section;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List<Object> payloads) {
        holder.petName.setText(pet.getName());

    }

    class ViewHolder extends FlexibleViewHolder {

        TextView petName;
        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            petName = view.findViewById(R.id.petName);
        }

    }
}
