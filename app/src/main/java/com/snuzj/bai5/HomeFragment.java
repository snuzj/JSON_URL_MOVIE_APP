package com.snuzj.bai5;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<MovieModelClass> movieList;
    private RecyclerView recyclerView;
    private Adaptery adaptery;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        movieList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);

        GetData getdata = new GetData();
        getdata.execute();

        return view;
    }

    public class GetData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    // Đặt URL của bạn ở đây
                    String JSON_URL = "https://api.mockfly.dev/mocks/8076091f-50cd-4b3b-8009-987d4b408404/movies";
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inputStream);

                    int data = isr.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isr.read();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("movies");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    MovieModelClass model = new MovieModelClass();
                    model.setImg(jsonObject1.getString("image"));
                    model.setName(jsonObject1.getString("name"));
                    model.setPrice(jsonObject1.getString("price"));

                    movieList.add(model);
                }

                // Hiển thị dữ liệu lên RecyclerView trong HomeFragment
                adaptery = new Adaptery(requireContext(), movieList);
                recyclerView.setAdapter(adaptery);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
