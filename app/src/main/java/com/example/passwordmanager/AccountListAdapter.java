package com.example.passwordmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AccountListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<List<String>> data;

    public AccountListAdapter(Context context,
                              ArrayList<List<String>> listData){
        this.context = context;
        this.data = listData;
    }

    @Override
    public int getGroupCount() {
        return this.data.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return data.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return data.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return Long.parseLong(data.get(i).get(0));
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.account_head, parent, false);
        }
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView url = (TextView) v.findViewById(R.id.url);


        title.setText(data.get(groupPosition).get(1));
        url.setText(data.get(groupPosition).get(2));


        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.account_info, parent, false);
        }

        TextView username = (TextView) v.findViewById(R.id.username);
        TextView password = (TextView) v.findViewById(R.id.password);
        TextView note = (TextView) v.findViewById(R.id.note);
        username.setText(data.get(groupPosition).get(3));
        password.setText(data.get(groupPosition).get(4));
        note.setText(data.get(groupPosition).get(5));

        String a = data.get(groupPosition).get(3) + data.get(groupPosition).get(4);

        Toast.makeText(context, a, Toast.LENGTH_LONG);

        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
