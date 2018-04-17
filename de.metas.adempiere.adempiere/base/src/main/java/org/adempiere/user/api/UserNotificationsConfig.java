package org.adempiere.user.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.adempiere.util.Check;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

/*
 * #%L
 * de.metas.adempiere.adempiere.base
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

@Value
public class UserNotificationsConfig
{
	private int userId;
	private String userADLanguage; // might be null
	private int adClientId;
	private int adOrgId;

	@Getter(AccessLevel.NONE)
	private final ImmutableList<UserNotificationsGroup> userNotificationGroups; // needed for toBuilder()
	private Map<NotificationGroupName, UserNotificationsGroup> userNotificationGroupsByInternalName;
	private final UserNotificationsGroup defaults;

	private String email;

	@Builder(toBuilder = true)
	private UserNotificationsConfig(
			@NonNull final Integer userId,
			final String userADLanguage,
			final int adClientId,
			final int adOrgId,
			@NonNull @Singular final List<UserNotificationsGroup> userNotificationGroups,
			@NonNull final UserNotificationsGroup defaults,
			final String email)
	{
		Check.assumeGreaterOrEqualToZero(userId, "adUserId");

		this.userId = userId;
		this.userADLanguage = Check.isEmpty(userADLanguage) ? null : userADLanguage;
		
		this.adClientId = adClientId >= 0 ? adClientId : 0;
		this.adOrgId = adOrgId >= 0 ? adOrgId : 0;

		this.userNotificationGroups = ImmutableList.copyOf(userNotificationGroups);
		this.userNotificationGroupsByInternalName = Maps.uniqueIndex(userNotificationGroups, UserNotificationsGroup::getGroupInternalName);
		this.defaults = defaults;

		this.email = Check.isEmpty(email, true) ? null : email.trim();
	}

	public UserNotificationsGroup getGroupByName(@NonNull final NotificationGroupName groupName)
	{
		return userNotificationGroupsByInternalName.getOrDefault(groupName, defaults);
	}

	public UserNotificationsConfig deriveWithNotificationTypes(final Set<NotificationType> notificationTypes)
	{
		return toBuilder()
				.clearUserNotificationGroups()
				.defaults(UserNotificationsGroup.prepareDefault().notificationTypes(notificationTypes).build())
				.build();
	}
}
