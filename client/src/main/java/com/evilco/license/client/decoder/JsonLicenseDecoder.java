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
import com.evilco.license.common.data.holder.ILicenseHolder;
import com.evilco.license.common.data.holder.LicenseHolderJsonAdapter;
import com.evilco.license.common.exception.LicenseDecoderException;
import com.evilco.license.common.exception.LicenseInvalidException;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.*;

/**
 * Provides a simple json based license decoder.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public class JsonLicenseDecoder implements ILicenseDecoder<byte []> {

	/**
	 * Defines the signature algorithm.
	 */
	public static final String ALGORITH_SIGNATURE = "SHA256withRSA";

	/**
	 * Defines the license text charset.
	 */
	public static final Charset CHARSET_LICENSE_TEXT = Charsets.UTF_8;

	/**
	 * Stores the internal gson builder.
	 */
	protected final GsonBuilder gsonBuilder = new GsonBuilder ();

	/**
	 * Stores the license public key.
	 */
	protected final PublicKey publicKey;

	/**
	 * Constructs a new SignatureLicenseDecoder.
	 * @param publicKey
	 */
	public JsonLicenseDecoder (@Nonnull PublicKey publicKey) {
		Preconditions.checkNotNull (publicKey, "publicKey");

		// store key
		this.publicKey = publicKey;

		// add default type adapters
		this.gsonBuilder.registerTypeAdapter (ILicenseHolder.class, new LicenseHolderJsonAdapter ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends ILicense> T decode (@Nonnull DataInputStream inputStream, @Nonnull Class<T> licenseType) throws LicenseDecoderException {
		Preconditions.checkNotNull (inputStream, "inputStream");
		Preconditions.checkNotNull (licenseType, "licenseType");

		// load data
		try {
			// read version
			double version = inputStream.readDouble ();
			this.gsonBuilder.setVersion (version);

			// read data length
			short dataLength = inputStream.readShort ();

			// read data
			byte[] data = new byte[dataLength];
			inputStream.readFully (data);

			// decode string
			String licenseText = new String (data, CHARSET_LICENSE_TEXT);

			// read signature length
			int signatureLength = inputStream.readShort ();

			// read signature
			byte[] signature = new byte[signatureLength];
			inputStream.readFully (signature);

			// get signature algorithm
			Signature signatureAlgorithm = this.getSignatureAlgorithm ();

			// initialize implementation
			signatureAlgorithm.initVerify (this.publicKey);

			// store data
			signatureAlgorithm.update (data);

			// verify
			if (!signatureAlgorithm.verify (signature)) throw new LicenseInvalidException ("The license signature is not valid.");

			// read json data
			ILicense license = this.getGson ().fromJson (licenseText, licenseType);

			// validate license
			license.validate ();

			// return license
			return ((T) license);
		} catch (IOException ex) {
			throw new LicenseDecoderException (ex.getMessage (), ex);
		} catch (InvalidKeyException ex) {
			throw new LicenseDecoderException (ex.getMessage (), ex);
		} catch (SignatureException ex) {
			throw new LicenseInvalidException (ex.getMessage (), ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends ILicense> T decode (@Nonnull byte[] input, @Nonnull Class<T> licenseType) throws LicenseDecoderException {
		// create input stream
		ByteArrayInputStream inputStream = new ByteArrayInputStream (input);
		DataInputStream dataInputStream = new DataInputStream (inputStream);

		// read data
		return this.decode (dataInputStream, licenseType);
	}

	/**
	 * Returns the gson instance for json de- and encoding.
	 * @return The gson instance.
	 */
	public Gson getGson () {
		return this.gsonBuilder.create ();
	}

	/**
	 * Returns the gson builder.
	 * @return
	 */
	public GsonBuilder getGsonBuilder () {
		return this.gsonBuilder;
	}

	/**
	 * Returns the signature algorithm used to verify licenses.
	 * @return The signature algorithm.
	 */
	public Signature getSignatureAlgorithm () {
		try {
			return Signature.getInstance (ALGORITH_SIGNATURE);
		} catch (NoSuchAlgorithmException ex) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAvailable () {
		return (this.getSignatureAlgorithm () != null);
	}
}