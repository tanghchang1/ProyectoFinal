package modelos;

import java.util.ArrayList;
import java.util.List;

public class Dietitian {
    private String dietitianId;
    private String name;
    private String specialty;
    private List<Patient> patients; // Lista para la relación con Patient

    public Dietitian(String dietitianId, String name, String specialty) {
        this.dietitianId = dietitianId;
        this.name = name;
        this.specialty = specialty;
        this.patients = new ArrayList<>(); // Inicializa la lista de pacientes
    }

    // Métodos de la clase Dietitian
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void showPatients() {
        for (Patient patient : patients) {
            patient.showDietPlan();
        }
    }

    public boolean deletePatient(String patientId) {
        return patients.removeIf(patient -> patient.getPatientId().equals(patientId));
    }

    public boolean modifyPatient(String patientId, Patient newPatientData) {
        int patientIndex = -1;
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatientId().equals(patientId)) {
                patientIndex = i;
                break;
            }
        }
        if (patientIndex != -1) {
            patients.set(patientIndex, newPatientData);
            return true;
        }
        return false;
    }

    // Métodos para manejar DietPlan y Meal son llamadas a métodos en objetos Patient
    // ... (implementar el resto de métodos según las necesidades)

    // Getters y Setters
    public String getDietitianId() {
        return dietitianId;
    }

    public void setDietitianId(String dietitianId) {
        this.dietitianId = dietitianId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}
