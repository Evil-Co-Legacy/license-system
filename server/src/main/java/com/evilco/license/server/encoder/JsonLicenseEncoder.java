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
import com.evilco.license.common.annotation.LicenseVersion;
import com.evilco.license.common.data.ILicenseHolder;
import com.evilco.license.common.data.LicenseHolderJsonAdater;
import com.evilco.license.common.exception.LicenseEncoderException;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.*;

/**
 * Provides a simple JSON based encoder.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public class JsonLicenseEncoder implements ILicenseEncoder<byte[]> {

	/**
	 * Defines the signature algorithm.
	 */
	public static final String ALGORITH_SIGNATURE = "SHA256withRSA";

	/**
	 * Defines the license text charset.
	 */
	public static final Charset CHARSET_LICENSE_TEXT = Charsets.UTF_8;

	/**
	 * Stores the gson builder.
	 */
	protected final GsonBuilder gsonBuilder = new GsonBuilder ();

	/**
	 * Stores the private signature key.
	 */
	protected final PrivateKey privateKey;

	/**
	 * Constructs a new JsonLicenseEncoder.
	 * @param privateKey
	 */
	public JsonLicenseEncoder (@Nonnull PrivateKey privateKey) {
		Preconditions.checkNotNull (privateKey, "privateKey");

		// store private key
		this.privateKey = privateKey;

		// add default adapters
		this.gsonBuilder.registerTypeAdapter (ILicenseHolder.class, new LicenseHolderJsonAdater ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void encode (@Nonnull ILicense license, @Nonnull DataOutputStream outputStream) throws IOException, LicenseEncoderException {
		Preconditions.checkNotNull (outputStream, "outputStream");

		// write data
		try {
			// get version
			double version = (license.getClass ().isAnnotationPresent (LicenseVersion.class) ? license.getClass ().getAnnotation (LicenseVersion.class).value () : 1.0);
			this.gsonBuilder.setVersion (version);

			// serialize data
			String data = this.getGson ().toJson (license);
			byte[] dataRaw = data.getBytes (CHARSET_LICENSE_TEXT);

			// write data
			outputStream.writeDouble (version);
			outputStream.writeInt (dataRaw.length);
			outputStream.write (dataRaw);

			// create signature
			Signature signature = this.getSignatureAlgorithm ();

			// initialize signature algorithm
			signature.initSign (this.privateKey);

			// sign
			signature.update (dataRaw);
			byte[] signatureRaw = signature.sign ();

			// write signature
			outputStream.writeInt (signatureRaw.length);
			outputStream.write (signatureRaw);

			// flush
			outputStream.flush ();
		} catch (InvalidKeyException ex) {
			throw new LicenseEncoderException (ex.getMessage (), ex);
		} catch (SignatureException ex) {
			throw new LicenseEncoderException (ex.getMessage (), ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] encode (@Nonnull ILicense license) throws LicenseEncoderException {
		Preconditions.checkNotNull (license, "license");

		// create output stream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream ();
		DataOutputStream dataOutputStream = new DataOutputStream (outputStream);

		// write data
		try {
			this.encode (license, dataOutputStream);
		} catch (IOException ex) {
			throw new LicenseEncoderException (ex.getMessage (), ex);
		}

		// return result
		return outputStream.toByteArray ();
	}

	/**
	 * Returns the gson instance for json de- and encoding.
	 * @return The gson instance.
	 */
	public Gson getGson () {
		return this.gsonBuilder.create ();
	}

	/**
	 * Returns the gson builder instance.
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