package com.pcr.myinfoweather.models;

import se.emilsjolander.sprinkles.annotations.AutoIncrementPrimaryKey;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * Created by Paula on 23/02/2015.
 */
@Table("location")
public class Location {

    @AutoIncrementPrimaryKey
    @Column("id") long id;
    @Column("latitude") private float latitude;
    @Column("longitude") private float longitude;
    @Column("city") private String city;
    @Column("state") private String state;
    @Column("country") private String country;


    public Location() {

    }







    public static final class Builder {

        private float latitude;
        private float longitude;
        private String city;
        private String state;
        private String country;

        private Builder() {

        }

        public Builder withLatitude(float latitude) {
            this.latitude = latitude;
            return this;
        }

    }



}
