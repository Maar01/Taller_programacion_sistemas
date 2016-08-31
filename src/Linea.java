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
    private boolean comentario = false;
    private boolean        end = false;
    private byte     tipoLinea = Validador.ETQ_CODOP_OP;
    private String       error = "";


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
        }else {
            this.etq = tokens[Validador.POSICION_ETIQUETA];
            this.codop = tokens[Validador.POSICION_CODOP];
        }

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

        if( Validador.es_comentario(this.lineaCopia) ){
            comentario = true;
            return comentario;
        }else{
            this.set_tokens();
            //this.muestra_tokens();
            this.set_tipoLinea(Validador.tipo_de_linea(tokens));

            switch ( this.tipoLinea ) {

                case Validador.ETQ_CODOP_OP:
                    showln("ETQ_CODOP_OP");

                    if( Validador.es_etiqueta(tokens[Validador.POSICION_ETIQUETA]) &&
                        Validador.es_codop(tokens[Validador.POSICION_CODOP]) &&
                        Validador.es_operando(tokens[Validador.POSICION_OPERANDO])    ) {
                            return true;
                    }

                    break;

                    case Validador.ETQ_CODOP:
                    showln("ETQ_CODOP");

                    if( Validador.es_etiqueta(tokens[Validador.POSICION_ETIQUETA] ) &&
                                    Validador.es_codop(tokens[Validador.POSICION_CODOP]) )
                    {  return true;                                                     }

                    break;

                case Validador._CODOP_OP:
                    showln("_CODOP_OP");
                    if( Validador.es_codop(tokens[Validador.POSICION_CODOP]) &&
                            Validador.es_operando(tokens[Validador.POSICION_OPERANDO]) )
                    {                      return true;                                 }
                    break;

                case Validador._CODOP_:
                    showln("_CODOP_");
                    if( Validador.es_codop( tokens[Validador.POSICION_CODOP] ) ) {
                        return true;
                    }
                    break;
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
}
