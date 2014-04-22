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
package com.evilco.license.client.decoder;

import com.evilco.license.common.ILicense;
import com.evilco.license.common.codec.ILicenseCodec;
import com.evilco.license.common.exception.LicenseDecoderException;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Provides default method structures for license decoders.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public interface ILicenseDecoder<T> extends ILicenseCodec {

	/**
	 * Decodes a license file.
	 * @param inputStream A license stream.
	 * @param licenseType The license representation type.
	 * @return The decoded license.
	 * @throws IOException
	 * @throws LicenseDecoderException
	 */
	public ILicense decode (@Nonnull DataInputStream inputStream, @Nonnull Class<? extends ILicense> licenseType) throws IOException, LicenseDecoderException;

	/**
	 * Decodes a license file.
	 * @param input The license representation.
	 * @param licenseType The license representation type.
	 * @return A decoded license instance.
	 * @throws LicenseDecoderException
	 */
	public ILicense decode (@Nonnull T input, @Nonnull Class<? extends ILicense> licenseType) throws LicenseDecoderException;
}