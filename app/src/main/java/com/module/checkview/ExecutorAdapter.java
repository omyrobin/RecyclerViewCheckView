package com.module.checkview;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.module.checkview.bean.ChildBean;
import com.module.checkview.bean.GroupBean;
import com.module.checkview.bean.TimeBean;
import com.module.checkview.data.Data;
import com.module.checkview.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.module.checkview.expandablerecyclerview.listeners.OnGroupClickListener;
import com.module.checkview.expandablerecyclerview.models.ExpandableGroup;
import com.module.checkview.expandablerecyclerview.models.ExpandableListPosition;
import com.module.checkview.expandablerecyclerview.viewholders.ChildViewHolder;
import com.module.checkview.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by wubo on 2018/9/28.
 */

public class ExecutorAdapter extends ExpandableRecyclerViewAdapter<ExecutorAdapter.ExecutorGVH, ExecutorAdapter.ExecutorCVH> {

    private Context context;
    private List<GroupBean> groupBeans;
    private ImageButton ib_all_check;
    //统计父布局选中状态----执行人
    private Map<Integer,Integer> groupCheckMap = new HashMap<>();
    //统计父布局下所有Item选中状态----当前GroupPosition所有日期
    private Map<Integer,Map<String ,Integer>> group_childCheckMap = new HashMap<>();
    //统计Item选中状态
    Map<String ,Integer> childCheckMap = new HashMap<>();
    //父布局ImageButton Check View集合;
    private Map<Integer, ImageButton> groupViewMap = new HashMap<>();

    public ExecutorAdapter(Context context, List<GroupBean> groupBeans, ImageButton ib_all_check) {
        super(groupBeans);
        this.context = context;
        this.groupBeans = groupBeans;
        this.ib_all_check = ib_all_check;
    }

