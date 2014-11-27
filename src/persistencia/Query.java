/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

/**
 *
 * @author Me
 */
public class Query implements Comparable<Query>{
    private int query_id;
    private String termino;
    private float frecuencia;
    
    public Query(int aQuery_id, String aTermino,float aFrecuencia){
        this.query_id = aQuery_id;
        this.termino = aTermino;
        this.frecuencia = aFrecuencia;
    }
    
    public Query(){
        
    }

    public int getQuery_id() {
        return query_id;
    }

    public void setQuery_id(int query_id) {
        this.query_id = query_id;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public float getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(float frecuencia) {
        this.frecuencia = frecuencia;
    }
    
    public int compareTo(Query query){
        if(getFrecuencia() < query.getFrecuencia()){
            return -1;
        }
         if(getFrecuencia() == query.getFrecuencia()){
            return 0;
        }
          if(getFrecuencia() > query.getFrecuencia()){
            return 1;
        }
        return 0;

    }
       
}
