package me.yamlee.jsbridge;

import android.net.Uri;

/**
 * @author by fengruicong on 16/12/13.
 */
public class NavMenuEntity {
    private Uri iconUri;//图片uri
    private String text;
    private String schema;

    public Uri getIconUri() {
        return iconUri;
    }

    public void setIconUri(Uri iconUri) {
        this.iconUri = iconUri;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
