package Tokens;

/**
 * Created by mario on 21/11/16.
 */
public class ReporteDirectiva {

    private boolean es_directiva;
    private  String directiva;
    private  String valor;
    private     int aumentar_contLoc;
    private  String error;
    private boolean es_error;
    private String operando;

    ReporteDirectiva ( String directiva, boolean es_directiva, int aumentar_contLoc, String valor ) {
        this.es_directiva = es_directiva;
        this.directiva = directiva;
        this.valor = valor;
        this.aumentar_contLoc = aumentar_contLoc;
    }

    ReporteDirectiva ( String directiva ){
        this.directiva = directiva;
    }

    public boolean Es_directiva() {
        return es_directiva;
    }

    public void setEs_directiva(boolean es_directiva) {
        this.es_directiva = es_directiva;
    }

    public String getDirectiva() {
        return directiva;
    }

    public void setDirectiva(String directiva) {
        this.directiva = directiva;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getAumentar_contLoc() {
        return aumentar_contLoc;
    }

    public void setAumentar_contLoc(int aumentar_contLoc) {
        this.aumentar_contLoc = aumentar_contLoc;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean Es_error() {
        return es_error;
    }

    public void setEs_error(boolean es_error) {
        this.es_error = es_error;
    }

    public String getOperando() {
        return operando;
    }

    public void setOperando(String operando) {
        this.operando = operando;
    }


}
