package com.pcr.myinfoweather.models;

import java.util.ArrayList;

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
    @Column("latitude") float latitude;
    @Column("longitude") float longitude;

    public Location() {

    }

    private Location(Builder builder) {
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private float latitude;
        private float longitude;

        private Builder() {

        }

        public Builder withLatitude(float latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withLongitude(float longitude) {
            this.longitude = longitude;
            return this;
        }

        public Location build() {
            return new Location(this);
        }

    }
}
