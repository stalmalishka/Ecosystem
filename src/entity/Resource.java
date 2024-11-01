package entity;

public class Resource {
    private String name;
    private double energy;

    public String getName() {
        return name;
    }

    public double getEnergy() {
        return energy;
    }

    public Resource(String name, double energy){
        this.name = name;
        this.energy = energy;
    }

    @Override
    public String toString() {
        return "Resource: " + name + ", Energy: " + energy;
    }
}
