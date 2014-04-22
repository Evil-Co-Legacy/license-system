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

import com.evilco.license.common.exception.LicenseInvalidException;

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
	protected long expiration;

	/**
	 * Stores the license licensee.
	 */
	protected String licensee;

	/**
	 * Constructs a new empty AbstractLicense.
	 */
	protected AbstractLicense () { }

	/**
	 * Constructs a new AbstractLicense.
	 * @param licensee
	 * @param expiration
	 */
	protected AbstractLicense (String licensee, long expiration) {
		this.licensee = licensee;
		this.expiration = expiration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getExpirationDate () {
		return (new Date (((long) this.expiration * 1000)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isExpired () {
		return (this.expiration < (System.currentTimeMillis () / 1000L));
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