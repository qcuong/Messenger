package com.quoccuong.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.quoccuong.data.Data;
import com.quoccuong.messenger.R;
import com.quoccuong.model.Contact;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/20/2015.
 */
public class ContactItemAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private ArrayList<Contact> listContacts = new ArrayList<>();
    private ArrayList<Contact> listFilterContacts = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private int resource;


    public ContactItemAdapter(Context context, int resource) {
        this.mContext = context;
        this.listContacts = Data.listContacts;
        this.resource = resource;
        this.layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int position) {
        return listFilterContacts.get(position);
    }

    @Override
    public int getCount() {
        return listFilterContacts.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(resource, null);

        TextView tvName = (TextView) convertView.findViewById(R.id.contact_item_tv_name);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.contact_item_tv_phone);

        Contact contact = listFilterContacts.get(position);

        tvName.setText(contact.getName());
        tvPhone.setText("Phone   " + contact.getPhoneNumber());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {

        class MyFilter extends Filter {


            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Contact> suggestions = new ArrayList<>();
                if (constraint != null) {
                    for (int i = 0; i < listContacts.size(); i++) {
                        Contact contact = listContacts.get(i);
                        if (contact.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())
                                || contact.getPhoneNumber().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(contact);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            public String convertResultToString(Object resultValue) {
                String str = ((Contact) (resultValue)).getName();
                return str;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<Contact> filteredList = (ArrayList<Contact>) results.values;
                if (results.count > 0) {
                    listFilterContacts = filteredList;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }

        return new MyFilter();
    }
}
