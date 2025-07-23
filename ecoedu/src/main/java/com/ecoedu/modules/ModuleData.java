package com.ecoedu.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleData {
    public static List<Module> getModules() {
        List<Module> modules = new ArrayList<>();
        modules.add(new Module(
            1,
            "Recycling Basics",
            "Learn the fundamentals of recycling and why it matters.",
            "♻️",
            Arrays.asList(
                new Lesson(1, "What is Recycling?", "Recycling is the process of converting waste into reusable material. It helps reduce pollution and saves resources.", null),
                new Lesson(2, "How to Recycle", "Sort your waste into paper, plastic, glass, and metal. Clean items before recycling.", null),
                new Lesson(3, "Why Recycle?", "Recycling conserves natural resources, saves energy, and reduces landfill waste.", null)
            )
        ));
        modules.add(new Module(
            2,
            "Water Conservation",
            "Discover ways to save water at home and in your community.",
            "💧",
            Arrays.asList(
                new Lesson(4, "Why Save Water?", "Water is a precious resource. Conserving water helps the environment and saves money.", null),
                new Lesson(5, "Tips to Save Water", "Take shorter showers, fix leaks, and turn off the tap while brushing your teeth.", null)
            )
        ));
        modules.add(new Module(
            3,
            "Renewable Energy",
            "Explore different types of renewable energy and their benefits.",
            "⚡",
            Arrays.asList(
                new Lesson(6, "What is Renewable Energy?", "Energy from sources that are naturally replenished, like solar, wind, and hydro.", null),
                new Lesson(7, "Solar Power", "Solar panels capture sunlight and turn it into electricity.", null),
                new Lesson(8, "Wind Power", "Wind turbines use wind to generate electricity.", null)
            )
        ));
        modules.add(new Module(
            4,
            "Wildlife Protection",
            "Learn how to protect animals and their habitats.",
            "🦉",
            Arrays.asList(
                new Lesson(9, "Why Protect Wildlife?", "Wildlife is essential for a balanced ecosystem. Protecting animals helps maintain biodiversity.", null),
                new Lesson(10, "Endangered Species", "Some animals are at risk of extinction. Learn how to help them.", null),
                new Lesson(11, "How You Can Help", "Support wildlife organizations, avoid products from endangered species, and spread awareness.", null)
            )
        ));
        modules.add(new Module(
            5,
            "Green Transportation",
            "Discover eco-friendly ways to get around.",
            "🚲",
            Arrays.asList(
                new Lesson(12, "Why Green Transportation?", "Using eco-friendly transportation reduces pollution and saves energy.", null),
                new Lesson(13, "Biking & Walking", "Biking and walking are healthy and have zero emissions.", null),
                new Lesson(14, "Public Transport", "Buses and trains are more efficient than cars for many people.", null)
            )
        ));
        modules.add(new Module(
            6,
            "Plastic Pollution",
            "Understand the impact of plastic and how to reduce it.",
            "🧴",
            Arrays.asList(
                new Lesson(15, "The Problem with Plastic", "Plastic takes hundreds of years to decompose and harms wildlife.", null),
                new Lesson(16, "Alternatives to Plastic", "Use reusable bags, bottles, and containers instead of single-use plastics.", null),
                new Lesson(17, "Community Cleanups", "Join or organize a cleanup to remove plastic from your local environment.", null)
            )
        ));
        return modules;
    }
} 