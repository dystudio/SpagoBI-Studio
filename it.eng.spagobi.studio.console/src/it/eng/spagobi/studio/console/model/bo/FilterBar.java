/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.studio.console.model.bo;

import java.util.Vector;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class FilterBar {
	private String type;
	private Defaults defaults;
	private boolean refreshDataAfterAction;
	private Vector<Filter> filters;
	private Vector<GenericAction> actions;
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the defaults
	 */
	public Defaults getDefaults() {
		return defaults;
	}
	/**
	 * @param defaults the defaults to set
	 */
	public void setDefaults(Defaults defaults) {
		this.defaults = defaults;
	}
	/**
	 * @return the filters
	 */
	public Vector<Filter> getFilters() {
		return filters;
	}
	/**
	 * @param filters the filters to set
	 */
	public void setFilters(Vector<Filter> filters) {
		this.filters = filters;
	}
	/**
	 * @return the refreshDataAfterAction
	 */
	public boolean isRefreshDataAfterAction() {
		return refreshDataAfterAction;
	}
	/**
	 * @param refreshDataAfterAction the refreshDataAfterAction to set
	 */
	public void setRefreshDataAfterAction(boolean refreshDataAfterAction) {
		this.refreshDataAfterAction = refreshDataAfterAction;
	}
	/**
	 * @return the actions
	 */
	public Vector<GenericAction> getActions() {
		return actions;
	}
	/**
	 * @param actions the actions to set
	 */
	public void setActions(Vector<GenericAction> actions) {
		this.actions = actions;
	}

}
