package graduationwork.buskingtown;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.RoadConcert;
import graduationwork.buskingtown.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class RealtimeBuskingMap extends Fragment implements NMapView.OnMapStateChangeListener, NMapOverlayManager.OnCalloutOverlayListener {

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


    SharedPreferences prefUser, prefBusker;

    RestApiService apiService;

    String user_token,busker_name;
    int busker_id, user_id;

    Intent buskerChannel1, buskerChannel2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_realtime_busking_map, container, false);
        MapContainer = (RelativeLayout) v.findViewById(R.id.map_container);

        prefBusker = this.getActivity().getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        prefUser = this.getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);

        buskerChannel1 = new Intent(getActivity(), ChannelUser.class);
        buskerChannel2 = new Intent(getActivity(), ChannelBusker.class);

        restApiBuilder();

        getLocalData();

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext =  new NMapContext(super.getActivity());
        mMapContext.onCreate();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Geocoder geocoder = new Geocoder(getContext());

        mMapView = (NMapView)getView().findViewById(R.id.map_view);
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
        mMapView.setOnMapStateChangeListener(this);

        mMapViewerResourceProvider = new NMapViewerResourceProvider(getContext());
        // create overlay manager
        mOverlayManager = new NMapOverlayManager(getContext(), mMapView, mMapViewerResourceProvider);

        testPOIdataOverlay();
    }

    @Override

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

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    private void startMyLocation() {

        mMapLocationManager = new NMapLocationManager(getContext());
        mMapLocationManager

                .setOnLocationChangeListener(onMyLocationChangeListener);


//퍼미션 권한 안주면 여기서 멈춤
        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            Toast.makeText(getActivity(), "위치에 접근하기위해서는 위치 접근 권한이 필요해요", Toast.LENGTH_LONG).show();
        }
        else {
            boolean isMyLocationEnabled = mMapLocationManager

                    .enableMyLocation(true);

            if (!isMyLocationEnabled) {

                Toast.makeText(getActivity(), "Please enable a My Location source in system settings", Toast.LENGTH_LONG).show();



                Intent goToSettings = new Intent(

                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                startActivity(goToSettings);

                //       finish();

            }else{

            }
        }




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
            // stop location updating

            // Runnable runnable = new Runnable() {

            // public void run() {

            // stopMyLocation();

            // }

            // };

            // runnable.run();

        }


        @Override
        public void onLocationUnavailableArea(

                NMapLocationManager locationManager, NGeoPoint myLocation) {

            Toast.makeText(getActivity(),

                    "Your current location is unavailable area.",

                    Toast.LENGTH_LONG).show();
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        mMapContext.onDestroy();
        super.onDestroy();
        mMapView.setOnMapStateChangeListener((NMapView.OnMapStateChangeListener) null);
    }

    @Override
    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay nMapOverlay, NMapOverlayItem nMapOverlayItem, Rect rect) {
        return null;
    }

    private void testPOIdataOverlay() {

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat stf = new SimpleDateFormat("HH:MM");
        String getDate = sdf.format(date);
        String getTime = stf.format(date);

        Call<List<RoadConcert>> listCall = apiService.getLiveBuking(user_token,getDate,getTime);
        listCall.enqueue(new Callback<List<RoadConcert>>() {
            @Override
            public void onResponse(Call<List<RoadConcert>> call, Response<List<RoadConcert>> response) {
                if(response.isSuccessful()){
                    List<RoadConcert> roadConcerts = response.body();
                    if(roadConcerts.size()!=0){
                        Log.e("메세지", String.valueOf(roadConcerts.size()));
                        for (int i=0;i<roadConcerts.size();i++){
                            // Markers for POI item
                            int markerId = NMapPOIflagType.PIN;

                            Call<Busker> buskerCall = apiService.buskerDetail(user_token,roadConcerts.get(i).getBusker());
                            int finalI = i;
                            buskerCall.enqueue(new Callback<Busker>() {
                                @Override
                                public void onResponse(Call<Busker> call, Response<Busker> response) {
                                    if(response.isSuccessful()){
                                        busker_name = String.valueOf(response.body().getTeam_name());
                                        int final_busker_id = response.body().getBusker_id();

                                        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
                                        poiData.beginPOIdata(2);

                                        // set POI data
                                        NMapPOIitem item = poiData.addPOIitem(roadConcerts.get(finalI).getRoad_lon(), roadConcerts.get(finalI).getRoad_lat(), busker_name, markerId, 0);
                                        item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);

                                        // create POI data overlay
                                        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

                                        // set event listener to the overlay
                                        my_channel_check(busker_name,final_busker_id,poiDataOverlay);

                                        // select an item
                                        poiDataOverlay.selectPOIitem(0, true);

                                        // show all POI data
                                        poiDataOverlay.showAllPOIdata(0);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Busker> call, Throwable t) {

                                }
                            });


                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RoadConcert>> call, Throwable t) {

            }
        });

    }

    /* POI data State Change Listener*/
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener1 = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle());
            }
            startActivity(buskerChannel1);

            // [[TEMP]] handle a click event of the callout
            //Toast.makeText(v.RealtimeBuskingMap.this, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
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

    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener2 = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle());
            }

            startActivity(buskerChannel2);

            // [[TEMP]] handle a click event of the callout
            //Toast.makeText(v.RealtimeBuskingMap.this, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
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



    private void testGeocoder(){

    }

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

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getLocalData(){
        user_token = prefUser.getString("auth_token",null);
        user_id = prefUser.getInt("user_id",0);
        busker_id = prefBusker.getInt("busker_id",0);
    }

    public void my_channel_check(String team_name,int final_busker_id,NMapPOIdataOverlay poiDataOverlay){
        retrofit2.Call<User> userDetail = apiService.getUserDetail(user_token,user_id);
        userDetail.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    if(user.getBusker()!=null){ //유저가 버스커일 경우
                        //내 채널이 아닐 경우, 팀네임이 같지않은 경우
                        if(!user.getBusker().getTeam_name().equals(team_name)){
                            //개인 아이디를 다음 액티비티에서 받아 세팅
                            buskerChannel1.putExtra("busker_id",final_busker_id);
                            buskerChannel1.putExtra("team_name",team_name);
                            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener1);
                        } //내 채널일 경우, 팀네임이 같을 경우
                        else {
                            //개인 아이디를 다음 액티비티에서 받아 세팅
                            buskerChannel2.putExtra("busker_id",final_busker_id);
                            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener2);

                        }
                    } //내 채널이 아닐 경우, 유저가 버스커가 아닐 경우 정상 진행
                    else {
                        //개인 아이디를 다음 액티비티에서 받아 세팅
                        buskerChannel1.putExtra("busker_id",final_busker_id);
                        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener1);
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }
}