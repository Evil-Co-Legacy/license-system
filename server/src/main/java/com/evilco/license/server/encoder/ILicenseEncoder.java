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
package com.evilco.license.server.encoder;

import com.evilco.license.common.ILicense;
import com.evilco.license.common.codec.ILicenseCodec;
import com.evilco.license.common.exception.LicenseEncoderException;

import javax.annotation.Nonnull;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Provides default method structures for license encoders.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public interface ILicenseEncoder<T> extends ILicenseCodec {

	/**
	 * Encodes a license.
	 * @param license The license.
	 * @param outputStream The output.
	 */
	public void encode (@Nonnull ILicense license, @Nonnull DataOutputStream outputStream) throws IOException, LicenseEncoderException;

	/**
	 * Encodes a license.
	 * @param license The license.
	 * @return The license representation.
	 */
	public T encode (@Nonnull ILicense license) throws LicenseEncoderException;
}