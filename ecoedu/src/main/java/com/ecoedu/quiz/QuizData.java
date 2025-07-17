package com.ecoedu.quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizData {
    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(
            "What is the main cause of global warming?",
            new String[]{"Deforestation", "Greenhouse gases", "Overfishing", "Plastic waste"},
            1
        ));
        questions.add(new Question(
            "Which of these is a renewable energy source?",
            new String[]{"Coal", "Oil", "Solar", "Natural Gas"},
            2
        ));
        questions.add(new Question(
            "What can you do to save water?",
            new String[]{"Leave the tap running", "Take shorter showers", "Water the garden at noon", "Ignore leaks"},
            1
        ));
        questions.add(new Question(
            "Which of these is NOT recyclable?",
            new String[]{"Glass bottle", "Plastic bag", "Aluminum can", "Newspaper"},
            1
        ));
        questions.add(new Question(
            "What is composting?",
            new String[]{"Burning waste", "Burying plastic", "Turning organic waste into soil", "Throwing trash in the ocean"},
            2
        ));
        questions.add(new Question(
            "Which gas do trees absorb from the atmosphere?",
            new String[]{"Oxygen", "Carbon Dioxide", "Nitrogen", "Methane"},
            1
        ));
        questions.add(new Question(
            "What is the best way to reduce plastic pollution?",
            new String[]{"Use more plastic", "Recycle plastic", "Use single-use plastics", "Burn plastic"},
            1
        ));
        questions.add(new Question(
            "Which of these helps save energy at home?",
            new String[]{"Leaving lights on", "Using LED bulbs", "Running water constantly", "Open fridge often"},
            1
        ));
        questions.add(new Question(
            "What is the effect of oil spills in the ocean?",
            new String[]{"Clean water", "Harm to marine life", "Faster fish growth", "More oxygen"},
            1
        ));
        questions.add(new Question(
            "Which of these is a benefit of planting trees?",
            new String[]{"More pollution", "Less oxygen", "Cleaner air", "More plastic"},
            2
        ));
        questions.add(new Question(
            "What is the 3R principle?",
            new String[]{"Reduce, Reuse, Recycle", "Read, Run, Rest", "Rain, River, Road", "Red, Rose, Rock"},
            0
        ));
        questions.add(new Question(
            "Which animal is most affected by plastic in the ocean?",
            new String[]{"Elephants", "Sea turtles", "Lions", "Cows"},
            1
        ));
        questions.add(new Question(
            "What is an eco-friendly way to travel short distances?",
            new String[]{"Drive alone", "Walk or bike", "Take a plane", "Use a speedboat"},
            1
        ));
        questions.add(new Question(
            "Which of these is a greenhouse gas?",
            new String[]{"Helium", "Carbon Dioxide", "Argon", "Neon"},
            1
        ));
        questions.add(new Question(
            "What should you do with e-waste (old electronics)?",
            new String[]{"Throw in trash", "Recycle at special centers", "Burn it", "Bury in garden"},
            1
        ));
        return questions;
    }
} 