package ru.home13.www.dayx.Model.News;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import ru.home13.www.dayx.R;

public class NewsListAdapter extends BaseAdapter {

    private Activity activity;
    private NewsList newsList;

    public NewsListAdapter(final Activity activity, final NewsList newsList) {
        super();
        this.newsList = newsList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public News getItem(int position) {
        return position < getCount() ? newsList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(R.layout.row_news, parent);
        }

        News news = getItem(position);
        return news.getRow(row);
    }
}
