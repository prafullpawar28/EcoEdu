package com.ecoedu.dailytasks;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DailyTaskManager {
    private static DailyTaskManager instance;
    private List<DailyTask> allTasks;
    private List<DailyTask> currentDailyTasks;
    private LocalDate lastTaskGeneration;
    private int totalPointsEarned;
    private int streakDays;

    private DailyTaskManager() {
        this.allTasks = new ArrayList<>();
        this.currentDailyTasks = new ArrayList<>();
        this.lastTaskGeneration = LocalDate.now().minusDays(1);
        this.totalPointsEarned = 0;
        this.streakDays = 0;
        initializeTaskPool();
    }

    public static DailyTaskManager getInstance() {
        if (instance == null) {
            instance = new DailyTaskManager();
        }
        return instance;
    }

    private void initializeTaskPool() {
        // Recycling Tasks
        allTasks.add(new DailyTask(1, "Sort Your Recycling", 
            "Separate paper, plastic, and glass into proper recycling bins", 50, DailyTask.TaskCategory.RECYCLING));
        allTasks.add(new DailyTask(2, "Compost Kitchen Waste", 
            "Start composting fruit peels and vegetable scraps", 75, DailyTask.TaskCategory.RECYCLING));
        allTasks.add(new DailyTask(3, "Use Reusable Shopping Bag", 
            "Bring your own bag instead of using plastic bags", 30, DailyTask.TaskCategory.RECYCLING));
        allTasks.add(new DailyTask(19, "Make a Recycled Craft", 
            "Create something new from old materials (e.g., bottle planter)", 60, DailyTask.TaskCategory.RECYCLING));
        allTasks.add(new DailyTask(20, "Teach a Friend to Recycle", 
            "Show a friend or family member how to recycle properly", 40, DailyTask.TaskCategory.RECYCLING));

        // Energy Tasks
        allTasks.add(new DailyTask(4, "Turn Off Unused Lights", 
            "Switch off lights in empty rooms", 25, DailyTask.TaskCategory.ENERGY));
        allTasks.add(new DailyTask(5, "Unplug Electronics", 
            "Unplug chargers and devices when not in use", 40, DailyTask.TaskCategory.ENERGY));
        allTasks.add(new DailyTask(6, "Use Natural Light", 
            "Open curtains and use sunlight instead of artificial lighting", 35, DailyTask.TaskCategory.ENERGY));
        allTasks.add(new DailyTask(21, "Family Energy Challenge", 
            "Challenge your family to save energy for a day!", 55, DailyTask.TaskCategory.ENERGY));
        allTasks.add(new DailyTask(22, "Draw an Energy-Saving Poster", 
            "Make a poster about saving energy and hang it up", 45, DailyTask.TaskCategory.ENERGY));

        // Water Tasks
        allTasks.add(new DailyTask(7, "Take Shorter Showers", 
            "Reduce shower time by 2 minutes", 45, DailyTask.TaskCategory.WATER));
        allTasks.add(new DailyTask(8, "Fix a Leaky Faucet", 
            "Repair or report any dripping taps", 60, DailyTask.TaskCategory.WATER));
        allTasks.add(new DailyTask(9, "Use a Water Bottle", 
            "Drink from a reusable water bottle instead of disposable cups", 30, DailyTask.TaskCategory.WATER));
        allTasks.add(new DailyTask(23, "Collect Rainwater for Plants", 
            "Use a bucket to collect rainwater and water your plants", 50, DailyTask.TaskCategory.WATER));
        allTasks.add(new DailyTask(24, "Water Fact of the Day", 
            "Share a cool water-saving fact with someone", 35, DailyTask.TaskCategory.WATER));

        // Transportation Tasks
        allTasks.add(new DailyTask(10, "Walk or Bike", 
            "Use active transportation for short trips", 80, DailyTask.TaskCategory.TRANSPORTATION));
        allTasks.add(new DailyTask(11, "Use Public Transport", 
            "Take bus, train, or carpool instead of driving alone", 70, DailyTask.TaskCategory.TRANSPORTATION));
        allTasks.add(new DailyTask(12, "Plan Efficient Routes", 
            "Combine multiple errands into one trip", 40, DailyTask.TaskCategory.TRANSPORTATION));
        allTasks.add(new DailyTask(25, "Nature Walk Adventure", 
            "Go for a walk and spot 3 different plants or animals", 60, DailyTask.TaskCategory.TRANSPORTATION));
        allTasks.add(new DailyTask(26, "Bike Safety Check", 
            "Check your bike for safety and share a tip with a friend", 35, DailyTask.TaskCategory.TRANSPORTATION));

        // Waste Tasks
        allTasks.add(new DailyTask(13, "Use Reusable Containers", 
            "Pack lunch in reusable containers instead of disposable ones", 50, DailyTask.TaskCategory.WASTE));
        allTasks.add(new DailyTask(14, "Avoid Single-Use Items", 
            "Choose reusable alternatives to disposable products", 55, DailyTask.TaskCategory.WASTE));
        allTasks.add(new DailyTask(15, "Repair Instead of Replace", 
            "Fix a broken item instead of throwing it away", 65, DailyTask.TaskCategory.WASTE));
        allTasks.add(new DailyTask(27, "Trash-Free Snack Day", 
            "Eat snacks with no wrappers or waste for a day", 45, DailyTask.TaskCategory.WASTE));
        allTasks.add(new DailyTask(28, "Litter Patrol", 
            "Pick up 5 pieces of litter in your neighborhood (with an adult)", 60, DailyTask.TaskCategory.WASTE));

        // Education Tasks
        allTasks.add(new DailyTask(16, "Learn About Local Wildlife", 
            "Research native plants and animals in your area", 45, DailyTask.TaskCategory.EDUCATION));
        allTasks.add(new DailyTask(17, "Share Eco Tips", 
            "Teach someone about an environmental topic", 60, DailyTask.TaskCategory.EDUCATION));
        allTasks.add(new DailyTask(18, "Read Environmental News", 
            "Read an article about environmental issues", 40, DailyTask.TaskCategory.EDUCATION));
        allTasks.add(new DailyTask(29, "Eco Quiz Time!", 
            "Take an online quiz about the environment and share your score", 50, DailyTask.TaskCategory.EDUCATION));
        allTasks.add(new DailyTask(30, "Draw Your Dream Green City", 
            "Draw a picture of an eco-friendly city and show your family", 55, DailyTask.TaskCategory.EDUCATION));

        // --- NEW CHALLENGES ---
        // Recycling
        allTasks.add(new DailyTask(31, "Upcycle a Plastic Bottle", "Turn a used plastic bottle into something useful (e.g., a bird feeder)", 70, DailyTask.TaskCategory.RECYCLING));
        allTasks.add(new DailyTask(32, "Host a Recycling Race", "Challenge your family to see who can sort recyclables fastest", 60, DailyTask.TaskCategory.RECYCLING));
        // Energy
        allTasks.add(new DailyTask(33, "Digital Detox Evening", "Spend an evening with no screens to save energy", 65, DailyTask.TaskCategory.ENERGY));
        allTasks.add(new DailyTask(34, "Solar Power Research", "Find out how solar panels work and share a fact", 50, DailyTask.TaskCategory.ENERGY));
        // Water
        allTasks.add(new DailyTask(35, "Greywater for Plants", "Use leftover water from washing veggies to water plants", 40, DailyTask.TaskCategory.WATER));
        allTasks.add(new DailyTask(36, "Shower Song Challenge", "Play a 4-minute song and finish your shower before it ends", 55, DailyTask.TaskCategory.WATER));
        // Transportation
        allTasks.add(new DailyTask(37, "Car-Free Day", "Go a whole day without using a car", 90, DailyTask.TaskCategory.TRANSPORTATION));
        allTasks.add(new DailyTask(38, "Map Your Route", "Draw a map of your walk or bike ride and share it", 45, DailyTask.TaskCategory.TRANSPORTATION));
        // Waste
        allTasks.add(new DailyTask(39, "Zero-Waste Lunch", "Pack a lunch with no disposable packaging", 70, DailyTask.TaskCategory.WASTE));
        allTasks.add(new DailyTask(40, "Community Clean-Up", "Pick up litter in a park or public space (with permission)", 80, DailyTask.TaskCategory.WASTE));
        // Education
        allTasks.add(new DailyTask(41, "Eco-Friendly Art", "Create art using only recycled or natural materials", 60, DailyTask.TaskCategory.EDUCATION));
        allTasks.add(new DailyTask(42, "Plant a Tree", "Help plant a tree or care for a young plant", 100, DailyTask.TaskCategory.EDUCATION));

        // --- EVEN MORE NEW CHALLENGES ---
        // Recycling
        allTasks.add(new DailyTask(43, "Recycling Detective", "Find 3 items at home that can be recycled and put them in the correct bin", 55, DailyTask.TaskCategory.RECYCLING));
        allTasks.add(new DailyTask(44, "Eco Poster", "Create a poster about recycling and display it in your home or school", 60, DailyTask.TaskCategory.RECYCLING));
        // Energy
        allTasks.add(new DailyTask(45, "Fan-Free Day", "Go a whole day without using a fan or air conditioner", 70, DailyTask.TaskCategory.ENERGY));
        allTasks.add(new DailyTask(46, "LED Hunt", "Check your home for old bulbs and suggest switching to LEDs", 40, DailyTask.TaskCategory.ENERGY));
        // Water
        allTasks.add(new DailyTask(47, "Water the Plants", "Water your plants early in the morning to save water", 35, DailyTask.TaskCategory.WATER));
        allTasks.add(new DailyTask(48, "No Running Tap", "Brush your teeth without leaving the tap running", 30, DailyTask.TaskCategory.WATER));
        // Transportation
        allTasks.add(new DailyTask(49, "Carpool Challenge", "Share a ride with friends or family to reduce pollution", 65, DailyTask.TaskCategory.TRANSPORTATION));
        allTasks.add(new DailyTask(50, "Public Transport Day", "Use only public transport for a day", 80, DailyTask.TaskCategory.TRANSPORTATION));
        // Waste
        allTasks.add(new DailyTask(51, "No Plastic Day", "Avoid using any single-use plastic for a whole day", 90, DailyTask.TaskCategory.WASTE));
        allTasks.add(new DailyTask(52, "Repair a Toy", "Fix a broken toy instead of throwing it away", 50, DailyTask.TaskCategory.WASTE));
        // Education
        allTasks.add(new DailyTask(53, "Eco Book Reading", "Read a book about the environment and share a fact you learned", 60, DailyTask.TaskCategory.EDUCATION));
        allTasks.add(new DailyTask(54, "Green Pledge", "Write and share your own eco-friendly pledge", 55, DailyTask.TaskCategory.EDUCATION));
    }

    public void generateDailyTasks() {
        LocalDate today = LocalDate.now();
        
        // Only generate new tasks if it's a new day
        if (today.isAfter(lastTaskGeneration)) {
            currentDailyTasks.clear();
            Random random = new Random();
            
            // Generate 3 random tasks for the day
            List<DailyTask> availableTasks = new ArrayList<>(allTasks);
            for (int i = 0; i < 10 && !availableTasks.isEmpty(); i++) {
                int randomIndex = random.nextInt(availableTasks.size());
                DailyTask selectedTask = availableTasks.get(randomIndex);
                
                // Create a new instance with today's date
                DailyTask dailyTask = new DailyTask(
                    selectedTask.getId(),
                    selectedTask.getTitle(),
                    selectedTask.getDescription(),
                    selectedTask.getPoints(),
                    selectedTask.getCategory()
                );
                dailyTask.setAssignedDate(today);
                
                currentDailyTasks.add(dailyTask);
                availableTasks.remove(randomIndex);
            }
            
            lastTaskGeneration = today;
        }
    }

    public List<DailyTask> getCurrentDailyTasks() {
        generateDailyTasks(); // Ensure tasks are generated
        return new ArrayList<>(currentDailyTasks);
    }

    public void markTaskComplete(int taskId) {
        for (DailyTask task : currentDailyTasks) {
            if (task.getId() == taskId && !task.isCompleted()) {
                task.setCompleted(true);
                totalPointsEarned += task.getPoints();
                updateStreak();
                break;
            }
        }
    }

    public void markTaskIncomplete(int taskId) {
        for (DailyTask task : currentDailyTasks) {
            if (task.getId() == taskId && task.isCompleted()) {
                task.setCompleted(false);
                totalPointsEarned -= task.getPoints();
                break;
            }
        }
    }

    private void updateStreak() {
        // Simple streak logic - if all tasks are completed today, increment streak
        boolean allCompleted = currentDailyTasks.stream().allMatch(DailyTask::isCompleted);
        if (allCompleted) {
            streakDays++;
        }
    }

    public int getCompletedTasksCount() {
        return (int) currentDailyTasks.stream().filter(DailyTask::isCompleted).count();
    }

    public int getTotalTasksCount() {
        return currentDailyTasks.size();
    }

    public int getTotalPointsEarned() {
        return totalPointsEarned;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public int getTodayPoints() {
        return currentDailyTasks.stream()
                .filter(DailyTask::isCompleted)
                .mapToInt(DailyTask::getPoints)
                .sum();
    }

    public double getCompletionPercentage() {
        if (currentDailyTasks.isEmpty()) return 0.0;
        return (double) getCompletedTasksCount() / getTotalTasksCount() * 100;
    }

    public List<DailyTask> getTasksByCategory(DailyTask.TaskCategory category) {
        return currentDailyTasks.stream()
                .filter(task -> task.getCategory() == category)
                .collect(Collectors.toList());
    }

    public void resetDailyTasks() {
        currentDailyTasks.clear();
        lastTaskGeneration = LocalDate.now().minusDays(1);
        generateDailyTasks();
    }

    public boolean isTaskCompleted(int taskId) {
        return currentDailyTasks.stream()
                .anyMatch(task -> task.getId() == taskId && task.isCompleted());
    }

    public DailyTask getTaskById(int taskId) {
        return currentDailyTasks.stream()
                .filter(task -> task.getId() == taskId)
                .findFirst()
                .orElse(null);
    }
} 