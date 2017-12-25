package com.example.day21_rikao;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public String urlNews1 = "http://api.expoon.com/AppNews/getNewsList/type/1/p/1";
    public String urlNews2 = "http://api.expoon.com/AppNews/getNewsList/type/2/p/1";
    public String urlNews3 = "http://api.expoon.com/AppNews/getNewsList/type/3/p/1";
    ArrayList<News.DataBean> list = new ArrayList<>();

    String[] imgs = {
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510669248643&di=fdcc544fe8012b0f5496c1e06293d349&imgtype=0&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F19%2F64%2F90%2F56fe106233eee_1024.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510669248639&di=728884e791c20785c02531f7e6e731da&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F279759ee3d6d55fb26f6d4ef67224f4a21a4ddc3.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510669297237&di=56861cac8cec4891271ff1a9caf8a852&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3D78212b4e4aa7d933bffdec77987bfd25%2F48540923dd54564ee5caf563bbde9c82d1584f38.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510669297236&di=a3ed6411f9456a4f450e66197c3d9cb9&imgtype=0&src=http%3A%2F%2Fimg.pcgames.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fgamephotolib%2F1607%2F21%2Fc1%2F15708251_1469103598002.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2567131626,1221882321&fm=27&gp=0.jpg"
    };

    private Banner banner;
    private PullToRefreshListView pull;
    private Myadpater md;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取控件
        banner = (Banner) findViewById(R.id.banner);
        banner.setImageLoader(new Mybanner());//重写图片的类
        banner.setImages(Arrays.asList(imgs));//放入图片数组
        banner.start();//开始轮播
        //获取控件
        pull = (PullToRefreshListView) findViewById(R.id.pull);
        pull.setMode(PullToRefreshBase.Mode.BOTH);//允许上拉下拉
        //获取网络数据
        getDtata();
        pull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                list.clear();
                getDtata2();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getDtata3();
            }
        });
        md = new Myadpater();
        ListView lv = pull.getRefreshableView();
        lv.setAdapter(md);//适配器



    }

    //获取网络数据
    private void getDtata() {
        new MyAsyctask().execute(urlNews1);
    }

    private void getDtata2() {
        new MyAsyctask().execute(urlNews2);
    }

    private void getDtata3() {
        new MyAsyctask().execute(urlNews3);
    }

    //适配器
    private class Myadpater extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                 view = View.inflate(MainActivity.this, R.layout.datatext, null);
            }
            TextView textView = view.findViewById(R.id.text);
            textView.setText(list.get(i).getNews_title());
            ImageView img = view.findViewById(R.id.img);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(list.get(i).getPic_url(),img);
            return view;
        }
    }

    private class Mybanner extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage((String) path, imageView);
        }
    }

    //解析数据
    private class MyAsyctask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NetUtil.getNetJson(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            News news = gson.fromJson(s, News.class);
            List<News.DataBean> data = news.getData();
            list.addAll(data);

            //刷新适配器
            md.notifyDataSetChanged();
            //停止刷新
            pull.onRefreshComplete();
        }
    }
}
