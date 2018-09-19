package graduationwork.buskingtown;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;

import java.util.ArrayList;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_realtime_busking_map, container, false);

        MapContainer = (RelativeLayout) v.findViewById(R.id.map_container);

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

    }

    @Override

    public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {

        if (errorInfo == null) { // success

            startMyLocation();//현재위치로 이동

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

                Toast.makeText(getActivity(), errInfo.toString(), Toast.LENGTH_LONG).show();

                return;

            }else{

                Toast.makeText(getActivity(), address, Toast.LENGTH_LONG).show();

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



            Toast.makeText(getActivity(),

                    "Your current location is temporarily unavailable.",

                    Toast.LENGTH_LONG).show();

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
}