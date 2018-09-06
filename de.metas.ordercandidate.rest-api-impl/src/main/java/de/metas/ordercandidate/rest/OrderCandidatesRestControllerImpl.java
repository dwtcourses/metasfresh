package de.metas.ordercandidate.rest;

import java.util.List;
import java.util.Properties;

import org.adempiere.ad.security.IUserRolePermissions;
import org.adempiere.ad.trx.api.ITrxManager;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.model.InterfaceWrapperHelper;
import org.adempiere.util.Services;
import org.compiere.util.Env;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableList;

import de.metas.ordercandidate.api.OLCand;
import de.metas.ordercandidate.api.OLCandCreateRequest;
import de.metas.ordercandidate.api.OLCandRepository;
import de.metas.ordercandidate.model.I_C_OLCand;
import lombok.NonNull;

/*
 * #%L
 * de.metas.ordercandidate.rest-api
 * %%
 * Copyright (C) 2018 metas GmbH
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

@RestController
@RequestMapping(OrderCandidatesRestEndpoint.ENDPOINT)
public class OrderCandidatesRestControllerImpl implements OrderCandidatesRestEndpoint
{
	public static final String DATA_SOURCE_INTERNAL_NAME = "SOURCE." + OrderCandidatesRestControllerImpl.class.getName();

	@Autowired
	private JsonConverters jsonConverters;
	@Autowired
	private OLCandRepository olCandRepo;

	@PostMapping
	@Override
	public JsonOLCand createOrder(@RequestBody final JsonOLCandCreateRequest request)
	{
		return createOrders(JsonOLCandCreateBulkRequest.of(request)).getSingleResult();
	}

	@PostMapping(PATH_BULK)
	@Override
	public JsonOLCandCreateBulkResponse createOrders(@RequestBody @NonNull final JsonOLCandCreateBulkRequest bulkRequest)
	{
		assertCanCreateNewOLCand();

		final ITrxManager trxManager = Services.get(ITrxManager.class);
		return trxManager.call(() -> creatOrdersInTrx(bulkRequest));
	}

	private JsonOLCandCreateBulkResponse creatOrdersInTrx(final JsonOLCandCreateBulkRequest bulkRequest)
	{
		final MasterdataProvider masterdataProvider = MasterdataProvider.newInstance();
		final List<OLCandCreateRequest> requests = bulkRequest
				.getRequests()
				.stream()
				.map(request -> fromJson(request, masterdataProvider))
				.collect(ImmutableList.toImmutableList());

		final List<OLCand> olCands = olCandRepo.create(requests);
		return jsonConverters.toJson(olCands, masterdataProvider);
	}

	private void assertCanCreateNewOLCand()
	{
		final IUserRolePermissions userPermissions = Env.getUserRolePermissions();
		final Properties ctx = Env.getCtx();
		final int adClientId = Env.getAD_Client_ID(ctx);
		final int adOrgId = Env.getAD_Org_ID(ctx);
		final int adTableId = InterfaceWrapperHelper.getTableId(I_C_OLCand.class);
		final String errmsg = userPermissions.checkCanCreateNewRecord(adClientId, adOrgId, adTableId);
		if (errmsg != null)
		{
			throw new AdempiereException(errmsg);
		}
	}

	private OLCandCreateRequest fromJson(
			final JsonOLCandCreateRequest request,
			final MasterdataProvider masterdataProvider)
	{
		return jsonConverters.fromJson(request, masterdataProvider)
				.adInputDataSourceInternalName(DATA_SOURCE_INTERNAL_NAME)
				.build();
	}
}
