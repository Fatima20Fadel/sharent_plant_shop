package com.proposal.Nature.Heaven.model;

import jakarta.persistence.*;
@Table(name = "plant_guide")
@Entity
public class PlantGuide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String light;
    private String petFriendly;
    private String water;
    private String humidity;
    private String temperature;
    @OneToOne(mappedBy = "plantGuide")
    private Plant plant;
    public PlantGuide(Long id, String light, String petFriendly, String water, String humidity, String temperature, Plant plant) {
        this.id = id;
        this.light = light;
        this.petFriendly = petFriendly;
        this.water = water;
        this.humidity = humidity;
        this.temperature = temperature;
        this.plant = plant;
    }

    public PlantGuide( ) {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getPetFriendly() {
        return petFriendly;
    }

    public void setPetFriendly(String petFriendly) {
        this.petFriendly = petFriendly;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
