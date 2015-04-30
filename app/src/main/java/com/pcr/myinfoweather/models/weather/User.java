package com.pcr.myinfoweather.models.weather;

import com.pcr.myinfoweather.models.Location;
import com.pcr.myinfoweather.models.WeatherData;

import java.util.Date;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.AutoIncrementPrimaryKey;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * Created by Paula on 23/02/2015.
 */
@Table("weather")
public class User extends Model {

    @AutoIncrementPrimaryKey
    @Column("id") long id;
    @Column("image") private float image;
    @Column("title") private String title;
    @Column("temp_min") private float temp_min;
    @Column("temp_max") private float temp_max;
    @Column("windSpeed") private float windSpeed;
    @Column("date") private String date;
    @Column("weatherCode") private int weatherCode;
    @Column("address") private UserAdress address;
    @Column("location") private Location geoLocation;

    public User() {

    }

    private User(Builder builder) {
        title = builder.title;
        image = builder.image;
        temp_min = builder.temp_min;
        temp_max = builder.temp_max;
        windSpeed = builder.windSpeed;
        date = builder.date;
        weatherCode = builder.weatherCode;
        geoLocation = builder.geoLocation;
        address = builder.address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public float getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(float temp_min) {
        this.temp_min = temp_min;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(float temp_max) {
        this.temp_max = temp_max;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float speed) {
        this.windSpeed = speed;
    }

    public float getImage() {
        return image;
    }

    public void setImage(float image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UserAdress getAddress() {
        return address;
    }

    public void setAddress(UserAdress address) {
        this.address = address;
    }


    public Location getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(Location geoLocation) {
        this.geoLocation = geoLocation;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String title;
        private int image;
        private float temp_min;
        private float temp_max;
        private float windSpeed;
        private String date;
        private int weatherCode;
        private Location geoLocation;
        private UserAdress address;

        private Builder() {

        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withImage(int image) {
            this.image = image;
            return this;
        }

        public Builder withTempMin(float tempMin) {
            this.temp_min = tempMin;
            return this;
        }

        public Builder withTempMax(float tempMax) {
            this.temp_max = tempMax;
            return this;
        }

        public Builder withWindSpeed(float windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public Builder withDate(String date) {
            this.date = date;
            return this;
        }

        public Builder withAddress(UserAdress address) {
            this.address = address;
            return this;
        }

        public Builder withWeatherCode(int code) {
            this.weatherCode = code;
            return this;
        }

        public Builder withGeoLocation(Location geoLocation) {
            this.geoLocation = geoLocation;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }


}
