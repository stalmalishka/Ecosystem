import EcoSystem.FinalParameters;
import entity.*;

import java.util.List;

public class PopulationPredictor {

    private final EcosystemManager ecosystemManager;
    private final FinalParameters params;

    public PopulationPredictor(EcosystemManager ecosystemManager, FinalParameters params) {
        this.ecosystemManager = ecosystemManager;
        this.params = params;
    }

    public void predictPopulationChanges() {
        double currentTemp = ecosystemManager.getTemperature();
        double currentHumidity = ecosystemManager.getHumidity();
        double currentWaterLevel = ecosystemManager.getWaterLevel();

        List<Organism> organisms = ecosystemManager.getOrganisms();
        List<Resource> resources = ecosystemManager.getResources();

        for (Organism organism : organisms) {
            if (organism instanceof Animal) {
                predictAnimalChange((Animal) organism, currentTemp, currentHumidity, currentWaterLevel, resources);
            } else if (organism instanceof Plant) {
                predictPlantChange((Plant) organism, currentTemp, currentHumidity, currentWaterLevel, resources);
            }
        }
    }

    private void predictAnimalChange(Animal animal, double temp, double humidity, double water, List<Resource> resources) {
        double normTemp = getNormTemp(animal);
        double normHumidity = getNormHumidity(animal);
        double normWater = getNormWater(animal);

        double tempDiff = Math.abs(temp - normTemp);
        double humidityDiff = Math.abs(humidity - normHumidity);
        double waterDiff = Math.abs(water - normWater);

        String result = animal.getName() + " population ";

        if (tempDiff <= 5 && humidityDiff <= 5 && waterDiff <= 5 && hasSufficientFood(animal, resources)) {
            result += "is stable.";
        } else if (tempDiff > 10 || humidityDiff > 10 || waterDiff > 10 || !hasSufficientFood(animal, resources)) {
            result += "is likely to decrease.";
        } else {
            result += "may increase with some changes in resource availability.";
        }

        System.out.println(result);
    }

    private void predictPlantChange(Plant plant, double temp, double humidity, double water, List<Resource> resources) {
        double normTemp = params.NORMAL_PLANT_TEMP;
        double normHumidity = params.NORMAL_PLANT_HUMIDITY;
        double normWater = params.NORMAL_PLANT_WATER;

        double tempDiff = Math.abs(temp - normTemp);
        double humidityDiff = Math.abs(humidity - normHumidity);
        double waterDiff = Math.abs(water - normWater);

        String result = plant.getName() + " population ";

        if (tempDiff <= 5 && humidityDiff <= 5 && waterDiff <= 5 && hasSufficientResources(plant, resources)) {
            result += "is stable.";
        } else if (tempDiff > 10 || humidityDiff > 10 || waterDiff > 10 || !hasSufficientResources(plant, resources)) {
            result += "is likely to decrease.";
        } else {
            result += "may increase with favorable conditions.";
        }

        System.out.println(result);
    }

    private boolean hasSufficientFood(Animal animal, List<Resource> resources) {
        if (animal.getNutrition() == Nutrition.HERBIVORE) {
            return resources.stream().anyMatch(resource -> resource.getName().equalsIgnoreCase("plant"));
        } else if (animal.getNutrition() == Nutrition.CARNIVORE) {
            return ecosystemManager.getOrganisms().stream()
                    .anyMatch(org -> org instanceof Animal && ((Animal) org).getNutrition() == Nutrition.HERBIVORE);
        }
        return true; // Для всеядных
    }

    private boolean hasSufficientResources(Plant plant, List<Resource> resources) {
        return resources.stream().anyMatch(resource -> resource.getName().equalsIgnoreCase("soil")
                || resource.getName().equalsIgnoreCase("sunlight")
                || resource.getName().equalsIgnoreCase("water"));
    }

    private double getNormTemp(Animal animal) {
        return animal.getNutrition() == Nutrition.CARNIVORE ? params.NORMAL_CARN_TEMP : params.NORMAL_HERB_TEMP;
    }

    private double getNormHumidity(Animal animal) {
        return animal.getNutrition() == Nutrition.CARNIVORE ? params.NORMAL_CARN_HUMIDITY : params.NORMAL_HERB_HUMIDITY;
    }

    private double getNormWater(Animal animal) {
        return animal.getNutrition() == Nutrition.CARNIVORE ? params.NORMAL_CARN_WATER : params.NORMAL_HERB_WATER;
    }
}


