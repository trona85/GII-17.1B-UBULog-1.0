/**
 * 
 */
package model;

/**
 * Clase Group para distinguir los grupos que hay en un curso, así como los grupos en los que se encuentra un usuario.
 * 
 * @author Oscar Fernández Armengol
 * @author Claudia Martínez Herrero
 * 
 * @version 1.0
 */
public class Group {
	private int id;
	private String name;
	private String description;

	/**
	 * Constructor de la clase Group. Establece un grupo.
	 * 
	 * @param id
	 *            id del grupo
	 * @param name
	 *            nombre del grupo
	 * @param description
	 *            descripción del grupo
	 */
	public Group(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	/**
	 * Devuelve el id del grupo
	 * 
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Modifica el id del grupo
	 * 
	 * @param id, id.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devuelve el nombre del grupo
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Modifica el nombre del grupo
	 * 
	 * @param name, name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Devuelve la descripción del grupo
	 * 
	 * @return description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Modifica la descripción del grupo
	 * 
	 * @param description, description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Metodo para imprimir datos.
	 */
	public String toString(){
		return "id: "+ getId() + "\n"+
				"name: "+ getName() + "\n"+
				"description: "+ getDescription() ;
	}
}
