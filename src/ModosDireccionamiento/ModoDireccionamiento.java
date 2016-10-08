package ModosDireccionamiento;

/**
 * Created by mario on 6/10/16.
 * domingo 30 de oct fiesta de Yeriel. Avisar
 */
public class ModoDireccionamiento {
    private String modoDireccionamientoTabop;
    private short rangoPermitido;
    private String[] modosDireccionamiento;


    public ModoDireccionamiento(String modo){
        modoDireccionamientoTabop = modo;
        modosDireccionamiento = modoDireccionamientoTabop.split(" ");
    }

    public String getModoDireccionamientoTabop() {
        return this.modoDireccionamientoTabop;
    }

    public void setModoDireccionamientoTabop( String modoDireccionamiento ) {
        this.modoDireccionamientoTabop = modoDireccionamiento;
    }
}
