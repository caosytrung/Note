package tech.soft.notemaster.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dee on 03/04/2017.
 */

public class Note implements Serializable {
    private int id;
    private String label;
    private String body;
    private int type;
    private int textColor;
    private Date dateCreate;
    private String imageS;
    private String backgroundS;
    private String mutilColor;

    public Note(int id, String label, String body,
                int type, int textColor,
                String imageS, Date dateCreate,
                String backgroundS, String mutilColor) {
        this.id = id;
        this.label = label;
        this.body = body;
        this.type = type;
        this.textColor = textColor;
        this.dateCreate = dateCreate;
        this.imageS = imageS;
        this.backgroundS = backgroundS;
        this.mutilColor = mutilColor;
    }

    public Note(int id, String label, String body,
                int type, int textColor, String imageS,
                String backgroundS, String mutilColor) {
        this.id = id;
        this.label = label;
        this.body = body;
        this.type = type;
        this.textColor = textColor;
        this.imageS = imageS;
        this.backgroundS = backgroundS;
        this.mutilColor = mutilColor;
    }


    public Note(String label, String body,
                int type, int textColor, String imageS,
                String backgroundS, String mutilColor,
                Date dateCreate) {
        this.label = label;
        this.body = body;
        this.type = type;
        this.textColor = textColor;
        this.dateCreate = dateCreate;
        this.backgroundS = backgroundS;
        this.imageS  = imageS;
        this.mutilColor = mutilColor;
    }

    public Note(String label, String body, int type,
                int textColor, String imageS, String backgroundS, String mutilColor) {
        this.label = label;
        this.body = body;
        this.type = type;
        this.textColor = textColor;
        this.imageS = imageS;
        this.backgroundS = backgroundS;
        this.mutilColor = mutilColor;
    }

    public String getMutilColor() {
        return mutilColor;
    }

    public void setMutilColor(String mutilColor) {
        this.mutilColor = mutilColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getImageS() {
        return imageS;
    }

    public void setImageS(String imageS) {
        this.imageS = imageS;
    }

    public String getBackgroundS() {
        return backgroundS;
    }

    public void setBackgroundS(String backgroundS) {
        this.backgroundS = backgroundS;
    }
}
