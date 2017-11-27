package UBULogPersonalException;

/**
 * clase UBULogError. clase tipo enum para almacenar errores..
 * @author oscar Fernández Armengol
 * 
 * @version 1.0
 */
public enum UBULogError {
		
		FICHERO_NO_VALIDO("El fichero seleccionado no es valido"),
		FICHERO_CANCELADO("Cancelación de selección de fichero"),
		FICHERO_CON_EXTENSION_CORRECTA_PERO_EXTRUCTURA_INCORRECTA("El fichero seleccionado tiene una extructura que no se asemeja a la esperada");
		
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
