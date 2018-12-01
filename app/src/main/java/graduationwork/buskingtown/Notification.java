package graduationwork.buskingtown;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

public class Notification extends Fragment {

    //화면에 세팅되는 리스트뷰
    SwipeMenuListView listView;
    //리스트 연결 어댑터
    NotificationAdapter mAdapter;
    //리스트에 들어갈 정보들
    ArrayList<NotificationItem> listItems;

    public Notification() {
        // Required empty public constructor
    }

    private static final String TAG = "Notification";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_notification, container, false);

        //스와이프메뉴
        listItems = new ArrayList<NotificationItem>();
        listView = (SwipeMenuListView) v.findViewById(R.id.listView);
        mAdapter = new NotificationAdapter((listItems));
        listItems.add(new NotificationItem("mitch"));

        listView.setAdapter(mAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.arrow_r);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Log.d(TAG, "onMenuItemClick : clicked item" + index);
                        break;
                    case 1:
                        Log.d(TAG, "onMenuItemClick : clicked item" + index);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        return v;
    }

    //리스트 아이템들(화면에 띄워지는 정보들을 세팅)
    public class NotificationItem {
        private String team_name;
        //이런식으로 들어가는 정보 세팅하셈

        public NotificationItem(String team_name) {
            this.team_name = team_name;
        }
    }

    //Adapter
    public class NotificationAdapter extends BaseAdapter {

        private ArrayList<NotificationItem> list;

        public NotificationAdapter(ArrayList<NotificationItem> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public NotificationItem getItem(int position) {
            //현재 position에 따른 list값 반환
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //부모뷰에 리스트로 세팅될 하나의 조각을 세팅하는 것
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View result = inflater.inflate(R.layout.simple_list_item_1, parent, false);

            return result;
        }
    }

}
