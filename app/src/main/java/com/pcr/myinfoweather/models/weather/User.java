package com.pcr.myinfoweather.models.weather;

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
    @Column("temp_min") private float temp_min;
    @Column("temp_max") private float temp_max;
    @Column("windSpeed") private float windSpeed;
    @Column("date") private Date date;
    @Column("city") private String city;
    @Column("adminArea") private String state;
    @Column("country") private String country;

    public User() {

    }

    private User(Builder builder) {
        image = builder.image;
        temp_min = builder.temp_min;
        temp_max = builder.temp_max;
        windSpeed = builder.windSpeed;
        date = builder.date;
        city = builder.city;
        state = builder.state;
        country = builder.country;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private int image;
        private float temp_min;
        private float temp_max;
        private float windSpeed;
        private Date date;
        private String city;
        private String state;
        private String country;


        private Builder() {

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

        public Builder withSpeed(float windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public Builder withDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withState(String state) {
            this.state = state;
            return this;
        }

        public Builder withCountry(String country) {
            this.country = country;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }



}
