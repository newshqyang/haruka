package com.sw.haruka.view;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.sw.haruka.R;
import com.sw.haruka.dal.Namespace;
import com.sw.haruka.model.BaseStatusActivity;
import com.sw.haruka.helper.utils.SPUtils;
import com.sw.haruka.helper.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageCollectionActivity extends BaseStatusActivity {

    private ListView mCollectionListView;
    private List<String> mFolderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_collection);

        mCollectionListView = findViewById(R.id.collection_list_view);
        mFolderList = new ArrayList<>();
        mFolderList.addAll(SPUtils.getSetDefault0(Namespace.COLLECTION_SET));
        mCollectionListView.setAdapter(new CollectionAdapter());
    }

    public void finish(View view) {
        finish();
    }

    class CollectionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFolderList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder") final View view = LayoutInflater.from(ManageCollectionActivity.this).inflate(R.layout.item_collection, null);
            TextView textView = view.findViewById(R.id.collection_folder);
            final String path = mFolderList.get(position);
            textView.setText(FileUtils.getNameFromPath(path));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu menu = new PopupMenu(ManageCollectionActivity.this, view);
                    menu.getMenu().add(path);
                    menu.show();
                }
            });
            Button delete = view.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFolderList.remove(position);
                    Set<String> set = new HashSet<>(mFolderList);
                    SPUtils.saveSet(Namespace.COLLECTION_SET, set);
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }
}
