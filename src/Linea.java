/**
 * Created by mario on 28/08/16.
 */
public class Linea {

    private String lineaOriginal;
    private String codop;
    private String etq;
    private String oper;
    private  short numeroLinea;
    private String lineaCopia;
    private boolean   comentario = false;
    private boolean          end = false;
    private byte       tipoLinea = Validador.ETQ_CODOP_OP;
    private String         error = "";
    private String comentarioStr = "";


    private String tokens[];

    Linea(String linea){
        lineaOriginal = linea;
        lineaCopia = lineaOriginal.trim();
        tokens = lineaCopia.split("\\s+");
    }

    Linea(){    }

    public void setLineaOriginal(String lineaOriginal) {

        this.lineaOriginal = lineaOriginal;
        this.setLineaCopia(lineaOriginal);
    }

    private void set_tokens(){
        this.tokens = lineaCopia.split("\\s+");
        if( tokens.length == 3 ) {
            this.etq = tokens[Validador.POSICION_ETIQUETA];
            this.codop = tokens[Validador.POSICION_CODOP];
            this.oper = tokens[Validador.POSICION_OPERANDO];
        }else if ( tokens.length == 4 ) {
            this.etq = tokens[Validador.POSICION_ETIQUETA];
            this.codop = tokens[Validador.POSICION_CODOP];
            this.oper = tokens[Validador.POSICION_OPERANDO];
            this.comentarioStr = tokens[Validador.POSICION_COMENTARIO];
        }else if( tokens.length > 4 ){
            this.etq = tokens[Validador.POSICION_ETIQUETA];
            this.codop = tokens[Validador.POSICION_CODOP];
            this.oper = tokens[Validador.POSICION_OPERANDO];
            this.comentarioStr = tokens[Validador.POSICION_COMENTARIO];
            for(byte index = Validador.POSICION_COMENTARIO; index < tokens.length; index++ ){
                this.comentarioStr += tokens[index];
            }
        }
        else if( tokens.length == 1){
            this.etq   = tokens[Validador.POSICION_ETIQUETA];
        } else{
            showln("" + tokens.length);
            this.etq   = tokens[Validador.POSICION_ETIQUETA];
            this.codop = tokens[Validador.POSICION_CODOP];
        }
        muestra_tokens();
    }

    public String getLineaOriginal() {
        return lineaOriginal;
    }

    public String getCodop() {
        return codop;
    }


    public String getEtq() {
        return etq;
    }


    public String getOper() {
        return oper;
    }


    public short getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(short numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getLineaCopia() {
        return lineaCopia;
    }

    public void setLineaCopia(String lineaCopia) {
        this.lineaCopia = lineaCopia;
    }

    public boolean analizar_linea() {
        byte estado = 0;

        if( Validador.es_comentario(this.lineaOriginal) ){
            comentario = true;
            return false;
        }else{
            this.set_tokens();
            //this.muestra_tokens();
            this.set_tipoLinea(Validador.tipo_de_linea(tokens));

            switch ( this.tipoLinea ) {

                case Validador.ETQ_CODOP_OP:
                   // showln("ETQ_CODOP_OP");
                    
                    if( Validador.es_etiqueta(tokens[Validador.POSICION_ETIQUETA]) ){
                        if( Validador.es_codop(tokens[Validador.POSICION_CODOP]) ){
                            if( Validador.es_operando(tokens[Validador.POSICION_OPERANDO]) ){
                                return true;
                                
                            }else { this.error = "Formato de operando no es correcto "; }//
                            
                        }else{ this.error = "Formato de codop no es el adecuado"; }//
                        
                    }else{ this.error = "El formato de la etiqueta no es correcto"; }//

                    break;

                    case Validador.ETQ_CODOP:
                   // showln("ETQ_CODOP");

                    if( Validador.es_etiqueta(tokens[Validador.POSICION_ETIQUETA] ) ){
                        if(Validador.es_codop(tokens[Validador.POSICION_CODOP])){
                            return true;
                        }
                    }

                    break;

                case Validador._CODOP_OP:
                   // showln("_CODOP_OP");
                    if(tokens[Validador.POSICION_ETIQUETA].equals("")){
                        if( Validador.es_codop(tokens[Validador.POSICION_CODOP])) {
                            if(Validador.es_operando(tokens[Validador.POSICION_OPERANDO])){
                                return true;
                            }else{ this.error = "El formato de OPERANDO no es correcto"; }
                    }else{ this.error = "El formato de CODOP no es correcto"; }
                }else{ this.error = "El formato de OPERANDO no es correcto"; }
                            
                    break;

                case Validador._CODOP_:
                   // showln("_CODOP_");
                    if( Validador.es_codop( tokens[Validador.POSICION_CODOP] ) ) {
                        return true;
                    }
                    break;

                case Validador.ETQ_CODOP_OP_COM:
                    if ( Validador.es_comentario( tokens[Validador.POSICION_COMENTARIO] ) ){
                        return true;
                    }else{
                        this.error = "numero de tokens invalido para la linea";
                        break;
                    }

                    //break;
                default:
                    this.error = "numero de tokens invalido para la linea";
                    return false;
            }
        }
        return false;
    }

    public void showln(String texto) {
        System.out.println(texto);
    }

    public void show(String texto) {
        System.out.print(texto);
    }

    public void muestra_tokens(){
        for(int i = 0; i < tokens.length; i++){
            if( tokens[i].equals("") ){
                show("espacio 'vacio' ");
            }else{
                System.out.print(tokens[i] + " ");
            }
        }
        showln( "# tokens = " + tokens.length);
    }

    public boolean es_comentario(){
        return comentario;
    }

    public byte get_tipoLinea(  ) {
        return  this.tipoLinea;
    }

    public void set_tipoLinea(byte tipoLinea ) {
          this.tipoLinea = tipoLinea;
    }

    public void setComentario(boolean comentario) {
        this.comentario = comentario;
    }

    public String getError() {
        return error;
    }
}
