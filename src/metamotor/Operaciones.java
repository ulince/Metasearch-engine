/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metamotor;

import java.sql.SQLException;
import persistencia.DBConnection;
import persistencia.Query;
import ir.utilities.MoreMath;
import java.util.ArrayList;
import java.util.List;
import persistencia.Query2;
import persistencia.Representativo;

/**
 *
 * @author Me
 */
public class Operaciones {

    static public ArrayList<double[]> vectoresPrueba;
    static public List<double[]> consultasElegidas;//[q_id][Xinversa][docsXConsulta]
    static public ArrayList<Representativo> representativos;
    static public int k;
    static public int google;
    static public int yahoo;
    static public int bing;
    static public int motor;

    public Operaciones() {
        vectoresPrueba = new ArrayList<double[]>();
        representativos = new ArrayList<Representativo>();
    }

    public void setK(int k) {
        this.k = k;
        consultasElegidas = new ArrayList<double[]>();
        google = 0;
        yahoo = 0;
        bing = 0;
        motor = 0;
    }

    //Calcula la distancia entre una consulta y las consultas de entrenamiento 
    //result[0] corresponde a la distancia con query 1, result[1] corresponde al query 2...
    //el parametro medida se refiere a la medida de similitud que se empleará (coseno_mrdd)
    //y tabla se refiere a la tabla consulta que se usará (consulta_mrdd)
    public Query[] SimDocCons(String medida, String tabla) {
        DBConnection db = new DBConnection();
        Query result[] = new Query[22];

        try {
            result[0] = db.StoredProc(medida);
            for (int i = 1; i < 22; i++) {
                db.SP(i + 1, i, tabla);
                result[i] = db.StoredProc(medida);
            }

            db.SP(0, 0, tabla);

            return result;
        } catch (SQLException e) {
            System.out.println("There was an error.");
            return result;
        }
    }

    //Toma queryList, que representa una consulta de entrenamiento, y guarda las frecuencias 
    //en un double[]
    public double[] getVectorFromList(List<Query> queryList) {
        double vector[] = new double[queryList.size()];
        int i = 0;
        for (Query q : queryList) {
            vector[i] = q.getFrecuencia();
            i++;
        }
        return vector;
    }

    //Este metodo calcula cosenos con la consulta nueva y regresa una lista
    //con las query_id de las consultas de prueba que tuvieron un coseno > 0.6
    public void getConsultasElegidas() {
        Query[] queryArray;
        double qId[];
        /*queryArray va a tener los cosenos de todas las consultas de prueba contra
         la nueva consulta*/
        queryArray = SimDocCons("coseno_mrdd", "consulta_mrdd");
        List<double[]> consultasElegidas = new ArrayList<double[]>();
        /*consultasElegidas va a tener el numero de las Query de entrenamiento que 
         tengan un coseno mayor a 0.6*/
        for (int i = 0; i < queryArray.length; i++) {
            if (queryArray[i].getFrecuencia() >= 0.5) {
                qId = new double[3];
                qId[0] = (double) queryArray[i].getQuery_id();
                consultasElegidas.add(qId);
            }
        }
        this.consultasElegidas = consultasElegidas;
    }

