package com.ecoedu.dashboard;

import javafx.scene.control.*;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCombination;

public class QuickActionsPanel extends ToolBar {
    public QuickActionsPanel() {
        Button addModuleBtn = new Button("➕ Add Module");
        addModuleBtn.setTooltip(new Tooltip("Add a new learning module"));
        addModuleBtn.getStyleClass().add("eco-btn");
        addModuleBtn.setOnAction(e -> {/* TODO: Add module dialog */});
        Button pushNotifBtn = new Button("📤 Push Notification");
        pushNotifBtn.setTooltip(new Tooltip("Send a notification to all users"));
        pushNotifBtn.getStyleClass().add("eco-btn");
        pushNotifBtn.setOnAction(e -> {/* TODO: Push notification dialog */});
        Button searchBtn = new Button("🔍 Search Global");
        searchBtn.setTooltip(new Tooltip("Search across all content"));
        searchBtn.getStyleClass().add("eco-btn");
        searchBtn.setOnAction(e -> {/* TODO: Global search dialog */});
        Button emergencyBtn = new Button("🚨 Emergency Broadcast");
        emergencyBtn.setTooltip(new Tooltip("Send an emergency alert to all users"));
        emergencyBtn.getStyleClass().add("eco-btn");
        emergencyBtn.setOnAction(e -> {/* TODO: Emergency broadcast dialog */});
        getItems().addAll(addModuleBtn, pushNotifBtn, searchBtn, emergencyBtn);
        setStyle("-fx-background-color: #A8E6CF; -fx-padding: 8 16; -fx-spacing: 10;");
    }
} 