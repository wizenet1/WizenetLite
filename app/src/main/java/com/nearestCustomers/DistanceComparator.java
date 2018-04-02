package com.nearestCustomers;

import java.util.Comparator;

/**
 * Comparator used to compare between to users distances.
 */
public class DistanceComparator implements Comparator<CustomerTmp> {
    @Override
    public int compare(CustomerTmp customer1, CustomerTmp customer2) {

        if(customer1.getDistanceToUserValue() < customer2.getDistanceToUserValue()){
            return -1;
        }
        else if(customer1.getDistanceToUserValue() > customer2.getDistanceToUserValue()){
            return 1;
        }

        return 0;
    }
}
