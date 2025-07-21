package com.ecoedu.quiz;

import java.util.*;

public class QuizData {
    private static final Map<String, List<Question>> quizMap = new LinkedHashMap<>();
    static {
        // General Eco Quiz
        List<Question> ecoQuiz = new ArrayList<>();
        ecoQuiz.add(new Question("What is the main cause of global warming?", new String[]{"Deforestation", "Greenhouse gases", "Overfishing", "Plastic waste"}, 1));
        ecoQuiz.add(new Question("Which of these is a renewable energy source?", new String[]{"Coal", "Oil", "Solar", "Natural Gas"}, 2));
        ecoQuiz.add(new Question("What can you do to save water?", new String[]{"Leave the tap running", "Take shorter showers", "Water the garden at noon", "Ignore leaks"}, 1));
        ecoQuiz.add(new Question("Which of these is NOT recyclable?", new String[]{"Glass bottle", "Plastic bag", "Aluminum can", "Newspaper"}, 1));
        ecoQuiz.add(new Question("What is composting?", new String[]{"Burning waste", "Burying plastic", "Turning organic waste into soil", "Throwing trash in the ocean"}, 2));
        quizMap.put("🌱 General Eco Quiz", ecoQuiz);

        // Recycling Quiz
        List<Question> recyclingQuiz = new ArrayList<>();
        recyclingQuiz.add(new Question("Which bin should you put glass bottles in?", new String[]{"Compost", "Recycling", "Landfill", "Hazardous"}, 1));
        recyclingQuiz.add(new Question("What does the recycling symbol mean?", new String[]{"Trash", "Reuse", "Recycle", "Compost"}, 2));
        recyclingQuiz.add(new Question("Which of these is NOT recyclable?", new String[]{"Plastic bag", "Aluminum can", "Paper", "Glass jar"}, 0));
        recyclingQuiz.add(new Question("What color is the recycling bin usually?", new String[]{"Blue", "Red", "Green", "Yellow"}, 0));
        recyclingQuiz.add(new Question("What should you do before recycling a bottle?", new String[]{"Break it", "Rinse it", "Throw cap away", "Burn it"}, 1));
        quizMap.put("♻️ Recycling Quiz", recyclingQuiz);

        // Water Conservation Quiz
        List<Question> waterQuiz = new ArrayList<>();
        waterQuiz.add(new Question("What is a simple way to save water at home?", new String[]{"Take longer showers", "Fix leaks", "Water lawn at noon", "Leave tap running"}, 1));
        waterQuiz.add(new Question("Which of these uses the most water?", new String[]{"Shower", "Toilet", "Dishwasher", "Washing hands"}, 1));
        waterQuiz.add(new Question("What is greywater?", new String[]{"Dirty water from factories", "Reusable household water", "Ocean water", "Rainwater"}, 1));
        waterQuiz.add(new Question("Why should you turn off the tap while brushing teeth?", new String[]{"Save water", "No reason", "Make noise", "It’s fun"}, 0));
        waterQuiz.add(new Question("What is a rain barrel used for?", new String[]{"Collecting rainwater", "Throwing trash", "Storing oil", "Feeding pets"}, 0));
        quizMap.put("💧 Water Conservation Quiz", waterQuiz);
    }

    public static List<String> getQuizList() {
        return new ArrayList<>(quizMap.keySet());
    }

    public static List<Question> getQuestionsForQuiz(String quizTitle) {
        return quizMap.getOrDefault(quizTitle, Collections.emptyList());
    }

    // For backward compatibility
    public static List<Question> getQuestions() {
        return getQuestionsForQuiz(getQuizList().get(0));
    }
} 