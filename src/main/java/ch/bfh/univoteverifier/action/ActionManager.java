/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.action;

import java.util.HashMap;
import javax.swing.Action;

/**
 * This class maintains a list of Actions and implements methods to add and
 * retrieve Actions.
 *
 * @author prinstin
 */
public class ActionManager {

	private HashMap actions;
	protected static ActionManager manager = null;

	/**
	 * Create an action manager. Only one will be created.
	 */
	private ActionManager() {
		actions = new HashMap();
	}

	/**
	 * Get the instance of this class. This is a singleton pattern, and the
	 * same instance will always be returned.
	 *
	 * @return ActionManager the one and only instance.
	 */
	public static ActionManager getInstance() {
		if (manager == null) {
			manager = new ActionManager();
		}
		return manager;
	}

	/**
	 * Add an action to the list managed by this class.
	 *
	 * @param name A String which gives the name of the Action.
	 * @param action An Action.
	 */
	public void addActions(String name, Action action) {
		actions.put(name, action);
	}

	/**
	 * Get the action with the matching name.
	 *
	 * @param key the String of the name of an Action.
	 * @return Action the action with the name of the key.
	 */
	public Action getAction(String key) {
		return (Action) actions.get(key);
	}

	/**
	 * Change the enabled state of an Action to the boolean value provided.
	 *
	 * @param name The name of the Action for which to change the state.
	 * @param enabled boolean value for which true corresponds to enabled.
	 */
	public void setActionEnabled(String name, boolean enabled) {
		Action action = getAction(name);
		if (action != null) {
			action.setEnabled(enabled);
		}
	}
}
