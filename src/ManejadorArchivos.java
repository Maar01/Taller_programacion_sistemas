import Tokens.Codop;

import java.io.*;
import java.util.Scanner;

/**
 * Created by mario on 28/08/16.
 */

/**
 * Clase encargada de la lectura y escritura de archivos
 * en base a los resultados del análisis de la clase @Linea
 * */
public class ManejadorArchivos {

    private String  nombre;
    private String  ruta;
    private Scanner lector;
    private    File tronador;
    private   short cantidadLineas;
    private Tabop tabop ;
    private File ins;
    private File err;
    private BufferedWriter salidaInstrucciones;
    private BufferedWriter salidaErrores;

    ManejadorArchivos(String ruta){
        this.ruta = ruta;
        cantidadLineas = 0;
        tronador = new File(ruta);
        tabop = new Tabop();
          ins = new File("/home/mario/IdeaProjects/Programación de sistemas/test" + ".INS");
          err = new File("/home/mario/IdeaProjects/Programación de sistemas/test" + ".ERR");
        try {
            salidaInstrucciones = new BufferedWriter( new FileWriter( ins, true ) );
            salidaErrores = new BufferedWriter( new FileWriter( err,true) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea una instancia de Scanner, que se utilizará para leer archivos
     * @throws FileNotFoundException
     */
    public void inicializa_lector() throws FileNotFoundException {

           lector = new Scanner(tronador);
    }

    /**
     * Función encargada de escribir u omitir la línea en archivos
     * según sus propiedades resultantes del analisis.
     */
    public void analizar_lineas() throws IOException {

        tabop.carga_tabop();
        Linea linea = new Linea();
        short numLinea = 0;

        while ( linea.getCodop() != "END" && lector.hasNextLine() ) {
            boolean escribir = true;
            numLinea++;
            linea.setLineaOriginal(lector.nextLine());
            linea.setComentario(false);
            linea.setNumeroLinea(numLinea);
            linea.resetLinea();
            boolean existeCodop = false;

            if( linea.analizar_linea() ) {
                //escribir en archivo correcto
                if( linea.es_comentario() || linea.getLineaOriginal().isEmpty() ){

                }
                else {
                    //se prepara la línea que se escribirá en el archivo .ins
                      linea.setLineaEscribir();

                      for( Codop codop : tabop.getTabop() ) {
                          //si el codop cargado en memoria desde el tabop.txt es = codop de Linea
                         if( codop.getCodop().equals( linea.getCodop() )  ) {

                             if ( !codop.usaOper() &&
                                     ( linea.getOper().equals("") || linea.getOper().equals("NULL") ) ){
                                 existeCodop = true;
                                 linea.setLineaOriginal( linea.getLineaOriginal() + "   " +  codop.getModoDirec() );
                             }else if ( codop.usaOper() && //no es nulo o está vacío el operando
                                     ( !linea.getOper().equals("") && !linea.getOper().equals("NULL") ) ) {
                                 existeCodop = true;
                                 linea.setLineaOriginal( linea.getLineaOriginal() + "    " +  codop.getModoDirec() );

                             }else if( codop.usaOper() && ( linea.getOper().equals( "" ) || linea.getOper().equals( "NULL" ) )  ) {
                                 linea.set_error( " El codop utiliza operando y no tiene" );
                                 escribir = false;
                                 salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + linea.getError() + "\n" );
                                 existeCodop = true;
                             }else {
                                 linea.set_error(" El codop " + linea.getCodop() + " no utiliza operando" );
                                 salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + linea.getError() + "\n" );
                                 existeCodop = true;
                                 escribir = false;
                                 break;
                             }
                         }
                      }
                      /*Si se termina el ciclo que recorre el arrayList (tabop) y no encontró el codop:
                      * escribir en el archivo de errores.
                      * */
                      if( !existeCodop ){

                          linea.set_error(" El código de operación no existe");
                          salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + linea.getError() + "\n" );
                      }
                      else if( existeCodop && escribir ) {
                          //aquí verificar el operando con los modos de direccionamiento
                          //linea.verificaOperando();
                          salidaInstrucciones.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + "\n" );
                      }
                        /*Si ya se ha leído la etq END terminar el ciclo, además de que el operando debe ser nulo*/
                      if( linea.getCodop().contains("END") &&
                              ( linea.getOper().equals( "" ) || linea.getCodop().contains( "NULL" ) ) ){
                          linea.setEnd(true);
                          salidaInstrucciones.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + "\n" );
                          System.out.println("Entra al break " + linea.getEnd() );
                           break;
                      }
                      else if( linea.getCodop().contains( "END" ) &&
                              ( !linea.getOper().equals( "" ) && !linea.getCodop().contains( "NULL" ) ) ){
                          linea.setEnd(true);
                          System.out.println("Entra al break 2 " + linea.getEnd() );
                          break;
                      }
                }
            }
            else {
                //escribir en archivo de errores

                if( linea.es_comentario() ){/*omitimos acción de escritura*/ }
                else{
                    salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + linea.getError() + "\n" );
                }
            }
        }//fin while de analisis y escritura
        if( !linea.getEnd() ){
            salidaErrores.write( linea.getNumeroLinea() + "     " + "No existe END en el archivo\n" );
        }
        salidaInstrucciones.close();
        salidaErrores.close();
    }
}