    /*Debo sacar cada consulta de prueba de la BD y guardar sus frecuencias
     en double[], y luego guardar cada double[] en vectoresDePrueba
     Ejecutar despues de getConsultasElegidas*/
    public void getConsultasDePrueba() {
        DBConnection db = new DBConnection();
        List<Query> queryList = null;
        double[] queryVector;
        try {
            for (double[] q : this.consultasElegidas) {
                queryList = db.readDataBase("select * from mrdd where query_id = " + q[0]);
                queryVector = getVectorFromList(queryList);
                vectoresPrueba.add(queryVector);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*Saca los representativos de las consultasElegidas
     y los guarda en this.representativos*/
    public void getRepresentativos() {
        DBConnection db = new DBConnection();
        //ArrayList<Repre> queryList = null;
        //double[] queryVector;
        try {
            for (double[] q : this.consultasElegidas) {
                Representativo r = db.readDataBaseR("select * from representativos_mrdd where query_id = " + q[0]);
                this.representativos.add(r);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*Recibe las consultas elegidas y calcula el centroide*/
    public double[] calculaCentroide() {
        MoreMath mm = new MoreMath();
        double[] centroide = mm.averageVectors(vectoresPrueba);
        return centroide;
    }

    /*Calcula las distancias euclidianas de cada consulta con el centroide
     y guarda el inverso de cada distancia, luego calcula el numero de 
     documentos por consulta de prueba*/
    public void distEucl() {
        double suma_distancias = 0;
        /*Distancias y probabilidades inversas*/
        double centroide[] = calculaCentroide();
        for (int i = 0; i < consultasElegidas.size(); i++) {
            double temp = calculateDistance(vectoresPrueba.get(i), centroide);
            suma_distancias += temp;
        }
        
        for (int i = 0; i < consultasElegidas.size(); i++) {
            double temp = calculateDistance(vectoresPrueba.get(i), centroide);
            double temp1 = (temp * 100) / suma_distancias;
            consultasElegidas.get(i)[1] = 100 - temp1;
        }
        
        //Suma de probabilidades inversas
        double suma = 0;
        for (int i = 0; i < consultasElegidas.size(); i++) {
            suma += consultasElegidas.get(i)[1];
        }

        /*Numero de documentos por consulta
         numero por consulta / 3 = numero por motor*/
        for (int i = 0; i < consultasElegidas.size(); i++) {
            consultasElegidas.get(i)[2] = Math.rint((consultasElegidas.get(i)[1] * k)/ suma);
        }
    }

    public double calculateDistance(double[] array1, double[] array2) {
        double Sum = 0.0;
        for (int i = 0; i < array1.length; i++) {
            Sum = Sum + Math.pow((array1[i] - array2[i]), 2.0);
        }
        return Math.sqrt(Sum);
    }

    /*Con los this.representativos y las this.consultasElegidas, puedo calcular el numero
     de documentos por motor,y los voy guardando en representativo.googleDocs etc*/
    public void getDocsPorQuery() {
        for (int i = 0; i < consultasElegidas.size(); i++) {
            representativos.get(i).setGoogleDocs((int)Math.ceil(consultasElegidas.get(i)[2]
                    * representativos.get(i).getGoogleProp()));
            representativos.get(i).setYahooDocs((int)Math.ceil(consultasElegidas.get(i)[2]
                    * representativos.get(i).getYahooProp()));
            representativos.get(i).setBingDocs((int) Math.ceil(consultasElegidas.get(i)[2]
                    * representativos.get(i).getBingProp()));
            representativos.get(i).setMotorDocs((int) Math.ceil(consultasElegidas.get(i)[2]
                    * representativos.get(i).getMotorProp()));
        }
    }

    /*Calcula el total de documentos a recuperar de cada motor*/
    public void getTotalDocs() {
        for (Representativo r : representativos) {
            google += r.getGoogleDocs();
            yahoo += r.getYahooDocs();
            bing += r.getBingDocs();
            motor += r.getMotorDocs();
        }
    }
    
    //Inserta la consulta (ArrayList de Strings) a la tabla consulta_mrdd
    public void createQuery1(ArrayList<String> str) {
        try {
            DBConnection db = new DBConnection();
            for (int i = 0; i < str.size(); i++) {
                Query q = new Query();
                q.setTermino(str.get(i));
                q.setQuery_id(1);
                db.InsertQuery1(q);
            }

        } catch (SQLException e) {
            //Do something
        }
    }
     //Inserta la consulta (ArrayList de Strings) a la tabla consulta_mrdd
    public void createQuery0() {
        try {
            DBConnection db = new DBConnection();
            /*for (int i = 0; i < str.length; i++) {
                Query q = new Query();
                q.setTermino(str[i]);
                q.setQuery_id(1);
                db.InsertQuery1(q);
            }*/
            db.InsertQuery0();

        } catch (SQLException e) {
            //Do something
        }
    }
    
    
    //Consultas----------------------------------
    //Restaura consulta a su  estado original
    public void GenericStoredProc(String str1, String str2,String tabla) {
        try{
            DBConnection db = new DBConnection();
            db.SP(str1,str2,tabla);
        }catch(SQLException e){
            //Do something
        }
    }
    
    //Calcula la distancia entre una consulta y todos los documentos 
    //result[0] corresponde ala distancia con documento 1, result[1] corresponde al documento 2...
    //el parametro medida se refiere a la medida de similitud que se empleará
    //y tabla se refiere a la tabla consulta que se usará
    public Query2[] SimDocCons2(String medida, String tabla) {
        DBConnection db = new DBConnection();
        Query2 result[] = new Query2[10];

        try {
            result[0] = db.StoredProc3(medida);

            for (int i = 1; i < 10; i++) {
                db.SP(new Integer(i).toString(),new Integer(i-1).toString(),tabla);
                result[i] = db.StoredProc3(medida);
            }
            return result;
        } catch (SQLException e) {
            System.out.println("There was an error.");
            return result;
        }
    }
    
    //Inserta la consulta (ArrayList de Strings) a la tabla consulta
    public void createQuery(ArrayList<String> str) {
        try {
            DBConnection db = new DBConnection();
            for (int i = 0; i < str.size(); i++) {
                Query2 q = new Query2();
                q.setRaiz(str.get(i));
                q.setUri("0");
                db.InsertQuery(q);
            }

        } catch (SQLException e) {
            //Do something
        }
    }
}
