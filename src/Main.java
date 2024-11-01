import EcoSystem.FinalParameters;
import entity.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static EcosystemManager ecosystem = new EcosystemManager();
    private static String ecosystemFileName = null;

    public static void main(String[] args) throws IOException {

        try {
            int choice = getUserChoice(scanner);
            if (choice == 1) {
                ecosystemFileName = createNewEcosystem(scanner);
            } else if (choice == 2) {
                ecosystemFileName = loadExistingEcosystem(scanner);
            } else if (choice == 3) {
                deleteEcosystem(scanner);
            } else {
                System.out.println("Incorrect input.");
                return;
            }

            if (ecosystemFileName != null) {
                loadEcosystemFile(ecosystem, ecosystemFileName);
            }
        } catch (IOException e) {
            System.out.println("Error when working with ecosystem files.");
            e.printStackTrace();
        }

        Map<Integer, Runnable> commands = new HashMap<>();
        commands.put(1, Main::addPlant);
        commands.put(2, Main::addAnimal);
        commands.put(3, Main::addResource);
        commands.put(4, () -> {
            try {
                Main.setEnvironmentConditions();
            } catch (IOException e) {
                System.out.println("An error occurred while setting environment conditions: " + e.getMessage());
            }
        });
        commands.put(5, ecosystem::simulate);
        commands.put(6, Main::interactionBetweenEntities);
        commands.put(7, () -> {
            try {
                Main.deleteOrganism();
            } catch (IOException e) {
                System.out.println("An error occurred while deleting organism: " + e.getMessage());
            }
        });
        commands.put(8, () -> {
            try {
                Main.deleteResource();
            } catch (IOException e) {
                System.out.println("An error occurred while deleting resource: " + e.getMessage());
            }
        });
        commands.put(9, Main::saveAndExit);
        commands.put(10, Main::predictPopulationChanges);

        while (true) {
            displayMenu();
            int command = getUserInput();
            Runnable action = commands.get(command);
            if (action != null) {
                action.run();
                if (command == 9) {
                    break;
                }
            } else {
                System.out.println("Wrong command, try again.");
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("1. Add a plant");
        System.out.println("2. Add an animal");
        System.out.println("3. Add a resource");
        System.out.println("4. Set ecosystem conditions");
        System.out.println("5. Simulate interactions among all participants");
        System.out.println("6. Interaction between two selected organisms");
        System.out.println("7. Delete an organism");
        System.out.println("8. Delete a resource");
        System.out.println("9. Save and exit");
        System.out.println("10. Predict population changes");
        System.out.print("Enter command: ");
    }


    private static int getUserInput() {
        int command = scanner.nextInt();
        scanner.nextLine();
        return command;
    }

    private static void addPlant() {
        System.out.print("Enter plant's name: ");
        String name = scanner.nextLine();
        System.out.print("Enter plant's type: ");
        String type = scanner.nextLine();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter the energy level: ");
        double energy = scanner.nextDouble();
        ecosystem.addOrganism(new Plant(name, type, quantity, energy));
        System.out.println("The plant has been added.");
    }

    private static void addAnimal() {
        System.out.print("Enter animal's name: ");
        String name = scanner.nextLine();
        System.out.print("Enter animal's type: ");
        String type = scanner.nextLine();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter the energy level: ");
        double energy = scanner.nextDouble();
        System.out.print("Enter the type of power supply (HERBIVORE, CARNIVORE): ");
        String nutrition = scanner.next();
        ecosystem.addOrganism(new Animal(name, type, quantity, energy, Nutrition.valueOf(nutrition)));
        System.out.println("The animal has been added.");
    }

    private static void addResource() {
        System.out.print("Enter the resource name: ");
        String resourceName = scanner.nextLine();
        System.out.print("Enter the resource energy level: ");
        double resourceEnergy = scanner.nextDouble();
        ecosystem.addResource(new Resource(resourceName, resourceEnergy));
        System.out.println("Resource added: " + resourceName + " with energy " + resourceEnergy);
    }

    private static void setEnvironmentConditions() throws IOException {
        System.out.print("Enter ecosystem temperature: ");
        double temperature = scanner.nextDouble();
        System.out.print("Enter ecosystem humidity: ");
        double humidity = scanner.nextDouble();
        System.out.print("Enter ecosystem water level: ");
        double waterLevel = scanner.nextDouble();

        ecosystem.setTemperature(temperature);
        ecosystem.setHumidity(humidity);
        ecosystem.setWaterLevel(waterLevel);
        ecosystem.saveToFile(ecosystemFileName);
        System.out.println("Ecosystem conditions set and saved.");
    }


    private static void interactionBetweenEntities() {
        List<Organism> organisms = ecosystem.getOrganisms();
        List<Resource> resources = ecosystem.getResources();

        System.out.println("Available organisms:");
        for (int i = 0; i < organisms.size(); i++) {
            System.out.println((i + 1) + ". " + organisms.get(i).toString());
        }

        System.out.println("Available resources:");
        for (int i = 0; i < resources.size(); i++) {
            System.out.println((organisms.size() + i + 1) + ". " + resources.get(i).toString());
        }

        System.out.print("Choose the first entity (enter number): ");
        int firstIndex = scanner.nextInt() - 1;

        System.out.print("Choose the second entity (enter number): ");
        int secondIndex = scanner.nextInt() - 1;

        if (firstIndex < 0 || firstIndex >= organisms.size()) {
            System.out.println("Invalid choice for the first entity.");
            return;
        }

        Organism firstOrganism = organisms.get(firstIndex);

        if (secondIndex < 0 || secondIndex >= organisms.size() + resources.size()) {
            System.out.println("Invalid choice for the second entity.");
            return;
        }

        if (secondIndex < organisms.size()) {
            Organism secondOrganism = organisms.get(secondIndex);
            System.out.println("Interaction between " + firstOrganism.getName() + " and " + secondOrganism.getName());
            firstOrganism.interactWithOrganism(secondOrganism);
        } else {
            Resource resource = resources.get(secondIndex - organisms.size());
            System.out.println("Interaction between " + firstOrganism.getName() + " and resource " + resource.getName());
            firstOrganism.interactWithResource(resource);
        }
    }



    private static void deleteOrganism() throws IOException {
        System.out.print("Enter organism's name for to delete: ");
        String organismName = scanner.nextLine();
        ecosystem.deleteOrganismAndSave(organismName, ecosystemFileName);
    }

    private static void deleteResource() throws IOException {
        System.out.print("Enter resource's name for to delete: ");
        String resourceName = scanner.nextLine();
        ecosystem.deleteResourceAndSave(resourceName, ecosystemFileName);
    }

    private static void saveAndExit() {
        try {
            ecosystem.saveToFile(ecosystemFileName);
            System.out.println("The ecosystem is preserved. Completion of work.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Ecosystem conservation Error.");
        }
    }

    private static void predictPopulationChanges() {
        FinalParameters params = new FinalParameters();
        PopulationPredictor predictor = new PopulationPredictor(ecosystem, params);
        System.out.println("\nPopulation change forecast:");
        predictor.predictPopulationChanges();
    }

    private static int getUserChoice(Scanner scanner) {
        System.out.println("Do you want to create a new simulation or upload an existing one?");
        System.out.println("1. Create a new simulation");
        System.out.println("2. Upload an existing simulation");
        System.out.println("3. Delete ecosystem");
        System.out.print("Enter the command: ");
        return scanner.nextInt();
    }

    private static String createNewEcosystem(Scanner scanner) throws IOException {
        scanner.nextLine();
        System.out.print("Enter a name for the new ecosystem: ");
        String ecosystemName = scanner.nextLine();
        String ecosystemFileName = FileManager.createNewEcosystem(ecosystemName);

        if (ecosystemFileName == null) {
            System.out.println("An error occurred when creating a new ecosystem.");
        }

        return ecosystemFileName;
    }

    private static String loadExistingEcosystem(Scanner scanner) throws IOException {

        List<String> savedEcosystems = FileManager.getSavedEcosystems();
        if (savedEcosystems.isEmpty()) {
            System.out.println("There are no preserved ecosystems. Please create a new one.");
            return null;
        }

        System.out.println("Accessible ecosystems:");
        for (int i = 0; i < savedEcosystems.size(); i++) {
            System.out.println((i + 1) + ". " + savedEcosystems.get(i));
        }

        System.out.print("Select an ecosystem (enter a number): ");
        int ecosystemIndex = scanner.nextInt() - 1;

        if (ecosystemIndex >= 0 && ecosystemIndex < savedEcosystems.size()) {
            return savedEcosystems.get(ecosystemIndex);
        } else {
            System.out.println("Wrong choice.");
            return null;
        }
    }

    private static void loadEcosystemFile(EcosystemManager ecosystem, String ecosystemFileName) throws IOException {
        ecosystem.loadFromFile(ecosystemFileName);
        System.out.println("The ecosystem has been successfully uploaded from the file " + ecosystemFileName);
    }

    private static void deleteEcosystem(Scanner scanner) throws IOException {
        List<String> savedEcosystems = FileManager.getSavedEcosystems();
        if (savedEcosystems.isEmpty()) {
            System.out.println("No saved ecosystems available for deletion.");
            return;
        }

        System.out.println("Available ecosystems:");
        for (int i = 0; i < savedEcosystems.size(); i++) {
            System.out.println((i + 1) + ". " + savedEcosystems.get(i));
        }

        System.out.print("Select an ecosystem to delete (enter the number): ");
        int ecosystemIndex = scanner.nextInt() - 1;

        if (ecosystemIndex >= 0 && ecosystemIndex < savedEcosystems.size()) {
            String ecosystemName = savedEcosystems.get(ecosystemIndex).replace("_ecosystem.txt", "");
            boolean success = FileManager.deleteEcosystem(ecosystemName);
            if (success) {
                System.out.println("Ecosystem successfully deleted.");
            } else {
                System.out.println("Error occurred while deleting the ecosystem.");
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }



}