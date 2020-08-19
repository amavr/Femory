package com.amavr.femory.ui.main;

import androidx.core.view.DragStartHelper;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amavr.femory.MainActivity;
import com.amavr.femory.R;
import com.amavr.femory.data.IRepository;
import com.amavr.femory.ext.ChangedTextCallback;
import com.amavr.femory.ext.OnStartDragListener;
import com.amavr.femory.ext.Tools;
import com.amavr.femory.models.GroupInfo;
import com.amavr.femory.ext.SwipeController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.List;

public class MainFragment extends Fragment implements OnStartDragListener {

    static final String TAG = "XDBG.MainFragment";

    Gson gson = new Gson();
    MainViewModel mViewModel;

    RecyclerView rvGroups;
    GroupAdapter adp;
    ItemTouchHelper itemTouchHelper;
    IRepository repository;


    public static MainFragment newInstance(IRepository rep) {
        return new MainFragment(rep);
    }

    public MainFragment(IRepository rep){
        this.repository = rep;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        rvGroups = (RecyclerView) view.findViewById(R.id.rvGroups);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvGroups.setLayoutManager(manager);

        adp = new GroupAdapter(this, this);
        rvGroups.setAdapter(adp);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fabNewGroup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.dlgShowNewList(getContext(), "Название списка", "Новый", new ChangedTextCallback() {
                    @Override
                    public void onChange(String text) {
                        if(text == null || text.length() == 0){
                            Tools.toastIt(getContext(), "Имя не должно быть пустым");
                        }
                        else {
                            mViewModel.addGroup(text);
                        }
                    }
                });
            }
        });

        SwipeController swipeController = new SwipeController(adp);
        itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(rvGroups);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /// create viewmodel
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.setRepository(this.repository);
//        mViewModel = new ViewModelProvider(this, new ExtViewModelFactory(this.repository))
//                .get(MainViewModel.class);

        mViewModel.getGroups().observe(this.getActivity(), new Observer<List<GroupInfo>>() {
            @Override
            public void onChanged(List<GroupInfo> groups) {
                Log.d(TAG, String.format("groups: %s", gson.toJson(groups)));
                adp.updateGroups(groups);
            }
        });

//        mViewModel.post();

    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).setTitle("Списки");
//        RecyclerView
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
