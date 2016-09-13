package com.qiyun.cyt.expandablelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //可展开列表组件的Adapter
        BaseExpandableListAdapter baseExpandableListAdapter =  new BaseExpandableListAdapter(){
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
                    "类型c"
            };

            //列表各个群组数据
            private String[][] children = new String[][]{
                    {"a1","a2","a3","a4","a5"},
                    {"b1","b2","b3","b4","b5"},
                    {"c1","c2","c3","c4","c5"}
            };


            @Override
            public int getGroupCount() {
                return groupType.length;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return children[groupPosition].length;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return groupType[groupPosition];
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
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
                textView.setPadding(50,0,0,0);
                textView.setTextSize(20);
                textView.setText(getGroup(groupPosition).toString());
                linearLayout.addView(textView);
                return linearLayout;
            }

            @Override
            public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                TextView textView = new TextView(MainActivity.this);
                textView.setId(R.id.tv);
                textView.setPadding(60,0,0,0);
                textView.setTextSize(20);
                textView.setText(getChild(groupPosition,childPosition).toString());
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
                Toast.makeText(MainActivity.this,"onGroupClick:groupPosition:" + groupPosition,Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                TextView tv = (TextView) v.findViewById(R.id.tv);
                tv.setTextColor(getResources().getColor(R.color.colorAccent));
                Toast.makeText(MainActivity.this,"onChildClick:groupPosition,childPosition" + groupPosition + "," + childPosition,Toast.LENGTH_SHORT).show();
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
}
