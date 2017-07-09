package com.qf.ly.fm.discover.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.Adapter.CommenImagePageAdapter;
import com.qf.ly.fm.discover.ui.Adapter.ZhuBoListAdapter;
import com.qf.ly.fm.discover.ui.entiy.HideLoadingView;
import com.qf.ly.fm.discover.ui.entiy.ShowLoadlingView;
import com.qf.ly.fm.discover.ui.utlpath.Path;
import com.qf.ly.fm.discover.ui.utlpath.ZhuBo;
import com.qf.ly.fm.discover.ui.utlpath.ZhuBo2;
import com.qf.ly.fm.discover.ui.utlpath.widget.AutoScrollViewPage;
import com.qf.ly.fm.discover.ui.utlpath.widget.VerticalScrollTextView;
import com.qf.ly.fm.other.utils.HttpUtil;
import com.qf.ly.fm.other.utils.KaolaTask;
import com.qf.ly.fm.other.utils.SavePrefernce;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by LY on 2016/10/10.20:03
 * 版权所有 盗版必究
 * 主播
 */

public class ZhuBoFragment extends Fragment {
    private ListView listView;
    private View header;
    private AutoScrollViewPage autoScrollViewPage;
    private VerticalScrollTextView vstv1;
    private VerticalScrollTextView vstv2;
    private KaolaTask kaolaTask;
    private int width, height;
    private List<ZhuBo> list = new ArrayList<>();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (list.isEmpty()) {
                EventBus.getDefault().post(new ShowLoadlingView());
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        header = inflater.inflate(R.layout.listview_zhubo_hander, null);
//        if (list.isEmpty()) {
//            getActivity().sendBroadcast(new Intent(DiscoverFragment.LOADING_ACTION_SHOW));
//        }
        return inflater.inflate(R.layout.zhubo_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view;
        listView.addHeaderView(header);
        autoScrollViewPage = (AutoScrollViewPage) header.findViewById(R.id.zhubo_header_vp);
        vstv1 = (VerticalScrollTextView) header.findViewById(R.id.VerticalScrollTextView1);
        vstv2 = (VerticalScrollTextView) header.findViewById(R.id.VerticalScrollTextView2);
        //计算ViewPager应该显示的高度
        //获取屏幕的宽度
        width = getActivity().getResources().getDisplayMetrics().widthPixels;
        height = width * 3 / 8;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取保存的json
        String saveJson = SavePrefernce.getCacheFromPreference(getActivity(), "zbjson", "json");
        long saveTime = SavePrefernce.getCacheTimeFromPreference(getActivity(), "zbjson", "time");
        //获取当前时间
        long currTime = System.currentTimeMillis();
        long time = (currTime - saveTime) / 1000 / 60;

        kaolaTask = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                String json = HttpUtil.getJson(Path.ZHUBO_PATH);
                return json;
            }

            @Override
            public Object parseResult(Object obj) {
                return parseJson(obj);
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {
                //如果数据解析成功了，那么隐藏加载动画
                EventBus.getDefault().post(new HideLoadingView());
//                getActivity().sendBroadcast(new Intent(DiscoverFragment.LOADING_ACTION_HIDE));
                showData(object);
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "请求失败，请检查你的网络是否良好！", Toast.LENGTH_SHORT).show();
            }
        });
        //如果操作时间间隔超过30分钟，需要从服务器刷新
        if (time > 30) {
            kaolaTask.execute();//从网络下载json字符串
        } else {
            kaolaTask.execute(saveJson);//如果小于30分钟，直接解析本地的json并显示
        }

    }

    @Nullable
    private Object parseJson(Object obj) {
        String json = obj.toString();
        try {
            JSONObject root = new JSONObject(json);
            //拿到下载好的字符串之后，把他写到Preference里面
            SavePrefernce.saveCacheToPreference(getActivity(), "zbjson", "json", json);
            SavePrefernce.saveCacheTimeToPreference(getActivity(), "zbjson", "time", root.getLong("serverTime"));

            String code = root.getString("code");
            String message = root.getString("message");
            if ("10000".equals(code) && "success".equals(message)) {
                JSONObject result = root.getJSONObject("result");
                final List<ZhuBo> dataList = ZhuBo.arrayZhuBoFromData(result.toString(), "dataList");
                list.addAll(dataList);
                Log.d("print", "run: " + dataList.size());

                //返回主播集合
                return dataList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showData(Object object) {
        List<ZhuBo> dataList = (List<ZhuBo>) object;
        //轮播图片
        ZhuBo zhuBo = dataList.get(0);
        showHeaderView(zhuBo);
        //垂直滚动TextView1,2
        showScrollText1(dataList.get(1));
        showScrollText2(dataList.get(1));
        //截取从第3条开始剩下的（包含第三条，不包含dataList.size()）
        List<ZhuBo> list = dataList.subList(2, dataList.size());
        showListView(list);
    }

    /**
     * 显示自动滚动的ViewPage和TabLayout
     *
     * @param zhuBo
     */
    public void showHeaderView(ZhuBo zhuBo) {
        final List<ZhuBo2> zhuBo2List = zhuBo.getDataList();
        List<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < zhuBo2List.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            imageViews.add(imageView);
        }
        //创建适配器
        final CommenImagePageAdapter adapter = new CommenImagePageAdapter(imageViews) {
            @Override
            public void showImage(int position, ImageView imageView) {
                Picasso.with(getActivity())
                        .load(zhuBo2List.get(position).getPic())//加载图片地址
                        .resize(width, height)//设置图片的宽高
                        .into(imageView);//加载到指定的ImageView
            }
        };
        autoScrollViewPage.setPageAdapter(adapter, imageViews.size());
    }

    /**
     * 显示垂直滚动的textview1
     *
     * @param zhuBo
     */
    public void showScrollText1(ZhuBo zhuBo) {

        final List<ZhuBo2> dataList1 = zhuBo.getDataList();
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < dataList1.size(); i++) {
            list1.add(dataList1.get(i).getRname());
        }
        vstv1.setList(list1, true);
        vstv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = vstv1.getCurrentIndex();
                Toast.makeText(getActivity(), dataList1.get(index).getRname(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 显示垂直滚动的textview2
     *
     * @param zhuBo
     */
    public void showScrollText2(ZhuBo zhuBo) {

        final List<ZhuBo2> dataList2 = zhuBo.getDataList();
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < dataList2.size(); i++) {
            list2.add(dataList2.get(i).getDes());
        }
        vstv2.setList(list2, false);
        vstv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = vstv2.getCurrentIndex();
                Toast.makeText(getActivity(), dataList2.get(index).getDes(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示listview
     *
     * @param list
     */
    public void showListView(List<ZhuBo> list) {
        ZhuBoListAdapter adapter = new ZhuBoListAdapter(list, getActivity());
        listView.setAdapter(adapter);
    }

    /**
     * 当视图销毁的时候，销毁异步任务
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消任务，true任务没有执行完也取消，false让任务执行完取消
        kaolaTask.cancel(true);
        kaolaTask = null;
        list.clear();
    }
}
