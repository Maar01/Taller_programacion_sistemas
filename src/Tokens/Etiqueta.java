package Tokens;

import static ModosDireccionamiento.ValidadorModoDireccionamiento.esDigito;

/**
 * Created by mario on 6/10/16.
 */
public class Etiqueta {

    private String etiqueta;
    private String valor_etiqueta;

    public Etiqueta(String etiqueta, String valor_etiqueta) {
        this.etiqueta = etiqueta;

        try {
            this.valor_etiqueta =  valorEtiquetaAHex( valor_etiqueta );// Integer.parseInt( valor_etiqueta, 16 );
        } catch ( NumberFormatException e) {
            this.valor_etiqueta = valor_etiqueta;
        }


    }

    public String valorEtiquetaAHex( String valor_etiqueta ) {

        if ( valor_etiqueta.charAt( 0 ) == '$' ) {

            return  valor_etiqueta.substring( 1 );
        } else if (  esDigito( valor_etiqueta.charAt( 0 ) )  ) {

            return Integer.toHexString(  Integer.parseInt( valor_etiqueta ) );
        }
        String aux = ValidadorDirectivas.cambiaBaseNumericaDecimal( valor_etiqueta.charAt( 0 ), valor_etiqueta.substring( 1 ) ).toString()  ;

        System.out.println( "Valor etiqueta = " + valor_etiqueta + "   " + Integer.parseInt( aux ) );

         return Integer.toHexString( Integer.parseInt( aux ) );

        //return Integer.parseInt( aux.toString(), 16 ) ;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getValor_etiqueta() {
        return valor_etiqueta;
    }

    public void setValor_etiqueta(String valor_etiqueta) {
        this.valor_etiqueta = valor_etiqueta;
    }



}
