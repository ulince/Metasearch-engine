/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

/**
 *
 * @author Me
 */
public class Representativo {
    
    public int query_id;
    double googleProp;/*proporcion de documentos de Google*/
    double yahooProp;/*prop de docs de Yahoo*/
    double bingProp;/*prop de docs de Bing*/
    int googleDocs;/*documentos a recuperar de Google*/
    int yahooDocs;/*documentos a recuperar de Yahoo*/
    int bingDocs;/*documentos a recuperar de Bing*/
    int motorDocs;
    double motorProp;
    
    public Representativo(int id){
        this.query_id = id;
    }

    public double getGoogleProp() {
        return googleProp;
    }

    public void setGoogleProp(double googleProp) {
        this.googleProp = googleProp;
    }

    public double getYahooProp() {
        return yahooProp;
    }

    public void setYahooProp(double yahooProp) {
        this.yahooProp = yahooProp;
    }

    public double getBingProp() {
        return bingProp;
    }

    public void setBingProp(double bingProp) {
        this.bingProp = bingProp;
    }
    
     public double getMotorProp() {
        return motorProp;
    }

    public void setMotorProp(double motorProp) {
        this.motorProp = motorProp;
    }


    public int getGoogleDocs() {
        return googleDocs;
    }

    public void setGoogleDocs(int googleDocs) {
        this.googleDocs = googleDocs;
    }

    public int getYahooDocs() {
        return yahooDocs;
    }

    public void setYahooDocs(int yahooDocs) {
        this.yahooDocs = yahooDocs;
    }

    public int getBingDocs() {
        return bingDocs;
    }

    public void setBingDocs(int bingDocs) {
        this.bingDocs = bingDocs;
    }
    
     public int getMotorDocs() {
        return motorDocs;
    }

    public void setMotorDocs(int motorDocs) {
        this.motorDocs = motorDocs;
    }
    
    
    
}



