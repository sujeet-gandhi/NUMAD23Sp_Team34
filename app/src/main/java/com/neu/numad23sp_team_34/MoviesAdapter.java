package com.neu.numad23sp_team_34;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{

    private List<Movie> movies;
    private Context context;



    public MoviesAdapter(List<Movie> movies) {
        this.movies = movies;
    }


    @NonNull
    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.year.setText(movie.getYear());
        if (!TextUtils.isEmpty(movie.getImageUrl())) {
            Picasso.get().load(movie.getImageUrl()).into(holder.poster, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {


                }

                @Override
                public void onError(Exception e) {
                    //do smth when there is picture loading error
                    holder.poster.setImageResource(R.drawable.ic_launcher_background);

                }
            });
        } else {
            holder.poster.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailViewActivity.class);
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("year", movie.getYear());
                intent.putExtra("imageUrl", movie.getImageUrl());
                intent.putExtra("imdbId", movie.getImdbId());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView year;
        ImageView poster;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movieTitle);
            year = itemView.findViewById(R.id.movieYear);
            poster = itemView.findViewById(R.id.moviePoster);
        }
    }
}
