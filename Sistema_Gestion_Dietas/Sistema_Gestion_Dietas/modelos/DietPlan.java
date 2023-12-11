package modelos;

import java.util.ArrayList;
import java.util.List;

public class DietPlan {
    private String planId;
    private int dailyCalories;
    private int macronutrientDistribution;
    private String specificRecommendations;
    private List<Meal> meals; // Lista para la relación con Meal

    public DietPlan(String planId, int dailyCalories, int macronutrientDistribution, String specificRecommendations) {
        this.planId = planId;
        this.dailyCalories = dailyCalories;
        this.macronutrientDistribution = macronutrientDistribution;
        this.specificRecommendations = specificRecommendations;
        this.meals = new ArrayList<>(); // Inicializa la lista de comidas
    }

    // Métodos de la clase DietPlan
    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public void showMeals() {
        for (Meal meal : meals) {
            meal.printInfo();
        }
    }

    public boolean deleteMeal(String mealId) {
        return meals.removeIf(meal -> meal.getIdMeal().equals(mealId));
    }

    public boolean modifyMeal(String mealId, Meal newMeal) {
        int mealIndex = -1;
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getIdMeal().equals(mealId)) {
                mealIndex = i;
                break;
            }
        }
        if (mealIndex != -1) {
            meals.set(mealIndex, newMeal);
            return true;
        }
        return false;
    }

    // Getters y Setters
    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public int getDailyCalories() {
        return dailyCalories;
    }

    public void setDailyCalories(int dailyCalories) {
        this.dailyCalories = dailyCalories;
    }

    public int getMacronutrientDistribution() {
        return macronutrientDistribution;
    }

    public void setMacronutrientDistribution(int macronutrientDistribution) {
        this.macronutrientDistribution = macronutrientDistribution;
    }

    public String getSpecificRecommendations() {
        return specificRecommendations;
    }

    public void setSpecificRecommendations(String specificRecommendations) {
        this.specificRecommendations = specificRecommendations;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
