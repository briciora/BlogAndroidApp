package com.example.blog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Post>> call = service.getAllPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                listviewPosts(response.body());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void listviewPosts(List<Post> postList)
    {
        final ListView lvPosts = findViewById(R.id.listOfPosts);
        PostsListAdapter adapter = new PostsListAdapter(this, postList);
        lvPosts.setAdapter(adapter);
        lvPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idTV = view.findViewById(R.id.username);
                String idString = idTV.getText().toString();
                startPostAct(idString);
            }
        });

    }

    public void startPostAct(String id)
    {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }


}