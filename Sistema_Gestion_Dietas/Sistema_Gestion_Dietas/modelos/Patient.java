package modelos;

public class Patient {
    private String patientId;
    private String name;
    private int age;
    private double weight;
    private double height;
    private String preexistingConditions;
    private DietPlan dietPlan; // Un único plan de dieta

    public Patient(String patientId, String name, int age, double weight, double height, String preexistingConditions) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.preexistingConditions = preexistingConditions;
        this.dietPlan = null; // Inicialmente no hay plan de dieta
    }

    // Getters y setters para los atributos del paciente
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getPreexistingConditions() {
        return preexistingConditions;
    }

    public void setPreexistingConditions(String preexistingConditions) {
        this.preexistingConditions = preexistingConditions;
    }

    // Métodos relacionados con DietPlan
    public DietPlan getDietPlan() {
        return dietPlan;
    }

    public void setDietPlan(DietPlan dietPlan) {
        this.dietPlan = dietPlan;
    }

    public void showDietPlan() {
        if (dietPlan != null) {
            // Suponiendo que DietPlan tenga un método para imprimir su información
            dietPlan.showMeals();
        } else {
            System.out.println("No hay un plan de dieta asignado.");
        }
    }
}
