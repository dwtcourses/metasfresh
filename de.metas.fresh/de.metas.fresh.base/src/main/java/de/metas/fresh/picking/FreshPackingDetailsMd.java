package de.metas.fresh.picking;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.lang.ObjectUtils;
import org.adempiere.warehouse.WarehouseId;

import com.google.common.collect.ImmutableList;

import de.metas.adempiere.form.terminal.context.ITerminalContext;
import de.metas.bpartner.BPartnerId;
import de.metas.bpartner.BPartnerLocationId;
import de.metas.picking.legacy.form.IPackingDetailsModel;
import de.metas.picking.model.I_M_PickingSlot;
import de.metas.picking.service.IPackingItem;
import de.metas.product.ProductId;
import de.metas.util.Check;
import de.metas.util.time.SystemTime;
import lombok.NonNull;

/**
 * 
 * @author cg
 */
public class FreshPackingDetailsMd implements IPackingDetailsModel
{
	private final ImmutableList<IPackingItem> unallocatedLines;

	private final ImmutableList<PickingSlotKey> availablePickingSlots;
	private final ImmutableList<PackingMaterialKey> availablePackingMaterialKeys;

	public FreshPackingDetailsMd(
			@NonNull final ITerminalContext terminalContext,
			final Collection<IPackingItem> unallocatedLines)
	{
		Check.assumeNotEmpty(unallocatedLines, "unallocatedLines not empty");

		this.unallocatedLines = ImmutableList.copyOf(unallocatedLines);

		final Date date = SystemTime.asDayTimestamp();
		final PackingMaterialKeyBuilder packingMaterialKeysBuilder = new PackingMaterialKeyBuilder(terminalContext, date);
		final PickingSlotKeyBuilder pickingSlotKeysBuilder = new PickingSlotKeyBuilder(terminalContext);
		for (final IPackingItem freshPackingItem : this.unallocatedLines)
		{
			final ProductId productId = freshPackingItem.getProductId();
			final BPartnerId bpartnerId = freshPackingItem.getBPartnerId();
			final BPartnerLocationId bpartnerLocationId = freshPackingItem.getBPartnerLocationId();
			final Set<WarehouseId> warehouseIds = freshPackingItem.getWarehouseIds();

			//
			// Get/Create PackingMaterialKeys
			packingMaterialKeysBuilder.addProduct(productId, bpartnerId, bpartnerLocationId);

			//
			// Get PickingSlots
			pickingSlotKeysBuilder.addBPartner(bpartnerId, bpartnerLocationId, warehouseIds);
		}

		this.availablePickingSlots = pickingSlotKeysBuilder.getPickingSlotKeys();
		if (availablePickingSlots.isEmpty())
		{
			throw new AdempiereException("@NotFound@ @" + I_M_PickingSlot.COLUMNNAME_M_PickingSlot_ID + "@");
		}

		//
		// Build available packing materials
		this.availablePackingMaterialKeys = packingMaterialKeysBuilder.getPackingMaterialKeys();
		if (availablePackingMaterialKeys.isEmpty())
		{
			throw new AdempiereException("@NotFound@ @M_HU_PI_ID@");
		}
	}

	/**
	 * 
	 * @return unallocated lines (read-only collection)
	 */
	public ImmutableList<IPackingItem> getUnallocatedLines()
	{
		return unallocatedLines;
	}

	public ImmutableList<PackingMaterialKey> getAvailablePackingMaterialKeys()
	{
		return availablePackingMaterialKeys;
	}

	public ImmutableList<PickingSlotKey> getAvailablePickingSlots()
	{
		return availablePickingSlots;
	}

	@Override
	public String toString()
	{
		return ObjectUtils.toString(this);
	}

}
