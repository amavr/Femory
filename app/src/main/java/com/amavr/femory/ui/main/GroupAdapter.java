package com.amavr.femory.ui.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.amavr.femory.R;
import com.amavr.femory.ext.ItemTouchHelperAdapter;
import com.amavr.femory.ext.ItemTouchHelperViewHolder;
import com.amavr.femory.models.GroupInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter {

    static final String TAG = "XDBG.grp-adpater";

    int SelectedColor;
    int NormalColor;

    Fragment fragment;
    List<GroupInfo> groups;

    public GroupAdapter(Fragment fragment) {
        this.fragment = fragment;
        this.groups = new ArrayList<>();
        this.SelectedColor = ContextCompat.getColor(fragment.getContext(), R.color.colorAccent);
        this.NormalColor = ContextCompat.getColor(fragment.getContext(), R.color.colorPrimaryDark);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.fragment.getContext()).inflate(R.layout.group, parent, false);
        return new GroupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupInfo gi = this.groups.get(position);
        GroupHolder grp_holder = (GroupHolder) holder;
        grp_holder.tvName.setText(gi.name);
    }

    @Override
    public int getItemCount() {
        return this.groups.size();
    }

    public void updateGroups(List<GroupInfo> groups) {
        this.groups.clear();
        this.groups.addAll(groups);
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Log.d(TAG, String.format("onItemMove from: %s to: %s", fromPosition, toPosition));
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(this.groups, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(this.groups, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        Log.d(TAG, String.format("onItemDismiss position: %s", position));
        this.groups.remove(position);
        notifyItemRemoved(position);
    }

    class GroupHolder
            extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder {
        TextView tvName;
        ImageButton btnRename;

        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            btnRename = itemView.findViewById(R.id.btnRename);
        }

        @Override
        public void onItemSelected() {
            Log.d(TAG, "onItemSelected");
            tvName.setTextColor(SelectedColor);
        }

        @Override
        public void onItemClear() {
            tvName.setTextColor(NormalColor);
        }
    }

}
