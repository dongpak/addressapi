/*
 *
 */
package com.churchclerk.addressapi.model;

import java.util.Objects;
import java.util.UUID;

/**
 *
 */
public class Address extends BaseModel {

    private String  street;
    private String  city;
    private String  state;
    private String  zip;
    private String  country;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        if (!super.equals(o)) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(city, address.city) &&
                Objects.equals(state, address.state) &&
                Objects.equals(zip, address.zip) &&
                Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), street, city, state, zip, country);
    }

    /**
     *
     * @param source
     */
    public void copy(Address source) {
        super.copy(source);

        setStreet(source.getStreet());
        setCity(source.getCity());
        setState(source.getState());
        setZip(source.getZip());
        setCountry(source.getCountry());
    }
}
