package com.ecoedu.dailytasks;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class TaskStatistics {
    private DailyTaskManager taskManager;
    private Map<LocalDate, List<DailyTask>> historicalTasks;
    private Map<DailyTask.TaskCategory, Integer> categoryStats;

    public TaskStatistics() {
        this.taskManager = DailyTaskManager.getInstance();
        this.historicalTasks = new HashMap<>();
        this.categoryStats = new HashMap<>();
        initializeCategoryStats();
    }

    private void initializeCategoryStats() {
        for (DailyTask.TaskCategory category : DailyTask.TaskCategory.values()) {
            categoryStats.put(category, 0);
        }
    }

    public void recordDailyCompletion(LocalDate date, List<DailyTask> tasks) {
        historicalTasks.put(date, new ArrayList<>(tasks));
        updateCategoryStats(tasks);
    }

    private void updateCategoryStats(List<DailyTask> tasks) {
        for (DailyTask task : tasks) {
            if (task.isCompleted()) {
                categoryStats.put(task.getCategory(), 
                    categoryStats.get(task.getCategory()) + 1);
            }
        }
    }

    public double getOverallCompletionRate() {
        if (historicalTasks.isEmpty()) return 0.0;
        
        int totalTasks = 0;
        int completedTasks = 0;
        
        for (List<DailyTask> dailyTasks : historicalTasks.values()) {
            totalTasks += dailyTasks.size();
            completedTasks += dailyTasks.stream()
                .filter(DailyTask::isCompleted)
                .count();
        }
        
        return totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0.0;
    }

    public double getWeeklyCompletionRate() {
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        return getCompletionRateForPeriod(weekAgo, LocalDate.now());
    }

    public double getMonthlyCompletionRate() {
        LocalDate monthAgo = LocalDate.now().minusDays(30);
        return getCompletionRateForPeriod(monthAgo, LocalDate.now());
    }

    private double getCompletionRateForPeriod(LocalDate start, LocalDate end) {
        int totalTasks = 0;
        int completedTasks = 0;
        
        for (Map.Entry<LocalDate, List<DailyTask>> entry : historicalTasks.entrySet()) {
            LocalDate taskDate = entry.getKey();
            if (!taskDate.isBefore(start) && !taskDate.isAfter(end)) {
                List<DailyTask> tasks = entry.getValue();
                totalTasks += tasks.size();
                completedTasks += tasks.stream()
                    .filter(DailyTask::isCompleted)
                    .count();
            }
        }
        
        return totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0.0;
    }

    public DailyTask.TaskCategory getMostCompletedCategory() {
        return categoryStats.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(DailyTask.TaskCategory.RECYCLING);
    }

    public DailyTask.TaskCategory getLeastCompletedCategory() {
        return categoryStats.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(DailyTask.TaskCategory.EDUCATION);
    }

    public int getLongestStreak() {
        int currentStreak = 0;
        int longestStreak = 0;
        LocalDate currentDate = LocalDate.now();
        
        // Check backwards from today
        for (int i = 0; i < 365; i++) { // Check up to a year back
            LocalDate checkDate = currentDate.minusDays(i);
            List<DailyTask> tasks = historicalTasks.get(checkDate);
            
            if (tasks != null && !tasks.isEmpty()) {
                boolean allCompleted = tasks.stream().allMatch(DailyTask::isCompleted);
                if (allCompleted) {
                    currentStreak++;
                    longestStreak = Math.max(longestStreak, currentStreak);
                } else {
                    currentStreak = 0;
                }
            } else {
                currentStreak = 0;
            }
        }
        
        return longestStreak;
    }

    public int getCurrentStreak() {
        int currentStreak = 0;
        LocalDate currentDate = LocalDate.now();
        
        // Check backwards from today
        for (int i = 0; i < 365; i++) {
            LocalDate checkDate = currentDate.minusDays(i);
            List<DailyTask> tasks = historicalTasks.get(checkDate);
            
            if (tasks != null && !tasks.isEmpty()) {
                boolean allCompleted = tasks.stream().allMatch(DailyTask::isCompleted);
                if (allCompleted) {
                    currentStreak++;
                } else {
                    break; // Streak broken
                }
            } else {
                break; // No tasks for this day
            }
        }
        
        return currentStreak;
    }

    public Map<DailyTask.TaskCategory, Double> getCategoryCompletionRates() {
        Map<DailyTask.TaskCategory, Double> rates = new HashMap<>();
        
        for (DailyTask.TaskCategory category : DailyTask.TaskCategory.values()) {
            int totalCategoryTasks = 0;
            int completedCategoryTasks = 0;
            
            for (List<DailyTask> dailyTasks : historicalTasks.values()) {
                for (DailyTask task : dailyTasks) {
                    if (task.getCategory() == category) {
                        totalCategoryTasks++;
                        if (task.isCompleted()) {
                            completedCategoryTasks++;
                        }
                    }
                }
            }
            
            double rate = totalCategoryTasks > 0 ? 
                (double) completedCategoryTasks / totalCategoryTasks * 100 : 0.0;
            rates.put(category, rate);
        }
        
        return rates;
    }

    public List<LocalDate> getPerfectDays() {
        return historicalTasks.entrySet().stream()
            .filter(entry -> entry.getValue().stream().allMatch(DailyTask::isCompleted))
            .map(Map.Entry::getKey)
            .sorted()
            .collect(Collectors.toList());
    }

    public int getTotalPerfectDays() {
        return getPerfectDays().size();
    }

    public double getAverageDailyPoints() {
        if (historicalTasks.isEmpty()) return 0.0;
        
        double totalPoints = 0;
        int daysWithTasks = 0;
        
        for (List<DailyTask> dailyTasks : historicalTasks.values()) {
            if (!dailyTasks.isEmpty()) {
                daysWithTasks++;
                totalPoints += dailyTasks.stream()
                    .filter(DailyTask::isCompleted)
                    .mapToInt(DailyTask::getPoints)
                    .sum();
            }
        }
        
        return daysWithTasks > 0 ? totalPoints / daysWithTasks : 0.0;
    }

    public Map<String, Object> getWeeklyProgress() {
        Map<String, Object> weeklyData = new HashMap<>();
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        
        List<Double> dailyRates = new ArrayList<>();
        List<Integer> dailyPoints = new ArrayList<>();
        
        for (int i = 0; i < 7; i++) {
            LocalDate date = weekAgo.plusDays(i);
            List<DailyTask> tasks = historicalTasks.get(date);
            
            if (tasks != null && !tasks.isEmpty()) {
                double completionRate = (double) tasks.stream()
                    .filter(DailyTask::isCompleted)
                    .count() / tasks.size() * 100;
                dailyRates.add(completionRate);
                
                int points = tasks.stream()
                    .filter(DailyTask::isCompleted)
                    .mapToInt(DailyTask::getPoints)
                    .sum();
                dailyPoints.add(points);
            } else {
                dailyRates.add(0.0);
                dailyPoints.add(0);
            }
        }
        
        weeklyData.put("completionRates", dailyRates);
        weeklyData.put("dailyPoints", dailyPoints);
        weeklyData.put("averageCompletion", dailyRates.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
        weeklyData.put("totalPoints", dailyPoints.stream().mapToInt(Integer::intValue).sum());
        
        return weeklyData;
    }

    public String getMotivationalMessage() {
        double completionRate = getOverallCompletionRate();
        int currentStreak = getCurrentStreak();
        
        if (completionRate >= 90) {
            return "🌟 Amazing! You're an eco-warrior! Keep up the fantastic work!";
        } else if (completionRate >= 75) {
            return "🌱 Great job! You're making a real difference for our planet!";
        } else if (completionRate >= 50) {
            return "🌿 Good progress! Every small action counts towards a greener future!";
        } else if (currentStreak > 0) {
            return "🔥 Nice streak! Keep the momentum going!";
        } else {
            return "🌍 Ready to start your eco-journey? Every task completed helps our planet!";
        }
    }

    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("overallCompletionRate", getOverallCompletionRate());
        summary.put("weeklyCompletionRate", getWeeklyCompletionRate());
        summary.put("monthlyCompletionRate", getMonthlyCompletionRate());
        summary.put("currentStreak", getCurrentStreak());
        summary.put("longestStreak", getLongestStreak());
        summary.put("totalPerfectDays", getTotalPerfectDays());
        summary.put("averageDailyPoints", getAverageDailyPoints());
        summary.put("mostCompletedCategory", getMostCompletedCategory());
        summary.put("leastCompletedCategory", getLeastCompletedCategory());
        summary.put("categoryRates", getCategoryCompletionRates());
        summary.put("motivationalMessage", getMotivationalMessage());
        
        return summary;
    }
} 