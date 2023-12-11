# ProyectoFinal

Clase: Dietitian

Atributos:
- dietitianId: String
- name: String
- specialty: String
- patients: List<Patient>
Constructores:
- Dietitian(String dietitianId, String name, String specialty)
Métodos:
- void addPatient(Patient patient)
- void showPatients()
- boolean deletePatient(String patientId)
- boolean modifyPatient(String patientId, Patient newPatientData)
// Métodos relacionados con DietPlan y Meal son llamadas a métodos en objetos Patient
// ... (implementar el resto de métodos según las necesidades)
Getters y Setters:
- String getDietitianId()
- void setDietitianId(String dietitianId)
- String getName()
- void setName(String name)
- String getSpecialty()
- void setSpecialty(String specialty)
- List<Patient> getPatients()
- void setPatients(List<Patient> patients)

Clase: DietPlan
Atributos:
- planId: String
- dailyCalories: int
- macronutrientDistribution: int
- specificRecommendations: String
- meals: List<Meal>
Constructores:
- DietPlan(String planId, int dailyCalories, int macronutrientDistribution, String specificRecommendations)
Métodos:
- void addMeal(Meal meal)
- void showMeals()
- boolean deleteMeal(String mealId)
- boolean modifyMeal(String mealId, Meal newMeal)
Getters y Setters:
- String getPlanId()
- void setPlanId(String planId)
- int getDailyCalories()
- void setDailyCalories(int dailyCalories)
- int getMacronutrientDistribution()
- void setMacronutrientDistribution(int macronutrientDistribution)
- String getSpecificRecommendations()
- void setSpecificRecommendations(String specificRecommendations)
- List<Meal> getMeals()
- void setMeals(List<Meal> meals)

Clase: Meal
Atributos:
- idMeal: String
- name: String
- macronutrients: double
- calories: double
- timeOfDay: String
Constructores:
- Meal(String idMeal, double calories, double macronutrients, String timeOfDay, String name)
Métodos:
- void printInfo()
Getters y Setters:
- String getIdMeal()
- void setIdMeal(String idMeal)
- double getCalories()
- void setCalories(double calories)
- double getMacronutrients()
- void setMacronutrients(double macronutrients)
- String getTimeOfDay()
- void setTimeOfDay(String timeOfDay)
- String getName()
- void setName(String name)

Clase: Patient
Atributos:
- patientId: String
- name: String
- age: int
- weight: double
- height: double
- preexistingConditions: String
- dietPlan: DietPlan
Constructores:
- Patient(String patientId, String name, int age, double weight, double height, String preexistingConditions)
Métodos:
- DietPlan getDietPlan()
- void setDietPlan(DietPlan dietPlan)
- void showDietPlan()
Getters y Setters:
- String getPatientId()
- void setPatientId(String patientId)
- String getName()
- void setName(String name)
- int getAge()
- void setAge(int age)
- double getWeight()
- void setWeight(double weight)
- double getHeight()
- void setHeight(double height)
- String getPreexistingConditions()
- void setPreexistingConditions(String preexistingConditions)
