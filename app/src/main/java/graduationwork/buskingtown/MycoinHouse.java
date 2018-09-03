package graduationwork.buskingtown;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MycoinHouse extends AppCompatActivity implements View.OnClickListener {

    Button oneMonth, threeMonth, sixMonth, oneYearz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycoin_house);

        //버튼 참조변수
        oneMonth = (Button) findViewById(R.id.oneMonth);
        threeMonth = (Button) findViewById(R.id.threeMonth);
        sixMonth = (Button) findViewById(R.id.sixMonth);
        oneYearz = (Button) findViewById(R.id.oneYearz);

        //클릭메소드 연결
        oneMonth.setOnClickListener(this);
        threeMonth.setOnClickListener(this);
        sixMonth.setOnClickListener(this);
        oneYearz.setOnClickListener(this);

        //아래는 뒤로가기 버튼 클릭시 뒤로가는거임
        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { MycoinHouse.super.onBackPressed(); }
        });
    }

    public void previousActivity(View v){
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.oneMonth :
                //1개월 버튼색 활성화
                oneMonth.setBackground(getDrawable(R.drawable.able_btn));
                oneMonth.setTextColor(Color.parseColor("#ffffff"));
                //나머지 버튼색 비활성화
                threeMonth.setBackground(getDrawable(R.drawable.date_rounded));
                threeMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                sixMonth.setBackground(getDrawable(R.drawable.date_rounded));
                sixMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                oneYearz.setBackground(getDrawable(R.drawable.date_rounded));
                oneYearz.setTextColor(getResources().getColor(R.color.mainPurple));
                break;

            case R.id.threeMonth :
                //3개월 버튼색 활성화
                threeMonth.setBackground(getDrawable(R.drawable.able_btn));
                threeMonth.setTextColor(Color.parseColor("#ffffff"));
                //나머지 버튼색 비활성화
                oneMonth.setBackground(getDrawable(R.drawable.date_rounded));
                oneMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                sixMonth.setBackground(getDrawable(R.drawable.date_rounded));
                sixMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                oneYearz.setBackground(getDrawable(R.drawable.date_rounded));
                oneYearz.setTextColor(getResources().getColor(R.color.mainPurple));
                break;

            case R.id.sixMonth :
                //6개월 버튼색 활성화
                sixMonth.setBackground(getDrawable(R.drawable.able_btn));
                sixMonth.setTextColor(Color.parseColor("#ffffff"));
                //나머지 버튼색 비활성화
                oneMonth.setBackground(getDrawable(R.drawable.date_rounded));
                oneMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                threeMonth.setBackground(getDrawable(R.drawable.date_rounded));
                threeMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                oneYearz.setBackground(getDrawable(R.drawable.date_rounded));
                oneYearz.setTextColor(getResources().getColor(R.color.mainPurple));
                break;

            case R.id.oneYearz :
                //1년 버튼색 활성화
                oneYearz.setBackground(getDrawable(R.drawable.able_btn));
                oneYearz.setTextColor(Color.parseColor("#ffffff"));
                //나머지 버튼색 비활성화
                oneMonth.setBackground(getDrawable(R.drawable.date_rounded));
                oneMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                threeMonth.setBackground(getDrawable(R.drawable.date_rounded));
                threeMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                sixMonth.setBackground(getDrawable(R.drawable.date_rounded));
                sixMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                break;
        }
    }
}
