package entity;

import EcoSystem.FinalParameters;

public class Plant extends Organism{

    public Plant(String name, String type, int quantity, double energy) {
        super(name, type, quantity, energy);
    }

    @Override
    public void interactWithResource(Resource resource) {
        System.out.println(this.getName() + " interacts with the resource " + resource.getName() + ".");

        if (resource.getName().equalsIgnoreCase("Sunlight")) {
            this.setEnergy(this.getEnergy() + resource.getEnergy() * 1.5);
        } else if (resource.getName().equalsIgnoreCase("soil") && this instanceof Plant) {
            this.setEnergy(this.getEnergy() + resource.getEnergy() * 1.2);
        } else {
            this.setEnergy(this.getEnergy() + resource.getEnergy());
        }
    }

    @Override
    public void interactWithOrganism(Organism other) {
    }

}
