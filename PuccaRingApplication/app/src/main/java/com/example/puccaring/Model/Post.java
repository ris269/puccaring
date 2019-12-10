package com.example.puccaring.Model;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

public class Post {
    private String postid;
    private String postimage;
    private String description;
    private String publisher;
    private String tags;

    public Post(String postid, String postimage, String description, String publisher, String tags) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
        this.tags = tags;
    }

    public Post() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<String> getListTagSeparate() {
        if (tags == null)
            return null;
        return Arrays.asList(tags.split(","));
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Post))
            return false;
        else {
            Post temp = (Post) obj;
            return temp.postid.equals(this.postid);
        }
    }
}
