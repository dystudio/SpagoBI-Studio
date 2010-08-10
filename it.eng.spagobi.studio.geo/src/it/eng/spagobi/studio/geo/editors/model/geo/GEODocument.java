/**
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class GEODocument  implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6527389398919946121L;

	public GEODocument(GEODocument geoDocToCopy) {
		
	}
	private MapProvider mapProvider;
	private DatamartProvider datamartProvider;
	private MapRenderer mapRenderer;
	
	public MapProvider getMapProvider() {
		return mapProvider;
	}
	public void setMapProvider(MapProvider mapProvider) {
		this.mapProvider = mapProvider;
	}
	public DatamartProvider getDatamartProvider() {
		return datamartProvider;
	}
	public void setDatamartProvider(DatamartProvider datamartProvider) {
		this.datamartProvider = datamartProvider;
	}
	public MapRenderer getMapRenderer() {
		return mapRenderer;
	}
	public void setMapRenderer(MapRenderer mapRenderer) {
		this.mapRenderer = mapRenderer;
	}

}
