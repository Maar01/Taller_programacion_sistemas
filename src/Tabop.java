import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Created by mario on 5/09/16.
 */

/**
 * Clase encargada de cargar en memoria, desde un archivo, los valores
 * de TABOP para su posterior y constante consulta.
 */
public class Tabop {
    private ArrayList<Codop> tabop;
    private          Scanner lectorTabop;

    Tabop(){

        File tabopFile = new File("/home/mario/IdeaProjects/Programación de sistemas/TABOP.txt");

        try {
            this.lectorTabop = new Scanner(tabopFile);
        } catch (FileNotFoundException e) {
            System.out.println("Error con la ruta del archivo");
        }

        tabop = new ArrayList<>(600);
    }

    /**
     * Carga el tabop en memoria para su constante consulta
     */

    public void carga_tabop(){
        String lineaTabop;
        while(lectorTabop.hasNextLine()){
            lineaTabop = lectorTabop.nextLine( );
            tabop.add(  new Codop(lineaTabop ) );
        }

    }

    /**
     * Regresa el Tabop cargado en un "arrayList" para su consulta
     * @return
     */
    public ArrayList<Codop> getTabop(){
        return tabop;
    }
}
