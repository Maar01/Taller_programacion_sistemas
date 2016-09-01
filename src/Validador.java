import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mario on 28/08/16.
 */
 public final class Validador {

     //REGEX
    private static final String ETIQUETA = "^([a-zA-Z]){1}[\\w]{1,7}";//
    private static final String CODOP = "([a-zA-Z]){1,5}\\.?";//
    private static final String OPERANDO = ".{1,}";//
    private static final String COMENTARIO = "^[;]{1}.*";//

    //Posiciones en arreglo que le corresponde a cada token generado por linea.split()
    public static final byte   POSICION_ETIQUETA = 0;
    public static final byte      POSICION_CODOP = 1;
    public static final byte   POSICION_OPERANDO = 2;
    public static final byte POSICION_COMENTARIO = 3;

    //Tipos de linea
    public static final byte      ETQ_CODOP_OP = 0;
    public static final byte         ETQ_CODOP = 1;
    public static final byte         _CODOP_OP = 2;
    public static final byte           _CODOP_ = 3;
    public static final byte  ETQ_CODOP_OP_COM = 99;
    //Tipos de archivo
    public static final byte ARCHIVO_ERRORES = 0;
    public static final byte     ARCHIVO_ASM = 1;
    public static final byte    ARCHIVO_INST = 2;


    public static boolean  es_comentario(String token){

        Pattern patron = Pattern.compile(COMENTARIO);
        Matcher matcher = patron.matcher(token);

        return matcher.matches();
    }

    public static boolean es_etiqueta(String token){
        Pattern patron = Pattern.compile(ETIQUETA);
        Matcher matcher = patron.matcher(token);

        return matcher.matches();
    }

    public static boolean es_codop(String token){
        Pattern patron = Pattern.compile(CODOP);
        Matcher matcher = patron.matcher(token);

        return matcher.matches();
    }

    public static boolean es_operando(String token){
        Pattern patron = Pattern.compile(OPERANDO);
        Matcher matcher = patron.matcher(token);

        return matcher.matches();
    }

    public static byte tipo_de_linea( String[] tokens ) {
        if( tokens.length == 3 ){
            if( tokens[POSICION_ETIQUETA].equals("") ) {
                return _CODOP_OP;
            }else {
                return ETQ_CODOP_OP;
            }
        }else if( tokens.length == 2 ) {

            if( tokens[POSICION_ETIQUETA].equals("") ) {
                return _CODOP_;
            }else {
                return ETQ_CODOP;
            }
        }else if( tokens.length == 4 || tokens.length > 3 ){
                return ETQ_CODOP_OP_COM;
        }
        //si llega aqui quiere decir que la longitud del arreglo tokens es mayor a 3 lo cual es invalido
        //siempre y cuando no sea un comentario el último token
        return (byte) tokens.length;
    }
}
