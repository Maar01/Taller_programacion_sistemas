package ModosDireccionamiento;

import Tokens.Validador;
import com.sun.org.apache.regexp.internal.RE;

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
    private final static   int RANGO_MAXIMO_IDX2_INDIRECTO = 65535;

    private final static boolean    NI_MERGAS = false;
    private final static boolean SIMON_QUE_SI = true;


    private final static char[] simbolos_hex =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
             'a', 'b', 'c', 'd', 'e', 'f',
             'A', 'B', 'C', 'D', 'E', 'F'};

    private final static String REGISTROS_IDX[] = {"X", "Y", "SP", "PC"};


    public final static String REGISTROS_INCREMENTO_DECREMENTO[] =
            { "X+", "Y+", "SP+",
                    "X-", "Y-", "SP-",
                    "+X", "+Y", "+SP",
                    "-X", "-Y", "-SP" };



    public static ReporteModoDireccionamiento esInherente(String operando, ReporteModoDireccionamiento reporte) {
        //aceptacion
        reporte.setModo_direccionamiento("INH");
        if( operando.equals( "" ) || operando.equals("NULL") ) {
            reporte.setError(false);
            reporte.setMensaje_error("");
            return reporte;
        }
        reporte.setError(true);
        reporte.setMensaje_error("Error, este código de operación no lleva operando");

        return reporte;
    }

    public static ReporteModoDireccionamiento esInmediato8(String operando, ReporteModoDireccionamiento reporte) {
        int numero;
        reporte.setModo_direccionamiento("IMM8");

        if ( operando.startsWith("#")  ) {
            if ( esDigito( operando.charAt( 1 ) ) ) {
                numero = Integer.parseInt( operando.substring(1) );
                if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                    //return true;
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    return reporte;
                } else {
                    reporte.setError(true);
                    reporte.setMensaje_error("Operando fuera de rango");
                    return reporte;
                }
            } else {
                if( verificarBaseNumerica( operando.charAt( 1 ) ) ){
                    numero = cambiaBaseNumericaDecimal( operando.charAt( 1 ), operando.substring(2) );
                    if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                        //return true;
                        reporte.setError(false);
                        reporte.setMensaje_error("");
                        return reporte;
                    } else {

                        reporte.setError(true);
                        reporte.setMensaje_error("Operando fuera de rango");
                        return reporte;
                    }
                } else {
                    reporte.setError(true);
                    reporte.setMensaje_error("Base numerica no reconocida");
                }

            }
        }
        reporte.setMensaje_error("El operando debe iniciar con #");
        reporte.setError(true);

        return reporte;
    }


    public static ReporteModoDireccionamiento esInmediato16(String operando, ReporteModoDireccionamiento reporte ) {
        int numero;
        reporte.setModo_direccionamiento("IMM16");

        if ( operando.startsWith("#")  ) {
            if ( esDigito( operando.charAt( 1 ) ) ) {
                numero = Integer.parseInt( operando.substring(1) );
                if( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                    //return true;
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    return reporte;
                } else {
                    reporte.setError(true);
                    reporte.setMensaje_error("Operando fuera de rango");
                    return reporte;
                }
            }else {
                if( verificarBaseNumerica( operando.charAt( 1 ) ) ) {
                    numero = cambiaBaseNumericaDecimal(operando.charAt(1), operando.substring(2));
                    if (numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16) {
                        //return true;
                        reporte.setError(false);
                        reporte.setMensaje_error("");
                        return reporte;
                    }
                    else {
                        reporte.setError(true);
                        reporte.setMensaje_error("Operando fuera de rango");
                        return reporte;
                    }
                } else {
                    reporte.setError(true);
                    reporte.setMensaje_error("Base numerica no reconocida");
                }
            }
        }
        reporte.setMensaje_error("El operando debe iniciar con #");
        reporte.setError(true);

        //return false;
        return reporte;
    }

    public static ReporteModoDireccionamiento esDirecto(String operando, ReporteModoDireccionamiento reporte) {

        int numero;
        reporte.setModo_direccionamiento("DIR");

        if ( esDigito( operando.charAt( 0 ) ) && !operando.contains(",") ) {

            numero = Integer.parseInt( operando.substring(0)  ); //antes tenía valor 1
            if( numero <= RANGO_MAXIMO_DIR && numero >= RANGO_MINIMO_DIR ) {

                reporte.setError(false);
                return reporte;
            } else {

                reporte.setError(true);
                reporte.setMensaje_error("Operando fuera de rango");
                return reporte;
            }

        }else if ( verificarBaseNumerica( operando.charAt( 0 ) ) && !operando.contains(",")){

            numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) ); //antes 1,0
            if( numero <= RANGO_MAXIMO_DIR && numero >= RANGO_MINIMO_DIR ) {
                //return true;
                reporte.setError(false);
                reporte.setMensaje_error("");
                return reporte;
            } else {
                reporte.setError(true);
                reporte.setMensaje_error("Operando fuera de rango");
                return reporte;
            }
        }
        reporte.setError(true);
        if(  !operando.contains(",") ) {
            reporte.setMensaje_error("Base numerica no reconocida");
        }else {
            reporte.setMensaje_error("No debe contener cooma ,");
        }


        //return false;
        return reporte;
    }

    public static ReporteModoDireccionamiento esExtendido(String operando, ReporteModoDireccionamiento reporte) {
        int numero;
        reporte.setModo_direccionamiento("EXT");

        if ( esDigito( operando.charAt( 0 ) ) && !operando.contains(",") ) {
            numero = Integer.parseInt( operando.substring(1) );
            if( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                //return true;
                reporte.setError(false);
                reporte.setMensaje_error("");
                return reporte;
            }
            else {
                reporte.setError(true);
                reporte.setMensaje_error("Operando fuera de rango");
                return reporte;
            }
        }else if ( verificarBaseNumerica( operando.charAt( 0 ) ) ) {
            //posible error en cada método de es*();
            numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) );//el substring comienza del 0 o 1?
            if( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                //return true;
                reporte.setError(false);
                reporte.setMensaje_error("");
                return reporte;
            } else {
                reporte.setError(true);
                reporte.setMensaje_error("Operando fuera de rango");
                return reporte;
            }
        }
        reporte.setError(true);
        reporte.setMensaje_error( "Base númerica no reconocida" );
        //return false;
        return reporte;

    }

    public static ReporteModoDireccionamiento esIDX(String operando, ReporteModoDireccionamiento reporte) {
        char base_numerica = operando.charAt( 0 );
        int numero;
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;
        reporte.setModo_direccionamiento("IDX");

        if( tokensIDX.length > 1  ){
            for( int index = 0; index < REGISTROS_IDX.length; index++ ) {
                if( tokensIDX[1].equals( REGISTROS_IDX[index] )  ){
                    registroCorrecto = true;
                    break;
                }
            }
        }

        if ( tokensIDX[0].equals( "" ) && registroCorrecto ) {
            //return true;
            reporte.setError(false);
            reporte.setMensaje_error("");
            return  reporte;

        } else if  ( registroCorrecto ){
            if ( esDigito( tokensIDX[0].charAt(0) ) ) {
                numero = Integer.parseInt( tokensIDX[0] );
                if( numero <= RANGO_MAXIMO_IDX && numero >= RANGO_MINIMO_IDX ) {
                    //return true;
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    return reporte;
                } else {
                    reporte.setError(true);
                    reporte.setMensaje_error("Operando fuera de rango");
                    return reporte;
                }
            } else if( operando.charAt( 0 ) == '-' ) {
                //aquí tendría que aplicar algo de complemento a 2

            } else if ( verificarBaseNumerica( operando.charAt(0) ) ) {
                numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) );
                if( numero <= RANGO_MAXIMO_IDX && numero >= RANGO_MINIMO_IDX ) {
                   // return true;
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    return reporte;
                } else {
                    reporte.setError(true);
                    reporte.setMensaje_error("Operando fuera de rango");
                    return reporte;
                }
            }
            //return false;
            reporte.setError(true);
            reporte.setMensaje_error(" base númerica no reconocida ");
            return reporte;

        }

        reporte.setError(true);
        reporte.setMensaje_error(" Registro no reconocido ");
        return reporte;
    }

    public static ReporteModoDireccionamiento esIDX1(String operando, ReporteModoDireccionamiento reporte) {

        char base_numerica = operando.charAt( 1 );
        int numero;
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;
        reporte.setModo_direccionamiento("IDX1");

        if( tokensIDX.length > 1 ) {
            for( int index = 0; index < REGISTROS_IDX.length; index++ ) {
                if( tokensIDX[1].equals( REGISTROS_IDX[index] )  ){
                    registroCorrecto = true;
                    break;
                }
            }
        }

        if ( tokensIDX[0].equals( "" ) && registroCorrecto ) {
            //return true;
            reporte.setError(false);
            reporte.setMensaje_error("");
            return reporte;

        }else if  ( registroCorrecto ) {
            if ( esDigito( tokensIDX[0].charAt(0) ) ) {
                numero = Integer.parseInt( tokensIDX[0] );
                if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                    //return true;
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    return reporte;
                }
            }else if( operando.charAt( 0 ) == '-' ) {
                //aquí tendría que aplicar algo de complemento a 2

            }else if (verificarBaseNumerica( operando.charAt( 0 ) ) ) {
                numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) );
                if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                    //return true;
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    return reporte;
                } else {
                    reporte.setError(true);
                    reporte.setMensaje_error( " operando fuera de rango " );
                }
            }
            //return false;
            reporte.setMensaje_error( " base númerica no reconocida" );
            reporte.setError(true);
            return reporte;

        }
        reporte.setError(true);
        reporte.setMensaje_error(" Registro no reconocido ");
        return reporte;
    }

    public static ReporteModoDireccionamiento esIDX2(String operando, ReporteModoDireccionamiento reporte) {

        char base_numerica = operando.charAt( 1 );
        int numero;
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;
        reporte.setModo_direccionamiento(" IDX2");

        if ( tokensIDX.length > 1 ) {
            for( int index = 0; index < REGISTROS_IDX.length; index++ ) {
                if( tokensIDX[1].equals( REGISTROS_IDX[index] )){
                    registroCorrecto = true;
                    break;
                }
            }
        }

            if ( esDigito( tokensIDX[0].charAt(0) ) ) {
                numero = Integer.parseInt( tokensIDX[0] );
                if( numero <= RANGO_MAXIMO_IDX2_INDIRECTO && numero >= RANGO_MINIMO_IDX2_INDIRECTO && registroCorrecto ) {
                    //return true;
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    return reporte;
                }
            } else {
                //System.out.println(operando.charAt(0) + "Aquí mero");
                if ( verificarBaseNumerica( operando.charAt( 0 ) ) ) {
                    numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) );
                    if( numero <= RANGO_MAXIMO_IDX2_INDIRECTO && numero >= RANGO_MINIMO_IDX2_INDIRECTO && registroCorrecto ) {
                        //return true;
                        reporte.setError(false);
                        reporte.setMensaje_error("");
                        reporte.setModo_direccionamiento("IDX2");
                        return reporte;
                    }    else {
                        reporte.setError(true);
                        reporte.setModo_direccionamiento("IDX2");
                        reporte.setMensaje_error("Operando fuera de rango para IDX2");
                    }
                }
                reporte.setError(true);
                reporte.setModo_direccionamiento("IDX2");
                reporte.setMensaje_error("No se reconoce la base numérica");

            }
            reporte.setError(true);
            reporte.setModo_direccionamiento("IDX2");
            reporte.setMensaje_error("Operando fuera de rango para IDX2");
            //return false;
            return reporte;


    }

    public static ReporteModoDireccionamiento esIDX2Indirecto(String operando, ReporteModoDireccionamiento reporte) {

        char base_numerica = operando.charAt( 1 );
        int numero;
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;
        reporte.setModo_direccionamiento("IDX2 indirecto");

        if ( operando.startsWith("[") && operando.endsWith("]") ) {
            for( int index = 0; index < REGISTROS_IDX.length; index++ ) {
                if( tokensIDX[1].contains( REGISTROS_IDX[index] )   ){
                    registroCorrecto = true;
                    break;
                }
            }

            if ( esDigito( tokensIDX[0].charAt(1) ) ) {
                numero = Integer.parseInt( tokensIDX[0].substring(1) );// o el substring con 1?
                if( numero <= RANGO_MAXIMO_IDX2_INDIRECTO && numero >= RANGO_MINIMO_IDX2_INDIRECTO && registroCorrecto) {
                    //return true;
                    reporte.setError(NI_MERGAS);
                    reporte.setMensaje_error( "" );
                    return reporte;
                } else {
                    reporte.setError(SIMON_QUE_SI);
                    reporte.setMensaje_error( " operando fuera de rango para IDX2 indirecto" );

                    return reporte;
                }
            } else if ( verificarBaseNumerica( operando.charAt( 0 ) ) ) {
                numero = cambiaBaseNumericaDecimal( operando.charAt( 1 ), operando.substring(1) );
                if( numero <= RANGO_MAXIMO_IDX2_INDIRECTO && numero >= RANGO_MINIMO_IDX2_INDIRECTO && registroCorrecto) {
                    //return true;
                    reporte.setError(NI_MERGAS);
                    reporte.setMensaje_error( "" );
                    reporte.setModo_direccionamiento("IDX2 indirecto");
                    return reporte;
                }
                else {
                    reporte.setError( SIMON_QUE_SI );
                    reporte.setMensaje_error( " operando fuera de rango para IDX2 indirecto" );
                    reporte.setModo_direccionamiento("IDX2 indirecto");
                    return reporte;
                }
            }
            reporte.setError( SIMON_QUE_SI );
            reporte.setMensaje_error( " Operando fuera de rango" );
            reporte.setModo_direccionamiento("IDX2 indirecto");
            //return false;
            return reporte;

        } else {
            reporte.setMensaje_error( " El operando debe iniciar y terminar con corchetes repectivamente" );
            reporte.setError( SIMON_QUE_SI );
            reporte.setModo_direccionamiento("IDX2 indirecto");
            //return false;
            return reporte;
        }


    }


    public static ReporteModoDireccionamiento esIDXPrePost( String operando, ReporteModoDireccionamiento reporte ) {
        char base_numerica = operando.charAt( 1 );
        int numero;
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;

        for( int index = 0; index < REGISTROS_INCREMENTO_DECREMENTO.length; index++ ) {
            if( tokensIDX[1].equals( REGISTROS_INCREMENTO_DECREMENTO[index] )  ){
                registroCorrecto = true;
                break;
            }
        }
        if ( registroCorrecto ) {
            if ( esDigito( tokensIDX[0].charAt(0) ) ) {
                numero = Integer.parseInt( tokensIDX[0] );
                if( numero <= RANGO_MAXIMO_IDX_PRE_POST && numero >= RANGO_MINIMO_IDX_PRE_POST && registroCorrecto ) {
                    //return true;
                    reporte.setError(NI_MERGAS);
                    reporte.setMensaje_error("");
                    reporte.setModo_direccionamiento("IDX pre / post");
                    return reporte;
                }
            } else if ( verificarBaseNumerica( tokensIDX[0].charAt(0) ) ) {
                numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) );
                if( numero <= RANGO_MAXIMO_IDX && numero >= RANGO_MINIMO_IDX && registroCorrecto) {
                    //return true;
                    reporte.setError(NI_MERGAS);
                    reporte.setMensaje_error("");
                    reporte.setModo_direccionamiento("IDX pre / post");
                    return reporte;
                } else {
                    reporte.setError( SIMON_QUE_SI );
                    reporte.setMensaje_error( " Operando fuera de rango para IDX pre /post" );
                    //return false;
                    reporte.setModo_direccionamiento("IDX pre / post");
                    return reporte;
                }
            }
        }
         reporte.setError(SIMON_QUE_SI);
         reporte.setMensaje_error(" El registro no se reconoce IDX pre / post");
        reporte.setModo_direccionamiento("IDX pre / post");

            //return false;

        return reporte;
    }

    public static ReporteModoDireccionamiento esIDXAcumulador( String operando, ReporteModoDireccionamiento reporte ) {
        String[] operando_dividido = operando.split( "," );


        if( operando_dividido[0].equals( "A" )  || operando_dividido[0].equals( "B" )  || operando_dividido[0].equals( "D" )  ) {
            for( short index = 0; index < REGISTROS_IDX.length; index++ ) {
                if( operando_dividido[1].equals( REGISTROS_IDX[index] )  ) {
                    //return true;
                    reporte.setError( NI_MERGAS );
                    reporte.setMensaje_error( "" );
                    reporte.setModo_direccionamiento("IDX acumulador");
                    return reporte;
                }

            }
            reporte.setError( true );
            reporte.setMensaje_error( " El registro no se reconoce " );
            reporte.setModo_direccionamiento("IDX acumulador");
            return reporte;
        }
        reporte.setError(SIMON_QUE_SI);
        reporte.setMensaje_error( "  Acumulador es diferente a A,B y D" );
        reporte.setModo_direccionamiento("IDX acumulador");
        //return false;
        return reporte;

    }

    public static ReporteModoDireccionamiento esIDXAcumuladorIndirecto(String operando, ReporteModoDireccionamiento reporte) {
        String[] operando_dividido = operando.split( "," );
        if( operando_dividido[0].equals( "[D" )  ) {
            for( short index = 0; index < REGISTROS_IDX.length; index++ ) {
                if( operando_dividido[1].equals(REGISTROS_IDX[index]+"]") ) {
                    //return true;
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    reporte.setModo_direccionamiento("IDX acumulador indirecto");
                    return reporte;
                }
            }
            reporte.setError( true );
            reporte.setMensaje_error( " El registro no se reconoce " );
            reporte.setModo_direccionamiento(" IDXAcumulador indirecto");
            return reporte;
        }
        reporte.setError( true );
        reporte.setModo_direccionamiento("IDX");
        reporte.setMensaje_error( " El acumulador no es D y sólo este es aceptado" );

        //return false;
        return reporte;

    }
    // se tendría que verificar el codop antes de esta función, ya que los codops de 16 bits comienzan con l
    public static ReporteModoDireccionamiento esRelativo8(String operando, ReporteModoDireccionamiento reporte) {
        int numero;
        if( esDigito( operando.charAt( 0 )  ) ) {
            numero = Integer.parseInt( operando );
            if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                //return true;
                reporte.setError( false );
                reporte.setMensaje_error( "" );
                reporte.setModo_direccionamiento("REL8");
                return reporte;
            }else {
                reporte.setError( true );
                reporte.setMensaje_error( "  Operando fuera de rango" );
                reporte.setModo_direccionamiento("REL8");
                return reporte;
            }
        } else if ( verificarBaseNumerica( operando.charAt( 0 ) ) )  {

            numero = cambiaBaseNumericaDecimal( operando.charAt(0) , operando.substring(1) );
            if ( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                //return true;
                reporte.setError( false );
                reporte.setMensaje_error( "" );
                reporte.setModo_direccionamiento("REL8");
                return reporte;
            } else {
                reporte.setError( true );
                reporte.setMensaje_error( "  Operando fuera de rango" );
                reporte.setModo_direccionamiento("REL8");
                return reporte;
            }

        } else if (Validador.es_etiqueta(operando)) {
            reporte.setError(false);
            reporte.setMensaje_error("");
            reporte.setModo_direccionamiento("REL8");
            return reporte;
        }
            reporte.setError(true);
            reporte.setMensaje_error( " No se reconoce la base numerica" );
            reporte.setModo_direccionamiento("REL8");
            return reporte;

        //return false;

    }

    public static ReporteModoDireccionamiento esRelativo16(String operando, ReporteModoDireccionamiento reporte) {

        int numero;
        if( esDigito( operando.charAt( 0 )  ) ) {
            numero = Integer.parseInt( operando );
            if( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                //return true;
                reporte.setError(false);
                reporte.setMensaje_error("");

                return reporte;
            } else {
                reporte.setError( true );
                reporte.setMensaje_error( "operando fuera de rango" );
                reporte.setModo_direccionamiento("REL16");
            }
        } else if ( verificarBaseNumerica( operando.charAt( 0 ) ) ) {
            numero = cambiaBaseNumericaDecimal( operando.charAt(0) , operando.substring(1) );
            if ( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                //return true;
                reporte.setError(false);
                reporte.setMensaje_error("");
                reporte.setModo_direccionamiento("REL16");
                return reporte;
            } else {
                reporte.setError( true );
                reporte.setMensaje_error( "operando fuera de rango " );
                reporte.setModo_direccionamiento("REL16");
                return reporte;
            }
        }else if ( Validador.es_etiqueta( operando ) ) {
            reporte.setModo_direccionamiento("REL16");
            reporte.setMensaje_error("");
            reporte.setError(false);
            //return false;
            return reporte;
        }
            reporte.setModo_direccionamiento("REL16");
            reporte.setMensaje_error(" No se reconoce la base numérica");
            reporte.setError(true);
            //return false;
            return reporte;

    }

    public static boolean verificarBaseNumerica( char base_numerica ) {

        switch ( base_numerica ) {
            //octal
            case '@':
                return true;

            //Hexadecimal
            case '$':
                return true;

            //binario
            case '%':
                return true;

            default:
                return false;
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
