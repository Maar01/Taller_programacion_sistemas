import ModosDireccionamiento.ValidadorModoDireccionamiento;
import Tokens.Codop;
import Tokens.Etiqueta;
import Tokens.ReporteDirectiva;
import Tokens.ValidadorDirectivas;

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
        boolean existe_org = false;
        boolean was_equ = false;
        int cont_loc = 0;

        TabSim tab_sim = new TabSim();
       // System.out.println( new String( "Solía \\\\ ñ \\ ; mas" ).length() - 2 );



        while ( linea.getCodop() != "END" && lector.hasNextLine() ) {
            boolean escribir = true;
            numLinea++;
            linea.setLineaOriginal(lector.nextLine());
            linea.setModos_dire("");
            linea.setComentario(false);
            linea.setNumeroLinea(numLinea);
            linea.resetLinea();
            Codop codop_f = null;
            was_equ = false;
            boolean existeCodop = false;
            ReporteDirectiva reporte_directiva;
            Etiqueta temporal;



            if( linea.analizar_linea() ) {

                //escribir en archivo correcto
                if( linea.es_comentario() || linea.getLineaOriginal().isEmpty() ){
                    was_equ = true;
                }  else if ( linea.getCodop().equals( "EQU" ) ) {
                    if ( !linea.getEtq().equals("") && !linea.getEtq().equals("NULL")  ) {
                        if ( !linea.getOper().equals("NULL") && !linea.getOper().equals("")  ) {
                             temporal =  new Etiqueta(linea.getEtq(), linea.getOper());
                            if ( !tab_sim.existeEtiqueta( temporal )  ) { //agregar una verificacion de rango
                                if ( ValidadorModoDireccionamiento.cambiaBaseNumericaDecimal( linea.getOper().charAt(0), linea.getOper().substring(1)  ) <= 65635 ) {
                                    tab_sim.agregaEtiqueta( temporal );
                                    linea.setEtiqueta_obj( temporal );
                                   // System.out.println( linea.getNumeroLinea() + "  " +  Integer.parseInt( String.valueOf( cont_loc ) )  + "  " + linea.getEtq() + "  " + linea.getCodop() + "    " + linea.getOper() + "\n" );
                                    salidaInstrucciones.write( linea.getNumeroLinea() + "   "+ Integer.toHexString(  cont_loc ) +"  " + linea.getEtq() + " " + linea.getCodop() + " " + linea.getOper() + "\n");
                                }
                                else {
                                    salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + "  -> Valor fuera de rango" + "\n" );
                                }

                            } else {
                                salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + "  -> Etiqueta repetida" + "\n" );
                            }

                        } else {
                            salidaErrores.write(linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + "  -> EQU debe tener un operando" + "\n");
                        }

                    } else {
                        salidaErrores.write(linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + "  -> EQU debe tener una etiqueta" + "\n");
                    }
                    was_equ = true;
                } //si no es equ, entonces es codop y se analiza
                else {
                    //se prepara la línea que se escribirá en el archivo .ins
                    linea.setLineaEscribir();

                    // para cargar los modos de direccionamiento
                    for ( Codop codop : tabop.getTabop() ) {
                        if ( codop.getCodop().equals( linea.getCodop() ) ) {
                            linea.setModos_dire( linea.getModos_dire()+" " + codop.getModoDirec() );
                        }
                    }


                    for ( Codop codop : tabop.getTabop() ) {
                        //si el codop cargado en memoria desde el tabop.txt es = codop de Linea
                        codop_f = codop;
                        if( codop.getCodop().equals( linea.getCodop() )  ) {

                            linea.setCodop_obj( codop );
                            if ( existe_org && linea.getCodop().equals("ORG") ) {
                                salidaErrores.write( linea.getNumeroLinea() + "     " + linea.getLineaOriginal() + "    " + " Ya existe un org en el archivo \n" );
                            }

                            if ( !codop.usaOper() &&
                                    ( linea.getOper().equals("") || linea.getOper().equals("NULL") ) && existe_org ){
                                existeCodop = true;
                                escribir = false;
                                linea.setLineaOriginal( linea.getLineaOriginal() + "   " +  codop.getModoDirec() );
                                linea.setLineaOriginal2( linea.getLineaOriginal2() + "   " +  codop.getModoDirec() );
                                cont_loc = cont_loc + Integer.parseInt( codop.getBytesCalculados());
                                if ( linea.verificaOperando() ) {
                                    salidaInstrucciones.write( linea.getNumeroLinea() + "   " + Integer.toHexString(  cont_loc  ) + "    " + linea.getLineaOriginal() + "       "+ linea.get_codigoMaquina()+" \n"  );
                                }

                                if ( !linea.getEtq().equals("NULL") || !linea.getEtq().equals("")  ) { //aquí escribimos y verificamos el tabsim }
                                    //System.out.println( linea.getEtq() + "etq" );
                                    if ( !tab_sim.existeEtiqueta( new Etiqueta( linea.getEtq() , String.valueOf( cont_loc )  ) )) {
                                        tab_sim.agregaEtiqueta( new Etiqueta( linea.getEtq() , String.valueOf( cont_loc ) ) );
                                        linea.setEtiqueta_obj( new Etiqueta( linea.getEtq() , String.valueOf( cont_loc ) ) );
                                    } else {
                                        salidaErrores.write( linea.getNumeroLinea() + "     "+ linea.getLineaOriginal() + " ->etiqueta repetida" );
                                    }
                                }
                            } else if ( codop.usaOper() && //no es nulo o está vacío el operando
                                    ( !linea.getOper().equals("") && !linea.getOper().equals("NULL") )  ) { //&& existe_org
                                existeCodop = true;
                                cont_loc = cont_loc + Integer.parseInt( codop.getBytesCalculados() );
                                linea.setLineaOriginal( linea.getLineaOriginal() + "    " +  codop.getModoDirec() );
                                linea.setLineaOriginal2( linea.getLineaOriginal2() + "   " +  codop.getModoDirec() );
                                temporal = new Etiqueta( linea.getEtq(), linea.getOper() );
                                if ( !tab_sim.existeEtiqueta( new Etiqueta( linea.getEtq() , String.valueOf( cont_loc )  ) )) {
                                    tab_sim.agregaEtiqueta( new Etiqueta( linea.getEtq() , String.valueOf( cont_loc ) ) );
                                    linea.setEtiqueta_obj( new Etiqueta( linea.getEtq() , String.valueOf( cont_loc ) ) );
                                } else {
                                    salidaErrores.write( linea.getNumeroLinea() + "     "+ linea.getLineaOriginal() + " ->etiqueta repetida" );
                                }

                                break;
                            } else if ( codop.usaOper() && ( linea.getOper().equals( "" ) || linea.getOper().equals( "NULL" ) ) && existe_org ) {
                                linea.set_error( " El codop utiliza operando y no tiene" );
                                escribir = false;
                                salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + linea.getError() + "\n" );
                                existeCodop = true;
                                break;
                            } else if ( existe_org ) {
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
                    if( !existeCodop && existe_org ) {
                        //aquí debemos de revisar si pertenece a alguna de las directivas.

                        reporte_directiva = ValidadorDirectivas.analizaDirectiva( linea.getCodop(), linea.getOper() );

                        if ( reporte_directiva.Es_directiva() ) {
                            if ( !reporte_directiva.Es_error() ) {
                                cont_loc = cont_loc + reporte_directiva.getAumentar_contLoc();
                              //  System.out.println( linea.getNumeroLinea() + "  " +  Integer.parseInt( String.valueOf( cont_loc ) )  + "  " + linea.getEtq() + "  " + linea.getCodop() + "    " + linea.getOper() + "\n" );
                                salidaInstrucciones.write(linea.getNumeroLinea() + "  " +  Integer.toHexString( cont_loc )  + "  " + linea.getEtq() + "  " + linea.getCodop() + "    " + linea.getOper() + "\n");
                                //escribe en el archivo de tabla de simbolos?
                                if ( !linea.getEtq().equals("NULL") && !linea.getEtq().equals("")  ) { //aquí escribimos y verificamos el tabsim }
                                    //System.out.println( linea.getEtq() + "etq" );
                                    if ( !tab_sim.existeEtiqueta( new Etiqueta( linea.getEtq() , String.valueOf( cont_loc ) )  ) ) {
                                        tab_sim.agregaEtiqueta( new Etiqueta( linea.getEtq() ,  String.valueOf( cont_loc )    ) );
                                        linea.setEtiqueta_obj( new Etiqueta( linea.getEtq() ,  String.valueOf( cont_loc ) ) );
                                    } else {
                                        salidaErrores.write( linea.getNumeroLinea() + "     "+ linea.getLineaOriginal() + " ->etiqueta repetida" );
                                    }
                                }

                            } else {
                                salidaErrores.write(linea.getNumeroLinea() + "  " + linea.getLineaOriginal() + "     " + reporte_directiva.getError() + "\n");
                            }
                        } else {
                            linea.set_error(" El código de operación no existe");
                            //reporte_directiva = ValidadorDirectivas.analizaDirectiva( linea.getOper(), linea.getOper() );


                            salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + linea.getError() + "\n" );
                        }
                    }
                    else if( existeCodop && escribir && existe_org ) {
                          /*Modificaciones en este archivo para práctica 3*/
                        //aquí verificar el operando con los modos de direccionamiento
                        if ( linea.verificaOperando() || linea.getCodop().equals("END") ) {
                            cont_loc = cont_loc + Integer.parseInt( codop_f.getBytesCalculados() );
                            //salidaInstrucciones.write( linea.getNumeroLinea() + "   " + Integer.toHexString(  cont_loc  ) + "    " + linea.getLineaOriginal() + "   Modo correspondiente:  "+ linea.getModo_direccionamiento_linea() + " \n" );
                            salidaInstrucciones.write( linea.getNumeroLinea()+"     " + Integer.toHexString(  cont_loc  )
                                    + "    " + linea.getEtq() + "     " + linea.getCodop() + "     " + linea.getOper()+"        " + linea.getModo_direccionamiento_linea() + "   " +linea.get_codigoMaquina() + "\n");
                          //  System.out.println( "codigomaquina" +  linea.getModo_direccionamiento_linea() );
                        } else {

                            salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + linea.getError() + "\n" );
                        }
                        //salidaInstrucciones.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + "\n" );
                    }

                    if ( !existe_org && linea.getCodop().equals("ORG") ) { //aquí deberíamos iniciar el cont_loc
                        if ( linea.verificaOperando() ) {
                            cont_loc = ValidadorDirectivas.cambiaBaseNumericaDecimal( linea.getOper().charAt( 0 ) , linea.getOper().substring( 1 ) );
                            //salidaInstrucciones.write( linea.getNumeroLinea() + "      " + Integer.toHexString(  cont_loc  )+ "     " + linea.getLineaOriginal() + "   Modo correspondiente:  "+ linea.getModo_direccionamiento_linea() + " \n" );
                            salidaInstrucciones.write( linea.getNumeroLinea()+"     " + Integer.toHexString(  cont_loc  )
                                    + "    " + linea.getEtq() + "     " + linea.getCodop() + "     " + linea.getOper() +"       " + linea.getModo_direccionamiento_linea() + "      " +linea.get_codigoMaquina() + "\n");
                           // System.out.println( "codigomaquina  "  +  linea.getModo_direccionamiento_linea());
                            existe_org = true;
                        } else  {
                            salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + "     "+linea.getError() + "\n" );
                            break;
                        }

                    }

                        /*Si ya se ha leído la etq END terminar el ciclo, además de que el operando debe ser nulo*/
                    if( linea.getCodop().contains("END") &&
                            ( linea.getOper().equals( "NULL" ) || linea.getCodop().contains( "NULL" ) ) ){
                        linea.setEnd(true);
                        cont_loc = cont_loc + Integer.parseInt( codop_f.getBytesCalculados() );
                        salidaInstrucciones.write( linea.getNumeroLinea() + "   " + Integer.toHexString( cont_loc ) + "  " + linea.getLineaOriginal() + "\n" );

                        break;
                    }
                    else if( linea.getCodop().contains( "END" ) &&
                            ( !linea.getOper().equals( "" ) && !linea.getCodop().contains( "NULL" ) ) ){
                        linea.setEnd(true);

                        break;
                    }
                }
            }
            else {
                //escribir en archivo de errores

                if( linea.es_comentario() ) {
                    was_equ = true;/*omitimos acción de escritura*/
                    if ( linea.getCodop().equals( "FCC" ) ) {
                        reporte_directiva = ValidadorDirectivas.analizaDirectiva( linea.getCodop(), "\""+linea.getLineaOriginal().split("\"")[1]+"\"" );
                        linea.setOper( "\""+linea.getLineaOriginal().split("\"")[1]+"\"" );
                        salidaInstrucciones.write(linea.getNumeroLinea() + "  " +  Integer.toHexString( cont_loc )  + "  " + linea.getEtq() + "  " + linea.getCodop() + "    " + linea.getOper() + "\n");

                        if ( !linea.getEtq().equals("NULL") && !tab_sim.existeEtiqueta( new Etiqueta( linea.getEtq() , linea.getOper() )  ) ) {
                            tab_sim.agregaEtiqueta( new Etiqueta( linea.getEtq() ,  linea.getOper()    ) );
                            linea.setEtiqueta_obj( new Etiqueta( linea.getEtq() ,  linea.getOper()    ) );
                        } else {
                            salidaErrores.write( linea.getNumeroLinea() + "     "+ linea.getLineaOriginal() + " ->etiqueta repetida" );
                        }


                    } else if ( linea.getCodop().isEmpty() ) {
                        continue;
                    }else {
                        reporte_directiva = ValidadorDirectivas.analizaDirectiva( linea.getCodop(), linea.getOper() );
                    }


                    if ( reporte_directiva.Es_directiva() ) {
                        if ( !reporte_directiva.Es_error() ) {
                            cont_loc = cont_loc + reporte_directiva.getAumentar_contLoc();
                            salidaInstrucciones.write(linea.getNumeroLinea() + "  " +  Integer.toHexString(  cont_loc  )  + "  " + linea.getEtq() + "  " + linea.getCodop() + "    " + linea.getOper() + "\n");
                            //escribe en el archivo de tabla de simbolos?
                        } else {
                            salidaErrores.write(linea.getNumeroLinea() + "  " + linea.getLineaOriginal() + "    " +reporte_directiva.getError() + "\n" );
                        }
                    } else {
                        linea.set_error(reporte_directiva.getError());
                        salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + "    " +linea.getError() + "\n" );
                    }

                }
                else if ( existe_org ) {

                    if ( linea.getCodop().equals( "FCC" ) ) {
                        reporte_directiva = ValidadorDirectivas.analizaDirectiva( linea.getCodop(), "\""+linea.getLineaOriginal().split("\"")[1]+"\"" );
                        linea.setOper( "\""+linea.getLineaOriginal().split("\"")[1]+"\"" );
                        salidaInstrucciones.write(linea.getNumeroLinea() + "  " +  Integer.toHexString( cont_loc )  + "  " + linea.getEtq() + "  " + linea.getCodop() + "    " + linea.getOper() + "\n");
                    } else {
                        reporte_directiva = ValidadorDirectivas.analizaDirectiva( linea.getCodop(), linea.getOper() );
                    }


                    if ( reporte_directiva.Es_directiva() ) {
                        if ( !reporte_directiva.Es_error() ) {
                            cont_loc = cont_loc + reporte_directiva.getAumentar_contLoc();
                            salidaInstrucciones.write(linea.getNumeroLinea() + "  " +  Integer.toHexString( cont_loc )  + "  " + linea.getEtq() + "  " + linea.getCodop() + "    " + linea.getOper() + "\n");
                            //escribe en el archivo de tabla de simbolos?
                        } else {
                            salidaErrores.write(linea.getNumeroLinea() + "  " + linea.getLineaOriginal() + "    " +reporte_directiva.getError() + "\n" );
                        }
                    } else {
                        linea.set_error(reporte_directiva.getError());
                        salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + "    " +linea.getError() + "\n" );
                    }

                    //salidaErrores.write( linea.getNumeroLinea() + "    " + linea.getLineaOriginal() + linea.getError() + "\n" );
                }
            }
            if ( !existe_org && !was_equ ) { break; }

        }//fin while de analisis y escritura
        if( !linea.getEnd() && existe_org){
            salidaErrores.write( linea.getNumeroLinea() + "     " + "No existe END en el archivo\n" );
        }
        if ( !existe_org  ) {
            salidaErrores.write( linea.getNumeroLinea() + "     " + "No inicia con ORG el archivo\n" );
        }
        tab_sim.escribeTabsim();
        salidaInstrucciones.close();
        salidaErrores.close();

    }
}