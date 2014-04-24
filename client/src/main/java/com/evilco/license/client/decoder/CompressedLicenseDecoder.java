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
import com.evilco.license.common.exception.LicenseDecoderException;
import com.google.common.base.CharMatcher;
import com.google.common.io.BaseEncoding;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Provides a compressed text-based decoder.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public class CompressedLicenseDecoder implements ILicenseDecoder<String> {

	/**
	 * Stores the child decoder.
	 */
	protected final ILicenseDecoder childDecoder;

	/**
	 * Constructs a new CompressedLicenseDecoder.
	 * @param decoder
	 */
	public CompressedLicenseDecoder (ILicenseDecoder decoder) {
		this.childDecoder = decoder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends ILicense> T decode (@Nonnull DataInputStream inputStream, @Nonnull Class<T> licenseType) throws IOException, LicenseDecoderException {
		throw new UnsupportedOperationException ("Decoding compressed licenses from DataInputStreams is not supported.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends ILicense> T decode (@Nonnull String input, @Nonnull Class<T> licenseType) throws LicenseDecoderException {
		// define streams
		ByteArrayInputStream inputStream = null;
		GZIPInputStream gzipInputStream = null;
		DataInputStream dataInputStream = null;

		// read data
		try {
			// remove newlines
			input = CharMatcher.anyOf ("\n\r").removeFrom (input);

			// decode data
			byte[] data = BaseEncoding.base64 ().decode (input);

			// create streams
			inputStream = new ByteArrayInputStream (data);
			gzipInputStream = new GZIPInputStream (inputStream);
			dataInputStream = new DataInputStream (gzipInputStream);

			// decode data
			return ((T) this.childDecoder.decode (dataInputStream, licenseType));
		} catch (IOException ex) {
			throw new LicenseDecoderException (ex.getMessage (), ex);
		} finally {
			IOUtils.closeQuietly (dataInputStream);
			IOUtils.closeQuietly (gzipInputStream);
			IOUtils.closeQuietly (inputStream);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAvailable () {
		return this.childDecoder.isAvailable ();
	}
}