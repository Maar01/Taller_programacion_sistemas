/**
 * Created by mario on 5/09/16.
 */

/**
 * Clase encargada de guardar los datos de todo codop
 * encontrado  en el archivo .asm
 */
public class Codop {

    private  String modoDirec;
    private  String codMaquina;
    private  String bytesCalculados;
    private  String bytesCalcular;
    private  String bytesSuma;
    private  String codop;
    private boolean usaOper;

    Codop(String lineaTabop){
        String campos[] = lineaTabop.split("\\|");
        codop = campos[0].toUpperCase();

        if( campos[1].equals( "SI" ) ){
            usaOper = true;
        }else{
            usaOper = false;
        }
        modoDirec = campos[2];
        codMaquina = campos[3];
        bytesCalculados = campos[4];
        bytesCalcular = campos[5];
        bytesSuma = campos[6];
    }

    /**Inicio
     * getter and setters
     *
     */

    public boolean usaOper() {
        return usaOper;
    }

    public void setUsaOper(boolean usaOper) {
        this.usaOper = usaOper;
    }

    public String getCodop() {
        return codop.toUpperCase();
    }

    public void setCodop(String codop) {
        this.codop = codop;
    }


    public String getModoDirec() {
        return modoDirec;
    }

    public void setModoDirec(String modoDirec) {
        this.modoDirec = modoDirec;
    }

    public String getCodMaquina() {
        return codMaquina;
    }

    public void setCodMaquina(String codMaquina) {
        this.codMaquina = codMaquina;
    }

    public String getBytesCalculados() {
        return bytesCalculados;
    }

    public void setBytesCalculados(String bytesCalculados) {
        this.bytesCalculados = bytesCalculados;
    }

    public String getBytesCalcular() {
        return bytesCalcular;
    }

    public void setBytesCalcular(String bytesCalcular) {
        this.bytesCalcular = bytesCalcular;
    }

    public String getBytesSuma() {
        return bytesSuma;
    }

    public void setBytesSuma(String bytesSuma) {
        this.bytesSuma = bytesSuma;
    }
    /**
     * Fin
     * getters and setters
     */
}
