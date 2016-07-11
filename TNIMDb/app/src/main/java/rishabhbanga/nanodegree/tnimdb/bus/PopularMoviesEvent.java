package rishabhbanga.nanodegree.tnimdb.bus;

import rishabhbanga.nanodegree.tnimdb.retrofit.model.Movie;

/**
 * Created by erishba on 7/12/2016.
 */
public class PopularMoviesEvent {

    //Event for listening the categories change from the settings.

    public static class PreferenceChangeEvent {
        public PreferenceChangeEvent() {
        }
    }

    public static class MoviePosterSelectionEvent {
        public Movie movie;

        public MoviePosterSelectionEvent(Movie movie) {
            this.movie = movie;
        }
    }

    public static class MovieUnFavourite {

        public MovieUnFavourite() {

        }
    }
}
