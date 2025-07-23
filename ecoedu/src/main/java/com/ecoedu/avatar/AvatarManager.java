package com.ecoedu.avatar;

public class AvatarManager {
    private static AvatarManager instance;
    private Avatar currentAvatar;

    private AvatarManager() {
        // Default avatar
        currentAvatar = new Avatar("Short", "Smile", "Glasses", "#43e97b");
    }

    public static AvatarManager getInstance() {
        if (instance == null) instance = new AvatarManager();
        return instance;
    }

    public Avatar getCurrentAvatar() {
        return currentAvatar;
    }

    public void saveAvatar(Avatar avatar) {
        this.currentAvatar = avatar;
    }
} 