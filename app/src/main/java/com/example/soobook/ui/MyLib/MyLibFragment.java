package com.example.soobook.ui.MyLib;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.fragment.app.Fragment;

import com.example.soobook.My_lib_add;
import com.example.soobook.R;
import com.example.soobook.ui.FriLib.FriLibFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class MyLibFragment extends Fragment {

    private static String TAG = "phptest_Event";
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_owner = "owner";
    private static final String TAG_title = "title";

    ArrayList<HashMap<String, String>> nArrayList;
    ListView nlistView;
    String nJsonString;
    ImageButton search_btn, add_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_lib, container, false);

        add_btn = rootView.findViewById(R.id.add_btn);
        add_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), My_lib_add.class);
            startActivity(intent);

        });
        nlistView = rootView.findViewById(R.id.ListviewId);
        nArrayList = new ArrayList<>();



        MyLibFragment.GetData task = new MyLibFragment.GetData();
        task.execute("https://ar8350.cafe24.com/ehfvlsqhdks20/testjson.php");
        return rootView;
    }
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "ㄱㄷㄱㄷ", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.e(this.getClass().getName(), "response  - " + result);
            if (result == null) {
            } else {
                nJsonString = result;
                showResult();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            try {
                URL url = new URL(serverURL);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

                httpsURLConnection.setReadTimeout(5000);
                httpsURLConnection.setConnectTimeout(5000);
                httpsURLConnection.connect();

                int responseStatusCode = httpsURLConnection.getResponseCode();
                Log.e(this.getClass().getName(), "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpsURLConnection.HTTP_OK) {
                    inputStream = httpsURLConnection.getInputStream();
                } else {
                    inputStream = httpsURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.e(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }
    private void showResult () {
        try {
            JSONObject jsonObject = new JSONObject(nJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);
                String owner = item.getString(TAG_owner);
                String title = item.getString(TAG_title);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_owner, owner);
                hashMap.put(TAG_title, title);
                nArrayList.add(hashMap);
            }
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), nArrayList, R.layout.activity_soobook_listview_shape,
                    new String[]{TAG_title, TAG_owner},
                    new int[]{R.id.titletext, R.id.ownertext}

            );
            //Collections.reverse(nArrayList);
            nlistView.setAdapter(adapter);
        } catch (JSONException e) {
            Log.e(TAG, "showResult : ", e);
        }
        //리스트뷰 클릭 이벤트 처리
        nlistView.setOnItemClickListener((parent, view, position, id) -> {

            HashMap<String, String> this_item = (HashMap) parent.getAdapter().getItem(position);
            String owner = this_item.get(TAG_owner);
            String title = this_item.get(TAG_title);

            //Event에서 EventEdit으로 값 전달
            Intent intent = new Intent(getActivity(), FriLibFragment.class);
            intent.putExtra("owner", owner);
            intent.putExtra("title", title);
            startActivity(intent);
        });
    }
}

