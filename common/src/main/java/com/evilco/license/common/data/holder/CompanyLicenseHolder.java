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
package com.evilco.license.common.data.holder;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides a simple company license holder implementation.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public class CompanyLicenseHolder implements ILicenseHolder {

	/**
	 * Stores the company name.
	 */
	protected String companyName;

	/**
	 * Stores the license contact.
	 */
	protected ILicenseHolder contact;

	/**
	 * Constructs a new empty CompanyLicenseHolder.
	 */
	protected CompanyLicenseHolder () { }

	/**
	 * Constructs a new CompanyLicenseHolder.
	 * @param companyName The licensee name.
	 * @param contact The license contact person/company.
	 */
	public CompanyLicenseHolder (@Nonnull String companyName, @Nullable ILicenseHolder contact) {
		Preconditions.checkNotNull (companyName);

		this.companyName = companyName;
		this.contact = contact;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLicenseeName () {
		return this.companyName;
	}
}