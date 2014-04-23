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
package com.evilco.license.common;

import com.evilco.license.common.data.ILicenseHolder;
import com.evilco.license.common.exception.LicenseInvalidException;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.Date;

/**
 * Provides a default implementation for license representations.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public abstract class AbstractLicense implements ILicense {

	/**
	 * Stores the license expiration.
	 */
	protected Date expiration = null;

	/**
	 * Stores the license licensee.
	 */
	protected ILicenseHolder licensee;

	/**
	 * Constructs a new empty AbstractLicense.
	 */
	protected AbstractLicense () { }

	/**
	 * Constructs a new AbstractLicense.
	 * @param licensee
	 * @param expiration
	 */
	protected AbstractLicense (@Nonnull ILicenseHolder licensee, Date expiration) {
		Preconditions.checkNotNull (licensee);

		this.licensee = licensee;
		this.expiration = expiration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getExpirationDate () {
		if (this.expiration == null) return null;
		return this.expiration;
	}

	/**
	 * Returns the licensee.
	 * @return
	 */
	public ILicenseHolder getLicensee () {
		return this.licensee;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
		if (this.isExpired ()) throw new LicenseInvalidException ("The license is expired.");
	}
}