package com.qiyun.cyt.expandablelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 可展开的列表组件
 */
public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //可展开列表组件的Adapter
        final BaseExpandableListAdapter baseExpandableListAdapter = new BaseExpandableListAdapter() {


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
                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                ImageView logo = new ImageView(MainActivity.this);
//                logo.setImageResource(logos[groupPosition]);
//                linearLayout.addView(logo);
                TextView textView = new TextView(MainActivity.this);
                textView.setPadding(50, 0, 0, 0);
                textView.setTextSize(20);
                textView.setText(getGroup(groupPosition).toString());
                linearLayout.addView(textView);
                return linearLayout;
            }

            @Override
            public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                TextView textView = new TextView(MainActivity.this);
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
        ExpandableListView elv = (ExpandableListView) findViewById(R.id.elv);
        elv.setAdapter(baseExpandableListAdapter);

        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                Toast.makeText(MainActivity.this,"onGroupClick:groupPosition:" + groupPosition,Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                TextView tv = (TextView) v.findViewById(R.id.tv);
                if (childPosition == 0) {
                    //全选
                    if (children[groupPosition][childPosition].mark) {
                        //全选勾选→未勾选
                        switchAllSelect(parent, v, groupPosition, false, R.color.black);
                    } else {
                        //全选未勾选→勾选
                        switchAllSelect(parent, v, groupPosition, true, R.color.colorAccent);
                    }

                }

                //改变点击的View的勾选状态(显示状态+标记状态)
                if (children[groupPosition][childPosition].mark) {
                    tv.setTextColor(getResources().getColor(R.color.black));
                    children[groupPosition][childPosition].mark = false;

                    closeAllSelect(parent, v, groupPosition, childPosition);

                } else {
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    children[groupPosition][childPosition].mark = true;

                    checkAllSelectAndOpen(parent, v, groupPosition, childPosition);
                }


//                Toast.makeText(MainActivity.this,"onChildClick:groupPosition,childPosition" + groupPosition + "," + childPosition,Toast.LENGTH_SHORT).show();
                return false;
            }
        });

//        elv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this,"onItemClick:" + position,Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    /**
     * 检查是否开启全选
     *
     * @param parent        ExpandableListView
     * @param v             点击的View
     * @param groupPosition 选中的当前组
     * @param childPosition 点击的View在当前组的位置
     */
    public void checkAllSelectAndOpen(ExpandableListView parent, View v, int groupPosition, int childPosition){
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
            openAllSelect(parent, v, groupPosition, childPosition);
        }
    }

    /**
     * 开启全选选择
     *
     * @param parent        ExpandableListView
     * @param v             点击的View
     * @param groupPosition 选中的当前组
     * @param childPosition 点击的View在当前组的位置
     */
    public void openAllSelect(ExpandableListView parent, View v, int groupPosition, int childPosition) {
        //取消“全选”的标记状态
        children[groupPosition][0].mark = true;
        //取消“全选”的显示状态
        //活得父类的子类个数（就是显示出来的总得条目行数）
        int frontChild = 0;
        for (int i = parent.getChildCount() - 1; i >= frontChild; i--) {
            if(i<0){//考虑到上部分可能有未显示的item，保证position>=0
                return;
            }
            View child = parent.getChildAt(i);
            //是否从头遍历到点击的View
            if (child.equals(v)) {
                //调整循环次数，使循环不必遍历完全，只需要定位到“全选”即可
                frontChild = i - childPosition;
            }

            if (i == frontChild) {
                //找到“全选”View后才执行，把“全选”改变显示状态
                View child_tv = child.findViewById(R.id.tv);
                if (child_tv instanceof TextView) {
                    TextView childTextView = (TextView) child_tv;
                    childTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        }
    }

    /**
     * 关闭全选选择
     *
     * @param parent        ExpandableListView
     * @param v             点击的View
     * @param groupPosition 选中的当前组
     * @param childPosition 点击的View在当前组的位置
     */
    public void closeAllSelect(ExpandableListView parent, View v, int groupPosition, int childPosition) {
        //取消“全选”的标记状态
        children[groupPosition][0].mark = false;
        //取消“全选”的显示状态
        //活得父类的子类个数（就是显示出来的总得条目行数）
        int frontChild = 0;
        for (int i = parent.getChildCount() - 1; i >= frontChild; i--) {

            if(i<0){//考虑到上部分可能有未显示的item，保证position>=0
                return;
            }
            Log.e("frontChild",frontChild + ",当前：" + i);
            View child = parent.getChildAt(i);
            //是否从尾遍历到点击的View
            if (child.equals(v)) {
                //调整循环次数，使循环不必遍历完全，只需要定位到“全选”即可
                frontChild = i - childPosition;
            }

            if (i == frontChild) {
                //找到“全选”View后才执行，把“全选”改变显示状态
                View child_tv = child.findViewById(R.id.tv);
                if (child_tv instanceof TextView) {
                    TextView childTextView = (TextView) child_tv;
                    childTextView.setTextColor(getResources().getColor(R.color.black));
                }
            }

        }
    }

    /**
     * 全选切换
     *
     * @param parent        ExpandableListView
     * @param v             点击的View
     * @param groupPosition 选中的当前组
     * @param mark          使该组标记值标记为mark
     * @param TextColor     使该组显示状态改变为TextColor
     */
    public void switchAllSelect(ExpandableListView parent, View v, int groupPosition, boolean mark, int TextColor) {
        for (int i = 1; i < children[groupPosition].length; i++) {
            //改变全选指定的条目的标记值mark（不包含“全选”View）
            children[groupPosition][i].mark = mark;
        }
        //活得父类的子类个数（就是显示出来的总得条目行数）
        int childCount = parent.getChildCount();
        boolean isFindSelectAll = false;
        for (int i = 0; i < childCount; i++) {
            Log.e("childCount",childCount + ",当前：" + i);
            View child = parent.getChildAt(i);
            //是否从头遍历到点击的“全选”View
            if (child.equals(v)) {
                //调整循环次数，使循环不必遍历完全，只需要把该“全选”所指定的条目遍历到即可(考虑到可能有未显示出来的条目，这里取二者最小值)
                childCount = i + children[groupPosition].length<childCount?i + children[groupPosition].length:childCount;
                isFindSelectAll = true;
            }

            if (isFindSelectAll) {
                //找到“全选”View后才执行，把“全选”后面属于它指定的条目改变显示状态
                View child_tv = child.findViewById(R.id.tv);
                if (child_tv instanceof TextView) {
                    TextView childTextView = (TextView) child_tv;
                    childTextView.setTextColor(getResources().getColor(TextColor));
                }
            }
        }
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
