
package com.logistics.model;

import java.sql.Date;

public class Shipment {

    private int shipmentId;
    private String source;
    private String destination;
    private double weight;
    private Date shipmentDate;
    private String status;

    public Shipment() {
    }

    public Shipment(String source, String destination,
                    double weight, Date shipmentDate) {

        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.shipmentDate = shipmentDate;
        this.status = "Pending";
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
