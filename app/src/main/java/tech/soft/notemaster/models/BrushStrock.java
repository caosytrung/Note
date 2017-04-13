package tech.soft.notemaster.models;

import android.graphics.Path;

/**
 * Created by dee on 12/04/2017.
 */
public class BrushStrock {
    private int with;
    private int color;
    private Path path;

    public int getWith() {
        return with;
    }

    public void setWith(int with) {
        this.with = with;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public BrushStrock(int with, int color, Path path) {

        this.with = with;
        this.color = color;
        this.path = path;
    }
}
