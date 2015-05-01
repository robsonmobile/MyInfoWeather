package com.pcr.myinfoweather.models.currentweather;

import com.google.gson.annotations.Expose;

/**
 * Created by Paula on 10/11/2014.
 */
public class Clouds {

    @Expose
    private int all;

    /**
     *
     * @return
     * The all
     */
    public int getAll() {
        return all;
    }

    /**
     *
     * @param all
     * The all
     */
    public void setAll(int all) {
        this.all = all;
    }

}