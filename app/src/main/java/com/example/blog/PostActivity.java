package com.example.blog;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import android.content.Intent;

//SOURCES:
// https://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android

public class PostActivity extends AppCompatActivity {

    String id;
    String postID;
    GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);

        //get ID
        id = getIntent().getStringExtra("ID");

        //calls
        Call<List<Comment>> call = service.getComments("/comments?postId=" + id);
        Call<Post> call1 = service.getPost("/posts/" + id);
        Call<User> call2 = service.getUser("/users/" + id);

        //responses
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                listviewComments(response.body());
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        call1.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();
                TextView title = findViewById(R.id.title);
                title.setText(post.getTitle());
                TextView body = findViewById(R.id.body);
                body.setText(post.getBody());
                postID = post.getId();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        call2.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Button name = findViewById(R.id.name);
                name.setText(user.getUsername());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void listviewComments(List<Comment> commentsList)
    {
        final ListView lvComments = findViewById(R.id.comments);
        CommentsListAdapter adapter = new CommentsListAdapter(this, commentsList);
        lvComments.setAdapter(adapter);
    }

    public void nameClick(View view)
    {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    public void back(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void comment(View view)
    {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText email = promptsView.findViewById(R.id.etEmail);
        final EditText name = promptsView.findViewById(R.id.etName);
        final EditText comment = promptsView.findViewById(R.id.etComment);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("SAVE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                final String e = email.getText().toString();
                                final String n = name.getText().toString();
                                final String c = comment.getText().toString();
                                saveComment(e, n, c);
                                Call<List<Comment>> call = service.getComments("/comments?postId=" + postID);
                                call.enqueue(new Callback<List<Comment>>() {
                                    @Override
                                    public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                                        Comment comment1 = new Comment(postID, null, n, e, c);
                                        List<Comment> tempList = response.body();
                                        tempList.add(comment1);
                                        listviewComments(tempList);
                                    }

                                    @Override
                                    public void onFailure(Call<List<Comment>> call, Throwable t) {
                                        Toast.makeText(PostActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void saveComment(String email, String name, String comment)
    {
        Call<Comment> call = service.postComment(postID, name, email, comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Toast.makeText(PostActivity.this, "Your comment was added!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}