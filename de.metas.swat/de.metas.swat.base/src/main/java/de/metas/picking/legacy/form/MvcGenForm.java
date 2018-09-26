/******************************************************************************
 * Copyright (C) 2009 Low Heng Sin                                            *
 * Copyright (C) 2009 Idalica Corporation                                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package de.metas.picking.legacy.form;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.minigrid.IMiniTable;
import org.slf4j.Logger;

import de.metas.logging.LogManager;
import de.metas.process.IProcessExecutionListener;
import de.metas.process.ProcessInfo;

/**
 * Custom form controller base class
 * 
 */
public abstract class MvcGenForm implements IProcessExecutionListener {

	public static final String PROP_UI_LOCKED = "uiLocked";

	public static final String PROP_SELPANEL_OKBUTTON_ENABLED = "selPanelOKButton";
	
	public static final String PROP_GENPANEL_OKBUTTON_ENABLED = "genPanelOKButton";
	
	public static final String PROP_GENPANEL_REFRESHBUTTON_ENABLED = "genPanelRefreshButton";
	
	private static final Logger logger = LogManager.getLogger(MvcGenForm.class);

	private MvcMdGenForm model;

	protected abstract void configureMiniTable(IMiniTable miniTable);

	public void validate() {

	}

	public String generate() {
		return null;
	}

	public void executeQuery() {

	}

	public void setModel(final MvcMdGenForm model)
	{
		if (model.getMiniTable() == null)
		{
			throw new AdempiereException("Illegal state: model's IMiniTable is null");
		}
		
		// NOTE: we need to set the model before calling other methods because in other methods it's a big change to call getModel() which will result in NPE
		this.model = model;
		
		configureMiniTable(model.getMiniTable());
	}

	@SuppressWarnings("unchecked")
	public <T extends MvcMdGenForm> T getModel() {
		return (T) model;
	}

	public boolean isUILocked() {

		return getModel().uiLocked;
	}

	@Override
	public void lockUI(ProcessInfo pi) {

		//c.ghita@metas.ro : need this for new picking/package
		getModel().uiLocked = true;
	}

	@Override
	public void unlockUI(ProcessInfo pi) {

		//c.ghita@metas.ro : need this for new picking/package
		getModel().uiLocked = false;
	}

	public void executeASync(ProcessInfo pi) {
		logger.info("Doing nothing");
	}

	protected abstract void initModel(MvcMdGenForm model);
}
