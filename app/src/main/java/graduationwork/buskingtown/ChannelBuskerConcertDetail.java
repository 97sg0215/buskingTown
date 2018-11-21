package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.LendLocationOption;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelBuskerConcertDetail extends AppCompatActivity {

    String p_name;
    String p_email;
    String p_phone;
    String p_info;
    String p_rule;
    String p_refund_rule;
    String p_address;
    String p_image;
    String p_start_time;
    String p_end_time;
    String user_token;
    String choice_option_price;
    String choice_option_name;
    Integer choice_option;
    double p_lon;
    double p_lat;

    int user_id, p_id;

    SharedPreferences prefUser;

    ArrayList<Integer> o_id = new ArrayList<Integer>();
    ArrayList<String> o_price = new ArrayList<String>();
    ArrayList<String> o_name = new ArrayList<String>();

    List<LendLocationOption> lendLocationOptions ;

    ImageView location_image;

    RadioGroup optionRadioGroup;
    RadioButton optionList;

    LinearLayout addRule, addRefund;

    ImageButton dropdown_sch, dropdown_sch_1;

    Button go_reservation;

    TextView loc_name,loc_main_name, loc_address, option, loc_info, loc_rule, loc_refund_rule, addRule_text,addRefundRule_text, sum_fee;

    RestApiService apiService;

    private static final String CLIENT_ID = "9a4pn7RAiPbnwnpUUS6k";

    private NMapContext mMapContext;

    RelativeLayout MapContainer;

    // 맵 컨트롤러

    NMapController mMapController = null;

    NMapView mMapView;

    // 오버레이의 리소스를 제공하기 위한 객체

    NMapViewerResourceProvider mMapViewerResourceProvider = null;

    // 오버레이 관리자

    private NMapMyLocationOverlay mMyLocationOverlay;

    private NMapLocationManager mMapLocationManager;

    private NMapCompassManager mMapCompassManager;

    private final String  TAG = "MainActivity";

    private ViewGroup mapLayout;

    private NMapResourceProvider nMapResourceProvider;
    private NMapOverlayManager mapOverlayManager;

    private NMapOverlayManager mOverlayManager;

    private static final String LOG_TAG = "NMapViewer";
    private static final boolean DEBUG = false;

    private static final int NMAP_ZOOMLEVEL_DEFAULT = 11;
    private static final int NMAP_VIEW_MODE_DEFAULT = NMapView.VIEW_MODE_VECTOR;
    private static final boolean NMAP_TRAFFIC_MODE_DEFAULT = false;
    private static final boolean NMAP_BICYCLE_MODE_DEFAULT = false;

    private static final String KEY_ZOOM_LEVEL = "NMapViewer.zoomLevel";
    private static final String KEY_CENTER_LONGITUDE = "NMapViewer.centerLongitudeE6";
    private static final String KEY_CENTER_LATITUDE = "NMapViewer.centerLatitudeE6";
    private static final String KEY_VIEW_MODE = "NMapViewer.viewMode";
    private static final String KEY_TRAFFIC_MODE = "NMapViewer.trafficMode";
    private static final String KEY_BICYCLE_MODE = "NMapViewer.bicycleMode";

    private SharedPreferences mPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_busker_concert_detail);

        prefUser = this.getSharedPreferences("User", Activity.MODE_PRIVATE);

        getLocalData();

        restApiBuilder();

        p_id = getIntent().getIntExtra("provide_id",0);
        p_name = getIntent().getStringExtra("provide_name");
        p_info = getIntent().getStringExtra("provide_description");
        p_phone = getIntent().getStringExtra("provider_phone");
        p_address = getIntent().getStringExtra("provide_address");
        p_refund_rule = getIntent().getStringExtra("provide_refund_rule");
        p_rule = getIntent().getStringExtra("provide_rule");
        p_image = getIntent().getStringExtra("provide_image");
        p_start_time = getIntent().getStringExtra("provide_start_time");
        p_end_time = getIntent().getStringExtra("provide_end_time");
        p_email = getIntent().getStringExtra("provide_email");

        p_lon = getIntent().getDoubleExtra("provide_lon",0);
        p_lat = getIntent().getDoubleExtra("provide_lat",0);

        location_image = (ImageView) findViewById(R.id.concertImage);
        optionRadioGroup = (RadioGroup) findViewById(R.id.optionRadioGroup);
        loc_main_name = (TextView) findViewById(R.id.busker_main_team_name);
        loc_name = (TextView) findViewById(R.id.concertName);
        loc_address = (TextView) findViewById(R.id.concertAddress);
        loc_info = (TextView) findViewById(R.id.description);
        addRule_text = (TextView) findViewById(R.id.addRule_text);
        dropdown_sch = (ImageButton) findViewById(R.id.dropdown_sch);
        addRefundRule_text = (TextView) findViewById(R.id.addRule_text_1);
        dropdown_sch_1 = (ImageButton) findViewById(R.id.dropdown_sch_1);
        addRule = (LinearLayout) findViewById(R.id.addRule);
        addRefund =(LinearLayout) findViewById(R.id.addRefund);
        sum_fee = (TextView) findViewById(R.id.sum_fee);

        loc_main_name.setText(p_name);
        loc_name.setText(p_name);

        loc_address.setText(p_address);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            location_image .setImageBitmap(getBitmapFromURL(p_image));
            location_image .setScaleType(ImageView.ScaleType.FIT_XY);
        }

        Call<List<LendLocationOption>> callOptionList = apiService.provideOptionList(user_token,p_id);
        callOptionList.enqueue(new Callback<List<LendLocationOption>>() {
            @Override
            public void onResponse(Call<List<LendLocationOption>> call, Response<List<LendLocationOption>> response) {
                if(response.isSuccessful()){
                    lendLocationOptions = response.body();
                    for(int i=0;i<lendLocationOptions.size();i++){
                        optionList = (RadioButton) getLayoutInflater().inflate(R.layout.reservation_practiceroom_detail_radio,optionRadioGroup,false);
                        optionList.setText(lendLocationOptions.get(i).getProvide_option_name() +" : "+lendLocationOptions.get(i).getProvide_price() + " 원/시");
                        o_price.add(lendLocationOptions.get(i).getProvide_price());
                        optionList.setId(i);
                        o_id.add(lendLocationOptions.get(i).getProvide_option_id());
                        o_name.add((lendLocationOptions.get(i).getProvide_option_name()));
                        if(optionList.getParent()!= null)
                            ((ViewGroup)optionList.getParent()).removeView(optionList);
                        optionRadioGroup.addView(optionList);
                    }
                }else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", String.valueOf(response.message()));
                    Log.e("리스폰스에러바디", String.valueOf(response.errorBody()));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<LendLocationOption>> call, Throwable t) {

            }
        });

        loc_info.setText(p_info);


        String[] phone_words = p_phone.split("-");

        String tel = "tel:"+phone_words[0]+phone_words[1]+phone_words[2];

        ImageButton phone = (ImageButton) findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });

        dropdown_sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRule.setVisibility(View.VISIBLE);
                addRule_text.setText(p_rule);
                dropdown_sch.setVisibility(View.GONE);
            }
        });

        dropdown_sch_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRefund.setVisibility(View.VISIBLE);
                addRefundRule_text.setText(p_refund_rule);
                dropdown_sch_1.setVisibility(View.GONE);
            }
        });

        optionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                sum_fee.setText(o_price.get(checkedId) + " 원 / 시");
                choice_option = o_id.get(checkedId);
                choice_option_name = o_name.get(checkedId);
                choice_option_price = o_price.get(checkedId);

            }
        });

        go_reservation = (Button) findViewById(R.id.go_reservation);
        go_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goReservaion = new Intent(getApplication(), ConcertDatailReservation.class);
                goReservaion.putExtra("provide_id",p_id);
                goReservaion.putExtra("provide_image",p_image);
                goReservaion.putExtra("provide_name",p_name);
                goReservaion.putExtra("provide_email",p_email);
                goReservaion.putExtra("provide_address",p_address);
                goReservaion.putExtra("provide_description",p_info);
                goReservaion.putExtra("provide_option_id", choice_option);
                goReservaion.putExtra("provide_option_price", choice_option_price);
                goReservaion.putExtra("provide_option_name",choice_option_name);
                goReservaion.putExtra("provide_start_time",p_start_time);
                goReservaion.putExtra("provide_end_time",p_end_time);
                Log.e("옵션아이디",String.valueOf(choice_option));
                startActivity(goReservaion);
                finish();
            }
        });

        MapContainer = (RelativeLayout) findViewById(R.id.map_container);
        mMapContext =  new NMapContext(this);
        mMapContext.onCreate();

        final Geocoder geocoder = new Geocoder(this);

        mMapView = (NMapView)findViewById(R.id.map_view);
        mMapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mMapContext.setupMapView(mMapView);

        // initialize map view
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();

        // 지도 객체로부터 컨트롤러 추출

        mMapController = mMapView.getMapController();

        // 확대/축소를 위한 줌 컨트롤러 표시 옵션 활성화
        mMapView.setBuiltInZoomControls(true, null);
        mMapContext.setMapDataProviderListener(onDataProviderListener);

        // 지도에 대한 상태 변경 이벤트 연결
        mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);

        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        // create overlay manager
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        testPOIdataOverlay();

    }

    /* MapView State Change Listener*/
    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {

            if (errorInfo == null) { // success
                // restore map view state such as map center position and zoom level.
                restoreInstanceState();

            } else { // fail
                Log.e(LOG_TAG, "onFailedToInitializeWithError: " + errorInfo.toString());

                //Toast.makeText(NMapViewer.this, errorInfo.toString(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
            }
        }

        @Override
        public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onMapCenterChange: center=" + center.toString());
            }
        }

        @Override
        public void onZoomLevelChange(NMapView mapView, int level) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onZoomLevelChange: level=" + level);
            }
        }

        @Override
        public void onMapCenterChangeFine(NMapView mapView) {

        }
    };

    /* Local Functions */
    private static boolean mIsMapEnlared = false;

    private void restoreInstanceState() {
        mPreferences = getPreferences(MODE_PRIVATE);

        NGeoPoint NMAP_LOCATION_DEFAULT = new NGeoPoint(p_lon, p_lat);

        int longitudeE6 = mPreferences.getInt(KEY_CENTER_LONGITUDE, NMAP_LOCATION_DEFAULT.getLongitudeE6());
        int latitudeE6 = mPreferences.getInt(KEY_CENTER_LATITUDE, NMAP_LOCATION_DEFAULT.getLatitudeE6());
        int level = mPreferences.getInt(KEY_ZOOM_LEVEL, NMAP_ZOOMLEVEL_DEFAULT);
        int viewMode = mPreferences.getInt(KEY_VIEW_MODE, NMAP_VIEW_MODE_DEFAULT);
        boolean trafficMode = mPreferences.getBoolean(KEY_TRAFFIC_MODE, NMAP_TRAFFIC_MODE_DEFAULT);
        boolean bicycleMode = mPreferences.getBoolean(KEY_BICYCLE_MODE, NMAP_BICYCLE_MODE_DEFAULT);

        mMapController.setMapViewMode(viewMode);
        mMapController.setMapViewTrafficMode(trafficMode);
        mMapController.setMapViewBicycleMode(bicycleMode);
        mMapController.setMapCenter(new NGeoPoint(longitudeE6, latitudeE6), level);

        if (mIsMapEnlared) {
            mMapView.setScalingFactor(2.0F);
        } else {
            mMapView.setScalingFactor(1.0F);
        }
    }

    public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {

        if (errorInfo == null) { // success

            //startMyLocation();//현재위치로 이동

            // mMapController.setMapCenter(new NGeoPoint(126.978371,

            // 37.5666091),

            // 11);

        } else { // fail

            android.util.Log.e("NMAP",

                    "onMapInitHandler: error=" + errorInfo.toString());
        }

    }

    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }



    private void stopMyLocation() {

        if (mMyLocationOverlay != null) {

            mMapLocationManager.disableMyLocation();



            if (mMapView.isAutoRotateEnabled()) {

                mMyLocationOverlay.setCompassHeadingVisible(false);



                mMapCompassManager.disableCompass();



                mMapView.setAutoRotateEnabled(false, false);



                MapContainer.requestLayout();

            }

        }

    }

    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {



        @Override

        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {


            String address = placeMark.doName+ " " + placeMark.siName+ " " + placeMark.dongName;
            if (errInfo != null) {

                Log.e("myLog", "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

                //Toast.makeText(getActivity(), errInfo.toString(), Toast.LENGTH_LONG).show();

                return;

            }else{

                //Toast.makeText(getActivity(), address, Toast.LENGTH_LONG).show();

            }
        }
    };

    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {



        @Override

        public boolean onLocationChanged(NMapLocationManager locationManager,

                                         NGeoPoint myLocation) {

            if (mMapController != null) {
                mMapController.animateTo(myLocation);
            }

            Log.d("myLog", "myLocation  lat " + myLocation.getLatitude());
            Log.d("myLog", "myLocation  lng " + myLocation.getLongitude());


            mMapContext.findPlacemarkAtLocation(myLocation.getLongitude(), myLocation.getLatitude());

            //위도경도를 주소로 변환

            return true;

        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {
        }


        @Override
        public void onLocationUnavailableArea(

                NMapLocationManager locationManager, NGeoPoint myLocation) {

            //Toast.makeText(getActivity(), "Your current location is unavailable area.", Toast.LENGTH_LONG).show();
            stopMyLocation();

        }

    };


    @Override
    public void onStart(){
        super.onStart();
        mMapContext.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }
    @Override
    public void onStop() {
        mMapContext.onStop();
        super.onStop();
    }
    //public void onDestroyView() {super.onDestroyView();}
    @Override
    public void onDestroy() {
        mMapContext.onDestroy();
        super.onDestroy();
        mMapView.setOnMapStateChangeListener((NMapView.OnMapStateChangeListener) null);
    }

    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay nMapOverlay, NMapOverlayItem nMapOverlayItem, Rect rect) {
        return null;
    }

    private void testPOIdataOverlay() {

        // Markers for POI item
        int markerId = NMapPOIflagType.PIN;

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(p_lon, p_lat, p_name, markerId, 0);
        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

        // set event listener to the overlay
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        // select an item
        poiDataOverlay.selectPOIitem(0, true);

        // show all POI data
        poiDataOverlay.showAllPOIdata(0);
    }

    /* POI data State Change Listener*/
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle());
            }
        }

        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                if (item != null) {
                    Log.i(LOG_TAG, "onFocusChanged: " + item.toString());
                } else {
                    Log.i(LOG_TAG, "onFocusChanged: ");
                }
            }
        }
    };


    public static Location findGeoPoint(Context mcontext, String address) {
        Location loc = new Location("");
        Geocoder coder = new Geocoder(mcontext);
        List<Address> addr = null;// 한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 설정

        try {
            addr = coder.getFromLocationName(address, 5);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 몇개 까지의 주소를 원하는지 지정 1~5개 정도가 적당
        if (addr != null) {
            for (int i = 0; i < addr.size(); i++) {
                Address lating = addr.get(i);
                double lat = lating.getLatitude(); // 위도가져오기
                double lon = lating.getLongitude(); // 경도가져오기
                loc.setLatitude(lat);
                loc.setLongitude(lon);
            }
        }
        return loc;
    }

    public Bitmap getBitmapFromURL(String src) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(src);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            if(connection!=null)connection.disconnect();
        }
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getLocalData(){
        user_token = prefUser.getString("auth_token",null);
        user_id = prefUser.getInt("user_id",0);
    }

}
