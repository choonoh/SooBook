package com.example.soobook.ui.FindLib;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.soobook.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class FindLibFragment extends Fragment implements OnMapReadyCallback {

    EditText et_search_text;
    ImageButton search_btn;

    boolean marker_exist = false;
    int only_zero = 0;
    int cur_or_se, marker_len, final_marker;
    double latitude, longitude;
    String search_location, current_location_si, current_location_dong;

    ArrayList<Library> list;
    Marker[] markers;
    Library library;
    String[][] library_info;
    // name, close_day, week(open, close), holiday(open, close), bookNum, bookPosNum, borPosDay, address, number, homePageUrl, dataTime

    private FusedLocationSource mLocationSource;
    private NaverMap mnaverMap;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_findlib, container, false);


        NaverMapSdk.getInstance(rootView.getContext()).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("vjckuvmnki"));

        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        mLocationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);

        et_search_text = rootView.findViewById(R.id.search_text);
        search_btn = rootView.findViewById(R.id.search_btn);

        search_btn.setOnClickListener(v -> {
            search_location = et_search_text.getText().toString();
            cur_or_se = 1;
            Log.e(this.getClass().getName(), search_location);
            if(search_location.equals("")) {
                cur_or_se = 0;
                GeoCoding geoCoding = new GeoCoding();
                geoCoding.execute();
                if(marker_exist) {
                    marker_exist = false;
                    for(int i = 0; i < markers.length; i++) {
                        markers[i].setMap(null);
                        markers[i] = null;
                    }
                }
                marker_len = 0;
                list = new ArrayList<>();
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            } else {
                if(marker_exist) {
                    marker_exist = false;
                    for(int i = 0; i < markers.length; i++) {
                        markers[i].setMap(null);
                        markers[i] = null;
                    }
                }
                marker_len = 0;
                list = new ArrayList<>();
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });
        return rootView;
    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        mnaverMap = naverMap;
        mnaverMap.setLocationSource(mLocationSource);
        mnaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        mnaverMap.addOnLocationChangeListener(location -> {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if(only_zero == 0) {
                only_zero++;
                cur_or_se = 0;
                GeoCoding geoCoding = new GeoCoding();
                geoCoding.execute();
                if(marker_exist) {
                    marker_exist = false;
                    for(int i = 0; i < markers.length; i++) {
                        markers[i].setMap(null);
                        markers[i] = null;
                    }
                }
                marker_len = 0;
                list = new ArrayList<>();
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });
        ActivityCompat.requestPermissions(this.getActivity(), PERMISSIONS, PERMISSION_REQUEST_CODE);
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mnaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }
    private class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.e(this.getClass().getName(), String.valueOf(latitude));
            Log.e(this.getClass().getName(), String.valueOf(longitude));

            Log.e(this.getClass().getName(), "0 or 1 : " + cur_or_se);
            StringBuilder urlBuilder = new StringBuilder("http://api.data.go.kr/openapi/tn_pubr_public_lbrry_api");
            try {
                urlBuilder.append("?").append(URLEncoder.encode("ServiceKey", "UTF-8")).append("=bCJdW6RD4qr5ygWtvTicA5sgPMvnvcpfzA3vXZj2k8HZ66cnR7OpoV24WdJgJMv7e3x2gu2swtG%2Bv84490FuAw%3D%3D");
                urlBuilder.append("&").append(URLEncoder.encode("pageNo", "UTF-8")).append("=").append(URLEncoder.encode("0", "UTF-8")); /*페이지 번호*/
                urlBuilder.append("&").append(URLEncoder.encode("numOfRows", "UTF-8")).append("=").append(URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
                urlBuilder.append("&").append(URLEncoder.encode("type", "UTF-8")).append("=").append(URLEncoder.encode("xml", "UTF-8")); /*XML/JSON 여부*/
                switch (cur_or_se) {
                    case 0:
//                    Log.e(this.getClass().getName(), current_location_si + " " + current_location_dong);
                        // TODO: 2021-01-25 "링크 이것저것 추가하기(0이면 geocoding에서 값 받아서, 1이면 해당 장소 받아서)"
                        break;
                    case 1:
                        Log.e(this.getClass().getName(), search_location);

                        break;
                }
                boolean name = false;
                boolean closeDay = false;
                boolean weekOpenTime = false;
                boolean weekCloseTime = false;

                boolean holidayOpenTime = false;
                boolean holidayCloseTime = false;
                boolean bookNum = false;
                boolean borPosNum = false;

                boolean borPosDay = false;
                boolean address = false;
                boolean number = false;
                boolean homePageUrl = false;
                boolean dataTime = false;

                URL url = new URL(urlBuilder.toString());
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                conn.setRequestProperty("Content-type", "application/json");
//                BufferedReader rd;
//                if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                } else {
//                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//                }
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    sb.append(line);
//                }
//                rd.close();
//                conn.disconnect();
//                System.out.println(sb.toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item") && library != null) {
                                if (marker_len < 20) {
                                    marker_len++;
                                    list.add(library);
                                }
                            }
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("item")) {
                                library = new Library();
                            }
//                            if (parser.getName().equals("dutyAddr")) address = true;
//                            if (parser.getName().equals("dutyDivNam")) divNam = true;
                            break;
                        case XmlPullParser.TEXT:
//                            if (address) {
//                                hospital.setAddress(parser.getText());
//                                address = false;
//                            } else if (divNam) {
//                                hospital.setDivNam(parser.getText());
//                                divNam = false;
//                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) { e.printStackTrace(); }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            Log.e(this.getClass().getName(), "len" + marker_len);
            if(marker_len != 0) {
                super.onPostExecute(s);
                Log.e(this.getClass().getName(), "onPostExecute len : " + marker_len);
                Log.e(this.getClass().getName(), "onPostExecute list_size() : " + list.size());

                // TODO: 2021-01-25 받을 정보 갯수 정해지면 여기 바꿔주기
                library_info = new String[13][marker_len];
                markers = new Marker[marker_len];
                double latitude = 0;
                double longitude = 0;
                marker_exist = true;
                for (int i = 0; i < marker_len; i++) {
// name, close_day, week(open, close), holiday(open, close), bookNum, bookPosNum, borPosDay, address, number, homePageUrl, dataTime
//                    library_info[0][i] = list.get(i).getAddress();
//                    library_info[1][i] = list.get(i).getName();
//                    library_info[2][i] = list.get(i).getTel();
//
//                    library_info[3][i] = list.get(i).getStartTime1();
//                    library_info[4][i] = list.get(i).getStartTime2();
//                    library_info[5][i] = list.get(i).getStartTime3();
//                    library_info[6][i] = list.get(i).getStartTime4();
//                    library_info[7][i] = list.get(i).getStartTime5();
//                    isEmpty(list.get(i).getStartTime6(), 8, i);
//                    isEmpty(list.get(i).getStartTime7(), 9, i);
//                    isEmpty(list.get(i).getStartTime8(), 10, i);
//
//                    library_info[11][i] = list.get(i).getEndTime1();
//                    library_info[12][i] = list.get(i).getEndTime2();
//                    library_info[13][i] = list.get(i).getEndTime3();
//
//                    latitude = Double.parseDouble(list.get(i).getLatitude());
//                    longitude = Double.parseDouble(list.get(i).getLongitude());
                    Marker marker = new Marker();
                    markers[i] = marker;
                    setMarker(markers[i], latitude, longitude, 0);
                    final int finalI = i;
                    marker.setOnClickListener(new Overlay.OnClickListener() {
                        @Override
                        public boolean onClick(@NonNull Overlay overlay) {
//                            hos_name.setText(hos_info[1][finalI]);
//                            hos_tel.setText(hos_info[2][finalI]);
//                            hos_layout.setVisibility(View.VISIBLE);
                            final_marker = finalI;
                            return true;
                        }
                    });
                }
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(latitude, longitude));
                mnaverMap.moveCamera(cameraUpdate);
            } else {
                AlertDialog dialog;
//                AlertDialog.Builder builder = new AlertDialog.Builder(Clinic_find_map.this);
//                builder.setMessage((getResources().getString(R.string.cli_find_home_no_cli)));
//                builder.setPositiveButton(getResources().getString(R.string.cli_my_chart_saved_ok_btn), null);
//                dialog = builder.create();
//                dialog.show();
            }
        }
        private  void isEmpty(String str, int num, int i) {
            if(str == null)
                library_info[num][i] = "";
            else
                library_info[num][i] = str;
        }
        private void setMarker(Marker marker,  double lat, double lng, int zIndex)
        {
            marker.setWidth(100);
            marker.setHeight(100);
            marker.setIconPerspectiveEnabled(true);     //원근감 표시
            marker.setIcon(OverlayImage.fromResource(R.drawable.marker));   //아이콘 지정
            marker.setAlpha(0.8f);  //마커의 투명도
            marker.setPosition(new LatLng(lat, lng));   //마커 위치
            marker.setZIndex(zIndex);   //마커 우선순위
            marker.setMap(mnaverMap);   //마커 표시
        }
    }
    private class GeoCoding extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.e(this.getClass().getName(), String.valueOf(latitude));
            Log.e(this.getClass().getName(), String.valueOf(longitude));
            if (latitude == 0 && longitude == 0) {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(Clinic_find_map.this);
//                        builder.setMessage((getResources().getString(R.string.fail_loc)));
//                        builder.setPositiveButton(getResources().getString(R.string.cha_pw_ok_btn), null);
//                        dialog = builder.create();
//                        dialog.show();
                    }
                }, 0);

            } else {
                String requestUrl = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr" +
//                        "&coords=" + lng + "," + lat +
//                        "&sourcecrs=epsg:4326&orders=admcode,legalcode,addr,roadaddr&output=xml" +
                        "&X-NCP-APIGW-API-KEY-ID=98bjke0ogk&X-NCP-APIGW-API-KEY=ugGOxqyWRiEw5H53vcvI7wE8mBFO1uWVx09ZyKYp";
                Log.e(this.getClass().getName(), requestUrl);
                try {
                    boolean area1 = false;
                    boolean area3 = false;
                    boolean name = false;

                    URL url = new URL(requestUrl);
                    InputStream is = url.openStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(new InputStreamReader(is, "UTF-8"));

                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.END_DOCUMENT:
                                break;
                            case XmlPullParser.END_TAG:
                                break;
                            case XmlPullParser.START_TAG:
                                if (parser.getName().equals("area1")) area1 = true;
                                if (parser.getName().equals("area3")) area3 = true;
                                if (parser.getName().equals("name")) name = true;
                                break;
                            case XmlPullParser.TEXT:
                                if(area1 && name) {
                                    current_location_si = parser.getText();
                                    area1 = false;
                                    name = false;
                                }
                                if (area3 && name) {
                                    current_location_dong = parser.getText();
                                    area3 = false;
                                    name = false;
                                }
                                break;
                        }
                        eventType = parser.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}

