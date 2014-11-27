/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metamotor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import persistencia.DBConnection;
import persistencia.Query;
import persistencia.Representativo;

/**
 *
 * @author Me
 */
public class MetaMotor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       Operaciones op = new Operaciones();
      /*Query[] q = op.SimDocCons("coseno_mrdd", "consulta_mrdd");
       List<Query> qList = (List<Query>) Arrays.asList(q);
       Collections.sort(qList);
       for(int i = 0; i < q.length;i++){
           System.out.println(qList.get(i).getFrecuencia());
       }*/
       /*op.getConsultasElegidas();
       op.getConsultasDePrueba();
       op.k = 10;
       op.distEucl();      
       for(int i = 0; i < op.consultasElegidas.size();i ++){
           System.out.println(op.consultasElegidas.get(i)[2]);
       }*/
       /*for(int i = 0; i < op.vectoresPrueba.size(); i++){
           System.out.println(op.calculateDistance(op.calculaCentroide(), op.vectoresPrueba.get(i)));
       }*/
       
        op.setK(10);
        op.getConsultasElegidas();
        op.getConsultasDePrueba();
       /* DBConnection db = new DBConnection();
        Representativo r = null;
        try{
        r = db.readDataBaseR("select * from representativos_mrdd where query_id = " + 1);
        }catch(Exception e){
        
        }
        
        System.out.println(r.getGoogleProp());*/
        
        
        op.getRepresentativos();
        op.distEucl();
        op.getDocsPorQuery();
        op.getTotalDocs();
 
        //System.out.println(op.representativos.size());
        
        /*if(op.representativos == null){
            System.out.println("es null");
        }*/
        for(int i = 0; i < op.representativos.size(); i++){
           System.out.println(op.representativos.get(i).getGoogleDocs());
       }
        
       // op.getDocsPorQuery();
        //op.getTotalDocs();
        
       // System.out.println("Google\n"+op.google + "\n" + op.bing);
    }
}
