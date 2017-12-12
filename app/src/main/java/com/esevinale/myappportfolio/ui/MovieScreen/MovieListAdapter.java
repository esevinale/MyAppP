//package com.esevinale.myappportfolio.ui.MovieScreen;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * Created by twili on 11.12.2017.
// */
//
//public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
//
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        @BindView(R.id.movie_poster)
//        ImageView poster;
//        @BindView(R.id.title_background)
//        View titleBackground;
//        @BindView(R.id.movie_name)
//        TextView name;
//
//        public Movie movie;
//
//        public ViewHolder(View root) {
//            super(root);
//            ButterKnife.bind(this, root);
//        }
//
//        @Override
//        public void onClick(View view) {
//            MovieListAdapter.this.view.onMovieClicked(movie);
//        }
//    }
//
//    @Override
//    public MovieListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(MovieListAdapter.ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//}
