package com.sw.haruka.model;

public class SearchTempFile {
    private String name;
    private String path;

    public SearchTempFile() {}

    public SearchTempFile(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
