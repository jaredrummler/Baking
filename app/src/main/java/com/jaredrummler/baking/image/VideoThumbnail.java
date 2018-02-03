package com.jaredrummler.baking.image;

import android.support.annotation.Nullable;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.data.model.Step;
import java.util.List;

public class VideoThumbnail {

    private final String url;

    public VideoThumbnail(String url) {
        this.url = url;
    }

    /**
     * Get the video thumbnail from a recipe.
     * The video thumbnail will be the last video from the recipe's steps.
     *
     * @param recipe The recipe
     * @return The video thumbnail to load with {@link GlideApp} or {@code null} if none exists
     */
    @Nullable
    public static VideoThumbnail from(Recipe recipe) {
        List<Step> steps = recipe.getSteps();
        if (steps != null) {
            int size = steps.size();
            for (int i = size - 1; i > 0; i--) {
                String videoURL = steps.get(i).getVideoURL();
                if (videoURL != null) {
                    return new VideoThumbnail(videoURL);
                }
            }
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "VideoThumbnail{" +
                "url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VideoThumbnail that = (VideoThumbnail) o;

        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

}
