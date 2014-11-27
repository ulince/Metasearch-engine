/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.util.HashMap;

/**
 *
 * @author Me
 */
public class Documento {

    public HashMap<Integer, String> baseDeDocs;
    
    
    
    public Documento(){
        baseDeDocs = new HashMap<>();
        
        baseDeDocs.put(new Integer(0), "GAMES CONSOLE MAKER Microsoft has talked about how...");
        baseDeDocs.put(new Integer(1), "Compared to Xbox 360, we've greatly improved...");
        baseDeDocs.put(new Integer(2), "This is it: the battle for the next generation...");
        baseDeDocs.put(new Integer(3), "Microsoft Studios audio director Paul...");
        baseDeDocs.put(new Integer(4), "DICE revealed the official PC requirements...");
        baseDeDocs.put(new Integer(5), "Microsoft and the Xbox One will be in..");
        baseDeDocs.put(new Integer(6), "One major point of contention cited among...");
        baseDeDocs.put(new Integer(7), "The forthcoming Xbox One got a bit of a...");
        baseDeDocs.put(new Integer(8), "The PlayStation 4 will go on sale in November...");
        baseDeDocs.put(new Integer(9), "Sony Corp. (6758) will release the PlayStation 4...");
        
    }
}
