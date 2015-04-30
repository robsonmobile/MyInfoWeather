package com.pcr.myinfoweather.models.weather;

import se.emilsjolander.sprinkles.annotations.AutoIncrementPrimaryKey;
import se.emilsjolander.sprinkles.annotations.Column;

/**
 * Created by Paula Rosa on 30/04/2015.
 */
public class UserAdress {

    @AutoIncrementPrimaryKey
    @Column("id") long id;
    @Column("city") String city;
    @Column("state") String state;
    @Column("country") String country;
    @Column("completeAdress") String completeAddress;


    public UserAdress() {

    }

    private UserAdress(Builder builder) {
        this.city = builder.city;
        this.state = builder.state;
        this.country = builder.country;
        this.completeAddress = builder.completeAddress;
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

    public String getCompleteAdress() {
        completeAddress = getCity() + ", " + getState() + " - " + getCountry();
        return completeAddress;
    }

    public void setCompleteAdress(String completeAdress) {
        this.completeAddress = completeAdress;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private String city;
        private String state;
        private String country;
        private String completeAddress;

        private Builder() {

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

        public Builder withCompleteAddress(String address) {
            this.completeAddress = address;
            return this;
        }

        public UserAdress build() {
            return new UserAdress(this);
        }

    }
}
