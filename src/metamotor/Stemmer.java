/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metamotor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tartarus.snowball.SnowballStemmer;
import persistencia.DBConnection;

/**
 *
 * @author Me
 */
public class Stemmer {

    List<String> stopwords = new ArrayList<String>();

    public Stemmer() {
    }

    //Recibe una la consulta como una cadena y regresa un Vector<String> con los stems de las palabras
    public ArrayList<String> Stem(String s) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String str = "Select * from stopwords";
        ArrayList<String> stems = new ArrayList<String>();

        /* Obtener la lista de stopwords de la base de datos */
        try {
            DBConnection db = new DBConnection();
            stopwords = db.readDataBase2(str);
        } catch (SQLException e) {
            System.out.println("Error recuperando stopwords");
        }

        Class stemClass = Class.forName("org.tartarus.snowball.ext." + "englishStemmer");
        SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();

        String[] result = s.split("\\s");

        for (int i = 0; i < result.length; i++) {
            if (stopwords.contains(result[i])) {
                continue;
            }
            stemmer.setCurrent(result[i]);
            stemmer.stem();
            stems.add(stemmer.getCurrent());
        }

        return stems;
    }

    public ArrayList<String> StemFile(String language, String file_name) throws ClassNotFoundException, InstantiationException, IllegalAccessException, FileNotFoundException, IOException {
        // ResultSet para guardar lo recuperado de la DB
        ResultSet rs = null;
        // nombre del documento
        String file;
        // lista donde se almacenarán las stopwords
        List<String> stopwords;
        Reader reader;
        ArrayList<String> result = new ArrayList<String>();

        stopwords = new ArrayList<String>();

        Class stemClass = Class.forName("org.tartarus.snowball.ext." + language
                + "Stemmer");
        SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();

        reader = new InputStreamReader(new FileInputStream(file_name));
        file = fileName(file_name);
        reader = new BufferedReader(reader);
        StringBuffer input = new StringBuffer();
        OutputStream outstream;

        DBConnection db;

        /* Clase de conexión a la base de datos */
        db = new DBConnection();

        /* Obtener la lista de stopwords de la base de datos */
        String str = "Select * from stopwords";
        try {
            stopwords = db.readDataBase2(str);
        } catch (SQLException e) {
            System.out.println("Error recuperando stopwords");
        }
        /*----------------------*/

        outstream = System.out;

        Writer output = new OutputStreamWriter(outstream);
        output = new BufferedWriter(output);
        int repeat = 1;
        int character;
        while ((character = reader.read()) != -1) {
            char ch = (char) character;
            // si ch es un guión, se cambia por un espacio
            if (ch == '-') {
                ch = ' ';
            }
            if (Character.isWhitespace((char) ch)) {
                if (input.length() > 0) {
                    /* Checar si la palbra a lematizar es una stopword */
                    if (stopwords.contains(input.toString())) {
                        input.delete(0, input.length());
                        continue;
                    }
                    /*-------------------*/

                    stemmer.setCurrent(input.toString());
                    for (int i = repeat; i != 0; i--) {
                        stemmer.stem();
                    }

                    String curr = stemmer.getCurrent();
                    result.add(curr);
                    
                    //System.out.println(curr);

                    /*
                     * Insertar la palabra a la tabla de términos y en la tabla
                     * de frecuencias para cada documento con una frecuencia
                     * inicial de 0
                     
                    String query = "insert into frecuencia (uri, raíz) values (?,?)";
                    try {
                        db.Insert(curr);
                        for (int i = 1; i < 11; i++) {
                            Integer integ = new Integer(i);
                            db.Insert(query, integ.toString(), curr);
                        }
                    } catch (SQLException e) {
                        // la palabra ya existe en los términos, no hacer nada
                    }*/
                    /*---------*/

                    /* Cambiar la frecuencia del término en el documento 
                    query = "insert into frecuencia (uri, raíz) values (?,?) on duplicate key update frecuencia = frecuencia + 1";
                    try {
                        db.Insert(query, file, curr);
                    } catch (SQLException e) {
                        //no hacer nada
                    }*/

                    /*---------*/

                    output.write(curr);
                    output.write('\n');
                    input.delete(0, input.length());
                }
            } else {
                // checar si el caracter es una letra o no
                if (!Character.isLetter((char) ch)) {
                    if (ch != '\'' && ch != '-') {
                        continue;
                    }
                }
                input.append(Character.toLowerCase(ch));
            }
        }

        /*Se llegó al final del documento, procesar la última palabra*/

        if (input.length() > 0) {
            /* Checar si la palbra a lematizar es una stopword */
            if (stopwords.contains(input.toString())) {
                input.delete(0, input.length());
                return result;
            }
            /*-------------------*/

            stemmer.setCurrent(input.toString());
            for (int i = repeat; i != 0; i--) {
                stemmer.stem();
            }

            String curr = stemmer.getCurrent();
            result.add(curr);
            
            //System.out.println(curr);

            /*
             * Insertar la palabra a la tabla de términos y en la tabla
             * de frecuencias para cada documento con una frecuencia
             * inicial de 0
             
            String query = "insert into frecuencia (uri, raíz) values (?,?)";
            try {
                db.Insert(curr);
                for (int i = 1; i < 11; i++) {
                    Integer integ = new Integer(i);
                    db.Insert(query, integ.toString(), curr);
                }
            } catch (SQLException e) {
                // la palabra ya existe en los términos, no hacer nada
            }*/
            /*---------*/

            /* Cambiar la frecuencia del término en el documento 
            query = "insert into frecuencia (uri, raíz) values (?,?) on duplicate key update frecuencia = frecuencia + 1";
            try {
                db.Insert(query, file, curr);
            } catch (SQLException e) {
                //no hacer nada
            }*/

            /*---------*/

            output.write(curr);
            output.write('\n');
            input.delete(0, input.length());

            /*-----------*/
        }
        output.flush();
        return result;
    }

    public static String fileName(String fullPath) {
        int dot = fullPath.lastIndexOf('.');
        int sep = fullPath.lastIndexOf('\\');
        return fullPath.substring(sep + 1, dot);
    }
}
