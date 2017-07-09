package com.qf.ly.fm.discover.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.Adapter.ComListAdapter;
import com.qf.ly.fm.discover.ui.entiy.Comment;
import com.qf.ly.fm.discover.ui.entiy.TuiJian2;
import com.qf.ly.fm.discover.ui.utlpath.Path;
import com.qf.ly.fm.other.utils.DeviceUtil;
import com.qf.ly.fm.other.utils.HttpUtil;
import com.qf.ly.fm.other.utils.KaolaTask;
import com.qf.ly.fm.other.utils.LogUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComActivity extends AppCompatActivity {
    private TuiJian2 tuiJian2;
    private ListView listView;
    private EditText editText;
    private ImageView imageView, headiv;
    private TextView tv1, tv2;
    private KaolaTask task, commentTask;
    private long resourceid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com);
        tuiJian2 = getIntent().getParcelableExtra("tuijian2");
        resourceid = tuiJian2.getRid();
        listView = (ListView) findViewById(R.id.comment_listview);
        editText = (EditText) findViewById(R.id.comment_edit);
        imageView = (ImageView) findViewById(R.id.comment_send_iv);
        View view = LayoutInflater.from(this).inflate(R.layout.list_header_comment, null);
        headiv = (ImageView) view.findViewById(R.id.header_comments_pic_iv);
        tv1 = (TextView) view.findViewById(R.id.header_comments_title_tv);
        tv2 = (TextView) view.findViewById(R.id.header_comments_name_tv);
        Picasso.with(this)
                .load(tuiJian2.getPic())
                .error(R.drawable.player_default_bg)
                .resize((int) DeviceUtil.getPxFromDp(this, 100), (int) DeviceUtil.getPxFromDp(this, 100))
                .centerCrop()
                .into(headiv);
        tv1.setText(tuiJian2.getAlbumName());
        tv2.setText(tuiJian2.getRname());
        listView.addHeaderView(view);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //先展示评论列表
        showList();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentTask = new KaolaTask(new KaolaTask.IRequest() {
                    @Override
                    public Object doRequest() {
                        //content=%E6%9D%A8%E6%B3%B0%E6%88%90&revieweruid=3206035&resourcetype=1&reviewername=%E4%BB%96%E5%A4%A9%E5%A4%A9%E5%A4%B4%E7%96%BC&commenttype=0&resourceid=1000002787676&
                        //content=wwwww&revieweruid=2754846&resourcetype=1&reviewername=QianFengLaoLiu&commenttype=0&resourceid=1000002787562&
                        Map<String, String> params = new HashMap<>();
                        params.put("content", editText.getText().toString().trim());//去掉空格
                        params.put("revieweruid", "3206035");
                        params.put("resourcetype", "1");
                        params.put("reviewername", "他天天头疼");
                        params.put("commenttype", "0");
                        params.put("resourceid", "" + resourceid);
                        return HttpUtil.doPost(Path.SUBMITCOMMNET, params);
                    }

                    @Override
                    public Object parseResult(Object obj) {
                        Log.d("print", "=========="+obj.toString());
                        try {
                            JSONObject root = new JSONObject(obj.toString());
                            String message = root.getString("message");
                            String code = root.getString("code");
                            Log.d("print", "=================="+code+message);
                            if ("10000".equals(code) && "success".equals(message)) {
                                return true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                }, new KaolaTask.IRequestCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        LogUtil.d(object.toString());
                        boolean result = (boolean) object;
                        //如果请求成功了，那么重新请求列表
                        if (result) {
                            editText.setText("");
                            Toast.makeText(ComActivity.this, "评论成功!", Toast.LENGTH_SHORT).show();
                            showList();
                        }
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(ComActivity.this, "评论失败!", Toast.LENGTH_SHORT).show();
                    }
                });
                commentTask.execute();
            }
        });
    }

    private void showList() {
        task = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                return HttpUtil.getJson(Path.COMMNETLSIT + resourceid);
            }

            @Override
            public Object parseResult(Object obj) {
                try {
                    JSONObject root = new JSONObject(obj.toString());

                    String message = root.getString("message");

                    String code = root.getString("code");

                    if ("10000".equals(code) && "success".equals(message)) {
                        JSONObject result = root.getJSONObject("result");
                        List<Comment> dataList = Comment.arrayCommentsFromData(result.toString(), "dataList");

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
                List<Comment> dataList = (List<Comment>) object;
                ComListAdapter adapter = new ComListAdapter(ComActivity.this, dataList);
                listView.setAdapter(adapter);

                recycleTask();
            }

            @Override
            public void onError() {
                Toast.makeText(ComActivity.this, "请求列表失败!", Toast.LENGTH_SHORT).show();
                recycleTask();
            }
        });

        task.execute();
    }

    private void recycleTask() {
        task.cancel(false);
        task = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel(false);
            task = null;
        }
        if (commentTask != null) {
            commentTask.cancel(false);
            commentTask = null;
        }

    }
}
