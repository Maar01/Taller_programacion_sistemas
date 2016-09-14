/**
 * Created by mario on 28/08/16.
 */

/**
 * Clase encargada de analizar y almacenar la información resultante
 * del análisis de cada linea leída desde un .asm
 */
public class Linea {

    private String lineaOriginal;
    private String codop = "";
    private String   etq = "";
    private String  oper = "";
    private  short numeroLinea;
    private String lineaCopia;
    private boolean   comentario = false;
    private boolean          end = false;
    private byte       tipoLinea = Validador.ETQ_CODOP_OP;
    private String         error = "";
    private String comentarioStr = "";

    private String tokens[];

    /**
     *
     * @param linea
     */
    Linea(String linea){
        lineaOriginal = linea;
        lineaCopia = lineaOriginal.trim();
        tokens = lineaCopia.split("\\s+");
    }

    /**
     *
     */
    Linea(){    }

    /**
     * inicializa o cambia el valor de lineaOriginal
     * @param lineaOriginal
     */
    public void setLineaOriginal(String lineaOriginal) {

        this.lineaOriginal = lineaOriginal;
        this.setLineaCopia(lineaOriginal);
    }

    /**
     * inicializa o cambia el valor de los tokens que componen a la linea
     * según la posición y cantidad de estos.
     */
    private void set_tokens(){

        this.tokens = lineaCopia.split("\\s+");
        limpiaTokens();

        if( tokens.length == 3 ) {
            this.etq   = tokens[Validador.POSICION_ETIQUETA];
            this.codop = tokens[Validador.POSICION_CODOP];
            this.oper  = tokens[Validador.POSICION_OPERANDO];
        }
        else if ( tokens.length == 4 ) {
            this.etq   = tokens[Validador.POSICION_ETIQUETA];
            this.codop = tokens[Validador.POSICION_CODOP];
            this.oper  = tokens[Validador.POSICION_OPERANDO];
            this.comentarioStr = tokens[Validador.POSICION_COMENTARIO];
        }
        else if( tokens.length > 4 ) {
            this.etq   = tokens[Validador.POSICION_ETIQUETA];
            this.codop = tokens[Validador.POSICION_CODOP];
            this.oper  = tokens[Validador.POSICION_OPERANDO];
            this.comentarioStr = tokens[Validador.POSICION_COMENTARIO];
            for(byte index = Validador.POSICION_COMENTARIO; index < tokens.length; index++ ) {
                this.comentarioStr += tokens[index];
            }
        }
        else if( tokens.length == 1){
            this.etq   = tokens[Validador.POSICION_ETIQUETA];
        }
        else {
            this.etq   = tokens[Validador.POSICION_ETIQUETA];
            this.codop = tokens[Validador.POSICION_CODOP];
        }

    }

    private void limpiaTokens() {
        for(int i = 0; i < tokens.length; i++){
            if(tokens[i].equals("")){
                tokens[i] = "NULL";
            }
        }
    }

    /**
     * Regresa el valor actual de lineaOriginal
     * @return String
     */
    public String getLineaOriginal() {
        return lineaOriginal;
    }

    /**
     * Regresa el valor actual del token codop
     * @return
     */
    public String getCodop() {
        return codop;
    }

    /**
     * Regresa el valor actual del token etq
     * @return
     */
    public String getEtq() {
        return etq;
    }

    /**
     * Regresa el valor actual del token operando
     * @return
     */
    public String getOper() {
        return oper;
    }

    /**
     * regresa el número de línea que le corresponde en el archivo .asm
     * @return
     */
    public short getNumeroLinea() {
        return numeroLinea;
    }

    /**
     * inicializa o actualiza el número de línea que corresponde al archivo .asm
     * @param numeroLinea
     */
    public void setNumeroLinea(short numeroLinea) {
        this.numeroLinea = numeroLinea;
    }


    /**
     * inicializa o cambia el valor de respaldo de la línea original
     * @param lineaCopia
     */
    public void setLineaCopia(String lineaCopia) {
        this.lineaCopia = lineaCopia;
    }

