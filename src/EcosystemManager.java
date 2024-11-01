import entity.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EcosystemManager {

    private List<Organism> organisms = new ArrayList<>();
    private List<Resource> resources = new ArrayList<>();
    private double temperature;
    private double humidity;
    private double waterLevel;

    public void addOrganism(Organism organism){
        organisms.add(organism);
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWaterLevel() {
        return waterLevel;
    }

    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Resource: ")) {
                    parseResource(line);
                } else if (line.startsWith("Name: ")) {
                    parseOrganism(line);
                } else {
                    parseEnvironmentCondition(line);
                }
            }
        }
    }

    private void parseResource(String line) {
        String[] parts = line.split(", ");
        String name = getValue(parts[0]);
        double energyValue = Double.parseDouble(getValue(parts[1]));
        resources.add(new Resource(name, energyValue));
    }

    private void parseOrganism(String line) {
        String[] parts = line.split(", ");
        String name = getValue(parts[0]);
        String type = getValue(parts[1]);
        int quantity = Integer.parseInt(getValue(parts[2]));
        double energy = Double.parseDouble(getValue(parts[3]));

        if (parts.length == 5) {
            Nutrition nutrition = Nutrition.valueOf(getValue(parts[4]));
            organisms.add(new Animal(name, type, quantity, energy, nutrition));
        } else {
            organisms.add(new Plant(name, type, quantity, energy));
        }
    }

    private void parseEnvironmentCondition(String line) {
        if (line.startsWith("Temperature: ")) {
            temperature = Double.parseDouble(getValue(line));
        } else if (line.startsWith("Humidity: ")) {
            humidity = Double.parseDouble(getValue(line));
        } else if (line.startsWith("WaterLevel: ")) {
            waterLevel = Double.parseDouble(getValue(line));
        }
    }

    private String getValue(String part) {
        return part.split(": ")[1];
    }

    public void simulate() {
        for (Organism org : organisms) {
            for (Organism other : organisms) {
                if (!org.equals(other)) {
                    org.interactWithOrganism(other);
                }
            }

            for (Resource resource : resources) {
                org.interactWithResource(resource);
            }
        }
    }

    public boolean removeOrganismByName(String name) {
        Iterator<Organism> iterator = organisms.iterator();
        while (iterator.hasNext()) {
            Organism organism = iterator.next();
            if (organism.getName().equalsIgnoreCase(name)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public void deleteOrganismAndSave(String name, String filename) throws IOException {
        if (removeOrganismByName(name)) {
            saveToFile(filename);
            System.out.println("Organism '" + name + "' is deleted and the file is updated.");
        } else {
            System.out.println("Organism with name '" + name + "' is not found.");
        }
    }

    public void saveToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        for (Resource resource : resources) {
            writer.write(resource.toString());
            writer.newLine();
        }

        for (Organism org : organisms) {
            writer.write(org.toString());
            writer.newLine();
        }

        writer.write("Temperature: " + temperature);
        writer.newLine();
        writer.write("Humidity: " + humidity);
        writer.newLine();
        writer.write("WaterLevel: " + waterLevel);
        writer.newLine();

        writer.close();
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }

    public boolean removeResourceByName(String name) {
        Iterator<Resource> iterator = resources.iterator();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            if (resource.getName().equalsIgnoreCase(name)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public void deleteResourceAndSave(String name, String filename) throws IOException {
        if (removeResourceByName(name)) {
            saveToFile(filename);
            System.out.println("Resource '" + name + "' is deleted and the file is updated");
        } else {
            System.out.println("Resource with name '" + name + "' is not found.");
        }
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public List<Resource> getResources() {
        return resources;
    }

    public List<Organism> getOrganisms() {
        return organisms;
    }
}
