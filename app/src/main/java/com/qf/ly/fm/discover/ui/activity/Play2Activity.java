package com.qf.ly.fm.discover.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.Adapter.PlayerAdapter;
import com.qf.ly.fm.discover.ui.entiy.Player;
import com.qf.ly.fm.discover.ui.utlpath.Path;
import com.qf.ly.fm.discover.ui.utlpath.widget.PageView;
import com.qf.ly.fm.other.utils.DeviceUtil;
import com.qf.ly.fm.other.utils.HttpUtil;
import com.qf.ly.fm.other.utils.ImageUtil;
import com.qf.ly.fm.other.utils.KaolaTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Play2Activity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<PageView> pageViewList;
    private List<Player> playerList;
    private KaolaTask task, task1;
    private PlayerAdapter adapter;
    private LinearLayout imagell;
    private float maxScale = 1.0f, minScale = 0.6f;
    private ImageView iv_tittle;
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play2);
        viewPager = (ViewPager) findViewById(R.id.play2_viewpager);
        iv_tittle = (ImageView) findViewById(R.id.top_play2_iv);
        tv_name = (TextView) findViewById(R.id.top_title2_tv);
        imagell = (LinearLayout) findViewById(R.id.activity_play2);

        //参数1:绘制的时候是否按顺序
        //参数2：对每一个page进行修改
        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                // -1 0 1
                if (position > 1) {
                    position = 1;
                } else if (position < -1) {
                    position = -1;
                }
                //tempPosition代表展示原图的tempPosition大小
                float tempPosition = position >= 0 ? (1 - position) : (1 + position);
                //缩放比例
                float scale = minScale + tempPosition * (maxScale - minScale);
                page.setScaleX(scale);
                page.setScaleY(scale);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                final Player player = playerList.get(position);
                showBg(player);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        playerList = new ArrayList<>();
        pageViewList = new ArrayList<>();
        adapter = new PlayerAdapter(this, pageViewList);
        viewPager.setAdapter(adapter);
        task = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                return HttpUtil.getJson(Path.PLAYER);
            }

            @Override
            public Object parseResult(Object obj) {
                String json = obj.toString();
                try {
                    JSONObject root = new JSONObject(json);
                    String code = root.getString("code");
                    String message = root.getString("message");
                    if ("10000".equals(code) && "success".equals(message)) {
                        JSONObject result = root.getJSONObject("result");
                        List<Player> players = Player.arrayPlayerFromData(result.toString(), "dataList");
                        Log.d("print", "parseResult: " + players.size());
                        //返回分类集合1
                        return players;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {
                List<Player> list = (List<Player>) object;
                for (int i = 0; i < list.size(); i++) {
                    playerList.add(list.get(i));
                    //添加布局
                    PageView view = new PageView(Play2Activity.this);
                    view.setPlayer(list.get(i));
                    pageViewList.add(view);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                Toast.makeText(Play2Activity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
        task.execute();
    }

    private void showBg(final Player player) {
        task1 = new KaolaTask(new KaolaTask.IDownIamge() {
            @Override
            public Bitmap loadImage() {
                return HttpUtil.getBitmap(player.getPic());
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {
                Bitmap bitmap = (Bitmap) object;

                Matrix matrix = new Matrix();
                float scale = 1.0f * DeviceUtil.getPxFromDp(Play2Activity.this, 50) / bitmap.getWidth();
                matrix.setScale(scale, scale);
                Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                tv_name.setText(player.getName());
                iv_tittle.setImageBitmap(bitmap1);

                imagell.setBackground(ImageUtil.BlurImages(bitmap1, Play2Activity.this));
                bitmap.recycle();
            }

            @Override
            public void onError() {
                Toast.makeText(Play2Activity.this, "图片下载失败", Toast.LENGTH_SHORT).show();
            }
        });
        task1.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel(true);
            task = null;
        }
        if (task1 != null) {
            task1.cancel(true);
            task = null;
        }
    }
}
