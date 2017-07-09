package com.qf.ly.fm.discover.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.Adapter.GridViewAdapter;
import com.qf.ly.fm.discover.ui.entiy.FenLeiBottom1;
import com.qf.ly.fm.discover.ui.entiy.FenLeiBottom2;
import com.qf.ly.fm.discover.ui.entiy.FenLeiTop;
import com.qf.ly.fm.discover.ui.entiy.HideLoadingView;
import com.qf.ly.fm.discover.ui.entiy.ShowLoadlingView;
import com.qf.ly.fm.discover.ui.utlpath.Path;
import com.qf.ly.fm.discover.ui.utlpath.widget.FenLeiView;
import com.qf.ly.fm.other.utils.HttpUtil;
import com.qf.ly.fm.other.utils.KaolaTask;
import com.qf.ly.fm.other.utils.SavePrefernce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by LY on 2016/10/10.20:03
 * 版权所有 盗版必究
 */

public class FenleiFragment extends Fragment {
    private FenLeiView fenLeiView;
    private KaolaTask task, task2;
    private GridView gridView;
    private TextView textview;
    private List<FenLeiTop> topList = new ArrayList<>();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (topList.isEmpty()) {
                EventBus.getDefault().post(new ShowLoadlingView());
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fenlei_fragment, null);
//        if (topList.isEmpty()) {
//            getActivity().sendBroadcast(new Intent(DiscoverFragment.LOADING_ACTION_SHOW));
//        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fenLeiView = (FenLeiView) view.findViewById(R.id.fenleiview);
        textview = (TextView) view.findViewById(R.id.fenlei_bottom_fenlei_tv);
        gridView = (GridView) view.findViewById(R.id.fenlei_gv);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showTopView();
        showBottomView();

    }

    private void showBottomView() {
        //获取保存的json
        String saveJson = SavePrefernce.getCacheFromPreference(getActivity(), "flbottomjson", "json");
        long saveTime = SavePrefernce.getCacheTimeFromPreference(getActivity(), "flbottomjson", "time");
        //获取当前时间
        long currTime = System.currentTimeMillis();
        long time = (currTime - saveTime) / 1000 / 60;
        task2 = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                return HttpUtil.getJson(Path.FENLEI_BOTTOM);
            }

            @Override
            public Object parseResult(Object obj) {
                String json = obj.toString();
                try {
                    JSONObject root = new JSONObject(json);
                    //拿到下载好的字符串之后，把他写到Preference里面
                    SavePrefernce.saveCacheToPreference(getActivity(), "flbottomjson", "json", json);
                    SavePrefernce.saveCacheTimeToPreference(getActivity(), "flbottomjson", "time", root.getLong("serverTime"));

                    String code = root.getString("code");
                    String message = root.getString("message");
                    if ("10000".equals(code) && "success".equals(message)) {
                        JSONObject result = root.getJSONObject("result");
                        List<FenLeiBottom1> dataList1 = FenLeiBottom1.arrayFenLeiBottom1FromData(result.toString(), "dataList");
                        Log.d("print", "parseResult: " + dataList1.size());
                        //返回分类集合1
                        return dataList1;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {
                List<FenLeiBottom1> list = (List<FenLeiBottom1>) object;
                final List<FenLeiBottom2> dataList2 = list.get(0).getDataList();
                textview.setText(list.get(0).getName());
                GridViewAdapter adapter = new GridViewAdapter(getActivity(), dataList2);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity(), dataList2.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "请求json失败", Toast.LENGTH_SHORT).show();
            }
        });
        //如果操作时间间隔超过30分钟，需要从服务器刷新
        if (time > 30) {
            task2.execute();//从网络下载json字符串
        } else {
            task2.execute(saveJson);//如果小于30分钟，直接解析本地的json并显示
        }
    }

    private void showTopView() {
        //获取保存的json
        String saveJson = SavePrefernce.getCacheFromPreference(getActivity(), "fljson", "json");
        long saveTime = SavePrefernce.getCacheTimeFromPreference(getActivity(), "fljson", "time");
        //获取当前时间
        long currTime = System.currentTimeMillis();
        long time = (currTime - saveTime) / 1000 / 60;

        task = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                return HttpUtil.getJson(Path.FENLEI_TOP);
            }

            @Override
            public Object parseResult(Object obj) {
                String json = obj.toString();
                try {
                    JSONObject root = new JSONObject(json);
                    //拿到下载好的字符串之后，把他写到Preference里面
                    SavePrefernce.saveCacheToPreference(getActivity(), "fljson", "json", json);
                    SavePrefernce.saveCacheTimeToPreference(getActivity(), "fljson", "time", root.getLong("serverTime"));

                    String code = root.getString("code");
                    String message = root.getString("message");
                    if ("10000".equals(code) && "success".equals(message)) {
                        JSONObject result = root.getJSONObject("result");

                        List<FenLeiTop> dataList = FenLeiTop.arrayFenLeiTopFromData(result.toString(), "dataList");
                        topList.addAll(dataList);
                        Log.d("print", "parseResult: " + dataList.size());
                        //返回分类集合
                        return dataList;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {
                //EventBus发布消息让loadingview隐藏
                EventBus.getDefault().post(new HideLoadingView());
//                getActivity().sendBroadcast(new Intent(DiscoverFragment.LOADING_ACTION_HIDE));
                final List<FenLeiTop> fenleilist = (List<FenLeiTop>) object;
                fenLeiView.setFenLeiList(fenleilist);
                fenLeiView.setFenLeiOnClickListener(new FenLeiView.FenLeiOnClickListener() {
                    @Override
                    public void onClick(int position) {
                        Toast.makeText(getActivity(), fenleilist.get(position).getRname(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "请求json失败", Toast.LENGTH_SHORT).show();
            }
        });
        //如果操作时间间隔超过30分钟，需要从服务器刷新
        if (time > 30) {
            task.execute();//从网络下载json字符串
        } else {
            task.execute(saveJson);//如果小于30分钟，直接解析本地的json并显示
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        task.cancel(true);
        task = null;
        task2.cancel(true);
        task2 = null;

        topList.clear();
    }
}
