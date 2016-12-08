import Tokens.Etiqueta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mario on 21/11/16.
 */
public class TabSim {
    private BufferedWriter salidaTabSim;
    private File tabsim_file;

    private ArrayList<Etiqueta> tabSim;

    /**
     *
     */
    TabSim() {
        tabsim_file = new File("/home/mario/IdeaProjects/Programación de sistemas/test" + ".TDS");
        tabSim = new ArrayList<Etiqueta>(200);

        try {
            salidaTabSim = new BufferedWriter( new FileWriter( tabsim_file,true) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param etiqueta
     * @return
     */
    public boolean agregaEtiqueta ( Etiqueta etiqueta ) {

            tabSim.add( etiqueta );
            return true;
    }

    /**
     *
     * @param etiqueta
     * @return
     */
    public boolean existeEtiqueta( Etiqueta etiqueta ){
        try{
            return tabSim.contains( etiqueta );
        } catch (NullPointerException exception) {
            return false;
        }

    }

    /**
     *
     */
    public void escribeTabsim() {
        for ( Etiqueta etiqueta: tabSim   ) {
            try {
                if (etiqueta.getEtiqueta().equals("NULL") ) {

                }else {
                    salidaTabSim.write( etiqueta.getEtiqueta() + "      " + etiqueta.getValor_etiqueta() + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            salidaTabSim.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


