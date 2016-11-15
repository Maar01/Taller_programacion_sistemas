package ModosDireccionamiento;

import Tokens.Validador;

/**
 * Created by mario on 6/10/16.
 *
 * Clase que contiene  lo necesario para realizar las verificaciones y determinar el
 * Modo de direccionamiento de un operando.
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

    private final static byte RANGO_MINIMO_IDX_PRE_POST = 8;
    private final static byte RANGO_MAXIMO_IDX_PRE_POST = 1;

    private final static char[] IDX_ACUMULADOR = { 'A', 'B', 'D' };
    private final static byte RANGO_MAXIMO_IDX_ACUMULADOR = 15;

    private final static short RANGO_MINIMO_IDX2_INDIRECTO = 0;
    private final static   int RANGO_MAXIMO_IDX2_INDIRECTO = 65535;

    private final static boolean    NI_MERGAS = false;
    private final static boolean SIMON_QUE_SI = true;


    private final static char[] simbolos_hex =
            {'0', '1', '2', '3', '4', '5',
             '6', '7', '8', '9', 'a', 'b',
             'c', 'd', 'e', 'f', 'A', 'B',
                       'C', 'D', 'E', 'F'};

    private final static String REGISTROS_IDX[] = {"X", "Y", "SP", "PC"};


    public final static String REGISTROS_INCREMENTO_DECREMENTO[] =
            { "X+", "Y+", "SP+",
              "X-", "Y-", "SP-",
              "+X", "+Y", "+SP",
              "-X", "-Y", "-SP" };


    /**
     *  Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento Inherente.
     * @param operando
     * @param reporte
     * @return
     */
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

    /**
     *  Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento Inmediato de 8 bits.
     *
     * @param operando
     * @param reporte
     * @return
     */
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
                    reporte.setMensaje_error("Operando fuera de rango para IMM8");
                    return reporte;
                }
            } else {
                if( verificarBaseNumerica( operando.charAt( 1 ) ) ){
                    try{
                        numero = cambiaBaseNumericaDecimal( operando.charAt( 1 ), operando.substring(2) );
                    }catch (NumberFormatException error){
                        reporte.setError(true);
                        reporte.setError_final(true);
                        reporte.setMensaje_error("Error en el formato númerico del operando");
                        return reporte;
                    }

                    if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                        //return true;
                        reporte.setError(false);
                        reporte.setMensaje_error("");
                        return reporte;
                    } else {

                        reporte.setError(true);
                        reporte.setMensaje_error("Operando fuera de rango para IMM8");
                        return reporte;
                    }
                } else {
                    reporte.setError(true);
                    reporte.setMensaje_error("Base numerica no reconocida");
                }

            }
        }
        reporte.setMensaje_error(" El operando debe iniciar con #");
        reporte.setError(true);

        return reporte;
    }

    /**
     *  Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento Inmediato de 16 bits.
     * @param operando
     * @param reporte
     * @return
     */
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
                    reporte.setMensaje_error("Operando fuera de rango para IMM16");
                    reporte.setError_final(true);
                    return reporte;
                }
            }else {
                if( verificarBaseNumerica( operando.charAt( 1 ) ) ) {
                    try{
                        numero = cambiaBaseNumericaDecimal(operando.charAt(1), operando.substring(2));

                    }catch (NumberFormatException error){
                        reporte.setError(true);
                        reporte.setError_final(true);
                        reporte.setMensaje_error("Error en el formato númerico del operando");
                        return reporte;
                    }

                    if (numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16) {
                        //return true;
                        reporte.setError(false);
                        reporte.setMensaje_error("");
                        return reporte;
                    }
                    else {
                        reporte.setError(true);
                        reporte.setMensaje_error("Operando fuera de rango para IMM16");
                        reporte.setError_final(true);
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

    /**
     *Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento Directo.
     * @param operando
     * @param reporte
     * @return
     */
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
                reporte.setMensaje_error("Operando fuera de rango para DIR");
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
                reporte.setMensaje_error("Operando fuera de rango para DIR");
                return reporte;
            }
        }
        reporte.setError(true);
        if(  !operando.contains(",") ) {
            reporte.setMensaje_error("Base numerica no reconocida");
        }else {
            reporte.setMensaje_error("No debe contener cooma ,");
        }

        return reporte;
    }

    /**
     * Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento Extendido.
     *
     * @param operando
     * @param reporte
     * @return
     */
    public static ReporteModoDireccionamiento esExtendido(String operando, ReporteModoDireccionamiento reporte) {
        int numero;
        reporte.setModo_direccionamiento("EXT");

        if( !operando.contains( "," ) ) {
            if ( esDigito( operando.charAt( 0 ) )  ) {
                numero = Integer.parseInt( operando.substring(1) );
                if( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    return reporte;
                }
                else {
                    reporte.setError(true);
                    reporte.setMensaje_error("Operando fuera de rango para extendido");
                    reporte.setError_final(true);
                    return reporte;
                }
            } else if ( verificarBaseNumerica( operando.charAt( 0 ) )  ) {
                //posible error en cada método de es*();
                try{
                    numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) );//el substring comienza del 0 o 1?
                }catch ( NumberFormatException format ){
                    try {
                        numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1).split(",")[0] );//el substring comienza del 0 o 1?
                    } catch (NumberFormatException Error){
                        reporte.setError(true);
                        reporte.setError_final(true);
                        reporte.setMensaje_error("Error en el formato numerico del operando ");
                        return reporte;
                    }

                }

                if ( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    return reporte;

                } else {
                    reporte.setError(true);
                    reporte.setError_final(true);
                    reporte.setMensaje_error(" Operando fuera de rango para EXT ");
                    return reporte;
                }
            }else if (Validador.es_etiqueta( operando )) {
                reporte.setError( false );
                reporte.setMensaje_error("");
                return reporte;
            }
            reporte.setError(true);
            reporte.setMensaje_error( " Base númerica no reconocida" );
        }
        reporte.setError(true);
        reporte.setMensaje_error( " Operando inválido para EXT" );

        return reporte;
    }

    /**
     *Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento IDX.
     * @param operando
     * @param reporte
     * @return
     */
    public static ReporteModoDireccionamiento esIDX(String operando, ReporteModoDireccionamiento reporte) {

        int numero;
        if ( operando.startsWith(",") ) {
            operando = "0" + operando;
        }
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;
        reporte.setModo_direccionamiento("IDX");

        if( tokensIDX.length > 1  && operando.length() > 1){
            for( int index = 0; index < REGISTROS_IDX.length; index++ ) {
                if( tokensIDX[1].toUpperCase().equals( REGISTROS_IDX[index] )  ){
                    registroCorrecto = true;
                    break;
                }
            }
        }

        if ( tokensIDX.length > 2){
            reporte.setError(true);
            reporte.setError_final( true );
            reporte.setMensaje_error(" Operando erroneo para IDX ");
            return  reporte;
        }


        if ( registroCorrecto  && tokensIDX[0].equals( "" )  ) {
            reporte.setError(true);
            reporte.setError_final( true );
            reporte.setMensaje_error(" Operando incompleto para IDX ");
            return  reporte;

        } else if  ( registroCorrecto ) {

            if ( esDigito( tokensIDX[0].charAt(0) ) ) {
                numero = Integer.parseInt( tokensIDX[0] );
                if( numero <= RANGO_MAXIMO_IDX && numero >= RANGO_MINIMO_IDX  && tokensIDX.length < 6 ) {
                    reporte.setError(false);
                    reporte.setMensaje_error("");

                    return reporte;
                } else {
                    reporte.setError(true);
                    reporte.setMensaje_error("Operando fuera de rango para IDX");
                    return reporte;
                }
            } else if( operando.charAt( 0 ) == '-' ) {
                //aquí tendría que aplicar algo de complemento a 2

            } else if ( verificarBaseNumerica( operando.charAt(0) ) ) {
                if ( tokensIDX[0].length() < 16 ) {
                    numero = cambiaBaseNumericaDecimal( tokensIDX[0].charAt( 0 ), tokensIDX[0].substring(1) );

                    if( numero <= RANGO_MAXIMO_IDX && numero >= RANGO_MINIMO_IDX  ) {

                        reporte.setError(false);
                        reporte.setMensaje_error("");
                        return reporte;
                    } else {
                        reporte.setError(true);
                        reporte.setMensaje_error("Operando fuera de rango para IDX");
                        return reporte;
                    }
                } else {
                    reporte.setError(true);
                    reporte.setMensaje_error(" Operando fuera de rango para IDX");
                    return reporte;
                }

            }
            reporte.setError(true);
            reporte.setMensaje_error(" base númerica no reconocida ");
            return reporte;

        }

        reporte.setError(true);
        reporte.setMensaje_error(" Registro no reconocido ");
        return reporte;
    }

    /**
     * Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento IDX1
     *
     * @param operando
     * @param reporte
     * @return
     */
    public static ReporteModoDireccionamiento esIDX1(String operando, ReporteModoDireccionamiento reporte) {


        int numero;
        if ( operando.startsWith(",") ) {
            operando = "0" + operando;
        }
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

        if( operando.length() <= 1 && operando.equals(",")  ) {

        } else {
            if ( tokensIDX[0].equals( "" ) && registroCorrecto ) {

                reporte.setError(false);
                reporte.setMensaje_error("");
                reporte.setModo_direccionamiento( "IDX1" );
                return reporte;

            }else if  ( registroCorrecto ) {
                if ( esDigito( tokensIDX[0].charAt(0) ) ) {
                    numero = Integer.parseInt( tokensIDX[0] );
                    if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {

                        reporte.setError(false);
                        reporte.setMensaje_error("");
                        reporte.setModo_direccionamiento( "IDX1" );
                        return reporte;
                    }
                }else if( operando.charAt( 0 ) == '-' ) {
                    //aquí tendría que aplicar algo de complemento a 2

                }else if (verificarBaseNumerica( operando.charAt( 0 ) ) ) {
                    if ( tokensIDX[0].length() <= 16 ) {
                        numero = cambiaBaseNumericaDecimal( tokensIDX[0].charAt( 0 ), tokensIDX[0].substring(1) );
                        if( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                            reporte.setError(false);
                            reporte.setMensaje_error("");
                            reporte.setModo_direccionamiento( "IDX1" );
                            return reporte;
                        } else {
                            reporte.setError(true);
                            reporte.setMensaje_error( " operando fuera de rango para IDX1 " );
                            reporte.setModo_direccionamiento( "IDX1" );
                            return reporte;
                        }
                    } else {
                        reporte.setError(true);
                        reporte.setMensaje_error( " operando fuera de rango IDX1 " );
                        reporte.setModo_direccionamiento( "IDX1" );
                        return reporte;
                    }
                }
                reporte.setMensaje_error( " base númerica no reconocida" );
                reporte.setModo_direccionamiento( "IDX1" );
                reporte.setError(true);
                return reporte;

            }
            reporte.setError(true);
            reporte.setModo_direccionamiento( "IDX1" );
            reporte.setMensaje_error(" Registro no reconocido ");
            return reporte;
        }

        reporte.setError(true);
        reporte.setModo_direccionamiento( "IDX1" );
        reporte.setMensaje_error(" Valor no válido para operando ");
        return reporte;

    }

    /**Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento IDX2
     *
     * @param operando
     * @param reporte
     * @return
     */
    public static ReporteModoDireccionamiento esIDX2(String operando, ReporteModoDireccionamiento reporte) {

        int numero;
        if ( operando.startsWith(",") ) {
            operando = "0" + operando;
        }
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
        if ( !operando.equals( "," ) ) {
            if ( esDigito( tokensIDX[0].charAt(0) ) ) {
                numero = Integer.parseInt( tokensIDX[0] );
                if( numero <= RANGO_MAXIMO_IDX2_INDIRECTO && numero >= RANGO_MINIMO_IDX2_INDIRECTO && registroCorrecto ) {
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    return reporte;
                } else {
                    reporte.setError(true);
                    reporte.setMensaje_error("Operando fuera de rango para IDX2");
                    reporte.setError_final(true);
                    return reporte;
                }
            } else {

                if ( verificarBaseNumerica( operando.charAt( 0 ) ) ) {
                    if (tokensIDX[0].length() < 16){
                        System.out.println(tokensIDX[0].substring(1));
                        numero = cambiaBaseNumericaDecimal( tokensIDX[0].charAt( 0 ), tokensIDX[0].substring(1) );
                        if( numero <= RANGO_MAXIMO_IDX2_INDIRECTO && numero >= RANGO_MINIMO_IDX2_INDIRECTO && registroCorrecto ) {
                            reporte.setError(false);
                            reporte.setMensaje_error("");
                            reporte.setModo_direccionamiento("IDX2");
                            return reporte;
                        }    else if ( !registroCorrecto ) {
                            reporte.setError(true);
                            reporte.setModo_direccionamiento("IDX2");
                            reporte.setMensaje_error(" El registro no se reconoce");
                            reporte.setError_final(true);
                            return reporte;
                        } else {
                            reporte.setError(true);
                            reporte.setModo_direccionamiento("IDX2");
                            reporte.setMensaje_error("Operando fuera de rango para IDX2");
                            reporte.setError_final(true);
                            return reporte;
                        }

                    }else{
                        reporte.setError(true);
                        reporte.setModo_direccionamiento("IDX2");
                        reporte.setMensaje_error("Operando fuera de rango para IDX2");
                        reporte.setError_final(true);
                        return reporte;
                    }

                }
                reporte.setError(true);
                reporte.setModo_direccionamiento("IDX2");
                reporte.setMensaje_error("No se reconoce la base numérica");
                return reporte;

            }

        }

        reporte.setError(true);
        reporte.setModo_direccionamiento("IDX2");
        reporte.setMensaje_error(" Formato de operando incorrecto para IDX2");
        return reporte;
    }

    /**
     * Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento IDX Indirecto.
     *
     * @param operando
     * @param reporte
     * @return
     */

    public static ReporteModoDireccionamiento esIDX2Indirecto(String operando, ReporteModoDireccionamiento reporte) {

        int numero;
        if ( operando.startsWith(",") ) {
            operando = "0" + operando;
        }
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;
        reporte.setModo_direccionamiento("IDX2 indirecto");

        if ( operando.startsWith("[") && operando.endsWith("]") && tokensIDX.length > 1 ) {
            for( int index = 0; index < REGISTROS_IDX.length; index++ ) {
                if( tokensIDX[1].contains( REGISTROS_IDX[index] )   ){
                    registroCorrecto = true;
                    break;
                }
            }
            if ( tokensIDX[0].length() > 1 ) {
                if ( esDigito( tokensIDX[0].charAt(1) ) ) {
                    numero = Integer.parseInt( tokensIDX[0].substring(1) );// o el substring con 1?
                    if( numero <= RANGO_MAXIMO_IDX2_INDIRECTO && numero >= RANGO_MINIMO_IDX2_INDIRECTO && registroCorrecto) {
                        reporte.setError(false);
                        reporte.setMensaje_error( "" );
                        return reporte;
                    } else {
                        reporte.setError( true );
                        reporte.setMensaje_error( " operando fuera de rango para IDX2 indirecto" );
                        reporte.setError_final(true);
                        return reporte;
                    }
                } else if ( verificarBaseNumerica( operando.charAt( 0 ) ) ) {
                    numero = cambiaBaseNumericaDecimal( operando.charAt( 1 ), operando.substring(1) );
                    if( numero <= RANGO_MAXIMO_IDX2_INDIRECTO && numero >= RANGO_MINIMO_IDX2_INDIRECTO && registroCorrecto) {
                        reporte.setError(false);
                        reporte.setMensaje_error( "" );
                        reporte.setModo_direccionamiento("IDX2 indirecto");
                        return reporte;
                    }
                    else {
                        reporte.setError( true );
                        reporte.setMensaje_error( " operando fuera de rango para IDX2 indirecto" );
                        reporte.setModo_direccionamiento("IDX2 indirecto");
                        reporte.setError_final(true);
                        return reporte;
                    }
                }
                reporte.setError( true );
                reporte.setMensaje_error( " Operando fuera de rango para IDX2 indirecto" );
                reporte.setModo_direccionamiento("IDX2 indirecto");
                return reporte;

            } else {
                reporte.setError( true );
                reporte.setMensaje_error( " Error en el formato del operando " );
                reporte.setModo_direccionamiento("IDX2 indirecto");
                return reporte;
            }

        } else {

            reporte.setMensaje_error( " El operando debe iniciar y terminar con corchetes repectivamente" );
            reporte.setError( true );
            reporte.setModo_direccionamiento("IDX2 indirecto");

            return reporte;
        }

    }

    /**
     *  Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento IDX Pre/post.
     *
     * @param operando
     * @param reporte
     * @return
     */
    public static ReporteModoDireccionamiento esIDXPrePost( String operando, ReporteModoDireccionamiento reporte ) {
        int numero;
        if ( operando.startsWith(",") ) {
            operando = "0" + operando;
        }
        String[] tokensIDX = operando.split( "," );
        boolean registroCorrecto = false;

        if( tokensIDX.length > 1  ) {
            for( int index = 0; index < REGISTROS_INCREMENTO_DECREMENTO.length; index++ ) {
                if( tokensIDX[1].equals( REGISTROS_INCREMENTO_DECREMENTO[index] )  ){
                    registroCorrecto = true;
                    break;
                }
            }
        }

        if ( registroCorrecto ) {
            if ( esDigito( tokensIDX[0].charAt(0) ) ) {
                numero = Integer.parseInt( tokensIDX[0] );
                if( numero <= RANGO_MAXIMO_IDX_PRE_POST && numero >= RANGO_MINIMO_IDX_PRE_POST && registroCorrecto ) {
                    reporte.setError(true);
                    reporte.setMensaje_error("");
                    reporte.setModo_direccionamiento("IDX pre / post");
                    return reporte;
                } else {
                    reporte.setError(true);
                    reporte.setMensaje_error("operador fuera de rango para IDX pre/post");
                    reporte.setModo_direccionamiento("IDX pre / post");
                    reporte.setError_final(true);
                    return reporte;

                }
            } else if ( verificarBaseNumerica( tokensIDX[0].charAt(0) ) ) {
                numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring(1) );
                if( numero <= RANGO_MAXIMO_IDX && numero >= RANGO_MINIMO_IDX && registroCorrecto) {
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    reporte.setModo_direccionamiento("IDX pre / post");
                    return reporte;
                } else {
                    reporte.setError( true );
                    reporte.setMensaje_error( " Operando fuera de rango para IDX pre /post" );
                    reporte.setError_final(true);
                    reporte.setModo_direccionamiento("IDX pre / post");
                    return reporte;
                }
            }

        }

        if ( operando.contains("+" ) || operando.contains("-") ) {
            reporte.setError(true);
            reporte.setMensaje_error(" El registro no se reconoce IDX pre / post");
            reporte.setModo_direccionamiento("IDX pre / post");
            reporte.setError_final(true);
        } else {
            reporte.setError(true);
            reporte.setMensaje_error(" El registro no se reconoce IDX pre / post");
            reporte.setModo_direccionamiento("IDX pre / post");
        }

        return reporte;
    }

    /**
     *  Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento IDX acumulador.
     * @param operando
     * @param reporte
     * @return
     */
    public static ReporteModoDireccionamiento esIDXAcumulador( String operando, ReporteModoDireccionamiento reporte ) {
        if ( operando.startsWith(",") ) {
            operando = "0" + operando;
        }
        String[] operando_dividido = operando.split( "," );

        if( operando.length() > 1 ){
            if( ( operando_dividido[0].equals( "A" )  || operando_dividido[0].equals( "B" )  || operando_dividido[0].equals( "D" ) ) && operando.length() > 1 ) {
                for( short index = 0; index < REGISTROS_IDX.length; index++ ) {
                    if( operando_dividido[1].equals( REGISTROS_IDX[index] )  ) {
                        reporte.setError( NI_MERGAS );
                        reporte.setMensaje_error( "" );
                        reporte.setModo_direccionamiento("IDX acumulador");
                        return reporte;
                    }

                }
                reporte.setError( true );
                reporte.setMensaje_error( " El registro no se reconoce " );
                reporte.setModo_direccionamiento("IDX acumulador");
                reporte.setError_final(true);
                return reporte;
            }
            reporte.setError(SIMON_QUE_SI);
            reporte.setMensaje_error( "  Acumulador es diferente a A,B y D" );
            reporte.setModo_direccionamiento("IDX acumulador");
            return reporte;
        }

        reporte.setError( true );
        reporte.setMensaje_error( "  operando incompleto " );
        reporte.setModo_direccionamiento("IDX acumulador");
        return reporte;
    }

    /**
     *  Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento IDX de acumulador.
     *
     * @param operando
     * @param reporte
     * @return
     */

    public static ReporteModoDireccionamiento esIDXAcumuladorIndirecto(String operando, ReporteModoDireccionamiento reporte) {
        if ( operando.startsWith(",") ) {
            operando = "0" + operando;
        }
        String[] operando_dividido = operando.split( "," );
        if ( operando.equals(",") ) {
            reporte.setError( true );
            reporte.setModo_direccionamiento("IDX");
            reporte.setMensaje_error( " Error en el operando" );
            return reporte;
        }
        if( operando_dividido[0].equals( "[D" )  ) {
            for( short index = 0; index < REGISTROS_IDX.length; index++ ) {
                if( operando_dividido[1].equals(REGISTROS_IDX[index]+"]") ) {
                    reporte.setError(false);
                    reporte.setMensaje_error("");
                    reporte.setModo_direccionamiento("IDX acumulador indirecto");
                    return reporte;
                }
            }
            reporte.setError( true );
            reporte.setMensaje_error( " El registro no se reconoce para IDX de acumulador indirecto" );
            reporte.setModo_direccionamiento(" IDXAcumulador indirecto");
            reporte.setError_final(true);
            return reporte;
        }
        reporte.setError( true );
        reporte.setModo_direccionamiento("IDX");
        reporte.setMensaje_error( " El acumulador no es D y sólo este es aceptado" );

        return reporte;
    }
    // se tendría que verificar el codop antes de esta función, ya que los codops de 16 bits comienzan con l

    /**
     *  Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento Relativo de 8 bits
     *
     * @param operando
     * @param reporte
     * @return
     */
    public static ReporteModoDireccionamiento esRelativo8(String operando, ReporteModoDireccionamiento reporte) {
        int numero;
        if( esDigito( operando.charAt( 0 )  ) ) {
            numero = Integer.parseInt( operando );
            if ( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                reporte.setError( false );
                reporte.setMensaje_error( "" );
                reporte.setModo_direccionamiento("REL8");
                return reporte;
            } else {
                reporte.setError( true );
                reporte.setMensaje_error( "  Operando fuera de rango para REL8" );
                reporte.setModo_direccionamiento("REL8");
                return reporte;
            }
        } else if ( verificarBaseNumerica( operando.charAt( 0 ) ) )  {

            numero = cambiaBaseNumericaDecimal( operando.charAt(0) , operando.substring(1) );
            if ( numero <= RANGO_MAXIMO_IMM8_REL8_IDX1 && numero >= RANGO_MINIMO_IMM8_REL8_IDX1 ) {
                reporte.setError( false );
                reporte.setMensaje_error( "" );
                reporte.setModo_direccionamiento("REL8");
                return reporte;
            } else {
                reporte.setError( true );
                reporte.setMensaje_error( "  Operando fuera de rango para REL8" );
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

    }

    /**
     *  Función encargada de verificar que un operando cumpla con las caracteristicas de un modo
     *  de direccionamiento Relativo de 16 bits
     * @param operando
     * @param reporte
     * @return
     */
    public static ReporteModoDireccionamiento esRelativo16(String operando, ReporteModoDireccionamiento reporte) {

        int numero;
        if( esDigito( operando.charAt( 0 )  ) ) {
            numero = Integer.parseInt( operando );
            if( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                reporte.setError(false);
                reporte.setMensaje_error("");

                return reporte;
            } else {
                reporte.setError( true );
                reporte.setMensaje_error( " operando fuera de rango para REL16" );
                reporte.setError_final(true);
                reporte.setModo_direccionamiento("REL16");
                return reporte;
            }

        } else if ( verificarBaseNumerica( operando.charAt( 0 ) ) ) {

            numero = cambiaBaseNumericaDecimal( operando.charAt(0) , operando.substring(1) );
            if ( numero <= RANGO_MAXIMO_EXT_REL16_IMM16 && numero >= RANGO_MINIMO_EXT_REL16_IMM16 ) {
                reporte.setError(false);
                reporte.setMensaje_error("");
                reporte.setModo_direccionamiento("REL16");
                return reporte;
            } else {
                reporte.setError( true );
                reporte.setMensaje_error( "operando fuera de rango para REL16" );
                reporte.setError_final(true);
                reporte.setModo_direccionamiento("REL16");
                return reporte;
            }
        } else if ( Validador.es_etiqueta( operando ) ) {
            reporte.setModo_direccionamiento("REL16");
            reporte.setMensaje_error("");
            reporte.setError(false);
            return reporte;
        }
            reporte.setModo_direccionamiento("REL16");
            reporte.setMensaje_error(" No se reconoce la base numérica");
            reporte.setError(true);
            return reporte;
    }

    /**
     * Función encargada de verificar la base numérica de un String, según los caracteres
     * aceptados para definir las bases.
     * @param base_numerica
     * @return
     */

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

    /**
     * Función encargada de cambiar un número en una base diferente a la decimal
     * a su valor en base decimal.
     * @param base_numerica
     * @param numero
     * @return
     */

    public static Integer cambiaBaseNumericaDecimal( char base_numerica, String numero  )  {

        switch ( base_numerica ) {
            //octal
            case '@':
                if( esOctal( numero )  ) {
                       return Integer.parseInt( numero, 8 );
                } else if ( esOctal( numero ) && numero.startsWith("7") ) {
                    //entonces es un número negativo, hay que convertirlo
                    Integer impresion = (Integer.parseInt( numero, 8 ) );
                    System.out.println( impresion + "Impresion" );
                    return (~Integer.parseInt( numero.substring(1), 8 ) )+ 1;
                }
                break;
            //Hexadecimal
            case '$':
                if ( esHexadecimal( numero ) ) {
                    return  Integer.parseInt(numero, 16);
                } else if ( esHexadecimal( numero ) && numero.startsWith("F")) {
                       return (~Integer.parseInt( numero.substring(1), 16 ) )+ 1;
                }
                break;
            //binario
            case '%':

                if ( esBinario( numero ) ) {
                    return Integer.parseInt( numero, 2 );
                } else if ( esBinario(numero) && numero.startsWith("1") ) {
                    return (~Integer.parseInt( numero.substring(1), 2 ) )+ 1;
                }
                break;

            default:
                return Integer.parseInt( numero );

        }
        throw new NumberFormatException();

    }

    /**
     *  Función encargada de verificar que un carácter es un digito.
     * @param digito
     * @return
     */
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
            case '-':
                return true;

            default:
                return false;
        }
    }

    /**
     *  Función encargada de verificar que un String contiene caracteres válidos
     * para un número hexadecimal.
     * @param numero
     * @return
     */

    public static boolean esHexadecimal( String numero ) {

        for( int index = 0; index < numero.length(); index++ ) {
            if ( !esHexadecimal( numero.charAt( index ) ) ) {
                return false;
            }
        }

        return true;
    }

    /**
     *  Función encargada de verificar que un caracter es válido
     *  para un número hexadecimal.
     * @param digito_hex
     * @return boolean
     */

    public static boolean esHexadecimal( char digito_hex ) {

        for( int index = 0; index < simbolos_hex.length; index++ ) {
            if( digito_hex == simbolos_hex[ index ] ){
                return true;
            }
        }
        return false;
    }

    /**
     * Función encargada de verificar que un String contiene caracteres válidos
     * para un número octal.
     * @param numero
     * @return boolean
     */

    public static boolean esOctal( String numero ) {
        for( int index = 0; index < numero.length(); index++ ) {
            if ( Integer.parseInt( numero.charAt( index ) + "" ) >= 8 ) {
                return false;
            }
        }

        return true;
    }

    /**
     * Función encargada de verificar que un String contiene caracteres válidos
     * para un número binario.
     * @param numero
     * @return boolean
     */
    public static boolean esBinario( String numero ) {

        for ( int index = 0; index < numero.length(); index++ ){
            if (  numero.charAt( index ) != '1' &&  numero.charAt( index ) != '0'   ) {
                return false;
            }
        }
        return true;
    }

    /**
     *  Convierte números de 8 bits a complemento a 2 según su base
     * @param numero
     * @param base
     * @return
     */
    public static byte complemento2_8( String numero, short base ){

        return Short.valueOf( numero, base ).byteValue();
    }

    /**
     *  Convierte números de 16 bits a complemento a 2 según su base
     * @param num
     * @param base
     * @return
     */
    public static Short complemento2_16(String num, short base) {

        return Integer.valueOf(num, base).shortValue();
    }
}
