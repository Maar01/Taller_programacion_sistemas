import java.io.File;
import java.util.Scanner;

/**
 * Created by mario on 28/08/16.
 */
public class Principal {
    Scanner lector = new Scanner(System.in);
    String rutaTronador;
    ManejadorArchivos manejador;
    public void main (String argumentos[]) {

        System.out.println("Ingresar ruta de archivo .ASM");
        rutaTronador = lector.nextLine();

        manejador = new ManejadorArchivos(rutaTronador);


    }
}
