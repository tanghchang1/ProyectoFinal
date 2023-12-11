package modelos;

public class Meal {
    private String idMeal;
    private String name;
    private double macronutrients;
    private double calories;
    private String timeOfDay; // Cambiado a String

    public Meal(String idMeal, double calories, double macronutrients, String timeOfDay, String name) {
        this.idMeal = idMeal;
        this.name = name;
        this.macronutrients = macronutrients;
        this.timeOfDay = timeOfDay;
        this.calories = calories;
    }

    public void printInfo() {
        System.out.println("ID de la comida: " + idMeal);
        System.out.println("Nombre: " + name);
        System.out.println("Macronutrientes: " + macronutrients);
        System.out.println("Hora del día: " + timeOfDay);
        System.out.println("Calorías: " + calories);
    }

    // Getters y Setters
    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getMacronutrients() {
        return macronutrients;
    }

    public void setMacronutrients(double macronutrients) {
        this.macronutrients = macronutrients;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

 
}
