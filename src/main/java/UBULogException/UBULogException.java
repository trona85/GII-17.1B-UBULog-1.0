package UBULogException;

/**
 * @author oscar
 *
 */
public class UBULogException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Error. */
	private UBULogError error;
	
	/**
	 * Constructor.
	 * 
	 * @param texto texto descriptivo
	 */
	public UBULogException(String texto) {
		super(texto);
	}

	/**
	 * Constructor.
	 * 
	 * @param error error
	 */
	public UBULogException(UBULogError error) {
		this.error = error;
	}

	/**
	 * Constructor.
	 * 
	 * @param error
	 *            error
	 * @param ex
	 *            si la excepción ha sido originada por otra excepción
	 */
	public UBULogException(UBULogError error, Exception ex) {
		super(ex);
		this.error = error;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param error
	 *            error
	 * @param texto
	 *            texto descriptivo
	 * @param ex
	 * 			  si la excepción ha sido originada por otra excepción
	 */
	public UBULogException(UBULogError error, String texto, Exception ex) {
		super(texto, ex);
		this.error = error;
	}

	/**
	 * Constructor.
	 * 
	 * @param error
	 *            error
	 * @param texto
	 *            texto descriptivo
	 */
	public UBULogException(UBULogError error, String texto) {
		super(texto);
		this.error = error;
	}

	/**
	 * Obtiene el tipo de error que genera la excepción.
	 * 
	 * @return error
	 */
	public UBULogError getError() {
		return error;
	}

	/**
	 * Obtiene el texto del error generado.
	 * 
	 * @return texto texto descriptivo del error
	 */
	@Override
	public String getMessage() {
		if (error != null)
			return error.getText() + super.getMessage();
		return getMessage();
	}
}
