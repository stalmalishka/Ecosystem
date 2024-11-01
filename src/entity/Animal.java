package entity;

import EcoSystem.FinalParameters;

public class Animal extends Organism{

    private Nutrition nutrition;

    public Nutrition getNutrition() {
        return nutrition;
    }

    public Animal(String name1, String type1, int quantity, double energy, Nutrition nutrition) {
        super(name1, type1, quantity, energy);
        this.nutrition = nutrition;
    }

    @Override
    public String toString() {
        return super.toString() + ", Nutrition: " + nutrition;
    }

    public void eat(Organism food) {
        System.out.println(getName() + " is eating " + food.getName());
        this.setEnergy(this.getEnergy() + food.getEnergy());
        food.isEaten();
    }

    @Override
    public void interactWithOrganism(Organism other) {
        if (other instanceof Plant) {
            if (this.getNutrition() == Nutrition.HERBIVORE) {
                eat(other);
            } else {
                System.out.println(this.getName() + " doesn't eat plants.");
            }
        } else if (other instanceof Animal) {
            Animal otherAnimal = (Animal) other;
            if (this.getNutrition() == Nutrition.CARNIVORE) {
                if (otherAnimal.getNutrition() == Nutrition.HERBIVORE) {
                    eat(other);
                } else {
                    System.out.println(this.getName() + " can't eat " + other.getName() + ".");
                }
            } else if (this.getNutrition() == Nutrition.HERBIVORE) {
                System.out.println(this.getName() + " can't eat other animals.");
            }
        } else {
            System.out.println(this.getName() + " doesn't interact with this type of object.");
        }
    }

    public void interactWithResource(Resource resource) {

        System.out.println(this.getName() + " interacts with the resource " + resource.getName() + ".");

        if (resource.getName().equalsIgnoreCase("soil")) {
            this.setEnergy(this.getEnergy() + resource.getEnergy() * 0.1);
        } else {
            this.setEnergy(this.getEnergy() + resource.getEnergy());
        }

    }

}
