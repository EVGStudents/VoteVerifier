/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.action;


import java.util.HashMap;
import javax.swing.Action;

/**
 *
 * @author prinstin
 */
public class ActionManager  {
    
    private HashMap actions;
    protected static ActionManager manager = null;
    
    private ActionManager()
    {
        actions = new HashMap();
    }

    public static ActionManager getInstance()
    {
        if (manager == null)
                manager = new ActionManager();
        return manager;
    }

    public void addActions(String name, Action action){
        actions.put(name, action);
    }


    public Action getAction(String key)
    {
        return (Action)actions.get(key);
    }



    public void setActionEnabled(String name, boolean enabled)
    {
        Action action = getAction(name);
        if(action != null)
            action.setEnabled(enabled);
    }


    
}
