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
import com.evilco.license.common.exception.LicenseInvalidException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;

/**
 * Provides a simple implementation for expiring licenses.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public abstract class AbstractExpiringLicense extends AbstractLicense {

	/**
	 * Stores the license expiration.
	 */
	protected Date expiration = null;

	/**
	 * Constructs a new empty AbstractExpiringLicense.
	 */
	protected AbstractExpiringLicense () {
		super ();
	}

	/**
	 * Constructs a new AbstractExpiringLicense.
	 * @param licensee
	 * @param expiration
	 */
	protected AbstractExpiringLicense (@Nonnull ILicenseHolder licensee, @Nullable Date expiration) {
		super (licensee);
		this.expiration = expiration;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getExpirationDate () {
		if (this.expiration == null) return null;
		return this.expiration;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpired () {
		return (this.expiration != null && this.expiration.before (new Date ()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid () {
		return !this.isExpired ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate () throws LicenseInvalidException {
		super.validate ();
		if (this.isExpired ()) throw new LicenseInvalidException ("The license is expired.");
	}
}