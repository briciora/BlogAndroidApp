package com.example.blog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import androidx.fragment.app.FragmentActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String id;
    static String latitude = "";
    static String longitude = "";
    User user;

    GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        //get ID
        id = getIntent().getStringExtra("ID");

        //calls
        Call<List<Post>> call = service.getUserPosts("/posts?userId=" + id);
        Call<User> call1 = service.getUser("/users/" + id);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                listviewPosts(response.body());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(UserActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        call1.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                TextView name = findViewById(R.id.name);
                name.setText(user.getName());
                TextView userName = findViewById(R.id.username);
                userName.setText(user.getUsername());
                TextView email = findViewById(R.id.email);
                email.setText(user.getEmail());
                TextView phone = findViewById(R.id.phone);
                phone.setText(user.getPhone());
                TextView website = findViewById(R.id.website);
                website.setText(user.getWebsite());
                latitude = user.getAddress().getGeo().getLat();
                longitude = user.getAddress().getGeo().getLng();
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(UserActivity.this);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
//        latitude = user.getAddress().getGeo().getLat();
//        longitude = user.getAddress().getGeo().getLng();

        mMap = googleMap;
        LatLng location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    public void listviewPosts(List<Post> postList)
    {
        final ListView lvPosts = findViewById(R.id.userPosts);
        UserPostsListAdapter adapter = new UserPostsListAdapter(this, postList);
        lvPosts.setAdapter(adapter);
    }

    public void back(View view)
    {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

}