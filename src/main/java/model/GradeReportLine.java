/**
 * 
 */
package model;

import java.util.ArrayList;

import model.Activity;
import model.GradeReportLine;

/**
 * @author oscar
 *
 */
public class GradeReportLine {

	private int id;
	private String name;
	private int level;
	private String grade;
	private float percentage;
	private float weight;
	private String rangeMin;
	private String rangeMax;
	private boolean type; // False = Category, True = Item
	private String typeName;
	private ArrayList<GradeReportLine> children;
	private Activity activity;

	/**
	 * Constructor GRL con 4 par�metros. Se le a�aden a posteriori los elementos
	 * de su suma de calificaciones
	 * 
	 * @param id
	 *            id de la categor�a
	 * @param name
	 *            nombre de la categor�a
	 * @param level
	 *            nivel de profundidad
	 * @param type
	 *            categor�a o item
	 */
	public GradeReportLine(int id, String name, int level, boolean type) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.type = type;
		this.children = new ArrayList<GradeReportLine>();
	}

	/**
	 * Constructor de GRL con 7 par�metros.
	 * 
	 * @param id
	 *            id de la categor�a
	 * @param name
	 *            nombre de la categor�a
	 * @param level
	 *            nivel de profundidad
	 * @param type
	 *            categor�a o item
	 * @param weight
	 *            peso
	 * @param rangeMin
	 *            rango m�nimo de calificaci�n
	 * @param rangeMax
	 *            rango m�ximo de calificaci�n
	 */
	public GradeReportLine(int id, String name, int level, boolean type, float weight, String rangeMin, String rangeMax,
			String grade, float percentage, String nameType) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.weight = weight;
		this.type = type;
		this.rangeMax = rangeMax;
		this.rangeMin = rangeMin;
		this.grade = grade;
		this.percentage = percentage;
		this.typeName = nameType;
		this.children = new ArrayList<GradeReportLine>();
	}

	/**
	 * Devuelve el id del GradeReportLine
	 * 
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Modifica el id del GradeReportLine
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devuelve el nombre del GradeReportLine
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Modifica el nombre del GradeReportLine
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Devuelve el nivel del GradeReportLine en el �rbol del calificador
	 * 
	 * @return level
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Modifica el nivel del GradeReportLine en el �rbol del calificador
	 * 
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Devuelve la nota del GradeReportLine
	 * 
	 * @return grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * Modifica la nota del GradeReportLine
	 * 
	 * @param grade
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * Devuelve el peso del GradeReportLine
	 * 
	 * @return weight
	 */
	public float getWeight() {
		return this.weight;
	}

	/**
	 * Modifica el peso del GradeReportLine
	 * 
	 * @param weight
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * Devuelve el rango m�ximo de nota
	 * 
	 * @return rangeMax
	 */
	// RMS changed
	public String getRangeMax() {
		// public float getRangeMax() {
		return this.rangeMax;
	}

	/**
	 * Modifica el rango m�ximo de nota
	 * 
	 * @param rangeMax
	 */
	public void setRangeMax(String rangeMax) {
		this.rangeMax = rangeMax;
	}

	/**
	 * Devuelve el rango m�nimo de nota
	 * 
	 * @return rangeMin
	 */
	public String getRangeMin() {
		return this.rangeMin;
	}

	/**
	 * Modifica el rango m�nimo de nota
	 * 
	 * @param rangeMin
	 */
	public void setRangeMin(String rangeMin) {
		this.rangeMin = rangeMin;
	}

	/**
	 * Devuelve el porcentaje del GradeReportLine
	 * 
	 * @return percentage
	 */
	public float getPercentage() {
		return percentage;
	}

	/**
	 * Modifica el porcentaje del GradeReportLine
	 * 
	 * @param percentage
	 */
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	/**
	 * Devuelve el tipo (boolean) de GradeReportLine
	 * 
	 * @return type
	 */
	public boolean getType() {
		return this.type;
	}

	/**
	 * Modifica el tipo (boolean) del GradeReportLine
	 * 
	 * @param type
	 */
	public void setType(boolean type) {
		this.type = type;
	}

	/**
	 * Devuelve el tipo de GradeReportLine
	 * 
	 * @return nameType
	 */
	public String getNameType() {
		return this.typeName;
	}

	/**
	 * Modifica el tipo de GradeReportLine
	 * 
	 * @param nameType
	 */
	public void setNameType(String nameType) {
		this.typeName = nameType;
	}

	/**
	 * Devuelve la actividad asociada al GradeReportLine
	 * 
	 * @return activity
	 */
	public Activity getActivity() {
		return this.activity;
	}

	/**
	 * Crea una actividad a partir del GradeReportLine
	 */
	public void setActivity() {
		this.activity = new Activity(name, typeName, weight, rangeMin, rangeMax);
	}

	/**
	 * Devuelve los hijos que tiene el GradeReportLine
	 * 
	 * @return children
	 */
	public ArrayList<GradeReportLine> getChildren() {
		return this.children;
	}

	/**
	 * A�ade un hijo al GradeReportLine
	 * 
	 * @param kid
	 */
	public void addChild(GradeReportLine child) {
		this.children.add(child);
	}

	/**
	 * Convierte el GradeReportLine a un String con su nombre.
	 */
	public String toString() {
		return this.getName();
	}
}
