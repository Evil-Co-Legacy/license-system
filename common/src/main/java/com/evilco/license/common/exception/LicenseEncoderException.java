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
package com.evilco.license.common.exception;

/**
 * Represents license encoding related errors.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public class LicenseEncoderException extends LicenseException {

	/**
	 * Constructs a new LicenseEncoderException.
	 */
	public LicenseEncoderException () {
		super ();
	}

	/**
	 * Constructs a new LicenseEncoderException.
	 * @param message The error mesage.
	 */
	public LicenseEncoderException (String message) {
		super (message);
	}

	/**
	 * Constructs a new LicenseEncoderException.
	 * @param message The error mesage.
	 * @param cause The error cause.
	 */
	public LicenseEncoderException (String message, Throwable cause) {
		super (message, cause);
	}

	/**
	 * Constructs a new LicenseEncoderException.
	 * @param cause The error cause.
	 */
	public LicenseEncoderException (Throwable cause) {
		super (cause);
	}

	/**
	 * Constructs a new LicenseEncoderException.
	 * @param message The error mesage.
	 * @param cause The error cause.
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	protected LicenseEncoderException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super (message, cause, enableSuppression, writableStackTrace);
	}
}