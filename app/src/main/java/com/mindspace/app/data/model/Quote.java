package com.mindspace.app.data.model;

import java.util.List;

public class Quote {
    private String _id;
    private String content;
    private String author;
    private List<String> tags;
    private String authorSlug;
    private int length;
    private String dateAdded;
    private String dateModified;

    public Quote() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return tags != null && !tags.isEmpty() ? tags.get(0) : "";
    }
}
