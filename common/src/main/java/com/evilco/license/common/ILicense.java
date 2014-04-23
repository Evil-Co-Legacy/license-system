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
 * Provides default method structures for license representations.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public interface ILicense {

	/**
	 * Checks whether the license is valid.
	 * @return True if the license is still valid.
	 */
	public boolean isValid ();

	/**
	 * Checks whether the license is valid.
	 * @throws LicenseInvalidException
	 */
	public void validate () throws LicenseInvalidException;
}