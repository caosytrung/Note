package tech.soft.notemaster.models.sp_view;

/**
 * Created by dee on 14/04/2017.
 */

public class BackgroundS {
    private int start;
    private int end;
    private int color;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public BackgroundS(int start, int end, int color) {

        this.start = start;
        this.end = end;
        this.color = color;
    }
}
