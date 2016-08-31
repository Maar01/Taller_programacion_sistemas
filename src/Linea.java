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
           this.etq = tokens[Validador.POSICION_ETIQUETA];
         this.codop = tokens[Validador.POSICION_CODOP];
          this.oper = tokens[Validador.POSICION_OPERANDO];
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
}
