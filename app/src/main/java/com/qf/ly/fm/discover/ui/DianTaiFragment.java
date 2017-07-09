package com.qf.ly.fm.discover.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.Adapter.CommenImagePageAdapter;
import com.qf.ly.fm.discover.ui.Adapter.RecylerAdapter;
import com.qf.ly.fm.discover.ui.entiy.DianTai1;
import com.qf.ly.fm.discover.ui.entiy.DianTai2;
import com.qf.ly.fm.discover.ui.entiy.DianTai3;
import com.qf.ly.fm.discover.ui.entiy.HideLoadingView;
import com.qf.ly.fm.discover.ui.entiy.ShowLoadlingView;
import com.qf.ly.fm.discover.ui.utlpath.Path;
import com.qf.ly.fm.discover.ui.utlpath.widget.AutoScrollViewPage;
import com.qf.ly.fm.discover.ui.utlpath.widget.DTfangView;
import com.qf.ly.fm.discover.ui.utlpath.widget.DTitemView;
import com.qf.ly.fm.discover.ui.utlpath.widget.DianTaiZhuBo;
import com.qf.ly.fm.other.utils.DeviceUtil;
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
 */

public class DianTaiFragment extends Fragment {
    private LinearLayout diantai_ll;
    private KaolaTask kaolaTask;
    private int width, height;
    private int wsize, hsize;
    private List<DianTai1> list = new ArrayList<>();

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
        View view = inflater.inflate(R.layout.diantai_fragment, null);
//        if (list.isEmpty()) {
//            getActivity().sendBroadcast(new Intent(DiscoverFragment.LOADING_ACTION_SHOW));
//        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        diantai_ll = (LinearLayout) view.findViewById(R.id.diantai_ll);
        //计算ViewPager应该显示的高度
        //获取屏幕的宽度
        width = getActivity().getResources().getDisplayMetrics().widthPixels;
        height = width * 3 / 8;
//        wsize= (int) (width- DeviceUtil.getPxFromDp(getActivity(),20));
//        hsize=wsize*138/680;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取保存的json
        String saveJson = SavePrefernce.getCacheFromPreference(getActivity(), "dtjson", "json");
        long saveTime = SavePrefernce.getCacheTimeFromPreference(getActivity(), "dtjson", "time");
        //获取当前时间
        long currTime = System.currentTimeMillis();
        long time = (currTime - saveTime) / 1000 / 60;

