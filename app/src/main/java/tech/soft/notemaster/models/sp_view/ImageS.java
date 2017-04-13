package tech.soft.notemaster.models.sp_view;

/**
 * Created by dee on 12/04/2017.
 */

public class ImageS {
    private int s;
    private int e;
    private String data;

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ImageS(int s, int e, String data) {

        this.s = s;
        this.e = e;
        this.data = data;
    }
}
