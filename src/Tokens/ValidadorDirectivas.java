package Tokens;


import ModosDireccionamiento.ValidadorModoDireccionamiento;

/**
 * Created by mario on 31/10/16.
 */
public final class ValidadorDirectivas {

    private static final String[] DIRECTIVAS_DE_CONSTANTES_1BYTE = {
            "DB", "DC.B", "FCB"
    };

    private static final String[] DIRECTIVAS_DE_CONSTANTES_2BYTE = {
            "DW", "DC.W", "FDB"
    };

    private static final String DIRECTIVAS_CARACTERES = "FCC";

    private static final String[] DIRECTIVAS_RESERVA_MEMORIA_1BYTE = {
            "DS", "DS.B", "RMB"
    };

    private static final String[] DIRECTIVAS_RESERVA_MEMORIA_2BYTES = {
            "DS.W", "RMW"
    };

    private static final short RANGO_MIN_CONSTANTES_1BYTE = 0;
    private static final short RANGO_MAX_CONSTANTES_1BYTE = 255;

    private static final short RANGO_MIN_CONSTANTES_2BYTE = 0;
    private static final   int RANGO_MAX_CONSTANTES_2BYTE = 65535;

    private static final short RANGO_MIN_RESERVA_MEMORIA = 0;
    private static final int   RANGO_MAX_RESERVA_MEMORIA = 65535;

    private final static char[] simbolos_hex =
            {'0', '1', '2', '3', '4', '5',
                    '6', '7', '8', '9', 'a', 'b',
                    'c', 'd', 'e', 'f', 'A', 'B',
                    'C', 'D', 'E', 'F'};

    public static boolean esDirectivaConstante1( String directiva ) {
        for ( int i = 0; i < DIRECTIVAS_DE_CONSTANTES_1BYTE.length; i++ ) {
            if ( directiva.toUpperCase().equals( DIRECTIVAS_DE_CONSTANTES_1BYTE[i] ) ) {
                return true;
            }
        }
        return false;
    }

    public static boolean esDirectivaConstante2( String directiva ) {
        for ( int i = 0; i < DIRECTIVAS_DE_CONSTANTES_2BYTE.length; i++ ) {
            if ( directiva.toUpperCase().equals( DIRECTIVAS_DE_CONSTANTES_2BYTE[i] ) ) {
                return true;
            }
        }
        return false;
    }

    public static boolean esDirectivaReserva1( String directiva ) {
        for ( int i = 0; i < DIRECTIVAS_RESERVA_MEMORIA_1BYTE.length; i++ )
        {
            if ( DIRECTIVAS_RESERVA_MEMORIA_1BYTE[i].equals( directiva.toUpperCase() ) ) {
                return true;
            }
        }
        return false;
    }

    public static boolean esDirectivaReserva2( String directiva ) {
        for ( int i = 0; i < DIRECTIVAS_RESERVA_MEMORIA_2BYTES.length; i++ )
        {
            if ( DIRECTIVAS_RESERVA_MEMORIA_2BYTES[i].equals( directiva.toUpperCase() ) ) {
                return true;
            }
        }
        return false;
    }

    public static boolean esDirectivaFCC( String directiva ) {
        if ( directiva.toUpperCase().equals( "FCC" ) ) {
            return true;
        }

        return false;
    }

