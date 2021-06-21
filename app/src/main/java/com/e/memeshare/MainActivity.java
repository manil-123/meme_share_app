package com.e.memeshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    ImageView memeImage;
    ProgressBar progressBar;
    Button shareButton;
    String currentImageUrl=null;

    @SuppressLint("ClickableViewAccessibility")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memeImage = findViewById(R.id.memeImage);
        progressBar=findViewById(R.id.progressBar);
        shareButton=findViewById(R.id.share);
        loadMeme();
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareMeme();
            }
        });
        memeImage.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                loadMeme();
            }
            @Override
            public void onSwipeRight() {
                loadMeme();
            }


        });
    }

    public void loadMeme() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://meme-api.herokuapp.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            currentImageUrl = response.getString("url");
                            Glide
                                    .with(MainActivity.this)
                                    .load(currentImageUrl)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(memeImage);
                        } catch (JSONException e) {

                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void shareMeme(){
        Intent sendIntent=new Intent(Intent.ACTION_SEND);
        sendIntent.setType("image/jpg");
        sendIntent.putExtra(Intent.EXTRA_TEXT,currentImageUrl);
        Intent shareIntent=Intent.createChooser(sendIntent,null);
        startActivity(shareIntent);
    }
}