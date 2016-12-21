package com.cbrain.cmh.selvbetjening;

import org.jsoup.nodes.Element;

public class Selfservice {
    public String title;
    public String linkhref;

    public Selfservice() {}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(Element link) {
        this.linkhref = R.string.http + link.select("a").attr("href");
    }

    public String getTitle() {
        return this.title;
    }

    public String getLink() {
        return this.linkhref;
    }
}
