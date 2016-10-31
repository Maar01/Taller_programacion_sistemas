package ModosDireccionamiento;

/**
 * Created by mario on 10/10/16.
 * Clase auxiliar a "ValidadorModoDireccionamiento" encargada de regresar una clase
 * con detalles de la revisión de un operando y su modo de direccionamiento correspondiente.
 */
public class ReporteModoDireccionamiento {

    private boolean error;
    private String  mensaje_error = "";
    private String  modo_direccionamiento;
    private boolean error_final = false;

    public ReporteModoDireccionamiento () {

    }

    /**
     *
     * @return
     */
    public boolean isError() {
        return error;
    }

    /**
     *
     * @param error
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     *
     * @return
     */
    public String getMensaje_error() {
        return mensaje_error;
    }

    /**
     *
     * @param mensaje_error
     */
    public void setMensaje_error(String mensaje_error) {
        this.mensaje_error = mensaje_error;
    }

    /**
     *
     * @return
     */
    public String getModo_direccionamiento() {
        return modo_direccionamiento;
    }

    /**
     *
     * @param modo_direccionamiento
     */
    public void setModo_direccionamiento(String modo_direccionamiento) {
        this.modo_direccionamiento = modo_direccionamiento;
    }

    /**
     *
      * @return
     */

    public boolean isError_final() {
        return error_final;
    }

    /**
     *
     * @param valor
     */

    public void setError_final( boolean valor ){
        error_final = valor;
    }

}
