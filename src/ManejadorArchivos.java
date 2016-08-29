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

        try {
            lector = new Scanner(tronador);
            set_cantidadLineas();
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            System.out.println("El archivo no existe. Verifique la ruta ingresada.");
        }
    }

    private void set_cantidadLineas(){
        while( lector.hasNextLine() ) {
            cantidadLineas++;
        }
    }

    private void analizar_lineas(){
        Linea linea = new Linea();
        while ( lector.hasNextLine() ) {
            linea.setLineaOriginal(lector.nextLine());
        }
    }

}