        kaolaTask = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                return HttpUtil.getJson(Path.DIANTAI);
            }

            @Override
            public Object parseResult(Object obj) {
                String json = obj.toString();
                try {
                    JSONObject root = new JSONObject(json);
                    //拿到下载好的字符串之后，把他写到Preference里面
                    SavePrefernce.saveCacheToPreference(getActivity(), "dtjson", "json", json);
                    SavePrefernce.saveCacheTimeToPreference(getActivity(), "dtjson", "time", root.getLong("serverTime"));

                    String code = root.getString("code");
                    String message = root.getString("message");
                    if ("10000".equals(code) && "success".equals(message)) {
                        JSONObject result = root.getJSONObject("result");

                        List<DianTai1> dataList1 = DianTai1.arrayDianTai1FromData(result.toString(), "dataList");
                        list.addAll(dataList1);
                        Log.d("print", "parseResult: " + dataList1.size());
                        //返回电台1集合
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
                //如果数据解析成功了，那么隐藏加载动画
                EventBus.getDefault().post(new HideLoadingView());
//                getActivity().sendBroadcast(new Intent(DiscoverFragment.LOADING_ACTION_HIDE));

                List<DianTai1> dt1list = (List<DianTai1>) object;
                //显示广告
//                DianTai1 dianTai1 = dt1list.get(0);
//                List<DianTai2> dataList2 = dianTai1.getDataList();
//                showBanner(dataList2);
                //显示电台
                DianTai1 dianTai112 = dt1list.get(0);
                List<DianTai2> dataList22 = dianTai112.getDataList();
                DianTai2 dianTai2 = dataList22.get(0);
                List<DianTai3> dataList = dianTai2.getDataList();
                showDianTai1(dataList);
                //显示多个电台
                DianTai1 dianTaiduo = dt1list.get(0);
                List<DianTai2> dataListduo = dianTaiduo.getDataList();
                DianTai2 dianTai2duo = dataListduo.get(1);
                List<DianTai3> dianTai3s = dianTai2duo.getDataList();
                showDianTaiDuo(dianTai3s);
                //显示精选
                showZnjx(dt1list.get(1));
                //显示第一个个item3
                showItem3(dt1list.get(2));
                //显示第二个item3
                showItemzb(dt1list.get(3));


            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });

        //如果操作时间间隔超过30分钟，需要从服务器刷新
        if (time > 30) {
            kaolaTask.execute();//从网络下载json字符串
        } else {
            kaolaTask.execute(saveJson);//如30分钟，直接解析本地的json并显示
        }

    }

    /**
     * 显示第二个item3
     *
     * @param dianTai1
     */
    private void showItemzb(DianTai1 dianTai1) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.diantai_zhubo_list, null);
        TextView tvtitle = (TextView) view.findViewById(R.id.diantai_zhubo_title_tv);
        TextView tvmore = (TextView) view.findViewById(R.id.diantai_zhubo_title_tv);
        tvtitle.setText(dianTai1.getName());
        if (dianTai1.getHasmore() != 1) {
            tvmore.setVisibility(View.GONE);
        }
        DianTaiZhuBo item1 = (DianTaiZhuBo) view.findViewById(R.id.DianTaiZhuBo1);
        DianTaiZhuBo item2 = (DianTaiZhuBo) view.findViewById(R.id.DianTaiZhuBo2);
        DianTaiZhuBo item3 = (DianTaiZhuBo) view.findViewById(R.id.DianTaiZhuBo3);
        item1.setDianTai2(dianTai1.getDataList().get(0));
        item2.setDianTai2(dianTai1.getDataList().get(1));
        item3.setDianTai2(dianTai1.getDataList().get(2));

        diantai_ll.addView(view);

    }

    /**
     * 显示第一个个item3
     *
     * @param dianTai11
     */
    private void showItem3(DianTai1 dianTai11) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.diantai_fang_item3, null);
        TextView tvname = (TextView) view.findViewById(R.id.item_diantai_name_tv3);
        TextView tvmore = (TextView) view.findViewById(R.id.item_diantai_more_tv3);
        DTfangView item1 = (DTfangView) view.findViewById(R.id.DTfangView1);
        DTfangView item2 = (DTfangView) view.findViewById(R.id.DTfangView2);
        DTfangView item3 = (DTfangView) view.findViewById(R.id.DTfangView3);
        tvname.setText(dianTai11.getName());
        int num = dianTai11.getHasmore();
        if (num == 0) {
            tvmore.setVisibility(View.GONE);
        }
        List<DianTai2> dataList = dianTai11.getDataList();
        item1.setDianTai2(dataList.get(0));
        item2.setDianTai2(dataList.get(1));
        item3.setDianTai2(dataList.get(2));
        diantai_ll.addView(view);

    }

    /**
     * 显示只能精选
     *
     * @param dianTai1
     */
    private void showZnjx(DianTai1 dianTai1) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.diantai_zhinengjinxuan, null);
        TextView tvname = (TextView) view.findViewById(R.id.diantai_znjx_tv_title);
        TextView tvmore = (TextView) view.findViewById(R.id.diantai_znjx_tv_more);
        tvname.setText(dianTai1.getName());
        int num = dianTai1.getHasmore();
        if (num != 1) {
            tvmore.setVisibility(View.GONE);
        }
        DTitemView item1 = (DTitemView) view.findViewById(R.id.DTitemView1);
        DTitemView item2 = (DTitemView) view.findViewById(R.id.DTitemView2);
        DTitemView item3 = (DTitemView) view.findViewById(R.id.DTitemView3);
        item1.setDianTai2(dianTai1.getDataList().get(0));
        item2.setDianTai2(dianTai1.getDataList().get(1));
        item3.setDianTai2(dianTai1.getDataList().get(2));

        diantai_ll.addView(view);

    }

    /**
     * 显示多个电台
     *
     * @param dianTai3s
     */
    //是否展开
    private boolean isExpand;

    private void showDianTaiDuo(List<DianTai3> dianTai3s) {
        dianTai3s.add(new DianTai3());//十六个数据

        final List<DianTai3> list1 = new ArrayList<>();
        list1.addAll(dianTai3s.subList(0, 7));
        list1.add(new DianTai3());//收起状态的数据源,八个数据

        final List<DianTai3> list2 = new ArrayList<>();
        list2.addAll(dianTai3s);//展开状态的数据源，十六个数据

        final List<DianTai3> temp = new ArrayList<>();
        temp.addAll(list1);//默认是收起的临时数据源

        //recyclerView显示Grid效果只能垂直滚动
        RecyclerView recyclerView = new RecyclerView(getActivity());
        //显示表格布局效果，第个参数表示显示多少列
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(manager);
        //设置布局属性
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = (int) DeviceUtil.getPxFromDp(getActivity(), 10);
        layoutParams.rightMargin = (int) DeviceUtil.getPxFromDp(getActivity(), 10);
        layoutParams.bottomMargin = (int) DeviceUtil.getPxFromDp(getActivity(), 10);
        recyclerView.setLayoutParams(layoutParams);

        final RecylerAdapter adapter = new RecylerAdapter(getActivity(), temp);
        recyclerView.setAdapter(adapter);
        adapter.setIonItemClickListener(new RecylerAdapter.IonItemClickListener() {
            @Override
            public void onClick(int position) {

                if (position != temp.size() - 1) {
                    //执行正常跳转
                    Toast.makeText(getActivity(), temp.get(position).getName(), Toast.LENGTH_SHORT).show();
                } else {
                    //如果现在是展开状态
                    if (isExpand) {
                        //那么要收起
                        temp.clear();
                        temp.addAll(list1);
                        adapter.notifyDataSetChanged();
                    } else {
                        //那么需要收起
                        temp.clear();
                        temp.addAll(list2);
                        adapter.notifyDataSetChanged();
                    }
                    isExpand = !isExpand;
                }
            }
        });
        diantai_ll.addView(recyclerView);

    }

    /**
     * @param dataList
     */
    private void showDianTai1(final List<DianTai3> dataList) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.diantaiitem, null);
        TextView tv1 = (TextView) view.findViewById(R.id.diantai_item_tv1);
        TextView tv2 = (TextView) view.findViewById(R.id.diantai_item_tv2);
        TextView tv3 = (TextView) view.findViewById(R.id.diantai_item_tv3);
        TextView tv4 = (TextView) view.findViewById(R.id.diantai_item_tv4);
        tv1.setText(dataList.get(0).getName());
        tv2.setText(dataList.get(1).getName());
        tv3.setText(dataList.get(2).getName());
        tv4.setText(dataList.get(3).getName());
        LinearLayout ll1 = (LinearLayout) view.findViewById(R.id.diantai_item1);
        LinearLayout ll2 = (LinearLayout) view.findViewById(R.id.diantai_item2);
        LinearLayout ll3 = (LinearLayout) view.findViewById(R.id.diantai_item3);
        LinearLayout ll4 = (LinearLayout) view.findViewById(R.id.diantai_item4);
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), dataList.get(0).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), dataList.get(1).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), dataList.get(2).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), dataList.get(3).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        diantai_ll.addView(view);

    }

    /**
     * 显示轮播广告
     *
     * @param dataList2
     */
    private void showBanner(final List<DianTai2> dataList2) {
        AutoScrollViewPage autoScrollViewPage = new AutoScrollViewPage(getActivity());
        List<ImageView> imagelist = new ArrayList<>();
        for (int i = 0; i < dataList2.size(); i++) {
            ImageView imageview = new ImageView(getActivity());
            imagelist.add(imageview);
        }
        CommenImagePageAdapter adapter = new CommenImagePageAdapter(imagelist) {
            @Override
            public void showImage(int position, ImageView imageView) {
                Picasso.with(getActivity())
                        .load(dataList2.get(position).getPic())
                        .resize(width, height)
                        .centerCrop()
                        .into(imageView);
            }
        };

        autoScrollViewPage.setPageAdapter(adapter, imagelist.size());

        diantai_ll.addView(autoScrollViewPage);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消任务，true任务没有执行完也取消，false让任务执行完取消
        kaolaTask.cancel(true);
        kaolaTask = null;
        list.clear();
    }
}
