package kirdmt.com.browserandgiphy;


import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GiphyActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    final static String API_KEY = "VT65vJSrCgXMchtdet66iVv7vfSyms8V";
    final static String DEBUG_TAG = "DebugTAG";

    String requestedPhrase;
    private Giphy giphy;


    ArrayList<String> ImgUrl = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager Manager;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giphy);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        requestedPhrase = query.replace(" ", "+");

        getGiphy();
        return false;

    }

    @Override
    public boolean onQueryTextChange(String s) {

        return false;
    }


    private void getGiphy() {

        String giphyUrl = "http://api.giphy.com/v1/gifs/search?q=" + requestedPhrase + "&api_key=" + API_KEY + "&limit=10";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(giphyUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                Log.i(DEBUG_TAG, "Request Failure");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
                try {
                    String jsonData = response.body().string();
                    if (response.isSuccessful()) {
                        giphy = parseData(jsonData);
                        //Log.v(DEBUG_TAG, "Giphy Gif Data from Response: " + giphy);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                display();
                            }
                        });
                    } else {
                        Log.i(DEBUG_TAG, "Response Unsuccessful");
                    }
                } catch (IOException e) {
                    Log.e(DEBUG_TAG, "Exception Caught: ", e);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private Giphy parseData(String jsonData) throws JSONException {

        Giphy giphy = new Giphy();
        giphy.setGiphyData(getGif(jsonData));

        return giphy;
    }

    private GiphyData[] getGif(String jsonData) throws JSONException {
        JSONObject giphy = new JSONObject(jsonData);
        JSONArray data = giphy.getJSONArray("data");

        final int NUM_OF_GIFS = data.length();
        GiphyData[] gifs = new GiphyData[NUM_OF_GIFS];

        for (int i = 0; i < NUM_OF_GIFS; i++) {
            JSONObject Gif = data.getJSONObject(i);
            GiphyData gif = new GiphyData();

            JSONObject images = Gif.getJSONObject("images");
            JSONObject original = images.getJSONObject("original");

            gif.setUrl(original.getString("url"));

            gifs[i] = gif;
        }

        return gifs;
    }

    private void display() {

        GiphyData[] gifs = giphy.getGiphyData();
        int imageAmount = gifs.length;

        while (imageAmount > 0) {

            GiphyData gif = gifs[imageAmount - 1];

            ImgUrl.add(gif.getUrl());

            imageAmount--;
        }

        this.recyclerView = (RecyclerView) findViewById(R.id.gifs_recycler_view);
        Manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(Manager);
        adapter = new GifsAdapter(ImgUrl, this);
        recyclerView.setAdapter(adapter);



    }


}

