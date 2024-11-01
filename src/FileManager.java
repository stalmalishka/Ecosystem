import entity.Organism;
import entity.Resource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String ECOSYSTEM_LIST_FILE = "saved_ecosystems.txt";
    private static final String LIST_FILE_NAME_ENDING = "_ecosystem.txt";

    public static String createNewEcosystem(String ecosystemName) throws IOException {
        String fileName = ecosystemName + LIST_FILE_NAME_ENDING;
        File file = new File(fileName);

        if (file.exists()) {
            System.out.println("File with name " + fileName + " already exists.");
            return null;
        }

        if (file.createNewFile()) {
            System.out.println("A new ecosystem file has been created: " + fileName);
            addEcosystemToList(fileName);
            return fileName;
        } else {
            System.out.println("Failed to create a file " + fileName);
            return null;
        }
    }

    private static void addEcosystemToList(String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(ECOSYSTEM_LIST_FILE, true)) {
            writer.write(fileName + "\n");
        }
    }

    public static List<String> getSavedEcosystems() throws IOException {
        List<String> ecosystems = new ArrayList<>();
        File file = new File(ECOSYSTEM_LIST_FILE);

        if (!file.exists()) {
            file.createNewFile();
        }

        List<String> lines = Files.readAllLines(Paths.get(ECOSYSTEM_LIST_FILE));
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                ecosystems.add(line.trim());
            }
        }
        return ecosystems;
    }

    public static boolean deleteEcosystem(String ecosystemName) throws IOException {
        String fileName = ecosystemName + LIST_FILE_NAME_ENDING;
        File file = new File(fileName);

        if (!file.exists() || !file.delete()) {
            System.out.println("Failed to delete the ecosystem file: " + fileName);
            return false;
        }

        List<String> ecosystems = getSavedEcosystems();
        ecosystems.remove(fileName);
        saveEcosystemsList(ecosystems);
        return true;
    }

    private static void saveEcosystemsList(List<String> ecosystems) throws IOException {
        try (FileWriter writer = new FileWriter(ECOSYSTEM_LIST_FILE, false)) {
            for (String ecosystem : ecosystems) {
                writer.write(ecosystem + "\n");
            }
        }
    }


}
