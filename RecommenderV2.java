import java.io.*;
import java.util.*;

public class RecommenderV2 {

    static final String FILE_NAME = "preferences.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Get user input
        System.out.print("Enter your name: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter items you like (comma separated): ");
        String[] likedItems = scanner.nextLine().split(",");

        List<String> userItems = new ArrayList<>();
        for (String item : likedItems) {
            userItems.add(item.trim());
        }

        // 2. Save to file
        saveUserPreferences(username, userItems);

        // 3. Load all users and their preferences
        Map<String, List<String>> userPreferences = loadPreferences();

        // 4. Recommend
        Set<String> yourLikes = new HashSet<>(userItems);
        Set<String> recommendations = new HashSet<>();

        for (Map.Entry<String, List<String>> entry : userPreferences.entrySet()) {
            String user = entry.getKey();
            if (user.equalsIgnoreCase(username)) continue;

            List<String> others = entry.getValue();
            int common = 0;
            for (String item : others) {
                if (yourLikes.contains(item)) {
                    common++;
                }
            }

            if (common >= 1) {
                for (String item : others) {
                    if (!yourLikes.contains(item)) {
                        recommendations.add(item);
                    }
                }
            }
        }

        System.out.println("\n✅ Recommended for " + username + ":");
        if (recommendations.isEmpty()) {
            System.out.println("No recommendations available yet.");
        } else {
            for (String item : recommendations) {
                System.out.println("👉 " + item);
            }
        }
    }

    // Save user preferences to file
    public static void saveUserPreferences(String name, List<String> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(name + ":" + String.join(",", items));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Load all user preferences from file
    public static Map<String, List<String>> loadPreferences() {
        Map<String, List<String>> preferences = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String user = parts[0].trim();
                    String[] items = parts[1].split(",");
                    List<String> itemList = new ArrayList<>();
                    for (String item : items) {
                        itemList.add(item.trim());
                    }
                    preferences.put(user, itemList);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading preferences: " + e.getMessage());
        }
        return preferences;
    }
}

