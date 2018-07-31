package com.nearestCustomers;

/**
 * TODO need to remove this class when it is no longer needed.
 * The class represents a temp customer, used only for the distance calculation.
 */
public class CustomerTmp {

    private String name;
    private String city;
    private String address;
    private String Longtitude;
    private String Latitude;

    //Distance string representation in a readable form.
    private String distanceToUserText;

    //Distance double representation, used for calculations.
    private double distanceToUserValue;

    public String getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(String longtitude) {
        Longtitude = longtitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    /**
     * Constructor.
     *
     * @param name    customer name

     * @param city    customer city
     * @param address customer address
     */

    public CustomerTmp(String name, String city, String address) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.distanceToUserText = null;
        this.distanceToUserValue = 0.0;
        //this.Longtitude = Longtitude;
        //this.Latitude = Latitude;
    }
    public CustomerTmp(String name, String city, String address,String longtitude,String latitude) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.distanceToUserText = null;
        this.distanceToUserValue = 0.0;
        this.Longtitude = longtitude;
        this.Latitude = latitude;
    }
    /**
     * Name getter.
     *
     * @return customer name
     */
    public String getName() {
        return name;
    }

    /**
     * City getter.
     *
     * @return customer city
     */
    public String getCity() {
        return city;
    }

    /**
     * Address getter.
     *
     * @return customer address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Distance to user text getter.
     *
     * @return distance in string representation
     */
    public String getDistanceToUserText() {
        return distanceToUserText;
    }

    /**
     * Distance to user value getter.
     *
     * @return distance
     */
    public double getDistanceToUserValue() {
        return distanceToUserValue;
    }

    /**
     * Distance to user value setter.
     *
     * @param distanceToUserValue distance to user
     */
    public void setDistanceToUserValue(double distanceToUserValue) {
        this.distanceToUserValue = distanceToUserValue;
    }

    /**
     * Distance to user text setter.
     *
     * @param distanceToUserText distance to user text
     */
    public void setDistanceToUserText(String distanceToUserText) {
        this.distanceToUserText = distanceToUserText;
    }
}