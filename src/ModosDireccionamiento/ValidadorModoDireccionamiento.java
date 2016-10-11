package ModosDireccionamiento;

import java.util.StringTokenizer;

/**
 * Created by mario on 6/10/16.
 */
public final class ValidadorModoDireccionamiento {

    private final static short RANGO_MINIMO_IMM8_REL8_IDX1 = -256;
    private final static short RANGO_MAXIMO_IMM8_REL8_IDX1 = 255;
    private final static char CARACTER_INICIAL_IMM = '#';

    private final static short RANGO_MINIMO_DIR = 0;
    private final static short RANGO_MAXIMO_DIR = 255;

    private final static short RANGO_MINIMO_EXT_REL16_IMM16 = -32768;
    private final static   int RANGO_MAXIMO_EXT_REL16_IMM16 = 65535;

    private final static byte RANGO_MINIMO_IDX = -16;
    private final static byte RANGO_MAXIMO_IDX = 15;

    private final static byte RANGO_MINIMO_IDX_PRE_POST = 8; //para la verificacion de este tipo se podrían tomar
    private final static byte RANGO_MAXIMO_IDX_PRE_POST = 1; //Valores absolutos -8 a -1 y 1 a 8

    private final static char[] IDX_ACUMULADOR = { 'A', 'B', 'D' };
    private final static byte RANGO_MAXIMO_IDX_ACUMULADOR = 15;

    private final static short RANGO_MINIMO_IDX2_INDIRECTO = 0;
    private final static int RANGO_MAXIMO_IDX2_INDIRECTO = 65535;


    private final static char[] simbolos_hex =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
             'a', 'b', 'c', 'd', 'e', 'f',
             'A', 'B', 'C', 'D', 'E', 'F'};

    private final static String REGISTROS_IDX[] = {"X", "Y", "SP", "PC"};

