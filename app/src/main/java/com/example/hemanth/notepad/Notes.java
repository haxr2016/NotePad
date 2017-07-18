package com.example.hemanth.notepad;

import java.util.Date;

/**
 * Created by hemanth on 7/17/17.
 */

public class Notes {
    private String description;
    private String dt;

    public String getDateTime(){return dt;}

    public void setDateTime(String dt){this.dt = dt;}

    public String getDescription(){
        return  description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
