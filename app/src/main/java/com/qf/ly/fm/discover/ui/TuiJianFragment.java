package com.qf.ly.fm.discover.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import com.qf.ly.fm.discover.ui.Adapter.EntryAdapter;
import com.qf.ly.fm.discover.ui.activity.WebActivity;
import com.qf.ly.fm.discover.ui.entiy.HideLoadingView;
import com.qf.ly.fm.discover.ui.entiy.ShowLoadlingView;
import com.qf.ly.fm.discover.ui.entiy.TuiJian;
import com.qf.ly.fm.discover.ui.entiy.TuiJian2;
import com.qf.ly.fm.discover.ui.utlpath.Path;
import com.qf.ly.fm.discover.ui.utlpath.widget.AutoScrollViewPage;
import com.qf.ly.fm.discover.ui.utlpath.widget.TJItemView;
import com.qf.ly.fm.discover.ui.utlpath.widget.TJitemLike;
import com.qf.ly.fm.other.utils.DeviceUtil;
import com.qf.ly.fm.other.utils.HttpUtil;
import com.qf.ly.fm.other.utils.KaolaTask;
import com.qf.ly.fm.other.utils.SavePrefernce;
import com.qf.ly.fm.other.utils.TransformatBanner;
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

public class TuiJianFragment extends Fragment {

    private LinearLayout tuijian_ll;
    private KaolaTask kaolaTask;
    private int width, height;
    private int wsize, hsize;
    private SwipeRefreshLayout refreshLayout;
    private List<TuiJian> list = new ArrayList<>();

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
        View view = inflater.inflate(R.layout.tuijian_fragment, null);
//        if (list.isEmpty()) {
//            getActivity().sendBroadcast(new Intent(DiscoverFragment.LOADING_ACTION_SHOW));
//        }
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        refreshLayout = (SwipeRefreshLayout) view;
        tuijian_ll = (LinearLayout) view.findViewById(R.id.tuijian_ll);

        //计算ViewPager应该显示的高度
        //获取屏幕的宽度
        width = getActivity().getResources().getDisplayMetrics().widthPixels;
        height = width * 3 / 8;
        wsize = (int) (width - DeviceUtil.getPxFromDp(getActivity(), 20));
        hsize = wsize * 138 / 680;

