package com.module.checkview.bean;


import com.module.checkview.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by wubo on 2018/9/28.
 */

public class GroupBean extends ExpandableGroup<ChildBean> {
    //执行人头像url
    private String url;
    //执行人昵称
    private String name;
    //选择状态 0未选中 1半选中 2全选中
    private int status;

    public GroupBean(String url,String name, List<ChildBean> items) {
        super(items);
        this.url = url;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
