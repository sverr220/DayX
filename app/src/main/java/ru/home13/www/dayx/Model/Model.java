package ru.home13.www.dayx.Model;

import android.database.Observable;

import ru.home13.www.dayx.Model.News.News;
import ru.home13.www.dayx.Model.News.NewsList;

public interface Model {

    Observable<User> auth(String name);
    Observable<User> getUser(String name);
    Observable<NewsList> getNewsList();
    Observable<News> getNews(int id);
    Observable<Action> getAction();

}