    public static ReporteDirectiva analizaDirectiva( String directiva, String operando  ) {

        ReporteDirectiva reporte = new ReporteDirectiva( directiva );
        int numero = -1;
        reporte.setEs_directiva( false );
        reporte.setOperando( operando );

        /*if( operando.equals("NULL") ) {
            System.out.println( operando + directiva );
            reporte.setEs_error( true );
            reporte.setError( "Las directivas deben llevar operando" );
            reporte.setEs_directiva(true);
            return reporte;
        }*/

        if ( !operando.contains("\"") ) {
            if ( !esDirectivaFCC( directiva )  ) {

                try{
                    numero = cambiaBaseNumericaDecimal( operando.charAt( 0 ), operando.substring( 1 ) );

                } catch (NumberFormatException exception) {
                    if( esDigito( operando.charAt( 0 ) ) ) {
                        numero = Integer.parseInt( operando );

                    } else if ( operando.equals("NULL") ) {
                        reporte.setError( "Sin operando válido" );
                        reporte.setEs_error( true );
                        //return reporte;
                    }else {
                        reporte.setError( "Error en formato de número" );
                        reporte.setEs_error( true );
                        return reporte;
                    }
                }

            } else {
                reporte.setError( "Operando invalido para FCC" );
                reporte.setEs_error( true );
                reporte.setEs_directiva( true );
                return reporte;
            }

        }

        if ( esDirectivaConstante1( directiva ) ) {

            reporte.setEs_directiva( true );
            if ( numero >= RANGO_MIN_CONSTANTES_1BYTE  && numero <= RANGO_MAX_CONSTANTES_1BYTE  ) {
               // reporte.setValor( numero );
                reporte.setValor( operando );
                reporte.setAumentar_contLoc( 1 );
            } else if ( numero == -1 ) {
                reporte.setError( " Sin operando válido." );
                reporte.setEs_error(true);
                return reporte;
            } else {
                reporte.setError( "Número fuera de rango para directiva de constantes 1 byte" );
                reporte.setEs_error(true);
                //reporte.setEs_directiva( true );
                return reporte;
            }

        } else if ( esDirectivaConstante2( directiva ) ) {
            reporte.setEs_directiva( true );
            if ( numero <= RANGO_MAX_CONSTANTES_2BYTE && numero >= RANGO_MIN_CONSTANTES_2BYTE ) {
                reporte.setValor( operando );
                reporte.setAumentar_contLoc( 2 );
            } else if ( numero == -1 ) {
                reporte.setError( " Sin operando válido." );
                reporte.setEs_error(true);
                return reporte;
            } else {
                reporte.setError( "Número fuera de rango para directiva de constantes 2 byte" );
                reporte.setEs_error(true);
                return reporte;
            }

        } else if ( esDirectivaFCC( directiva ) ) {
            reporte.setEs_directiva( true );

            if ( operando.startsWith("\"") && operando.endsWith("\"") && operando.length() > 1 ) {

                reporte.setAumentar_contLoc( operando.length() - 2 );
                reporte.setValor( new Integer( operando.length() - 2 ).toString() ) ;

            } else {
                reporte.setEs_error( true );
                reporte.setError( "Operando para FCC deben iniciar y terminar con comillas" );
                reporte.setEs_directiva( true );
                return reporte;
            }

        } else if ( esDirectivaReserva1( directiva ) ) {
            reporte.setEs_directiva( true );
            if ( numero <= RANGO_MAX_RESERVA_MEMORIA && numero >= RANGO_MIN_RESERVA_MEMORIA ) {
                reporte.setValor( operando );
                reporte.setAumentar_contLoc( numero );
            } else if ( numero == -1 ) {
                reporte.setError( " Sin operando válido." );
                reporte.setEs_error(true);
                return reporte;
            } else {
                reporte.setError( "Fuera de rango para " + directiva );
                reporte.setEs_directiva( true );
                reporte.setEs_error( true );
                return reporte;
            }

        } else if ( esDirectivaReserva2( directiva ) ) {
            reporte.setEs_directiva( true );
            if ( numero <= RANGO_MAX_RESERVA_MEMORIA && numero >= RANGO_MIN_RESERVA_MEMORIA ) {
                reporte.setValor( operando );
                reporte.setAumentar_contLoc( numero * 2 );
            } else if ( numero == -1 ) {
                reporte.setError( " Sin operando válido." );
                reporte.setEs_error(true);
                return reporte;
            }  else {
                reporte.setError( "Fuera de rango para " + directiva );
                reporte.setEs_error( true );
                return reporte;
            }

        } else {
            reporte.setEs_directiva( false );
            reporte.setEs_error( true );
            reporte.setError( "No es directiva ni codigo de operación" );
            return  reporte;
        }

        if( operando.equals("NULL") ) {

            reporte.setEs_error( true );
            reporte.setError( "Las directivas deben llevar operando" );
            reporte.setEs_directiva(true);
            return reporte;
        }


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
                if ( esOctal( numero ) && numero.startsWith("7") ) {
                    //entonces es un número negativo, hay que convertirlo
                    Integer impresion = (Integer.parseInt( numero, 8 ) );

                    return (~Integer.parseInt( numero.substring(1), 8 ) )+ 1;
                }
                if( esOctal( numero )  ) {
                    return Integer.parseInt( numero, 8 );
                }
                break;
            //Hexadecimal
            case '$':
                if ( esHexadecimal( numero ) && numero.toUpperCase().startsWith("F")) {
                    return (~Integer.parseInt( numero.substring(1), 16 ) ) + 1;
                }
                if ( esHexadecimal( numero ) ) {
                    return  Integer.parseInt(numero, 16);
                }
                break;
            //binario
            case '%':
                if ( esBinario(numero) && numero.startsWith("1") ) {
                    return (~Integer.parseInt( numero.substring(1), 2 ) )+ 1;
                }
                if ( esBinario( numero ) ) {
                    return Integer.parseInt( numero, 2 );
                }
                break;

            default:

                return Integer.parseInt( base_numerica+numero );

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


