package src;

import javax.swing.JOptionPane;
import modelos.Dietitian;
import modelos.Meal;
import modelos.Patient;
import modelos.DietPlan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<Dietitian> dietitians = new ArrayList<>();

    public static void main(String[] args) {
        String menu = "Seleccione una opción:\n" +
                      "1. Administrar Dietistas\n" +
                      "2. Administrar Pacientes\n" +
                      "3. Administrar Plan Dieta\n" +
                      "4. Administrar Meal\n" +
                      "5. Guardar Datos\n" +
                      "6. Cargar Datos\n" +
                      "7. Salir del Programa";

        boolean exit = false;
        while (!exit) {
            String option = JOptionPane.showInputDialog(null, menu, "Menú Principal", JOptionPane.PLAIN_MESSAGE);
            if (option != null) {
                switch (option) {
                    case "1":
                        manageDietitians();
                        break;
                    case "2":
                        managePatients();
                        break;
                    case "3":
                        manageDietPlans();
                        break;
                    case "4":
                        manageMeals();
                        break;
                    case "5":
                        saveData();
                        break;
                    case "6":
                        loadData();
                        break;
                    case "7":
                        exit = true;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opción no válida");
                }
            } else {
                exit = true;
            }
        }
    }

    private static void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data.csv"))) {
            String line = reader.readLine(); // Leer y descartar la línea de encabezado
    
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 18) continue; // Saltar líneas con formato incorrecto
    
                // Extraer y procesar datos del Dietitian y Patient
                String dietitianId = data[0];
                String dietitianName = data[1];
                String specialty = data[2];
                String patientId = data[3];
                String patientName = data[4];
                int age = !data[5].isEmpty() ? Integer.parseInt(data[5]) : 0;
                double weight = !data[6].isEmpty() ? Double.parseDouble(data[6]) : 0;
                double height = !data[7].isEmpty() ? Double.parseDouble(data[7]) : 0;
                String preexistingConditions = data[8];
    
                Dietitian dietitian = findOrCreateDietitian(dietitianId, dietitianName, specialty);
                Patient patient = findOrCreatePatient(patientId, patientName, age, weight, height, preexistingConditions, dietitian);
    
                // Procesar datos del DietPlan y Meal, si están presentes
                if (!data[9].isEmpty()) {
                    String dietPlanId = data[9];
                    int dailyCalories = Integer.parseInt(data[10]);
                    int macronutrientDistribution = Integer.parseInt(data[11]);
                    String specificRecommendations = data[12];
    
                    DietPlan dietPlan = findOrCreateDietPlan(dietPlanId, dailyCalories, macronutrientDistribution, specificRecommendations, patient);
                    
                    if (!data[13].isEmpty()) {
                        String mealId = data[13];
                        String mealName = data[14];
                        double calories = Double.parseDouble(data[15]);
                        double macronutrients = Double.parseDouble(data[16]);
                        String timeOfDay = data[17];
    
                        Meal meal = new Meal(mealId, calories, macronutrients, timeOfDay, mealName);
                        dietPlan.addMeal(meal);
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Datos cargados exitosamente desde data.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al convertir los números: " + e.getMessage());
        }
    }
    

    private static Dietitian findOrCreateDietitian(String dietitianId, String name, String specialty) {
        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                return dietitian;
            }
        }
        Dietitian newDietitian = new Dietitian(dietitianId, name, specialty);
        dietitians.add(newDietitian);
        return newDietitian;
    }
    
    private static Patient findOrCreatePatient(String patientId, String name, int age, double weight, double height, String preexistingConditions, Dietitian dietitian) {
        for (Patient patient : dietitian.getPatients()) {
            if (patient.getPatientId().equals(patientId)) {
                return patient;
            }
        }
        Patient newPatient = new Patient(patientId, name, age, weight, height, preexistingConditions);
        dietitian.addPatient(newPatient);
        return newPatient;
    }
    
    private static DietPlan findOrCreateDietPlan(String planId, int dailyCalories, int macronutrientDistribution, String specificRecommendations, Patient patient) {
        DietPlan dietPlan = patient.getDietPlan();
        if (dietPlan != null && dietPlan.getPlanId().equals(planId)) {
            return dietPlan;
        }
        DietPlan newDietPlan = new DietPlan(planId, dailyCalories, macronutrientDistribution, specificRecommendations);
        patient.setDietPlan(newDietPlan);
        return newDietPlan;
    }


    private static void saveData() {
        try (FileWriter writer = new FileWriter("data.csv")) {
            writer.write("Dietitian ID, Dietitian Name, Specialty, Patient ID, Patient Name, Age, Weight, Height, Preexisting Conditions, Diet Plan ID, Daily Calories, Macronutrient Distribution, Specific Recommendations, Meal ID, Meal Name, Calories, Macronutrients, Time of Day\n");

            for (Dietitian dietitian : dietitians) {
                for (Patient patient : dietitian.getPatients()) {
                    DietPlan dietPlan = patient.getDietPlan();
                    if (dietPlan == null) {
                        writer.write(String.join(",", 
                            dietitian.getDietitianId(), 
                            dietitian.getName(), 
                            dietitian.getSpecialty(), 
                            patient.getPatientId(), 
                            patient.getName(), 
                            String.valueOf(patient.getAge()), 
                            String.valueOf(patient.getWeight()), 
                            String.valueOf(patient.getHeight()), 
                            patient.getPreexistingConditions()) + ",,,,,,,\n"
                        );
                    } else {
                        for (Meal meal : dietPlan.getMeals()) {
                            writer.write(String.join(",", 
                                dietitian.getDietitianId(), 
                                dietitian.getName(), 
                                dietitian.getSpecialty(), 
                                patient.getPatientId(), 
                                patient.getName(), 
                                String.valueOf(patient.getAge()), 
                                String.valueOf(patient.getWeight()), 
                                String.valueOf(patient.getHeight()), 
                                patient.getPreexistingConditions(), 
                                dietPlan.getPlanId(), 
                                String.valueOf(dietPlan.getDailyCalories()), 
                                String.valueOf(dietPlan.getMacronutrientDistribution()), 
                                dietPlan.getSpecificRecommendations(), 
                                meal.getIdMeal(), 
                                meal.getName(), 
                                String.valueOf(meal.getCalories()), 
                                String.valueOf(meal.getMacronutrients()), 
                                meal.getTimeOfDay()) + "\n"
                            );
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Datos guardados exitosamente.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + e.getMessage());
        }
    }

    private static void manageDietitians() {

         String menuDietPlans = "Seleccione una acción:\n"
                             + "1. Agregar un Dietista\n"
                             + "2. Eliminar un Dietista\n"
                             + "3. Modificar un Dietista\n"
                             + "4. Ver informacion de un Dietista\n"
                             + "5. Ver todos los Pacientes de un Dietista\n"
                             + "6. Regresar al menú principal";
        boolean back = false;
        while (!back) {
            String dietitianOption = JOptionPane.showInputDialog(null, menuDietPlans, "Administrar Dietistas",
                    JOptionPane.PLAIN_MESSAGE);
            if (dietitianOption != null) {
                switch (dietitianOption) {
                    case "1":
                        addDietitian();
                        break;
                    case "2":
                        deleteDietitian();
                        break;
                    case "3":
                        modifyDietitian();
                        break;
                    case "4":
                        viewDietitian();
                        break;
                    case "5":
                        viewAllPatientsOfDietitian();
                        break;
                    case "6":
                        back = true;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Acción no válida");
                }
            } else {
                back = true;
            }
        }
    }

    private static void managePatients() {

           String menuDietPlans = "Seleccione una acción:\n"
                             + "1. Agregar un Paciente\n"
                             + "2. Eliminar un Paciente\n"
                             + "3. Modificar un Paciente\n"
                             + "4. Ver informacion de un Paciente\n"
                             + "5. Regresar al menú principal";
        boolean back = false;
        while (!back) {
            String patientOption = JOptionPane.showInputDialog(null, menuDietPlans, "Administrar Pacientes",
                    JOptionPane.PLAIN_MESSAGE);
            if (patientOption != null) {
                switch (patientOption) {
                    case "1":
                        addPatient();
                        break;
                    case "2":
                        deletePatient();
                        break;
                    case "3":
                        modifyPatient();
                        break;
                    case "4":
                        viewPatient();
                        break;
                    case "5":
                        back = true;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Acción no válida");
                }
            } else {
                back = true;
            }
        }
        
    }

    private static void manageDietPlans() {
        String menuDietPlans = "Seleccione una acción:\n"
                             + "1. Agregar Plan de Dieta a un Paciente\n"
                             + "2. Modificar un Plan de Dieta\n"
                             + "3. Eliminar un Plan de Dieta\n"
                             + "4. Ver Plan de Dieta de un Paciente\n"
                             + "5. Regresar al menú principal";
    
        boolean back = false;
        while (!back) {
            String dietPlanOption = JOptionPane.showInputDialog(null, menuDietPlans, "Administrar Planes de Dieta",
                    JOptionPane.PLAIN_MESSAGE);
            if (dietPlanOption != null) {
                switch (dietPlanOption) {
                    case "1":
                        addDietPlanToPatient();
                        break;
                    case "2":
                        modifyDietPlan();
                        break;
                    case "3":
                        deleteDietPlan();
                        break;
                    case "4":
                        viewPlanOfPatient();
                        break;
                    case "5":
                        back = true;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Acción no válida");
                }
            } else {
                back = true;
            }
        }
    }

    private static void manageMeals() {
        String menuMeals = "Seleccione una acción:\n"
                         + "1. Agregar Meal\n"
                         + "2. Eliminar Meal\n"
                         + "3. Actualizar Meal\n"
                         + "4. Ver Meal\n"
                         + "5. Regresar al menú principal";
    
        boolean back = false;
        while (!back) {
            String mealOption = JOptionPane.showInputDialog(null, menuMeals, "Administrar Comidas",
                    JOptionPane.PLAIN_MESSAGE);
            if (mealOption != null) {
                switch (mealOption) {
                    case "1":
                        addMeal();
                        break;
                    case "2":
                        deleteMeal();
                        break;
                    case "3":
                        updateMeal();
                        break;
                    case "4":
                        viewMeal();
                        break;
                    case "5":
                        back = true;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Acción no válida");
                }
            } else {
                back = true;
            }
        }
    }

    private static void viewMeal() {
        String dietitianId = JOptionPane.showInputDialog("Ingrese el ID del nutricionista:");
        Dietitian selectedDietitian = null;
        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }
    
        if (selectedDietitian == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un nutricionista con el ID proporcionado.");
            return;
        }
    
        String patientId = JOptionPane.showInputDialog("Ingrese el ID del paciente:");
        Patient selectedPatient = null;
        for (Patient patient : selectedDietitian.getPatients()) {
            if (patient.getPatientId().equals(patientId)) {
                selectedPatient = patient;
                break;
            }
        }
    
        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un paciente con el ID proporcionado.");
            return;
        }
    
        DietPlan dietPlan = selectedPatient.getDietPlan();
        if (dietPlan == null) {
            JOptionPane.showMessageDialog(null, "Este paciente no tiene un plan de dieta asignado.");
            return;
        }
    
        String idMealToView = JOptionPane.showInputDialog("Ingrese el ID de la comida que desea ver:");
        Meal mealToView = null;
        for (Meal meal : dietPlan.getMeals()) {
            if (meal.getIdMeal().equals(idMealToView)) {
                mealToView = meal;
                break;
            }
        }
    
        if (mealToView == null) {
            JOptionPane.showMessageDialog(null, "No se encontró una comida con el ID proporcionado.");
            return;
        }
    
        // Mostrar los detalles de la comida
        String mealDetails = "ID de la Comida: " + mealToView.getIdMeal() +
                             "\nNombre: " + mealToView.getName() +
                             "\nCalorías: " + mealToView.getCalories() +
                             "\nMacronutrientes: " + mealToView.getMacronutrients() +
                             "\nHora del Día: " + mealToView.getTimeOfDay();
        JOptionPane.showMessageDialog(null, mealDetails);
    }
    

    private static void updateMeal() {
        String dietitianId = JOptionPane.showInputDialog("Ingrese el ID del nutricionista:");
        Dietitian selectedDietitian = null;
        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }
    
        if (selectedDietitian == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un nutricionista con el ID proporcionado.");
            return;
        }
    
        String patientId = JOptionPane.showInputDialog("Ingrese el ID del paciente:");
        Patient selectedPatient = null;
        for (Patient patient : selectedDietitian.getPatients()) {
            if (patient.getPatientId().equals(patientId)) {
                selectedPatient = patient;
                break;
            }
        }
    
        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un paciente con el ID proporcionado.");
            return;
        }
    
        DietPlan dietPlan = selectedPatient.getDietPlan();
        if (dietPlan == null) {
            JOptionPane.showMessageDialog(null, "Este paciente no tiene un plan de dieta asignado.");
            return;
        }
    
        String idMealToUpdate = JOptionPane.showInputDialog("Ingrese el ID de la comida que desea actualizar:");
        Meal mealToUpdate = null;
        for (Meal meal : dietPlan.getMeals()) {
            if (meal.getIdMeal().equals(idMealToUpdate)) {
                mealToUpdate = meal;
                break;
            }
        }
    
        if (mealToUpdate == null) {
            JOptionPane.showMessageDialog(null, "No se encontró una comida con el ID proporcionado.");
            return;
        }
    
        // Recolectar nueva información para la comida
        String newMealName = JOptionPane.showInputDialog("Ingrese el nuevo nombre de la comida:", mealToUpdate.getName());
        double newCalories = Double.parseDouble(JOptionPane.showInputDialog("Ingrese las nuevas calorías de la comida:", mealToUpdate.getCalories()));
        double newMacronutrients = Double.parseDouble(JOptionPane.showInputDialog("Ingrese los nuevos macronutrientes de la comida:", mealToUpdate.getMacronutrients()));
        String newTimeOfDay = JOptionPane.showInputDialog("Ingrese la nueva hora del día para la comida:", mealToUpdate.getTimeOfDay());
    
        // Actualizar la comida
        mealToUpdate.setName(newMealName);
        mealToUpdate.setCalories(newCalories);
        mealToUpdate.setMacronutrients(newMacronutrients);
        mealToUpdate.setTimeOfDay(newTimeOfDay);
    
        JOptionPane.showMessageDialog(null, "Comida actualizada en el plan de dieta.");
    }
    

    private static void deleteMeal() {
        String dietitianId = JOptionPane.showInputDialog("Ingrese el ID del nutricionista:");
        Dietitian selectedDietitian = null;
        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }
    
        if (selectedDietitian == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un nutricionista con el ID proporcionado.");
            return;
        }
    
        String patientId = JOptionPane.showInputDialog("Ingrese el ID del paciente:");
        Patient selectedPatient = null;
        for (Patient patient : selectedDietitian.getPatients()) {
            if (patient.getPatientId().equals(patientId)) {
                selectedPatient = patient;
                break;
            }
        }
    
        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un paciente con el ID proporcionado.");
            return;
        }
    
        DietPlan dietPlan = selectedPatient.getDietPlan();
        if (dietPlan == null) {
            JOptionPane.showMessageDialog(null, "Este paciente no tiene un plan de dieta asignado.");
            return;
        }
    
        // Pedir al usuario que ingrese el ID de la comida a eliminar
        String idMealToDelete = JOptionPane.showInputDialog("Ingrese el ID de la comida que desea eliminar:");
        boolean isDeleted = dietPlan.deleteMeal(idMealToDelete);
    
        if (isDeleted) {
            JOptionPane.showMessageDialog(null, "Comida eliminada del plan de dieta.");
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró una comida con el ID proporcionado en el plan de dieta.");
        }
    }
    

    private static void addMeal() {
        String dietitianId = JOptionPane.showInputDialog("Ingrese el ID del nutricionista:");
        Dietitian selectedDietitian = null;
        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }
    
        if (selectedDietitian == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un nutricionista con el ID proporcionado.");
            return;
        }
    
        String patientId = JOptionPane.showInputDialog("Ingrese el ID del paciente:");
        Patient selectedPatient = null;
        for (Patient patient : selectedDietitian.getPatients()) {
            if (patient.getPatientId().equals(patientId)) {
                selectedPatient = patient;
                break;
            }
        }
    
        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un paciente con el ID proporcionado.");
            return;
        }
    
        DietPlan dietPlan = selectedPatient.getDietPlan();
        if (dietPlan == null) {
            JOptionPane.showMessageDialog(null, "Este paciente no tiene un plan de dieta asignado.");
            return;
        }
    
        // Recolectar información detallada de la nueva comida
        String idMeal = JOptionPane.showInputDialog("Ingrese el ID de la comida:");
        String mealName = JOptionPane.showInputDialog("Ingrese el nombre de la comida:");
        double calories = Double.parseDouble(JOptionPane.showInputDialog("Ingrese las calorías de la comida:"));
        double macronutrients = Double.parseDouble(JOptionPane.showInputDialog("Ingrese los macronutrientes de la comida:"));
        String timeOfDay = JOptionPane.showInputDialog("Ingrese la hora del día para la comida (ej. desayuno, almuerzo, cena):");
    
        // Crear y añadir la nueva comida
        Meal newMeal = new Meal(idMeal, calories, macronutrients, timeOfDay, mealName);
        dietPlan.addMeal(newMeal);
    
        JOptionPane.showMessageDialog(null, "Comida agregada al plan de dieta.");
    }
    
    

    private static void deleteDietPlan() {
        String dietitianId = JOptionPane.showInputDialog("Ingrese el ID del nutricionista:");
        Dietitian selectedDietitian = null;
        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }
    
        if (selectedDietitian == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un nutricionista con el ID proporcionado.");
            return;
        }
    
        String patientId = JOptionPane.showInputDialog("Ingrese el ID del paciente:");
        Patient selectedPatient = null;
        for (Patient patient : selectedDietitian.getPatients()) {
            if (patient.getPatientId().equals(patientId)) {
                selectedPatient = patient;
                break;
            }
        }
    
        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un paciente con el ID proporcionado.");
            return;
        }
    
        DietPlan currentDietPlan = selectedPatient.getDietPlan();
        if (currentDietPlan == null) {
            JOptionPane.showMessageDialog(null, "Este paciente no tiene un plan de dieta asignado.");
            return;
        }
    
        // Confirmar la eliminación del plan de dieta
        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar el plan de dieta?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            selectedPatient.setDietPlan(null); // Eliminar el plan de dieta
            JOptionPane.showMessageDialog(null, "Plan de dieta eliminado exitosamente.");
        }
    }
    

    private static void modifyDietPlan() {
        String dietitianId = JOptionPane.showInputDialog("Ingrese el ID del nutricionista:");
        Dietitian selectedDietitian = null;
        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }
    
        if (selectedDietitian == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un nutricionista con el ID proporcionado.");
            return;
        }
    
        String patientId = JOptionPane.showInputDialog("Ingrese el ID del paciente:");
        Patient selectedPatient = null;
        for (Patient patient : selectedDietitian.getPatients()) {
            if (patient.getPatientId().equals(patientId)) {
                selectedPatient = patient;
                break;
            }
        }
    
        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un paciente con el ID proporcionado.");
            return;
        }
    
        DietPlan currentDietPlan = selectedPatient.getDietPlan();
        if (currentDietPlan == null) {
            JOptionPane.showMessageDialog(null, "Este paciente no tiene un plan de dieta asignado.");
            return;
        }
    
        // Recolectar nueva información para el plan de dieta
        String planId = JOptionPane.showInputDialog("Ingrese el ID del plan de dieta:", currentDietPlan.getPlanId());
        int dailyCalories = Integer.parseInt(JOptionPane.showInputDialog("Ingrese las calorías diarias:", currentDietPlan.getDailyCalories()));
        int macroDistribution = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la distribución de macronutrientes:", currentDietPlan.getMacronutrientDistribution()));
        String specificRecommendations = JOptionPane.showInputDialog("Ingrese recomendaciones específicas:", currentDietPlan.getSpecificRecommendations());
    
        // Actualizar el plan de dieta
        currentDietPlan.setPlanId(planId);
        currentDietPlan.setDailyCalories(dailyCalories);
        currentDietPlan.setMacronutrientDistribution(macroDistribution);
        currentDietPlan.setSpecificRecommendations(specificRecommendations);
    
        JOptionPane.showMessageDialog(null, "Plan de dieta modificado exitosamente para el paciente.");
    }
    


    private static void viewPlanOfPatient() {
        String dietitianId = JOptionPane.showInputDialog("Ingrese el ID del nutricionista:");
        Dietitian selectedDietitian = null;
        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }
    
        if (selectedDietitian == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un nutricionista con el ID proporcionado.");
            return;
        }
    
        String patientId = JOptionPane.showInputDialog("Ingrese el ID del paciente:");
        Patient selectedPatient = null;
        for (Patient patient : selectedDietitian.getPatients()) {
            if (patient.getPatientId().equals(patientId)) {
                selectedPatient = patient;
                break;
            }
        }
    
        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un paciente con el ID proporcionado.");
            return;
        }
    
        DietPlan patientDietPlan = selectedPatient.getDietPlan();
        if (patientDietPlan != null) {
            String dietPlanInfo = "ID del Plan: " + patientDietPlan.getPlanId() +
                                  "\nCalorías Diarias: " + patientDietPlan.getDailyCalories() +
                                  "\nDistribución de Macronutrientes: " + patientDietPlan.getMacronutrientDistribution() +
                                  "\nRecomendaciones Específicas: " + patientDietPlan.getSpecificRecommendations();
            JOptionPane.showMessageDialog(null, "Plan de Dieta:\n" + dietPlanInfo);
        } else {
            JOptionPane.showMessageDialog(null, "Este paciente no tiene un plan de dieta asignado.");
        }
    }
    
    
    
    private static void addDietPlanToPatient() {
        String dietitianId = JOptionPane.showInputDialog("Ingrese el ID del nutricionista:");
        Dietitian selectedDietitian = null;
        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }
    
        if (selectedDietitian == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un nutricionista con el ID proporcionado.");
            return;
        }
    
        String patientId = JOptionPane.showInputDialog("Ingrese el ID del paciente:");
        Patient selectedPatient = null;
        for (Patient patient : selectedDietitian.getPatients()) {
            if (patient.getPatientId().equals(patientId)) {
                selectedPatient = patient;
                break;
            }
        }
    
        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un paciente con el ID proporcionado.");
            return;
        }
    
        // Recolectar información del plan de dieta
        String planId = JOptionPane.showInputDialog("Ingrese el ID del plan de dieta:");
        int dailyCalories = Integer.parseInt(JOptionPane.showInputDialog("Ingrese las calorías diarias:"));
        int macronutrientDistribution = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la distribución de macronutrientes:"));
        String specificRecommendations = JOptionPane.showInputDialog("Ingrese recomendaciones específicas:");
    
        // Crear y asignar el nuevo plan de dieta
        DietPlan newDietPlan = new DietPlan(planId, dailyCalories, macronutrientDistribution, specificRecommendations);
        selectedPatient.setDietPlan(newDietPlan);
    
        JOptionPane.showMessageDialog(null, "Plan de dieta agregado al paciente.");
    }

    

    



    private static void viewPatient() {
        String dietitianId = JOptionPane.showInputDialog(null, "Ingrese el ID del Dietista:", "Ver Paciente", JOptionPane.QUESTION_MESSAGE);
        Dietitian selectedDietitian = null;
    
        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }
    
        if (selectedDietitian != null) {
            String patientId = JOptionPane.showInputDialog(null, "Ingrese el ID del Paciente:", "Ver Paciente", JOptionPane.QUESTION_MESSAGE);
            Patient patientToView = null;
            for (Patient patient : selectedDietitian.getPatients()) {
                if (patient.getPatientId().equals(patientId)) {
                    patientToView = patient;
                    break;
                }
            }
    
            if (patientToView != null) {
                String patientInfo = "ID: " + patientToView.getPatientId() +
                                     "\nNombre: " + patientToView.getName() +
                                     "\nEdad: " + patientToView.getAge() +
                                     "\nPeso: " + patientToView.getWeight() +
                                     "\nAltura: " + patientToView.getHeight() +
                                     "\nCondiciones Preexistentes: " + patientToView.getPreexistingConditions();
                JOptionPane.showMessageDialog(null, patientInfo, "Información del Paciente", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un Paciente con el ID proporcionado.", "Ver Paciente", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un Dietista con el ID proporcionado.", "Ver Paciente", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private static void modifyPatient() {
        String dietitianId = JOptionPane.showInputDialog(null, "Ingrese el ID del Dietista del paciente a modificar:", "Modificar Paciente", JOptionPane.QUESTION_MESSAGE);
        Dietitian selectedDietitian = null;
    
        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }
    
        if (selectedDietitian != null) {
            String patientId = JOptionPane.showInputDialog(null, "Ingrese el ID del Paciente a modificar:", "Modificar Paciente", JOptionPane.QUESTION_MESSAGE);
            Patient patientToModify = null;
            for (Patient patient : selectedDietitian.getPatients()) {
                if (patient.getPatientId().equals(patientId)) {
                    patientToModify = patient;
                    break;
                }
            }
    
            if (patientToModify != null) {
                String newName = (String) JOptionPane.showInputDialog(null, "Ingrese el nuevo nombre del Paciente:", "Modificar Paciente", JOptionPane.QUESTION_MESSAGE, null, null, patientToModify.getName());
                int newAge = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Ingrese la nueva edad del Paciente:", "Modificar Paciente", JOptionPane.QUESTION_MESSAGE, null, null, Integer.toString(patientToModify.getAge())));
                double newWeight = Double.parseDouble((String) JOptionPane.showInputDialog(null, "Ingrese el nuevo peso del Paciente (kg):", "Modificar Paciente", JOptionPane.QUESTION_MESSAGE, null, null, Double.toString(patientToModify.getWeight())));
                double newHeight = Double.parseDouble((String) JOptionPane.showInputDialog(null, "Ingrese la nueva altura del Paciente (m):", "Modificar Paciente", JOptionPane.QUESTION_MESSAGE, null, null, Double.toString(patientToModify.getHeight())));
                String newConditions = (String) JOptionPane.showInputDialog(null, "Ingrese las nuevas condiciones preexistentes (si las hay):", "Modificar Paciente", JOptionPane.QUESTION_MESSAGE, null, null, patientToModify.getPreexistingConditions());
    
                patientToModify.setName(newName);
                patientToModify.setAge(newAge);
                patientToModify.setWeight(newWeight);
                patientToModify.setHeight(newHeight);
                patientToModify.setPreexistingConditions(newConditions);
    
                JOptionPane.showMessageDialog(null, "Paciente modificado exitosamente.", "Modificar Paciente", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un Paciente con el ID proporcionado en este Dietista.", "Modificar Paciente", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un Dietista con el ID proporcionado.", "Modificar Paciente", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    
    

    private static void deletePatient() {
        String dietitianId = JOptionPane.showInputDialog(null, "Ingrese el ID del Dietista del paciente a eliminar:",
                "Eliminar Paciente", JOptionPane.QUESTION_MESSAGE);
        Dietitian selectedDietitian = null;

        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }

        if (selectedDietitian == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un Dietista con el ID proporcionado.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String patientId = JOptionPane.showInputDialog(null, "Ingrese el ID del Paciente a eliminar:",
                "Eliminar Paciente", JOptionPane.QUESTION_MESSAGE);
        boolean removed = selectedDietitian.getPatients().removeIf(patient -> patient.getPatientId().equals(patientId));

        if (removed) {
            JOptionPane.showMessageDialog(null, "Paciente eliminado exitosamente.", "Eliminar Paciente",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un Paciente con el ID proporcionado en este Dietista.",
                    "Eliminar Paciente", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void addPatient() {
        String dietitianId = JOptionPane.showInputDialog(null,
                "Ingrese el ID del Dietista al que se asignará el Paciente:", "Agregar Paciente",
                JOptionPane.QUESTION_MESSAGE);
        Dietitian selectedDietitian = null;

        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }

        if (selectedDietitian == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un Dietista con el ID proporcionado.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String patientId = JOptionPane.showInputDialog(null, "Ingrese el ID del Paciente:", "Agregar Paciente",
                JOptionPane.QUESTION_MESSAGE);
        String name = JOptionPane.showInputDialog(null, "Ingrese el nombre del Paciente:", "Agregar Paciente",
                JOptionPane.QUESTION_MESSAGE);
        int age = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese la edad del Paciente:",
                "Agregar Paciente", JOptionPane.QUESTION_MESSAGE));
        double weight = Double.parseDouble(JOptionPane.showInputDialog(null, "Ingrese el peso del Paciente (kg):",
                "Agregar Paciente", JOptionPane.QUESTION_MESSAGE));
        double height = Double.parseDouble(JOptionPane.showInputDialog(null, "Ingrese la altura del Paciente (m):",
                "Agregar Paciente", JOptionPane.QUESTION_MESSAGE));
        String preexistingConditions = JOptionPane.showInputDialog(null,
                "Ingrese las condiciones preexistentes (si las hay):", "Agregar Paciente",
                JOptionPane.QUESTION_MESSAGE);

        Patient newPatient = new Patient(patientId, name, age, weight, height, preexistingConditions);
        selectedDietitian.addPatient(newPatient);
        JOptionPane.showMessageDialog(null, "Paciente agregado exitosamente al Dietista!", "Agregar Paciente",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static void viewAllPatientsOfDietitian() {
        String dietitianId = JOptionPane.showInputDialog(null, "Ingrese el ID del Dietista para ver sus pacientes:", "Ver Pacientes de Dietista", JOptionPane.QUESTION_MESSAGE);
        Dietitian selectedDietitian = null;
    
        for (Dietitian dietitian : dietitians) {
            if (dietitian.getDietitianId().equals(dietitianId)) {
                selectedDietitian = dietitian;
                break;
            }
        }
    
        if (selectedDietitian != null) {
            if (selectedDietitian.getPatients().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Este Dietista no tiene pacientes asignados.", "Ver Pacientes de Dietista", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder patientsInfo = new StringBuilder("Pacientes del Dietista " + selectedDietitian.getName() + ":\n");
                for (Patient patient : selectedDietitian.getPatients()) {
                    patientsInfo.append("ID: ").append(patient.getPatientId())
                                .append(", Nombre: ").append(patient.getName())
                                .append(", Edad: ").append(patient.getAge())
                                .append("\n");
                }
                JOptionPane.showMessageDialog(null, patientsInfo.toString(), "Pacientes de Dietista", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un Dietista con el ID proporcionado.", "Ver Pacientes de Dietista", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private static void addDietitian() {
        String dietitianId = JOptionPane.showInputDialog(null, "Ingrese el ID del Dietista:", "Agregar Dietista",
                JOptionPane.QUESTION_MESSAGE);
        String name = JOptionPane.showInputDialog(null, "Ingrese el nombre del Dietista:", "Agregar Dietista",
                JOptionPane.QUESTION_MESSAGE);
        String specialty = JOptionPane.showInputDialog(null, "Ingrese la especialidad del Dietista:",
                "Agregar Dietista", JOptionPane.QUESTION_MESSAGE);

        // Verificar que la información es completa
        if (dietitianId != null && name != null && specialty != null && !dietitianId.trim().isEmpty()
                && !name.trim().isEmpty() && !specialty.trim().isEmpty()) {
            // Crear la instancia del dietista y agregarla a la lista
            Dietitian newDietitian = new Dietitian(dietitianId, name, specialty);
            dietitians.add(newDietitian);
            JOptionPane.showMessageDialog(null, "Dietista agregado exitosamente!", "Agregar Dietista",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Debe completar todos los campos para agregar un dietista.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void deleteDietitian() {
        String dietitianId = JOptionPane.showInputDialog(null, "Ingrese el ID del Dietista a eliminar:",
                "Eliminar Dietista", JOptionPane.QUESTION_MESSAGE);
        if (dietitianId != null && !dietitianId.trim().isEmpty()) {
            boolean found = false;
            for (Dietitian dietitian : dietitians) {
                if (dietitian.getDietitianId().equals(dietitianId.trim())) {
                    dietitians.remove(dietitian);
                    JOptionPane.showMessageDialog(null, "Dietista eliminado exitosamente.", "Eliminar Dietista",
                            JOptionPane.INFORMATION_MESSAGE);
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(null, "No se encontró un Dietista con el ID proporcionado.",
                        "Eliminar Dietista", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se ingresó un ID válido.", "Eliminar Dietista",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void viewDietitian() {
        String dietitianId = JOptionPane.showInputDialog(null, "Ingrese el ID del Dietista a ver:", "Ver Dietista",
                JOptionPane.QUESTION_MESSAGE);
        if (dietitianId != null && !dietitianId.trim().isEmpty()) {
            boolean found = false;
            for (Dietitian dietitian : dietitians) {
                if (dietitian.getDietitianId().equals(dietitianId.trim())) {
                    String info = "ID: " + dietitian.getDietitianId() + "\nNombre: " + dietitian.getName()
                            + "\nEspecialidad: " + dietitian.getSpecialty();
                    JOptionPane.showMessageDialog(null, info, "Información del Dietista",
                            JOptionPane.INFORMATION_MESSAGE);
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(null, "No se encontró un Dietista con el ID proporcionado.",
                        "Ver Dietista", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se ingresó un ID válido.", "Ver Dietista",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void modifyDietitian() {
        String dietitianId = JOptionPane.showInputDialog(null, "Ingrese el ID del Dietista a modificar:",
                "Modificar Dietista", JOptionPane.QUESTION_MESSAGE);
        if (dietitianId != null && !dietitianId.trim().isEmpty()) {
            Dietitian dietitianToModify = null;
            for (Dietitian dietitian : dietitians) {
                if (dietitian.getDietitianId().equals(dietitianId.trim())) {
                    dietitianToModify = dietitian;
                    break;
                }
            }

            if (dietitianToModify != null) {
                String newName = JOptionPane.showInputDialog(null, "Ingrese el nuevo nombre del Dietista:",
                        "Modificar Dietista", JOptionPane.QUESTION_MESSAGE, null, null, dietitianToModify.getName())
                        .toString();
                String newSpecialty = JOptionPane
                        .showInputDialog(null, "Ingrese la nueva especialidad del Dietista:", "Modificar Dietista",
                                JOptionPane.QUESTION_MESSAGE, null, null, dietitianToModify.getSpecialty())
                        .toString();

                dietitianToModify.setName(newName);
                dietitianToModify.setSpecialty(newSpecialty);
                JOptionPane.showMessageDialog(null, "Dietista modificado exitosamente.", "Modificar Dietista",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un Dietista con el ID proporcionado.",
                        "Modificar Dietista", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se ingresó un ID válido.", "Modificar Dietista",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}