package com.pcr.myinfoweather.models.currentweather;

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
    @Column("latitude") double latitude;
    @Column("longitude") double longitude;

    public Location() {

    }

    private Location(Builder builder) {
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private double latitude;
        private double longitude;

        private Builder() {

        }

        public Builder withLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Location build() {
            return new Location(this);
        }

    }
}
