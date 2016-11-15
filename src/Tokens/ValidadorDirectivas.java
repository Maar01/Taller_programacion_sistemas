package Tokens;

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


}
