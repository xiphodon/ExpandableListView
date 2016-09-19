package com.qiyun.cyt.expandablelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 可展开的列表组件（API局部刷新）
 *
 * Created by GuoChang on 16/9/19.
 */
public class Main2Activity extends AppCompatActivity {

    //
    int[] logos = new int[]{
            R.drawable.aa,
            R.drawable.bb,
            R.drawable.cc
    };

    //群组类型
    private String[] groupType = new String[]{
            "类型a",
            "类型b",
            "类型c",
            "类型d",
            "类型e",
            "类型f"
    };

    //列表各个群组数据
    public Bean[][] children = new Bean[][]{
            {new Bean("全选", false), new Bean("a1", false), new Bean("a2", false), new Bean("a3", false), new Bean("a4", false), new Bean("a5", false)},
            {new Bean("全选", false), new Bean("b1", false), new Bean("b2", false), new Bean("b3", false), new Bean("b4", false), new Bean("b5", false)},
            {new Bean("全选", false), new Bean("c1", false), new Bean("c2", false), new Bean("c3", false), new Bean("c4", false), new Bean("c5", false)},
            {new Bean("全选", false), new Bean("d1", false), new Bean("d2", false), new Bean("d3", false), new Bean("d4", false), new Bean("d5", false)},
            {new Bean("全选", false), new Bean("e1", false), new Bean("e2", false), new Bean("e3", false), new Bean("e4", false), new Bean("e5", false)},
            {new Bean("全选", false), new Bean("f1", false), new Bean("f2", false), new Bean("f3", false), new Bean("f4", false), new Bean("f5", false)},
    };
    private ExpandableListView elv;
    private BaseExpandableListAdapter baseExpandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //可展开列表组件的Adapter
        //                ImageView logo = new ImageView(MainActivity.this);
//                logo.setImageResource(logos[groupPosition]);
//                linearLayout.addView(logo);
        baseExpandableListAdapter = new BaseExpandableListAdapter() {


            @Override
            public int getGroupCount() {
                return groupType.length;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return children[groupPosition].length;
            }

            @Override
            public String getGroup(int groupPosition) {
                return groupType[groupPosition];
            }

            @Override
            public Bean getChild(int groupPosition, int childPosition) {
                return children[groupPosition][childPosition];
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                LinearLayout linearLayout = new LinearLayout(Main2Activity.this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                ImageView logo = new ImageView(MainActivity.this);
//                logo.setImageResource(logos[groupPosition]);
//                linearLayout.addView(logo);
                TextView textView = new TextView(Main2Activity.this);
                textView.setPadding(50, 0, 0, 0);
                textView.setTextSize(20);
                textView.setText(getGroup(groupPosition).toString());
                linearLayout.addView(textView);
                return linearLayout;
            }

            @Override
            public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                TextView textView = new TextView(Main2Activity.this);
                textView.setId(R.id.tv);
                textView.setPadding(60, 0, 0, 0);
                textView.setTextSize(20);
                textView.setText(getChild(groupPosition, childPosition).name);
                if (getChild(groupPosition, childPosition).mark) {
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                return textView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }
        };
        elv = (ExpandableListView) findViewById(R.id.elv);
        elv.setAdapter(baseExpandableListAdapter);

//        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
////                Toast.makeText(MainActivity.this,"onGroupClick:groupPosition:" + groupPosition,Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                TextView tv = (TextView) v.findViewById(R.id.tv);
                if (childPosition == 0) {
                    //全选
                    if (children[groupPosition][childPosition].mark) {
                        //全选勾选→未勾选
                        switchAllSelect(groupPosition, false);
                    } else {
                        //全选未勾选→勾选
                        switchAllSelect(groupPosition, true);
                    }

                }

                //改变点击的View的勾选状态(显示状态+标记状态)
                if (children[groupPosition][childPosition].mark) {
                    tv.setTextColor(getResources().getColor(R.color.black));
                    children[groupPosition][childPosition].mark = false;

                    closeAllSelect(groupPosition);

                } else {
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    children[groupPosition][childPosition].mark = true;

                    checkAllSelectAndOpen(groupPosition);
                }


                return false;
            }
        });

    }

    /**
     * 检查是否开启全选
     *
     * @param groupPosition 选中的当前组
     */
    public void checkAllSelectAndOpen(int groupPosition){
        int markTrueCount = 0;
        //遍历检查点击后是否相当于全选
        for (int i = 1;i<children[groupPosition].length;i++){
            if (children[groupPosition][i].mark){
                //统计该组中选中的个数
                markTrueCount++;
            }
        }
        if(markTrueCount == children[groupPosition].length-1){
            //该组中已经全选，选中全选
            openAllSelect(groupPosition);
        }
    }

    /**
     * 开启全选选择
     *
     * @param groupPosition 选中的当前组
     */
    public void openAllSelect(int groupPosition) {
        //取消“全选”的标记状态
        children[groupPosition][0].mark = true;
        //取消“全选”的显示状态
        baseExpandableListAdapter.notifyDataSetChanged();
    }

    /**
     * 关闭全选选择
     *
     * @param groupPosition 选中的当前组
     */
    public void closeAllSelect(int groupPosition) {
        //取消“全选”的标记状态
        children[groupPosition][0].mark = false;
        baseExpandableListAdapter.notifyDataSetChanged();
    }

    /**
     * 全选切换
     *
     * @param groupPosition 选中的当前组
     * @param mark          使该组标记值标记为mark
     */
    public void switchAllSelect(int groupPosition, boolean mark) {
        for (int i = 1; i < children[groupPosition].length; i++) {
            //改变全选指定的条目的标记值mark（不包含“全选”View）
            children[groupPosition][i].mark = mark;
        }
        baseExpandableListAdapter.notifyDataSetChanged();
    }

    class Bean {
        String name;
        boolean mark;

        Bean(String name, boolean mark) {
            this.name = name;
            this.mark = mark;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