        //设置加载显示的颜色，最多可以使用5种
//      refreshLayout.setColorSchemeColors(new int[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN, Color.GRAY});
        refreshLayout.setColorSchemeColors(new int[]{getResources().getColor(R.color.colorzhuti)});
        //下拉监听，一般做些请求网络操作
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                excuteTask();
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        excuteTask();

    }

    private void excuteTask() {
        //获取保存的json
        String saveJson = SavePrefernce.getCacheFromPreference(getActivity(), "tjjson", "json");
        long saveTime = SavePrefernce.getCacheTimeFromPreference(getActivity(), "tjjson", "time");
        //获取当前时间
        long currTime = System.currentTimeMillis();
        long time = (currTime - saveTime) / 1000 / 60;

        kaolaTask = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                String json = HttpUtil.getJson(Path.TUIJIAN_PATH);
                return json;
            }

            @Override
            public Object parseResult(Object obj) {
                return praseJson(obj);
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {
                //执行刷新的时候，先清空所有的子控件，防止重复添加，
                // 放在解析成功之后执行，可以防止在删掉和添加这个段时间出现空白
                tuijian_ll.removeAllViews();
                //隐藏正在加载的效果
                refreshLayout.setRefreshing(false);

                //如果数据解析成功了，那么隐藏加载动画,EventBus发布消息
                EventBus.getDefault().post(new HideLoadingView());
//                getActivity().sendBroadcast(new Intent(DiscoverFragment.LOADING_ACTION_HIDE));

                List<TuiJian> tuiJianList = (List<TuiJian>) object;
                showBanner(tuiJianList.get(0));
                showEntry(tuiJianList.get(1));
                showItems6(tuiJianList.get(2));
                List<TuiJian> tuiJien = tuiJianList.subList(3, 8);
                showItem5(tuiJien);
                showJiaoQi(tuiJianList.get(8));
                showLike(tuiJianList.get(9));
                showJiFen(tuiJianList.get(10));
                List<TuiJian> tuiJien1 = tuiJianList.subList(12, tuiJianList.size());

                showAfter(tuiJien1);


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
     * 显示快捷入口
     *
     * @param tuiJian
     */
    private void showEntry(TuiJian tuiJian) {

        RecyclerView recyclerView = new RecyclerView(getActivity());
        //线性显示方式
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        //设置横向滚动
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置manager
        recyclerView.setLayoutManager(manager);
        EntryAdapter adapter = new EntryAdapter(getActivity(), tuiJian.getDataList());
        recyclerView.setAdapter(adapter);
        tuijian_ll.addView(recyclerView);

    }

    /**
     * 解析JSON
     *
     * @param obj
     * @return
     */
    private Object praseJson(Object obj) {
        String json = obj.toString();
        try {
            JSONObject root = new JSONObject(json);

            //拿到下载好的字符串之后，把他写到Preference里面
            SavePrefernce.saveCacheToPreference(getActivity(), "tjjson", "json", json);
            SavePrefernce.saveCacheTimeToPreference(getActivity(), "tjjson", "time", root.getLong("serverTime"));

            String code = root.getString("code");
            String message = root.getString("message");
            if ("10000".equals(code) && "success".equals(message)) {
                JSONObject result = root.getJSONObject("result");

                List<TuiJian> tuijianList = TuiJian.arrayTuiJianFromData(result.toString(), "dataList");
                list.addAll(tuijianList);
                Log.d("print", "parseResult: " + tuijianList.size());
                //返回推荐集合
                return tuijianList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 显示广告
     *
     * @param tuiJian
     */
    private void showBanner(TuiJian tuiJian) {
        AutoScrollViewPage viewPage = new AutoScrollViewPage(getActivity());
        final List<TuiJian2> tuiJian2List = tuiJian.getDataList();
        List<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < tuiJian2List.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            imageViews.add(imageView);
        }
        CommenImagePageAdapter adapter = new CommenImagePageAdapter(imageViews) {
            @Override
            public void showImage(int position, ImageView imageView) {
                Picasso.with(getActivity())
                        .load(tuiJian2List.get(position).getPic())//加载图片地址
                        .transform(new TransformatBanner(tuiJian2List.get(position).getPic(), "banner", getActivity()))
                        .into(imageView);//加载到指定的ImageView
            }
        };
        viewPage.setPageAdapter(adapter, imageViews.size());

        tuijian_ll.addView(viewPage);
    }

    /**
     * 显示第一行1
     */
    private void showItems6(TuiJian tuiJian) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tuijian_item6, null);
        TextView tvname = (TextView) view.findViewById(R.id.item_tuijian_name_tv);
        TextView tvmore = (TextView) view.findViewById(R.id.item_tuijian_more_tv);
        tvname.setText(tuiJian.getName());
        int i = tuiJian.getHasmore();
        if (i == 0) {
            tvmore.setVisibility(View.GONE);
        }
        TJItemView item1 = (TJItemView) view.findViewById(R.id.TJItemView1);
        TJItemView item2 = (TJItemView) view.findViewById(R.id.TJItemView2);
        TJItemView item3 = (TJItemView) view.findViewById(R.id.TJItemView3);
        TJItemView item4 = (TJItemView) view.findViewById(R.id.TJItemView4);
        TJItemView item5 = (TJItemView) view.findViewById(R.id.TJItemView5);
        TJItemView item6 = (TJItemView) view.findViewById(R.id.TJItemView6);
        final List<TuiJian2> tuiJian2List = tuiJian.getDataList();
        item1.setTuiJian2(tuiJian2List.get(0));
        item2.setTuiJian2(tuiJian2List.get(1));
        item3.setTuiJian2(tuiJian2List.get(2));
        item4.setTuiJian2(tuiJian2List.get(3));
        item5.setTuiJian2(tuiJian2List.get(4));
        item6.setTuiJian2(tuiJian2List.get(5));

        tuijian_ll.addView(view);

    }

    /**
     * 显示第二行5
     */
    public void showItem5(List<TuiJian> tuiJien) {

        for (int i = 0; i < tuiJien.size(); i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.tuijian_item3, null);
            TextView tvname = (TextView) view.findViewById(R.id.item_tuijian_name_tv3);
            TextView tvmore = (TextView) view.findViewById(R.id.item_tuijian_more_tv3);
            TJItemView item1 = (TJItemView) view.findViewById(R.id.TJItemView13);
            TJItemView item2 = (TJItemView) view.findViewById(R.id.TJItemView23);
            TJItemView item3 = (TJItemView) view.findViewById(R.id.TJItemView33);

            tvname.setText(tuiJien.get(i).getName());
            int num = tuiJien.get(i).getHasmore();
            if (num == 0) {
                tvmore.setVisibility(View.GONE);
            }

            List<TuiJian2> dataList = tuiJien.get(i).getDataList();
            if (dataList.size() > 2) {
                item1.setTuiJian2(dataList.get(0));
                item2.setTuiJian2(dataList.get(1));
                item3.setTuiJian2(dataList.get(2));
            } else {
                item1.setTuiJian2(dataList.get(0));
                item2.setTuiJian2(dataList.get(1));
            }


            tuijian_ll.addView(view);
        }


    }

    /**
     * 显示第三行小娇妻
     */
    public void showJiaoQi(final TuiJian tuiJian) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tuijian_jiaoqi, null);
        ImageView imageview = (ImageView) view.findViewById(R.id.tuijian_item_jq_iv);
        //设置图片
        Picasso.with(getActivity())
                .load(tuiJian.getDataList().get(0).getPic())
                .resize(wsize, hsize)
                .centerCrop()
                .into(imageview);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), tuiJian.getDataList().get(0).getRname(), Toast.LENGTH_SHORT).show();
            }
        });

        tuijian_ll.addView(view);
    }

    /**
     * 显示第四行猜你喜欢
     */
    public void showLike(TuiJian tuiJian) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tuijian_item_cnxh, null);
        TextView tvname = (TextView) view.findViewById(R.id.tuijian_cnxh_tv_title);
        TextView tvmore = (TextView) view.findViewById(R.id.tuijian_cnxh_tv_more);
        tvname.setText(tuiJian.getName());
        int num = tuiJian.getHasmore();
        if (num != 0) {
            tvmore.setVisibility(View.GONE);
        }

        TJitemLike item1 = (TJitemLike) view.findViewById(R.id.TJitemLike1);
        TJitemLike item2 = (TJitemLike) view.findViewById(R.id.TJitemLike2);
        TJitemLike item3 = (TJitemLike) view.findViewById(R.id.TJitemLike3);
        item1.setTuiJian2(tuiJian.getDataList().get(0));
        item2.setTuiJian2(tuiJian.getDataList().get(1));
        item3.setTuiJian2(tuiJian.getDataList().get(2));

        tuijian_ll.addView(view);


    }

    /**
     * 显示第五行积分商城
     */
    public void showJiFen(final TuiJian tuiJian) {

        int size = (int) DeviceUtil.getPxFromDp(getActivity(), 60);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tuijian_item_jifen, null);
        LinearLayout zhuanti = (LinearLayout) view.findViewById(R.id.tuijian_zhuanti);
        LinearLayout huodong = (LinearLayout) view.findViewById(R.id.tuijian_huodong);
        LinearLayout jifensc = (LinearLayout) view.findViewById(R.id.tuijian_jifenshangcheng);
        LinearLayout pdzx = (LinearLayout) view.findViewById(R.id.tuijian_pingdaozixuan);

        ImageView imageview1 = (ImageView) view.findViewById(R.id.tuijian_jifen_iv1);
        ImageView imageview2 = (ImageView) view.findViewById(R.id.tuijian_jifen_iv2);
        ImageView imageview3 = (ImageView) view.findViewById(R.id.tuijian_jifen_iv3);

        Picasso.with(getActivity())
                .load(tuiJian.getDataList().get(0).getPic())//加载图片地址
                .placeholder(R.drawable.ic_default_round_150_150)//还没加载出来的时候显示的默认的图片
                .error(R.drawable.no_net_error_chat_icon)//加载失败要显示的图片
                .resize(size, size)
                .centerCrop()
                .into(imageview1);//加载到指定的ImageView
        Picasso.with(getActivity())
                .load(tuiJian.getDataList().get(1).getPic())//加载图片地址
                .placeholder(R.drawable.ic_default_round_150_150)//还没加载出来的时候显示的默认的图片
                .error(R.drawable.no_net_error_chat_icon)//加载失败要显示的图片
                .resize(size, size)
                .centerCrop()
                .into(imageview2);//加载到指定的ImageView
        Picasso.with(getActivity())
                .load(tuiJian.getDataList().get(2).getPic())//加载图片地址
                .placeholder(R.drawable.ic_default_round_150_150)//还没加载出来的时候显示的默认的图片
                .error(R.drawable.no_net_error_chat_icon)//加载失败要显示的图片
                .resize(size, size)
                .centerCrop()
                .into(imageview3);//加载到指定的ImageView

        zhuanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TuiJian2 tuiJian2 = tuiJian.getDataList().get(0);

                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("tuiJian2", tuiJian2);
                startActivity(intent);

                Toast.makeText(getActivity(), tuiJian.getDataList().get(0).getRname(), Toast.LENGTH_SHORT).show();
            }
        });
        huodong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TuiJian2 tuiJian2 = tuiJian.getDataList().get(1);

                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("tuiJian2", tuiJian2);
                startActivity(intent);
                Toast.makeText(getActivity(), tuiJian.getDataList().get(1).getRname(), Toast.LENGTH_SHORT).show();
            }
        });
        jifensc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TuiJian2 tuiJian2 = tuiJian.getDataList().get(2);

                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("tuiJian2", tuiJian2);
                startActivity(intent);
                Toast.makeText(getActivity(), tuiJian.getDataList().get(2).getRname(), Toast.LENGTH_SHORT).show();
            }
        });
        pdzx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), tuiJian.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        tuijian_ll.addView(view);

    }

    /**
     * 显示积分商城剩下的部分
     */
    public void showAfter(final List<TuiJian> tuiJien1) {
        for (int i = 0; i < tuiJien1.size(); i++) {
            if (i == 3) {
                showJiaoQi(tuiJien1.get(i));
            } else {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.tuijian_item3, null);
                TextView tvname = (TextView) view.findViewById(R.id.item_tuijian_name_tv3);
                TextView tvmore = (TextView) view.findViewById(R.id.item_tuijian_more_tv3);
                TJItemView item1 = (TJItemView) view.findViewById(R.id.TJItemView13);
                TJItemView item2 = (TJItemView) view.findViewById(R.id.TJItemView23);
                TJItemView item3 = (TJItemView) view.findViewById(R.id.TJItemView33);

                tvname.setText(tuiJien1.get(i).getName());
                int num = tuiJien1.get(i).getHasmore();
                if (num == 0) {
                    tvmore.setVisibility(View.GONE);
                }
                List<TuiJian2> dataList = tuiJien1.get(i).getDataList();
                Log.d("print", "=============" + i);
                if (dataList.size() == 1) {
                    item1.setTuiJian2(dataList.get(0));
//                    item2.setTuiJian2(dataList.get(1));
                } else if (dataList.size() == 2) {
                    item1.setTuiJian2(dataList.get(0));
                    item2.setTuiJian2(dataList.get(1));
                } else {
                    item1.setTuiJian2(dataList.get(0));
                    item2.setTuiJian2(dataList.get(1));
                    item3.setTuiJian2(dataList.get(2));
                }


                tuijian_ll.addView(view);
            }

        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消任务，true任务没有执行完也取消，false让任务执行完取消
        kaolaTask.cancel(true);
        kaolaTask = null;
//        list.clear();
    }
}
