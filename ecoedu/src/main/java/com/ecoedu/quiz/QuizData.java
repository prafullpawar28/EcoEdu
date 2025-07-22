package com.ecoedu.quiz;

import java.util.*;

public class QuizData {
    public static List<String> getQuizCategories() {
        return Arrays.asList(
            "Eco Basics",
            "Ocean Life",
            "Recycling",
            "Energy",
            "Wildlife",
            "Climate Change"
        );
    }
    public static List<Question> getQuestionsForCategory(String category) {
        switch (category) {
            case "Eco Basics":
                return Arrays.asList(
                    new Question("What is the process by which plants make their food?", new String[]{"Photosynthesis", "Respiration", "Transpiration", "Fermentation"}, 0),
                    new Question("What gas do humans need to breathe?", new String[]{"Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen"}, 0),
                    new Question("Which of these is a renewable resource?", new String[]{"Sunlight", "Coal", "Oil", "Natural Gas"}, 0),
                    new Question("What do trees give us?", new String[]{"Oxygen", "Plastic", "Gold", "Iron"}, 0),
                    new Question("Which animal is a mammal?", new String[]{"Frog", "Shark", "Dolphin", "Eagle"}, 2),
                    new Question("What is composting?", new String[]{"Burning waste", "Turning organic waste into soil", "Throwing trash in the ocean", "Burying plastic"}, 1),
                    new Question("Which of these is NOT a plant?", new String[]{"Fern", "Moss", "Mushroom", "Grass"}, 2)
                );
            case "Ocean Life":
                return Arrays.asList(
                    new Question("What is the largest ocean on Earth?", new String[]{"Atlantic Ocean", "Indian Ocean", "Pacific Ocean", "Arctic Ocean"}, 2),
                    new Question("Which animal is known as the 'King of the Jungle'?", new String[]{"Tiger", "Lion", "Elephant", "Giraffe"}, 1),
                    new Question("What do whales eat?", new String[]{"Krill", "Grass", "Fish", "Bananas"}, 0),
                    new Question("Which of these is a sea mammal?", new String[]{"Octopus", "Seal", "Crab", "Jellyfish"}, 1),
                    new Question("What is coral?", new String[]{"A plant", "A rock", "A living animal", "A type of sand"}, 2),
                    new Question("Which fish is famous for its stripes?", new String[]{"Clownfish", "Shark", "Tuna", "Salmon"}, 0),
                    new Question("What causes ocean tides?", new String[]{"Wind", "Moon's gravity", "Rain", "Sunlight"}, 1)
                );
            case "Recycling":
                return Arrays.asList(
                    new Question("Which bin should you put a plastic bottle in?", new String[]{"Compost Bin", "Recycling Bin", "Landfill Bin", "Hazardous Waste Bin"}, 1),
                    new Question("Which of these helps reduce pollution?", new String[]{"Planting Trees", "Burning Trash", "Using More Cars", "Wasting Water"}, 0),
                    new Question("What color is the recycling bin usually?", new String[]{"Blue", "Red", "Green", "Yellow"}, 0),
                    new Question("What should you do before recycling a bottle?", new String[]{"Break it", "Rinse it", "Throw cap away", "Burn it"}, 1),
                    new Question("Which of these is NOT recyclable?", new String[]{"Plastic bag", "Aluminum can", "Paper", "Glass jar"}, 0),
                    new Question("What does the recycling symbol mean?", new String[]{"Trash", "Reuse", "Recycle", "Compost"}, 2),
                    new Question("What should you NOT put in the recycling bin?", new String[]{"Glass bottle", "Plastic bag", "Paper", "Aluminum can"}, 1)
                );
            case "Energy":
                return Arrays.asList(
                    new Question("Which of these is a renewable energy source?", new String[]{"Coal", "Oil", "Solar", "Natural Gas"}, 2),
                    new Question("What do bees collect from flowers?", new String[]{"Nectar", "Water", "Leaves", "Seeds"}, 0),
                    new Question("What is the main source of energy for Earth?", new String[]{"Wind", "Sun", "Coal", "Oil"}, 1),
                    new Question("Which device uses the most energy at home?", new String[]{"TV", "Refrigerator", "Lamp", "Toaster"}, 1),
                    new Question("What can you do to save energy?", new String[]{"Turn off lights", "Leave TV on", "Open fridge often", "Use more water"}, 0),
                    new Question("Which of these is NOT a fossil fuel?", new String[]{"Oil", "Coal", "Solar", "Natural Gas"}, 2),
                    new Question("What is energy conservation?", new String[]{"Using more energy", "Saving energy", "Wasting energy", "Creating energy"}, 1)
                );
            case "Wildlife":
                return Arrays.asList(
                    new Question("Which animal is the largest land animal?", new String[]{"Lion", "Elephant", "Giraffe", "Bear"}, 1),
                    new Question("What do pandas eat?", new String[]{"Bamboo", "Fish", "Grass", "Leaves"}, 0),
                    new Question("Which bird cannot fly?", new String[]{"Penguin", "Eagle", "Sparrow", "Crow"}, 0),
                    new Question("Where do kangaroos live?", new String[]{"Africa", "Australia", "Asia", "Europe"}, 1),
                    new Question("Which animal is known for changing its color?", new String[]{"Chameleon", "Elephant", "Tiger", "Rabbit"}, 0),
                    new Question("What is a group of lions called?", new String[]{"Pack", "Pride", "Flock", "School"}, 1),
                    new Question("Which animal hibernates in winter?", new String[]{"Bear", "Dog", "Cow", "Horse"}, 0)
                );
            case "Climate Change":
                return Arrays.asList(
                    new Question("What is the main cause of climate change?", new String[]{"Greenhouse gases", "Rain", "Wind", "Sun"}, 0),
                    new Question("Which gas is a greenhouse gas?", new String[]{"Oxygen", "Carbon Dioxide", "Nitrogen", "Helium"}, 1),
                    new Question("What can you do to help stop climate change?", new String[]{"Plant trees", "Burn more coal", "Use more cars", "Waste water"}, 0),
                    new Question("What is global warming?", new String[]{"Earth getting colder", "Earth getting warmer", "More rain", "Less sunlight"}, 1),
                    new Question("Which of these is a clean energy source?", new String[]{"Oil", "Coal", "Wind", "Gasoline"}, 2),
                    new Question("What is the effect of melting ice caps?", new String[]{"Sea levels rise", "More forests", "Less rain", "More deserts"}, 0),
                    new Question("What is a carbon footprint?", new String[]{"Amount of carbon you use", "Your shoe size", "A type of plant", "A kind of animal"}, 0)
                );
            default:
                // Fallback: return a default question
                return Arrays.asList(
                    new Question("What is the Earth made of?", new String[]{"Rocks", "Cheese", "Plastic", "Paper"}, 0)
                );
        }
    }
    public static List<Question> getQuestions() {
        // Return a default set of questions (Eco Basics)
        return getQuestionsForCategory("Eco Basics");
    }
} 