/**
 * Created by mario on 28/08/16.
 */
public class Linea {
    private String lineaOriginal;
    private String codop;
    private String etq;
    private String oper;
    private   byte numeroLinea;
    private String lineaCopia;


    private String tokens[];

    Linea(String linea){
        lineaOriginal = linea;
        lineaCopia = lineaOriginal.trim();
        tokens = lineaCopia.split("\\s+");
    }

    Linea(){    }

    public String getLineaOriginal() {
        return lineaOriginal;
    }

    public void setLineaOriginal(String lineaOriginal) {
        this.lineaOriginal = lineaOriginal;
    }

    public String getCodop() {
        return codop;
    }

    public void setCodop(String codop) {
        this.codop = codop;
    }

    public String getEtq() {
        return etq;
    }

    public void setEtq(String etq) {
        this.etq = etq;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public byte getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(byte numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getLineaCopia() {
        return lineaCopia;
    }

    public void setLineaCopia(String lineaCopia) {
        this.lineaCopia = lineaCopia;
    }

}