    /**
     * Regresa true en caso de que la línea cumpla con el formato correcto para ser procesada
     * o false en caso de incumplimiento, además de identificar el tipo de error.
     * @return
     */
    public boolean analizar_linea() {

        if( Validador.es_comentario(this.lineaOriginal) ){
            comentario = true;
            return false;
        }else{
            this.set_tokens();
            this.set_tipoLinea(Validador.tipo_de_linea(tokens));

            switch ( this.tipoLinea ) {

                case Validador.ETQ_CODOP_OP:
                    if( Validador.es_etiqueta(tokens[Validador.POSICION_ETIQUETA]) ){
                        if( Validador.es_codop(tokens[Validador.POSICION_CODOP]) ){
                            if( Validador.es_operando(tokens[Validador.POSICION_OPERANDO]) ){
                                return true;
                                
                            }else { this.error = " Formato de operando no es correcto "; }//
                            
                        }else{ this.error = " Formato de codop no es el adecuado"; }//
                        
                    }else{ this.error = " El formato de la etiqueta no es correcto"; }//

                    break;

                    case Validador.ETQ_CODOP:
                    if( Validador.es_etiqueta(tokens[Validador.POSICION_ETIQUETA] ) ){
                        if(tokens.length > 1){
                            if(Validador.es_codop(tokens[Validador.POSICION_CODOP])){
                                return true;
                            }else { this.error = " Error en el formato de CODOP"; }
                        }else{ this.error = " No puede ir solo una etiqueta "; }
                    }else { this.error = " Formato incorrecto de etiqueta" ; }

                    break;

                case Validador._CODOP_OP:
                    if(tokens[Validador.POSICION_ETIQUETA].equals("")){
                        if( Validador.es_codop(tokens[Validador.POSICION_CODOP])) {
                            if(Validador.es_operando(tokens[Validador.POSICION_OPERANDO])){
                                return true;
                            }else{ this.error = " El formato de OPERANDO no es correcto"; }
                    }else{ this.error = " El formato de CODOP no es correcto"; }
                }else{ this.error = " El formato de OPERANDO no es correcto"; }
                            
                    break;

                case Validador._CODOP_:
                    if( Validador.es_codop( tokens[Validador.POSICION_CODOP] ) ) {
                        return true;
                    }else{ this.error = " El formato del CODOP no es correcto" ;}
                    break;

                case Validador.ETQ_CODOP_OP_COM:
                    if ( Validador.es_comentario( tokens[Validador.POSICION_COMENTARIO] ) ){
                        return true;
                    }else if( tokens[Validador.POSICION_OPERANDO].contains(";") ){
                        String tokens2[] = tokens[Validador.POSICION_OPERANDO].split(";");
                        tokens[Validador.POSICION_OPERANDO] = tokens2[0];
                        tokens[Validador.POSICION_COMENTARIO] = tokens2[1] + " " +
                                tokens[Validador.POSICION_COMENTARIO];

                        for(int i = Validador.POSICION_COMENTARIO + 1; i < tokens.length; i++){
                            tokens[Validador.POSICION_COMENTARIO] += " " + tokens[i]   ;
                        }

                        return true;

                    }else{
                        for(int index = 0; index < tokens.length; index++){
                            if(tokens[index].equals("")){

                            }else{
                                if(Validador.es_comentario(tokens[index])){
                                    for(int i = index; i < tokens.length; i++){
                                        tokens[index] =  tokens[i] ;
                                    }
                                    comentario = true;
                                    return false;
                                }
                            }
                        }
                        this.error = " número de tokens invalido para la linea";
                        break;
                    }

                case Validador.ETQ_:
                    this.error = " No es posible sólo tener una etiqueta en la línea";
                    return false;
                    //break;
                default:
                    this.error = " número de tokens invalido para la linea";
            }
        }
        return false;
    }

    /**
     * Evita la necesidad de escribir la línea completa System.out.println()
     * @param texto
     */
    public void showln(String texto) {
        System.out.println(texto);
    }

    /**
     * Evita la necesidad de escribir la línea completa System.out.print()
     * @param texto
     */
    public void show(String texto) {
        System.out.print(texto);
    }

    /**
     * Imprime en consola los tokens identificados en la línea a partir de
     * set_token()
     */
    public void muestra_tokens(){
        for(int i = 0; i < tokens.length; i++){
            if( tokens[i].equals("") ){
                show("espacio 'vacio' ");
            }else{
                System.out.print(tokens[i] + " " );
            }
        }
    }

    /**
     * Regresa true en caso de que la línea se haya identificado como comentario posterior
     * a su analisis
     * @return
     */
    public boolean es_comentario(){
        return comentario;
    }

    /**
     * Inicializa o cambia el tipo de línea, que corresponde a los formatos esperados
     * y definidos en la @clase Validador
     * @param tipoLinea
     */
    public void set_tipoLinea(byte tipoLinea ) {
          this.tipoLinea = tipoLinea;
    }

    /**
     * inicializa o cambia el valor que afirma o niega si la línea actual es comentario
     * @param comentario
     */

    public void setComentario(boolean comentario) {
        this.comentario = comentario;
    }

    /**
     * Regresa el error identificado en la línea posterior a su analisis.
     * @return
     */
    public String getError() {
        return error;
    }

    public void setLineaEscribir(){
        lineaOriginal = "   " + etq.trim() + "     " + codop.trim() + "    " + oper.trim();
    }

    public void set_error( String error ){
        this.error = error;
    }

    public void resetLinea(){
        this.oper  = "NULL";
        this.codop = "";
        this.etq   = "NULL";
        this.error = "";
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean getEnd(){
        return this.end;
    }
}