    @Override
    public ExecutorGVH onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View GroupView = LayoutInflater.from(context).inflate(R.layout.item_executor_group, parent,false);
        return new ExecutorGVH(GroupView);
    }

    @Override
    public ExecutorCVH onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View ChildView = LayoutInflater.from(context).inflate(R.layout.item_executor_child, parent, false);
        return new ExecutorCVH(ChildView);
    }

    @Override
    public void onBindGroupViewHolder(ExecutorGVH holder, final int groupPosition, ExpandableGroup group) {
        holder.tv_executor_name.setText(groupBeans.get(groupPosition).getName());
        onBindGroupCheckBoxStatus(groupBeans.get(groupPosition).getStatus(),groupPosition,holder.ib_exexutor_check);
        //由于ChildViewHolder中无法获取Group中的ImageButton， 存入集合中取用
        groupViewMap.put(groupPosition, holder.ib_exexutor_check);

        holder.ib_exexutor_check.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int status = groupBeans.get(groupPosition).getStatus();
                status = status == Constants.CHECK ? Constants.UNCHECK : Constants.CHECK;
                onBindGroupCheckBoxStatus(status, groupPosition, (ImageButton) v);

                int childItemCount = groupBeans.get(groupPosition).getItems().size();
                int gridLayoutItemCount = groupBeans.get(groupPosition).getItems().get(0).getTimes().size();
                for (int i = 0; i < childItemCount; i++) {
                    //设置所有子View的ImageButton状态
                    groupBeans.get(groupPosition).getItems().get(i).setStatus(status);
                    childCheckMap.put(groupPosition+"-"+i, status);
                    group_childCheckMap.put(groupPosition, childCheckMap);
                    for (int j = 0; j < gridLayoutItemCount; j++) {
                        //修改数据 gridLayoutItem 时间选中状态
                        groupBeans.get(groupPosition).getItems().get(i).getTimes().get(j).setSelected(status == Constants.CHECK);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBindChildViewHolder(final ExecutorCVH holder, final int groupPosition, ExpandableGroup group, final int childIndex) {
        ChildBean childBeans = groupBeans.get(groupPosition).getItems().get(childIndex);
        holder.tv_child_date.setText(childBeans.getDate());
        onBindChildCheckBoxStatus(childBeans.getStatus(),groupPosition, childIndex, holder.ib_child_check);
        holder.gl_child_time.removeAllViews();
        holder.ib_child_check.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //子ITEM对应的ImageButton状态
                int status = groupBeans.get(groupPosition).getItems().get(childIndex).getStatus();
                status = status == Constants.CHECK ? Constants.UNCHECK : Constants.CHECK;
                onBindChildCheckBoxStatus(status,groupPosition, childIndex, (ImageButton) v);

                childCheckMap.put(groupPosition+"-"+childIndex, status);
                group_childCheckMap.put(groupPosition, childCheckMap);

                int gridLayoutItemCount = groupBeans.get(groupPosition).getItems().get(childIndex).getTimes().size();
                for (int i = 0; i < gridLayoutItemCount; i++) {
                    //修改数据 gridLayoutItem 时间选中状态
                    ChildBean childBean = groupBeans.get(groupPosition).getItems().get(childIndex);
                    List<TimeBean> timeBeans = childBean.getTimes();
                    timeBeans.get(i).setSelected(status == Constants.CHECK);

                    //修改gridLayoutItem UI
                    onBindGridLayoutItem(holder.gl_child_time.getChildAt(i), groupPosition, childIndex, i, (ImageButton) v);
                }
            }
        });

        //获取时间数据
        List<TimeBean> times = groupBeans.get(groupPosition).getItems().get(childIndex).getTimes();
        //获取行数
        int row;
        if(times.size()/4 == 0){
            row = times.size()/4;
        }else{
            row = times.size()/4 + 1;
        }
        //获取列数
        int column = 4;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i*column+j>= times.size()) return;
                TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.item_gridlayout_time,holder.gl_child_time, false);
                view.setText(groupBeans.get(groupPosition).getItems().get(childIndex).getTimes().get(i*column+j).getTime());
                view.setSelected(groupBeans.get(groupPosition).getItems().get(childIndex).getTimes().get(i*column+j).isSelected());
                view.setId(i*column+j);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChildBean childBean = groupBeans.get(groupPosition).getItems().get(childIndex);
                        List<TimeBean> timeBeans = childBean.getTimes();
                        boolean isSelected = timeBeans.get(v.getId()).isSelected();
                        groupBeans.get(groupPosition).getItems().get(childIndex).getTimes().get(v.getId()).setSelected(!isSelected);
                        onBindGridLayoutItem(v, groupPosition, childIndex, v.getId(), holder.ib_child_check);
                    }
                });
                //使用Spec定义子控件的位置
                GridLayout.Spec rowSpec = GridLayout.spec(i,1);
                GridLayout.Spec columnSpec = GridLayout.spec(j,1);

                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
