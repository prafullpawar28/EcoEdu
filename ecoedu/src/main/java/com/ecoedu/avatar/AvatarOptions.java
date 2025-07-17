package com.ecoedu.avatar;

import java.util.Arrays;
import java.util.List;

public class AvatarOptions {
    public static List<String> getHairStyles() {
        return Arrays.asList("Short", "Long", "Curly", "Straight", "Ponytail", "Bun", "Mohawk");
    }

    public static List<String> getHairColors() {
        return Arrays.asList("Black", "Brown", "Blonde", "Red", "Blue", "Green", "Pink");
    }

    public static List<String> getSkinTones() {
        return Arrays.asList("Light", "Fair", "Medium", "Olive", "Brown", "Dark");
    }

    public static List<String> getEyeColors() {
        return Arrays.asList("Brown", "Blue", "Green", "Hazel", "Gray", "Amber");
    }

    public static List<String> getAccessories() {
        return Arrays.asList("Glasses", "Hat", "Earrings", "Necklace", "Scarf", "Headphones", "Bowtie");
    }
} 