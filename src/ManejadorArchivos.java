import java.io.*;
import java.util.Scanner;

/**
 * Created by mario on 28/08/16.
 */
public class ManejadorArchivos {

    private String  nombre;
    private String  ruta;
    //private  short  tipoArchivo;
    private Scanner lector;
    private    File tronador;
    private   short cantidadLineas;

    ManejadorArchivos(String ruta){
        this.ruta = ruta;
        cantidadLineas = 0;
        tronador = new File(ruta);
        //procesa_lineas(tronador);

    }

    public void inicializa_lector() throws FileNotFoundException {

           lector = new Scanner(tronador);
    }

    private void set_cantidadLineas(){
        while( lector.hasNextLine() ) {
            cantidadLineas++;
        }
    }

    public void analizar_lineas(){
        Linea linea = new Linea();
        short numLinea = 0;
        while ( linea.getCodop() != "END" && lector.hasNextLine() ) {
            numLinea++;
            linea.setLineaOriginal(lector.nextLine());
            linea.setComentario(false);
            linea.setNumeroLinea(numLinea);

            if(linea.analizar_linea()){
                //escribir en archivo correcto
                if( linea.es_comentario() || linea.getLineaOriginal().isEmpty() ){

                }else{
                    File ins = new File("/home/mario/IdeaProjects/Programación de sistemas/test" + ".INS");

                    try {
                        BufferedWriter salidaInstrucciones = new BufferedWriter(new FileWriter(ins, true));
                        salidaInstrucciones.write(linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + "\n" );
                        salidaInstrucciones.close();
                        if( linea.getCodop().contains("END")  ){
                            break;
                        }

                    } catch (IOException e) {
                        //e.printStackTrace();
                        System.out.println("Problema al crear archivo de instrucciones");
                    }

                }

            }else {
                //escribir en archivo de errores
                File ins = new File("/home/mario/IdeaProjects/Programación de sistemas/test" + ".ERR");
                if( linea.es_comentario() ){

                }else{
                    try {

                        BufferedWriter salidaInstrucciones = new BufferedWriter(new FileWriter(ins, true));
                        //System.out.println( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() );
                        salidaInstrucciones.write(linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + linea.getError() + "\n" );
                        salidaInstrucciones.close();

                    } catch (IOException e) {
                        //e.printStackTrace();
                        System.out.println("Problema al crear archivo de instrucciones");
                    }
                }
            }
        }
    }
}
