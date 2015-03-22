package com.pcr.myinfoweather.models.weather;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.AutoIncrementPrimaryKey;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * Created by Paula on 23/02/2015.
 */
@Table("weather")
public class Weatherx extends Model {

    @AutoIncrementPrimaryKey
    @Column("id") long id;
    @Column("temp_min") private float temp_min;
    @Column("temp") private float temp;
    @Column("temp_max") private float temp_max;
    @Column("speed") private float speed;

    public Weatherx() {

    }

    private Weatherx(Builder builder) {
        temp_min = builder.temp_min;
        temp = builder.temp;
        temp_max = builder.temp_max;
        speed = builder.speed;
    }

    public float getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(float temp_min) {
        this.temp_min = temp_min;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(float temp_max) {
        this.temp_max = temp_max;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private float temp_min;
        private float temp;
        private float temp_max;
        private float speed;

        private Builder() {

        }

        public Builder withTempMin(float tempMin) {
            this.temp_min = tempMin;
            return this;
        }

        public Builder withTemp(float temp) {
            this.temp = temp;
            return this;
        }

        public Builder withTempMax(float tempMax) {
            this.temp_max = tempMax;
            return this;
        }

        public Builder withSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public Weatherx build() {
            return new Weatherx(this);
        }


    }





}