/** para convertir a diferentes bases númericas
 * temp1 = Integer.parseInt(display.getText().trim(), 16 );
 */

    public static String prueba(){
        return "test";
    }


    public static boolean esInherente(String operando) {
        if( operando.equals( "" ) || operando.equals("NULL") ) {
            return true;
        }

        return false;

    }

    public static boolean esInmediato8(String operando) {
        int numero;


        if ( operando.startsWith("#")  ) {
            if ( esDigito( operando.charAt( 0 ) ) ) {
                numero = Integer.parseInt( operando.substring(1) );
                if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                    return true;
                }
            }else {
                 numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(2) );
                 if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                     return true;
                 }
            }
        }

        return false;
    }



    public static ReporteModoDireccionamiento esInmediato16(String operando ) {
        int numero;

        if ( operando.startsWith("#")  ) {
            if ( esDigito( operando.charAt( 1 ) ) ) {
                numero = Integer.parseInt( operando.substring(1) );
                if( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                    return true;
                }
            }else {
                numero = cambiaBaseNumericaDecimal( operando.charAt( 1 ), operando.substring(1) );
                if( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                    return true;
                }
            }
        }

        return false;
    }

    public static ReporteModoDireccionamiento esDirecto(String operando) {
        ReporteModoDireccionamiento respuesta = new ReporteModoDireccionamiento();
        int numero;

        if ( esDigito( operando.charAt( 0 ) ) ) {
            numero = Integer.parseInt( operando.substring(1) );
            if( numero <= RANGO_MAXIMO_DIR && numero >= RANGO_MINIMO_DIR ) {
                return true;
            }
        }else {
            numero = cambiaBaseNumericaDecimal( operando.charAt( 1 ), operando.substring(1) );
            if( numero <= RANGO_MAXIMO_DIR && numero >= RANGO_MINIMO_DIR ) {
                return true;
            }
        }

        return false;
    }

    public static ReporteModoDireccionamiento esExtendido(String operando) {
        int numero;


        if ( esDigito( operando.charAt( 0 ) ) ) {
            numero = Integer.parseInt( operando.substring(1) );
            if( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                return true;
            }
        }else {
            //posible error en cada método de es*();
            numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) );//el substring comienza del 0 o 1?
            if( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                return true;
            }
        }
        return false;

    }

    public static ReporteModoDireccionamiento esIDX(String operando) {
        char base_numerica = operando.charAt( 0 );
        int numero;
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;

        for( int index = 0; index < REGISTROS_IDX.length; index++ ) {
            if( tokensIDX[1] == REGISTROS_IDX[index] ){
                 registroCorrecto = true;
                break;
            }
        }

        if ( tokensIDX[0].equals( "" ) && registroCorrecto ) {
            return true;

        }else {
            if ( esDigito( tokensIDX[0].charAt(0) ) ) {
                numero = Integer.parseInt( tokensIDX[0] );
                if( numero <= RANGO_MAXIMO_IDX && numero >= RANGO_MINIMO_IDX ) {
                    return true;
                }
            }else if( operando.charAt( 0 ) == '-' ) {
                //aquí tendría que aplicar algo de complemento a 2

            }else {
                numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) );
                if( numero <= RANGO_MAXIMO_IDX && numero >= RANGO_MINIMO_IDX ) {
                    return true;
                }
            }
            return false;

        }

        //return false;
    }

    public static ReporteModoDireccionamiento esIDX1(String operando) {

        char base_numerica = operando.charAt( 1 );
        int numero;
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;

        for( int index = 0; index < REGISTROS_IDX.length; index++ ) {
            if( tokensIDX[1] == REGISTROS_IDX[index] ){
                registroCorrecto = true;
                break;
            }
        }

        if ( tokensIDX[0].equals( "" ) && registroCorrecto ) {
            return true;

        }else {
            if ( esDigito( tokensIDX[0].charAt(0) ) ) {
                numero = Integer.parseInt( tokensIDX[0] );
                if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                    return true;
                }
            }else if( operando.charAt( 0 ) == '-' ) {
                //aquí tendría que aplicar algo de complemento a 2

            }else {
                numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) );
                if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                    return true;
                }
            }
            return false;

        }
    }

    public static ReporteModoDireccionamiento esIDX2(String operando) {

        char base_numerica = operando.charAt( 1 );
        int numero;
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;

        for( int index = 0; index < REGISTROS_IDX.length; index++ ) {
            if( tokensIDX[1] == REGISTROS_IDX[index] ){
                registroCorrecto = true;
                break;
            }
        }

        if ( tokensIDX[0].equals( "" ) && registroCorrecto ) {
            return true;

        }else {
            if ( esDigito( tokensIDX[0].charAt(0) ) ) {
                numero = Integer.parseInt( tokensIDX[0] );
                if( numero <= RANGO_MAXIMO_IDX2_INDIRECTO && numero >= RANGO_MINIMO_IDX2_INDIRECTO && registroCorrecto ) {
                    return true;
                }
            } else {
                numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) );
                if( numero <= RANGO_MAXIMO_IDX2_INDIRECTO && numero >= RANGO_MINIMO_IDX2_INDIRECTO && registroCorrecto ) {
                    return true;
                }
            }
            return false;

        }
    }

    public static ReporteModoDireccionamiento esIDX2Indirecto(String operando) {

        char base_numerica = operando.charAt( 1 );
        int numero;
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;

        if ( operando.startsWith("[") && operando.endsWith("]") ) {
            for( int index = 0; index < REGISTROS_IDX.length; index++ ) {
                if( tokensIDX[1].contains( REGISTROS_IDX[index] )   ){
                    registroCorrecto = true;
                    break;
                }
            }

            if ( esDigito( tokensIDX[0].charAt(1) ) ) {
                numero = Integer.parseInt( tokensIDX[0].substring(0) );// o el substring con 1?
                if( numero <= RANGO_MAXIMO_IDX2_INDIRECTO && numero >= RANGO_MINIMO_IDX2_INDIRECTO && registroCorrecto) {
                    return true;
                }
            } else {
                numero = cambiaBaseNumericaDecimal( operando.charAt( 1 ), operando.substring(1) );
                if( numero <= RANGO_MAXIMO_IDX2_INDIRECTO && numero >= RANGO_MINIMO_IDX2_INDIRECTO && registroCorrecto) {
                    return true;
                }
            }
            return false;

        } else {
            return false;
        }


    }

    public final static String REGISTROS_INCREMENTO_DECREMENTO[] =
            { "X+", "Y+", "SP+",
              "X-", "Y-", "SP-",
              "+X", "+Y", "+SP",
              "-X", "-Y", "-SP" };

    public static ReporteModoDireccionamiento esIDXPrePost( String operando ) {
        char base_numerica = operando.charAt( 1 );
        int numero;
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;

        for( int index = 0; index < REGISTROS_INCREMENTO_DECREMENTO.length; index++ ) {
            if( tokensIDX[1] == REGISTROS_INCREMENTO_DECREMENTO[index] ){
                registroCorrecto = true;
                break;
            }
        }

            if ( esDigito( tokensIDX[0].charAt(0) ) ) {
                numero = Integer.parseInt( tokensIDX[0] );
                if( numero <= RANGO_MAXIMO_IDX_PRE_POST && numero >= RANGO_MINIMO_IDX_PRE_POST && registroCorrecto ) {
                    return true;
                }
            } else {
                numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) );
                if( numero <= RANGO_MAXIMO_IDX && numero >= RANGO_MINIMO_IDX && registroCorrecto) {
                    return true;
                }
            }
            return false;
    }

    public static ReporteModoDireccionamiento esIDXAcumulador( String operando ) {
        String[] operando_dividido = operando.split( "," );


        if( operando_dividido[0] == "A" || operando_dividido[0] == "B" || operando_dividido[0] == "D" ) {
            for( short index = 0; index < REGISTROS_IDX.length; index++ ) {
                if( operando_dividido[1] == REGISTROS_IDX[index] ) {
                    return true;
                }
            }
        }
        return false;

    }

    public static ReporteModoDireccionamiento esIDXAcumuladorIndirecto(String operando) {
        String[] operando_dividido = operando.split( "," );
        if( operando_dividido[0] == "D" ) {
            for( short index = 0; index < REGISTROS_IDX.length; index++ ) {
                if( operando_dividido[1] == REGISTROS_IDX[index] ) {
                    return true;
                }
            }
        }
        return false;

    }
    // se tendría que verificar el codop antes de esta función, ya que los codops de 16 bits comienzan con l
    public static ReporteModoDireccionamiento esRelativo8(String operando) {
        int numero;
        if( esDigito( operando.charAt( 0 )  ) ) {
            numero = Integer.parseInt( operando );
            if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                return true;
            }else {
                numero = cambiaBaseNumericaDecimal( operando.charAt(0) , operando.substring(1) );
                if ( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ReporteModoDireccionamiento esRelativo16(String operando) {

        int numero;
        if( esDigito( operando.charAt( 0 )  ) ) {
            numero = Integer.parseInt( operando );
            if( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                return true;
            }else {
                numero = cambiaBaseNumericaDecimal( operando.charAt(0) , operando.substring(1) );
                if ( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void verificarBaseNumerica( char base_numerica, String numero ) {

        switch ( base_numerica ) {
            //octal
            case '@':

                break;
            //Hexadecimal
            case '$':

                break;
            //binario
            case '%':

                break;

        }
    }

    public static Integer cambiaBaseNumericaDecimal( char base_numerica, String numero ) {

        switch ( base_numerica ) {
            //octal
            case '@':
                if( esOctal( numero ) ) {
                       return Integer.parseInt( numero, 8 );
                }
                break;
            //Hexadecimal
            case '$':
                if ( esHexadecimal( numero ) ) {
                    return Integer.parseInt( numero, 16 );
                }
                break;
            //binario
            case '%':
                if ( esBinario( numero ) ) {
                    return Integer.parseInt( numero, 2 );
                }
                break;

            default:
                return Integer.parseInt( numero );

        }

        return Integer.parseInt( numero );
    }

    public static boolean esDigito( char digito ){
        switch (digito){
            case '0':
                return true;

            case '1':
                return true;
            case '2':
                return true;
            case '3':
                return true;
            case '4':
                return true;
            case '5':
                return true;
            case '6':
                return true;
            case '7':
                return true;
            case '8':
                return true;
            case '9':
                return true;

            default:
                return false;
        }
    }

    public static boolean esHexadecimal( String numero ) {

        for( int index = 0; index < numero.length(); index++ ) {
            if ( !esHexadecimal( numero.charAt( index ) ) ) {
                return false;
            }
        }

        return true;
    }

    public static boolean esHexadecimal( char digito_hex ) {

        for( int index = 0; index < simbolos_hex.length; index++ ) {
            if( digito_hex == simbolos_hex[ index ] ){
                return true;
            }
        }
        return false;
    }

    public static boolean esOctal( String numero ) {
        for( int index = 0; index < numero.length(); index++ ) {
            if ( numero.charAt( index ) >= 8 ) {
                return false;
            }
        }

        return true;
    }

    public static boolean esBinario( String numero ) {
        for ( int index = 0; index < numero.length(); index++ ){
            if ( numero.charAt( index ) > 1 ) {
                return false;
            }
        }
        return true;
    }

}
