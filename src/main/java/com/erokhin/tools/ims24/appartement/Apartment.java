package com.erokhin.tools.ims24.appartement;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * Created by Dmitry Erokhin (dmitry.erokhin@gmail.com)
 * 10/04/16
 */
@Entity
public class Apartment {
    public enum Conclusion {
        NO, MAYBE, YES
    }

    @Id
    private long id;
    private String url;
    private LocalDate vacantFrom;
    private Integer minMonthsRental;
    private Integer maxMonthsRental;
    private Double rooms;
    private Integer livingSpace;
    private Integer maxCapacity;
    private String floor;
    private boolean petsProhibited;
    private Integer finalRentCost;
    private Integer deposit;
    private String location;
    private String comment = "";
    private Conclusion conclusion;

    public Conclusion getConclusion() {
        return conclusion;
    }

    public void setConclusion(Conclusion conclusion) {
        this.conclusion = conclusion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getVacantFrom() {
        return vacantFrom;
    }

    public void setVacantFrom(LocalDate vacantFrom) {
        this.vacantFrom = vacantFrom;
    }

    public Integer getMinMonthsRental() {
        return minMonthsRental;
    }

    public void setMinMonthsRental(Integer minMonthsRental) {
        this.minMonthsRental = minMonthsRental;
    }

    public Integer getMaxMonthsRental() {
        return maxMonthsRental;
    }

    public void setMaxMonthsRental(Integer maxMonthsRental) {
        this.maxMonthsRental = maxMonthsRental;
    }

    public Double getRooms() {
        return rooms;
    }

    public void setRooms(Double rooms) {
        this.rooms = rooms;
    }

    public Integer getLivingSpace() {
        return livingSpace;
    }

    public void setLivingSpace(Integer livingSpace) {
        this.livingSpace = livingSpace;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public boolean isPetsProhibited() {
        return petsProhibited;
    }

    public void setPetsProhibited(boolean petsProhibited) {
        this.petsProhibited = petsProhibited;
    }

    public Integer getFinalRentCost() {
        return finalRentCost;
    }

    public void setFinalRentCost(Integer finalRentCost) {
        this.finalRentCost = finalRentCost;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Apartment)) {
            return false;
        }
        return id == ((Apartment) obj).id;
    }

    @Override
    public String toString() {
        return String.format(
                "Apartment [id=%s] of %f rooms for %d € located at %s.",
                id, rooms, finalRentCost, location);
    }
}
