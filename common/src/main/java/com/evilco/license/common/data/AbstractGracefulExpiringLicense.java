/**
 * Copyright (C) 2014 Evil-Co <http://wwww.evil-co.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evilco.license.common.data;

import com.evilco.license.common.data.holder.ILicenseHolder;
import com.evilco.license.common.exception.LicenseExpirationException;
import com.evilco.license.common.exception.LicenseInvalidException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Date;

/**
 * Provides an advanced implementation for expiring licenses.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public abstract class AbstractGracefulExpiringLicense extends AbstractExpiringLicense {

	/**
	 * Defines the default grace period (in days).
	 */
	public static final int DEFAULT_GRACE_PERIOD = 10;

	/**
	 * Constructs a new empty AbstractGracefulExpiringLicense.
	 */
	protected AbstractGracefulExpiringLicense () {
		super ();
	}

	/**
	 * Constructs a new AbstractGracefulExpiringLicense.
	 * @param licensee The license holder.
	 * @param expiration The license expiration.
	 * @param gracePeriod The grace period (in days).
	 */
	protected AbstractGracefulExpiringLicense (@Nonnull ILicenseHolder licensee, @Nullable Date expiration, int gracePeriod) {
		super (licensee, expiration);
	}

	/**
	 * Constructs a new AbstractGracefulExpiringLicense.
	 * @param licensee The license holder.
	 * @param expiration The license expiration.
	 */
	protected AbstractGracefulExpiringLicense (@Nonnull ILicenseHolder licensee, @Nullable Date expiration) {
		this (licensee, expiration, DEFAULT_GRACE_PERIOD);
	}

	/**
	 * Returns the grace period end.
	 * @return The grace period end.
	 */
	public Date getGracePeriodExpiration () {
		// get calendar instance
		Calendar calendar = Calendar.getInstance ();

		// set date to expiration date
		calendar.setTime (this.getExpirationDate ());

		// add grace period
		calendar.add (Calendar.DATE, this.DEFAULT_GRACE_PERIOD);

		// return finished date
		return calendar.getTime ();
	}

	/**
	 * Checks whether the license is still in it's grace period.
	 * @return True if the license is still in it's grace period.
	 */
	public boolean isInGracePeriod () {
		return this.getGracePeriodExpiration ().after (new Date ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate () throws LicenseInvalidException {
		try {
			super.validate ();
		} catch (LicenseExpirationException ex) {
			if (!this.isInGracePeriod ()) throw ex;
		}
	}
}