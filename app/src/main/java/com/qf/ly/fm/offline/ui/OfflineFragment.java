package com.qf.ly.fm.offline.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qf.ly.fm.R;
import com.qf.ly.fm.offline.ui.adapter.OffLineAdapter;
import com.qf.ly.fm.offline.ui.db.DownLoadManager;
import com.qf.ly.fm.offline.ui.entiy.DownLoadEntiy;

import java.util.List;

/**
 * Created by LY on 2016/10/10.15:51
 * 版权所有 盗版必究
 */

public class OfflineFragment extends Fragment {
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offline_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.offline_listview);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DownLoadManager manager = DownLoadManager.getInstance();
        List<DownLoadEntiy> list = manager.getList();
        OffLineAdapter adapter = new OffLineAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
