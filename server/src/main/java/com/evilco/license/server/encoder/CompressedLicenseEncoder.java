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
import com.evilco.license.common.exception.LicenseEncoderException;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.io.BaseEncoding;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Provides a compressed text-based encoder.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public class CompressedLicenseEncoder implements ILicenseEncoder<String> {

	/**
	 * Defines the chunk width.
	 */
	public static final int LICENSE_CHUNK_WIDTH = 80;

	/**
	 * Stores the child encoder.
	 */
	protected final ILicenseEncoder childEncoder;

	/**
	 * Constructs a new CompressedLicenseEncoder.
	 * @param encoder
	 */
	public CompressedLicenseEncoder (ILicenseEncoder encoder) {
		this.childEncoder = encoder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void encode (@Nonnull ILicense license, @Nonnull DataOutputStream outputStream) throws IOException, LicenseEncoderException {
		throw new UnsupportedOperationException ("Encoding compressed license representations into streams is not allowed.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encode (@Nonnull ILicense license) throws LicenseEncoderException {
		// define streams
		ByteArrayOutputStream outputStream = null;
		GZIPOutputStream gzipOutputStream = null;
		DataOutputStream dataOutputStream = null;

		// encode data
		try {
			// create stream instances
			outputStream = new ByteArrayOutputStream ();
			gzipOutputStream = new GZIPOutputStream (outputStream);
			dataOutputStream = new DataOutputStream (gzipOutputStream);

			// encode data
			this.childEncoder.encode (license, dataOutputStream);

			// flush buffers
			gzipOutputStream.flush ();
			outputStream.flush ();
		} catch (IOException ex) {
			throw new LicenseEncoderException (ex);
		} finally {
			IOUtils.closeQuietly (dataOutputStream);
			IOUtils.closeQuietly (gzipOutputStream);
			IOUtils.closeQuietly (outputStream);
		}

		// Encode Base64
		String licenseText = BaseEncoding.base64 ().encode (outputStream.toByteArray ());

		// split text into chunks
		Joiner joiner = Joiner.on ("\n").skipNulls ();
		return joiner.join (Splitter.fixedLength (LICENSE_CHUNK_WIDTH).split (licenseText));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAvailable () {
		return this.childEncoder.isAvailable ();
	}
}