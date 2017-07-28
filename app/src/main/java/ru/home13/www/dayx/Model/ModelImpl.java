package ru.home13.www.dayx.Model;

import android.database.Observable;

import ru.home13.www.dayx.Model.News.News;
import ru.home13.www.dayx.Model.News.NewsList;

public class ModelImpl implements Model {

    @Override
    public Observable<User> auth(String name) {
        return null;
    }

    @Override
    public Observable<User> getUser(String name) {
        return null;
    }

    @Override
    public Observable<NewsList> getNewsList() {
        return null;
    }

    @Override
    public Observable<News> getNews(int id) {
        return null;
    }

    @Override
    public Observable<Action> getAction() {
        return null;
    }
}
