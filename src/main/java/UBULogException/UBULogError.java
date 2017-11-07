/**
 * 
 */
package UBULogException;

/**
 * @author oscar
 *
 */
public enum UBULogError {
		
		FICHERO_NO_VALIDO("El fichero seleccionado no es valido");
		
		/** Texto. */
		private String text;
		
		/** 
		 * Constructor.
		 * 
		 * @param text texto con el mensaje de error generado
		 */
		private UBULogError(String text) {
			this.text = text;
		}	
		
		/**
		 * Consulta el texto.
		 * 
		 * @return texto
		 */
		public String getText(){
			return text;
		}
	}
