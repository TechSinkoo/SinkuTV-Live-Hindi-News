package com.abhinav.sinkutv;

public class CategoryModel {
    public String name;
 // private List<String> sets;
    String imageurl;
    private String url;
    String websiteurl;
    String key;



    public CategoryModel()
    {
        //for firebase
    }

    public CategoryModel(String name, String imageurl, String url, String websiteurl,String key) {
        this.name = name;
        this.imageurl = imageurl;
        this.url = url;
        this.key = key;
        this.websiteurl = websiteurl;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getWebsiteurl() {
        return websiteurl;
    }

    public void setWebsiteurl(String websiteurl) {
        this.websiteurl = websiteurl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
