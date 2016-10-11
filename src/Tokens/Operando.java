package Tokens;

/**
 * Created by mario on 23/09/16.
 */
public class Operando {
    private  char base_Numerica;
    private short rango;
    private String operando;



    public Operando( String oper ) {
        //baseNumerica = oper.charAt( 0 );
        operando = oper;
    }

    public String getOperando() {
        return this.operando;
    }

}
