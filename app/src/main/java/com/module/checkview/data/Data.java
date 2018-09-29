package com.module.checkview.data;

import com.module.checkview.bean.ChildBean;
import com.module.checkview.bean.GroupBean;
import com.module.checkview.bean.TimeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wubo on 2018/9/28.
 */

public class Data {
    public  List<GroupBean> groupBeans = new ArrayList<>();

    public void initData(){
        List<ChildBean> childBeans;
        List<TimeBean> timeBeans;
        for (int i = 0; i < 2; i++) {
            childBeans = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                timeBeans = new ArrayList<>();
                for (int k = 0; k < 6 ; k++) {
                    timeBeans.add(new TimeBean((19 + k + ": 00"), false));
                }
                childBeans.add(new ChildBean("2018-09-2" + (6+j), timeBeans));
            }
            groupBeans.add(new GroupBean("http://xxxxx","鸟鸟" + i,childBeans));
        }
    }
}
