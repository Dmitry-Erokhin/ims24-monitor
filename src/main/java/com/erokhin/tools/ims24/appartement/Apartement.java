package com.erokhin.tools.ims24.appartement;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.Period;

/**
 * Created by Dmitry Erokhin (dmitry.erokhin@gmail.com)
 * 10/04/16
 */
@Entity
public class Apartement {

    @Id
    String id;

    String url;
    LocalDate vacantFrom;
    Integer minMonthsRental;
    Integer maxMonthsRental;
    Integer rooms;
    boolean hasHalfroom;
    Integer livingSpace;
    Integer maxCapacity;
    String floor;
    boolean petsProhibited;
    Integer finalRentCost;
    Integer deposit;
    String location;
    String additionalInfo;
    String comment;
    Integer rating;


    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Apartement)) {
            return false;
        }
        return id.equalsIgnoreCase(((Apartement) obj).id);
    }

    @Override
    public String toString() {
        return String.format(
                "Apartement [id=%s] of %d rooms for %d € located at %s.",
                id, rooms, finalRentCost, location);
    }
}
