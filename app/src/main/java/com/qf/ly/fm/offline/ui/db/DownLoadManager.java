package com.qf.ly.fm.offline.ui.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qf.ly.fm.offline.ui.entiy.DownLoadEntiy;
import com.qf.ly.fm.other.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20 0020.20:41
 * 版权所有 盗版必究
 */

public class DownLoadManager {
    private MySqlite mySqlite;
    private SQLiteDatabase db;
    private static DownLoadManager manager;
    private static Context mContext;

    /**
     * 单例模式
     *
     * @return
     */
    public static DownLoadManager getInstance() {

        if (manager == null) {
            synchronized (DownLoadManager.class) {
                if (manager == null) {
                    manager = new DownLoadManager();
                }
            }
        }
        return manager;
    }

    //在Application中初始化
    public static void init(Context context) {
        mContext = context;
    }

    public DownLoadManager() {
        mySqlite = new MySqlite(mContext);
    }

    /**
     * 插入操作
     *
     * @param entity
     */
    public void insert(DownLoadEntiy entity) {
        db = mySqlite.getWritableDatabase();
        //表名 null ContentValues
        long insert = db.insert(MySqlite.TABLE_NAME, null, getContentValues(entity));
        if (insert != 0) {
            LogUtil.d("添加成功");
        }
        db.close();
    }

    /**
     * 删除操作
     *
     * @param entity
     */
    public void delete(DownLoadEntiy entity) {
        db = mySqlite.getWritableDatabase();
        db.delete(MySqlite.TABLE_NAME, MySqlite.DownloadTable.COLUMNS_ALBUMNAME + " = ?",
                new String[]{entity.getAlbumName()});
        db.close();
    }

    /**
     * 查询操作
     *
     * @return
     */
    public List<DownLoadEntiy> getList() {
        db = mySqlite.getReadableDatabase();
        String sql = "select * from " + MySqlite.TABLE_NAME;
        //执行查询语句会返回一个游标对象
//        Cursor cursor=db.query(MySqlite.TABLE_NAME, null, null, null, null, null, null);
        Cursor cursor = db.rawQuery(sql, null);

        List<DownLoadEntiy> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String image = cursor.getString(cursor.getColumnIndex(MySqlite.DownloadTable.COLUMNS_IMAGE));
            String albumname = cursor.getString(cursor.getColumnIndex(MySqlite.DownloadTable.COLUMNS_ALBUMNAME));
            int count = cursor.getInt(cursor.getColumnIndex(MySqlite.DownloadTable.COLUMNS_COUNT));
            String size = cursor.getString(cursor.getColumnIndex(MySqlite.DownloadTable.COLUMNS_SIZE));

            DownLoadEntiy entity = new DownLoadEntiy(image, albumname, count, size);
            list.add(entity);
        }
        cursor.close();
        db.close();
        return list;
    }


    public ContentValues getContentValues(DownLoadEntiy entity) {
        ContentValues values=new ContentValues();
        //字段名  字段值
        values.put(MySqlite.DownloadTable.COLUMNS_IMAGE,entity.getImage());
        values.put(MySqlite.DownloadTable.COLUMNS_ALBUMNAME,entity.getAlbumName());
        values.put(MySqlite.DownloadTable.COLUMNS_COUNT,entity.getCount());
        values.put(MySqlite.DownloadTable.COLUMNS_SIZE,entity.getSize());
        return values;
    }
}
