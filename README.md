# ProyectoFinal

Clase: Dietista

Atributos:

ID de dietista: cadena
nombre: cadena
especialidad: cuerda
pacientes: Lista Constructores:
Dietista(String dietitianId, String nombre, String especialidad) Métodos:
void addPatient(Paciente paciente)
mostrar pacientes vacíos ()
booleano eliminar paciente (cadena ID de paciente)
boolean modificarPatient(String pacienteId, Paciente newPatientData) // Métodos relacionados con DietPlan y Meal son llamadas a métodos en objetos Patient // ... (implementar el resto de métodos según las necesidades) Getters y Setters:
Cadena getDietitianId()
void setDietitianId(String dietistaId)
Cadena getName()
void setName (nombre de cadena)
Cadena getEspecialidad()
void setSpecialty (especialidad de cadena)
Lista obtenerPacientes()
void setPatients(Listar pacientes)
Clase: DietPlan Atributos:

ID del plan: cadena
Calorías diarias: int
Distribución de macronutrientes: int
Recomendaciones específicas: Cadena
comidas: Lista Constructores:
Plan de dieta (ID del plan de cadena, int calorías diarias, distribución de macronutrientes int, recomendaciones específicas de cadena) Métodos:
void addMeal(Comida de comida)
anular mostrarComidas()
booleano eliminar comida (cadena Id. de comida)
Modificación booleana de comida (String foodId, comida nueva comida) Getters y Setters:
Cadena getPlanId()
vacío setPlanId (cadena planId)
int getDailyCalories()
conjunto vacío Calorías diarias (int calorías diarias)
int getDistribución de macronutrientes()
conjunto vacío Distribución de macronutrientes (int distribución de macronutrientes)
Cadena getRecomendaciones Específicas()
void setSpecificRecommendations (Recomendaciones específicas de cadena)
Lista getMeals()
void setMeals(Lista de comidas)
Clase: Atributos de las comidas:

idComida: Cadena
nombre: cadena
macronutrientes: doble
calorías: doble
timeOfDay: Constructores de cadenas:
Comida(String idMeal, doble de calorías, doble de macronutrientes, String timeOfDay, String name) Métodos:
void printInfo() Getters y Setters:
Cadena getIdMeal()
void setIdMeal(String idMeal)
doble getCalorías()
conjunto vacío Calorías (calorías dobles)
doble getMacronutrientes()
conjunto vacíoMacronutrientes(macronutrientes dobles)
Cadena getTimeOfDay()
void setTimeOfDay (cadena hora del día)
Cadena getName()
void setName (nombre de cadena)
Clase: Atributos del Paciente:

ID del paciente: cadena
nombre: cadena
edad: int
peso:doble
altura: doble
Condiciones preexistentes: cadena
dietPlan: DietPlan Constructores:
Paciente(Cadena ID de paciente, Nombre de cadena, Edad int, Peso doble, Altura doble, Condiciones preexistentes de cadena) Métodos:
Plan de dieta obtener Plan de dieta()
conjunto vacíoDietPlan(DietPlan dietPlan)
void showDietPlan() Getters y Setters:
Cadena getPatientId()
void setPatientId(String pacienteId)
Cadena getName()
void setName (nombre de cadena)
int getEdad()
conjunto vacío Edad (int edad)
doble obtenerPeso()
conjunto vacíoPeso(doble peso)
doble obtener altura()
conjunto vacíoAltura(doble altura)
Cadena getCondicionesPreexistentes()
void setPreexistingConditions(Cadena condiciones preexistentes)
