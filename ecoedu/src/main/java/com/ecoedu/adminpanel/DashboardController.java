package com.ecoedu.adminpanel;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import java.util.List;

public class DashboardController {
    private final DashboardService service;
    private final DashboardView view;

    public DashboardController(DashboardService service, DashboardView view) {
        this.service = service;
        this.view = view;
        bindStatCards();
        bindNotifications();
        bindTopStudents();
        bindCharts();
    }

    private void bindStatCards() {
        view.getStudentCard().animateTo(service.getStudentCount());
        view.getTeacherCard().animateTo(service.getTeacherCount());
        view.getPrivateTeacherCard().animateTo(service.getPrivateTeacherCount());
        // Simulate live updates
        new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    view.getStudentCard().animateTo(service.getStudentCount());
                    view.getTeacherCard().animateTo(service.getTeacherCount());
                    view.getPrivateTeacherCard().animateTo(service.getPrivateTeacherCount());
                });
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private void bindNotifications() {
        service.getNotifications().addListener((ListChangeListener<String>) c -> {
            view.updateNotifications(service.getNotifications());
        });
        view.updateNotifications(service.getNotifications());
    }

    private void bindTopStudents() {
        service.getTopStudents().addListener((ListChangeListener<DashboardService.TopStudent>) c -> {
            view.updateTopStudents(service.getTopStudents());
        });
        view.updateTopStudents(service.getTopStudents());
    }

    private void bindCharts() {
        // Bind line chart and pie chart to service data (simulate updates)
        LineChart<Number, Number> lineChart = view.getLineChart();
        PieChart pieChart = view.getPieChart();
        new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    // Update line chart
                    lineChart.getData().clear();
                    var series = new javafx.scene.chart.XYChart.Series<Number, Number>();
                    for (int i = 1; i <= 5; i++) {
                        series.getData().add(new javafx.scene.chart.XYChart.Data<>(i, service.getStudentCount() + (int)(Math.random()*10)));
                    }
                    lineChart.getData().add(series);
                    // Update pie chart
                    pieChart.getData().clear();
                    pieChart.getData().addAll(
                        new PieChart.Data("Students", service.getStudentCount()),
                        new PieChart.Data("Teachers", service.getTeacherCount()),
                        new PieChart.Data("Private Teachers", service.getPrivateTeacherCount())
                    );
                });
                try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    // Actions for quick actions (add user/module)
    public void addUser(String name) {
        service.addNotification("User added: " + name);
        service.addRecentActivity("User added: " + name);
    }
    public void addModule(String title) {
        service.addNotification("Module added: " + title);
        service.addRecentActivity("Module added: " + title);
    }
} 