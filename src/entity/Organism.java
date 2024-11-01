package entity;
import java.lang.String;

public abstract class Organism {

    private String name;
    private String type;
    private int quantity;
    private double energy;
    private int plusOrganism;

    public Organism(String name, String type, int quantity, double energy){
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.energy = energy;
        this.plusOrganism = 0;
    }

    public String getName() {
        return name;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public void isEaten(){
        this.quantity--;
    }

    public abstract void interactWithResource(Resource resource);

    @Override
    public String toString() {
        return "Name: " + name + ", Type: " + type + ", Quantity: " + quantity + ", Energy: " + energy;
    }

    public abstract void interactWithOrganism(Organism other);

    public int getPlusOrganism() {
        return plusOrganism;
    }

    public void setPlusOrganism(int plusOrganism) {
        this.plusOrganism = plusOrganism;
    }
}
