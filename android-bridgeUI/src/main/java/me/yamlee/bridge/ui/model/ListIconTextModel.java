package me.yamlee.bridge.ui.model;

import android.net.Uri;


public class ListIconTextModel {
    private Uri iconUri;
    private String clickUri;
    private String text;

    public Uri getIconUri() {
        return iconUri;
    }

    public void setIconUri(Uri iconId) {
        this.iconUri = iconId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getClickUri() {
        return clickUri;
    }

    public void setClickUri(String clickUri) {
        this.clickUri = clickUri;
    }
}
