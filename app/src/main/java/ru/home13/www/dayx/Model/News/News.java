package ru.home13.www.dayx.Model.News;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.home13.www.dayx.R;

public class News implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("important")
    @Expose
    private String important;
    @SerializedName("image")
    @Expose
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImportant() {
        return important;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public View getRow(View convertView) {
        try {
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvTitle.setText(getTitle());

            TextView tvDescription = (TextView) convertView.findViewById(R.id.tv_description);
            tvDescription.setText(getDescription());

            ImageView ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
            if (getImage() != null) {
                ivImage.setImageDrawable(null);
                ivImage.setVisibility(View.VISIBLE);
            } else {
                ivImage.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            //TODO логгер
        }
        return convertView;

    }

    public View getView(View view) {
        try {
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvTitle.setText(getTitle());

            WebView webView = (WebView) view.findViewById(R.id.wv_text);
            WebViewClient webViewClient = new WebViewClient();
            // TODO доделать
            webView.setWebViewClient(webViewClient);
        } catch (Exception e) {
            //TODO логгер
        }
        return view;

    }
}
