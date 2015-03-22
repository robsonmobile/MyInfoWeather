package com.pcr.myinfoweather.models;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.AutoIncrementPrimaryKey;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * Created by Paula on 07/03/2015.
 */
@Table("location")
public class UserLocation extends Model {

    @AutoIncrementPrimaryKey
    @Column("id") long id;
    @Column("latitude") float latitude;
    @Column("longitude") float longitude;

    public UserLocation() {

    }

    private UserLocation(Builder builder) {
        latitude = builder.latitude;
        longitude = builder.longitude;
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

        public UserLocation build() {
            return new UserLocation(this);
        }

    }

}
