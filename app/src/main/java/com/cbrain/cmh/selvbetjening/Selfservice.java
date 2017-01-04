package com.cbrain.cmh.selvbetjening;

import org.jsoup.nodes.Element;

public class Selfservice {

    public String title;
    public String linkhref;

    public Selfservice(String linktitle, String href) {
        title = linktitle;
        linkhref = href;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.linkhref = link;
    }

    public String getTitle() {
        return this.title;
    }

    public String getLink() {
        return this.linkhref;
    }
}
