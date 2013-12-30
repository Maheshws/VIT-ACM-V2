package com.mahesh.vitacm;

/**
 * Created by Mahesh on 12/28/13.
 */
public class BlogObject {

    private int BlogID;
    private String BlogBy;
    private String BlogTitle;
    private String BlogTime;
    private String BlogImage;
    private String BlogContent;

    public BlogObject(int blogID, String blogBy, String blogTitle, String blogTime, String blogImage, String blogContent) {
        BlogID = blogID;
        BlogBy = blogBy;
        BlogTitle = blogTitle;
        BlogContent = blogContent;
        BlogTime = blogTime;
        BlogImage = blogImage;
    }


    public int getBlogID() {
        return BlogID;
    }

    public String getBlogBy() {
        return BlogBy;
    }

    public String getBlogTitle() {
        return BlogTitle;
    }

    public String getBlogTime() {
        return BlogTime;
    }

    public String getBlogImage() {
        return BlogImage;
    }

    public String getBlogContent() {
        return BlogContent;
    }
}