//                layoutParams.height = 100;
//                layoutParams.width = 100;
                holder.gl_child_time.addView(view,layoutParams);
            }
        }
    }

    private void onBindAllCheckBoxStatus(int status){
        setImageButtonStatus(status,ib_all_check);
    }

    //绑定父布局选择状态
    private void onBindGroupCheckBoxStatus(int status, int groupPosition, ImageButton button){
        groupBeans.get(groupPosition).setStatus(status);
        setImageButtonStatus(status,button);
    }

    //绑定子布局选择状态
    private void onBindChildCheckBoxStatus(int status, int groupPosition, int childIndex, ImageButton button){
        groupBeans.get(groupPosition).getItems().get(childIndex).setStatus(status);
        setImageButtonStatus(status,button);
    }

    //设置ImageButton对应状态的icon
    private void setImageButtonStatus(int status, ImageButton button){
        switch (status){
            case Constants.CHECK:
                button.setImageResource(R.drawable.icon_check);
                break;

            case Constants.HALFCHECK:
                button.setImageResource(R.drawable.icon_halfcheck);
                break;

            default:
                button.setImageResource(R.drawable.icon_uncheck);
                break;
        }
    }

    public void onBindGridLayoutItem(View v, int groupPosition, int childPosition, int count,ImageButton button) {
        //变更time的选择状态，并修改对应数据
        ChildBean childBean = groupBeans.get(groupPosition).getItems().get(childPosition);
        List<TimeBean> timeBeans = childBean.getTimes();
        boolean isSelected = timeBeans.get(count).isSelected();
        v.setSelected(isSelected);


        changeStatus(timeBeans, v, groupPosition, childPosition, count,button);

    }

    //统计计数 并根据计数修改UI
    private void changeStatus(List<TimeBean> timeBeans, View v, int groupPosition, int childPosition, int count,ImageButton button) {
        //子布局GridLayout Item选中状态计数
        int selectCount = 0;
        for (int i = 0; i < timeBeans.size(); i++) {
            if(timeBeans.get(i).isSelected()){
                selectCount ++;
            }
        }
        //根据子布局GridLayout Item选中计数变更子布局ImageButton状态
        if(selectCount == timeBeans.size()){
            onBindChildCheckBoxStatus(Constants.CHECK, groupPosition, childPosition, button);
            childCheckMap.put(groupPosition+"-"+childPosition, Constants.CHECK);
            group_childCheckMap.put(groupPosition, childCheckMap);
        }else if(selectCount != 0){
            onBindChildCheckBoxStatus(Constants.HALFCHECK, groupPosition, childPosition,button);
            childCheckMap.put(groupPosition+"-"+childPosition, Constants.HALFCHECK);
            group_childCheckMap.put(groupPosition, childCheckMap);
        }else{
            onBindChildCheckBoxStatus(Constants.UNCHECK, groupPosition, childPosition,button);
            childCheckMap.put(groupPosition+"-"+childPosition, Constants.UNCHECK);
            group_childCheckMap.put(groupPosition, childCheckMap);
        }

        //子布局选中状态计数
        int groupselectCount = 0;
        Map<String, Integer> childCheckMapTmp = group_childCheckMap.get(groupPosition);
        for (Map.Entry<String, Integer> item : childCheckMapTmp.entrySet()){
            String key = item.getKey().split("-")[0];
           if(Integer.valueOf(key) == groupPosition && item.getValue() == Constants.CHECK){
                groupselectCount ++;
            }
        }

        Log.i("groupselectCount", " groupselectCount :  " + groupselectCount);

        //根据子布局计数变更父布局ImageButton状态
        ImageButton groupButton = groupViewMap.get(groupPosition);
        if(groupButton != null){
            if(groupselectCount == groupBeans.get(groupPosition).getItems().size()){
                onBindGroupCheckBoxStatus(Constants.CHECK, groupPosition, groupButton);
                groupCheckMap.put(groupPosition,Constants.CHECK);
            }else if(groupselectCount != 0 || selectCount!=0){
                onBindGroupCheckBoxStatus(Constants.HALFCHECK, groupPosition,groupButton);
                groupCheckMap.put(groupPosition,Constants.HALFCHECK);
            }else{
                onBindGroupCheckBoxStatus(Constants.UNCHECK, groupPosition,groupButton);
                groupCheckMap.put(groupPosition,Constants.UNCHECK);
            }
        }
    }


//        int allSelectCount = 0;
//        for (Map.Entry<Integer, Integer> all : groupCheckMap.entrySet() ) {
//            if(all.getValue() == Constants.CHECK){
//                allSelectCount++;
//            }
//        }
//        if(allSelectCount == groupBeans.size()){
//            onBindAllCheckBoxStatus(Constants.CHECK);
//        }else if()

    //父布局ViewHolder
    class ExecutorGVH extends GroupViewHolder {

        private TextView tv_executor_name;
        private ImageButton ib_exexutor_check;

        public ExecutorGVH(View itemView) {
            super(itemView);
            tv_executor_name = itemView.findViewById(R.id.tv_executor_name);
            ib_exexutor_check = itemView.findViewById(R.id.ib_exexutor_check);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
        }

        @Override
        public void setOnGroupClickListener(OnGroupClickListener listener) {
            super.setOnGroupClickListener(listener);
        }

        @Override
        public void expand() {
            super.expand();
        }

        @Override
        public void collapse() {
            super.collapse();
        }
    }

    //子布局ViewHolder
    class ExecutorCVH extends ChildViewHolder {

        private TextView tv_child_date;
        private ImageButton ib_child_check;
        private GridLayout gl_child_time;

        public ExecutorCVH(View itemView) {
            super(itemView);
            tv_child_date = itemView.findViewById(R.id.tv_child_date);
            ib_child_check = itemView.findViewById(R.id.ib_child_check);
            gl_child_time = itemView.findViewById(R.id.gl_child_time);
        }
    }
}
