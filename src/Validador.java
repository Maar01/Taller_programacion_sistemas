import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mario on 28/08/16.
 */
 public final class Validador {
    //Reglas de los comentarios
    private static final String Comentario = "^[;]{1}[#|$|%|@]*[A-Fa-f]*[0-9]*";

    public static boolean  es_comentario(String token){

        Pattern patron = Pattern.compile(Comentario);
        Matcher matcher = patron.matcher(token);

        return matcher.matches();
    }
}
