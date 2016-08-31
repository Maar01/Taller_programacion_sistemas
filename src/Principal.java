
import java.io.FileNotFoundException;
import java.util.Scanner;
/*
 *   /home/mario/IdeaProjects/Programación de sistemas/test.ASM
**/
/**
 * Created by mario on 28/08/16.
 */
public class Principal {


    public static void main (String argumentos[]) {

        Scanner lector = new Scanner(System.in);
        String rutaTronador;
        ManejadorArchivos manejador;
        boolean repetir = false;

        do{
            System.out.println("Ingresar ruta de archivo .ASM");
            //rutaTronador = lector.nextLine();
            rutaTronador = "/home/mario/IdeaProjects/Programación de sistemas/test.ASM";
            manejador = new ManejadorArchivos(rutaTronador);

            try {
                manejador.inicializa_lector();
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
                System.out.println("El archivo no existe, verifique la ruta proporcionada");
                repetir = true;
            }

            manejador.analizar_lineas();
        }while(repetir == true);
    }
}
