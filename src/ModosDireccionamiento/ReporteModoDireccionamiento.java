package ModosDireccionamiento;

/**
 * Created by mario on 10/10/16.
 */
public class ReporteModoDireccionamiento {

    private boolean error;
    private String  mensaje_error = "";
    private String  modo_direccionamiento;
    private boolean error_final = false;

    public ReporteModoDireccionamiento () {

    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMensaje_error() {
        return mensaje_error;
    }

    public void setMensaje_error(String mensaje_error) {
        this.mensaje_error = mensaje_error;
    }

    public String getModo_direccionamiento() {
        return modo_direccionamiento;
    }

    public void setModo_direccionamiento(String modo_direccionamiento) {
        this.modo_direccionamiento = modo_direccionamiento;
    }

    public boolean isError_final() {
        return error_final;
    }

    public void setError_final( boolean valor ){
        error_final = valor;
    }

}
