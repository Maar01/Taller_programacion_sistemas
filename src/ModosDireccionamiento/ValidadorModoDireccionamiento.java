package ModosDireccionamiento;

/**
 * Created by mario on 6/10/16.
 */
public final class ValidadorModoDireccionamiento {

    private static short RANGO_MINIMO_IMM8_REL8_IDX1 = -256;
    private static short RANGO_MAXIMO_IMM8_REL8_IDX1 = 255;
    private static char CARACTER_INICIAL_IMM = '#';

    private static short RANGO_MINIMO_DIR = 0;
    private static short RANGO_MAXIMO_DIR = 255;

    private static short RANGO_MINIMO_EXT_REL16 = -32768;
    private static   int RANGO_MAXIMO_EXT_REL16 = 65535;

    private static byte RANGO_MINIMO_IDX = -16;
    private static byte RANGO_MAXIMO_IDX = 15;

    private static byte RANGO_MINIMO_IDX_PRE_POST = 8; //para la verificacion de este tipo se podrían tomar
    private static byte RANGO_MAXIMO_IDX_PRE_POST = 1; //Valores absolutos -8 a -1 y 1 a 8

    private static char[] IDX_ACUMULADOR = { 'A', 'B', 'D' };
    private static byte RANGO_MAXIMO_IDX_ACUMULADOR = 15;

    private static short RANGO_MINIMO_IDX2_INDIRECTO = -32768;
    private static int RANGO_MAXIMO_IDX2_INDIRECTO = 65535;


/** para convertir a diferentes bases númericas
 * temp1 = Integer.parseInt(display.getText().trim(), 16 );
 */

    public static String prueba(){
        return "test";
    }


}
