import java.io.File;
import java.io.FileNotFoundException;
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
        while ( lector.hasNextLine() ) {

            linea.setLineaOriginal(lector.nextLine());
            linea.setNumeroLinea(numLinea);
            linea.analizar_linea();
        }
    }

}
