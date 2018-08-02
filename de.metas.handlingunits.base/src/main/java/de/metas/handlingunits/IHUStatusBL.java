package de.metas.handlingunits;

import java.util.Collection;

/*
 * #%L
 * de.metas.handlingunits.base
 * %%
 * Copyright (C) 2015 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.ISingletonService;

import de.metas.handlingunits.model.I_M_HU;

public interface IHUStatusBL extends ISingletonService
{
	/**
	 * Tell if the storages of HUs with the given {@code huStatus} shall be considered when computing the on hand quantity.<br>
	 * E.g. planned HUs have a storage, but shall not be considered.
	 *
	 * @param huStatus
	 * @return
	 */
	boolean isQtyOnHand(String huStatus);

	/**
	 * See {@link #isQtyOnHand(String)} to get the idea.
	 *
	 * @return
	 */
	List<String> getQtyOnHandStatuses();

	/**
	 * Tell if the packing materials of empty HUs with the given status can be moved to the dedicated empties warehouse.
	 *
	 * @param huStatus
	 * @return
	 */
	boolean isMovePackagingToEmptiesWarehouse(String huStatus);

	/**
	 * Assert that a change of {@link I_M_HU#COLUMN_HUStatus} from the given {@code oldHuStatus} to the given {@code newHuStatus} is allowd.
	 *
	 * @param huRecord used to create a more informative error message
	 * @throw {@link AdempiereException} if the transition is not allowed.
	 */
	void assertStatusChangeIsAllowed(I_M_HU huRecord, String oldHuStatus, String newHuStatus);

	/**
	 * Assert that {@link I_M_HU#COLUMN_M_Locator_ID} may be updated in HUs that have the given {@code huStatus}.
	 *
	 * @param huRecord used to create a more informative error message
	 * @throws AdempiereException if the locatorId may not be updated.
	 */
	void assertLocatorChangeIsAllowed(I_M_HU huRecord, String huStatus);

	boolean isStatusPlanned(I_M_HU huRecord);

	boolean isStatusActive(I_M_HU huRecord);

	boolean isStatusIssued(I_M_HU huRecord);

	boolean isStatusDestroyed(I_M_HU huRecord);

	boolean isStatusShipped(I_M_HU huRecord);

	/**
	 * Check if an HU has a status that is "physical"/ "concrete"/ "material" Which means the HU exists as a box/ will still be used by us.
	 *
	 * The following hu statuses are not phyical:
	 * <ul>
	 * <li>{@link X_M_HU_Status#HUSTATUS_Planning Planning}: is a draft state, may or may not be used further
	 * <li>{@link X_M_HU_Status#HUSTATUS_Destroyed Destroyed}: not used any longer
	 * <li>{@link X_M_HU_Status#HUSTATUS_Shipped Shipped}: No longer in our warehouses
	 * </ul>
	 * NOTE: if status is <code>null</code>, it is considered not physical. It means that the HU was just created and will soon get another status.
	 *
	 * In the future, if another status of such kind (let's call it "intangible"), please add it in the implementation of this method.
	 *
	 * @return <code>true</code> if the status is a "physical" status (active or picked), false otherwise
	 */
	boolean isPhysicalHU(I_M_HU huRecord);

	/**
	 * Set the status of the HU. <br>
	 * In case we are dealing with a status that implies moving to/from Gebindelager, also do the collection of HUs in the huContext given as parameter (task 07617).<br>
	 *
	 * NOTE: this method is not saving the HU.
	 *
	 * @param huContext mandatory, because depending on the given {@code huStatus}, we might need the context's {@link IHUContext#getHUPackingMaterialsCollector()}.
	 */
	void setHUStatus(IHUContext huContext, I_M_HU hu, String huStatus);

	/**
	 * Same as {@link #setHUStatus(IHUContext, I_M_HU, String)}, but if <code>forceFetchPackingMaterial=true</code>, then the packing material will be fetched automatically.
	 *
	 * NOTE: this method is not saving the HU.
	 */
	void setHUStatus(IHUContext huContext, I_M_HU hu, String huStatus, boolean forceFetchPackingMaterial);

	/**
	 * Activate the HU (assuming it was Planning)
 */
	void setHUStatusActive(Collection<I_M_HU> hus);

}
