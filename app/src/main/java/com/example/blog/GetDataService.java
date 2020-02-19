package com.example.blog;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetDataService {
        @GET("/posts")
        Call<List<Post>> getAllPosts();
        @GET()
        Call<List<Comment>> getComments(@Url String url);
        @GET()
        Call<Post> getPost(@Url String url);
        @GET()
        Call<User> getUser(@Url String url);
        @GET
        Call<List<Post>> getUserPosts(@Url String url);
        @POST("/comments")
        @FormUrlEncoded
        Call<Comment> postComment(@Field("postId") String postId,
                                  @Field("name") String name,
                                  @Field("email") String email,
                                  @Field("body") String body);
}
