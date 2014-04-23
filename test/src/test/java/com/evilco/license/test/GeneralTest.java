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
package com.evilco.license.test;

import com.evilco.license.client.decoder.CompressedLicenseDecoder;
import com.evilco.license.client.decoder.JsonLicenseDecoder;
import com.evilco.license.common.data.AbstractExpiringLicense;
import com.evilco.license.common.data.holder.CompanyLicenseHolder;
import com.evilco.license.common.data.holder.ILicenseHolder;
import com.evilco.license.common.exception.LicenseDecoderException;
import com.evilco.license.common.exception.LicenseEncoderException;
import com.evilco.license.common.exception.LicenseInvalidException;
import com.evilco.license.common.utility.SignatureUtility;
import com.evilco.license.server.encoder.CompressedLicenseEncoder;
import com.evilco.license.server.encoder.JsonLicenseEncoder;
import com.google.common.base.Charsets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;

/**
 * Tests all library components with normal use cases.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
@RunWith (MockitoJUnitRunner.class)
public class GeneralTest {

	/**
	 * Defines the test key size.
	 */
	public static int KEY_SIZE = 1024;

	/**
	 * Stores the signature key pair.
	 */
	protected KeyPair keyPair;

	/**
	 * Prepares tests.
	 * @throws NoSuchAlgorithmException
	 */
	@Before
	public void setup () throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		// load key pair
		InputStream inputStream = GeneralTest.class.getResourceAsStream ("/test.key");
		byte[] privateKeyRaw = new byte[inputStream.available ()];
		inputStream.read (privateKeyRaw);

		inputStream = GeneralTest.class.getResourceAsStream ("/test.pub");
		byte[] publicKeyRaw = new byte[inputStream.available ()];
		inputStream.read (publicKeyRaw);

		// create key specs
		KeyFactory factory = SignatureUtility.getKeyFactory ();
		PrivateKey privateKey = factory.generatePrivate (new PKCS8EncodedKeySpec (privateKeyRaw));
		PublicKey publicKey = factory.generatePublic (new X509EncodedKeySpec (publicKeyRaw));

		this.keyPair = new KeyPair (publicKey, privateKey);
	}

	/**
	 * Generates a test license.
	 * @return
	 */
	public TestLicense generateTestLicense () {
		// create date for yesterday
		Calendar calendar = Calendar.getInstance ();
		calendar.add (Calendar.YEAR, 2048);

		// create license (expired)
		return new TestLicense (new CompanyLicenseHolder ("Example Ltd.", null), calendar.getTime (), 42);
	}

	/**
	 * Tries to decode a reference file.
	 * @throws LicenseDecoderException
	 * @throws IOException
	 */
	@Test
	public void decodeTest () throws LicenseDecoderException, IOException {
		// load key into string
		InputStream inputStream = GeneralTest.class.getResourceAsStream ("/test.license");
		byte[] data = new byte[inputStream.available ()];
		inputStream.read (data);

		String encodedLicense = new String (data, Charsets.UTF_8);

		// create decoder
		CompressedLicenseDecoder decoder = new CompressedLicenseDecoder (new JsonLicenseDecoder (this.keyPair.getPublic ()));

		// load license
		TestLicense license = ((TestLicense) decoder.decode (encodedLicense, TestLicense.class));

		// verify license
		Assert.assertNotNull (license);
		Assert.assertEquals (license.getTestValue (), 42);
	}

	/**
	 * Tests license encoding.
	 * @throws LicenseEncoderException
	 */
	@Test
	public void encodeTest () throws LicenseEncoderException, IOException {
		// create license
		TestLicense license = this.generateTestLicense ();

		// create license encoder
		CompressedLicenseEncoder encoder = new CompressedLicenseEncoder (new JsonLicenseEncoder (this.keyPair.getPrivate ()));

		// encode license
		String encodedLicense = encoder.encode (license);

		// verify output
		Assert.assertNotNull (encodedLicense);
	}

	/**
	 * Tests licence encoding and decoding.
	 * @throws LicenseEncoderException
	 */
	@Test
	public void encodeDecodeTest () throws LicenseEncoderException, LicenseDecoderException, IOException {
		// create license
		TestLicense license = this.generateTestLicense ();

		// create codec instances
		CompressedLicenseEncoder encoder = new CompressedLicenseEncoder (new JsonLicenseEncoder (this.keyPair.getPrivate ()));
		CompressedLicenseDecoder decoder = new CompressedLicenseDecoder (new JsonLicenseDecoder (this.keyPair.getPublic ()));

		// encode
		String encodedLicense = encoder.encode (license);

		// decode
		TestLicense license1 = ((TestLicense) decoder.decode (encodedLicense, TestLicense.class));

		// check
		Assert.assertNotNull (license1);
		Assert.assertEquals (license.getTestValue (), license1.getTestValue ());
	}

	/**
	 * Tests license validations (expiration).
	 * @throws LicenseEncoderException
	 * @throws LicenseDecoderException
	 */
	@Test (expected = LicenseInvalidException.class)
	public void validationExpirationTest () throws LicenseEncoderException, LicenseDecoderException {
		// create date for yesterday
		Calendar calendar = Calendar.getInstance ();
		calendar.add (Calendar.DATE, -1);

		// create license (expired)
		TestLicense license = new TestLicense (new CompanyLicenseHolder ("Example Ltd.", null), calendar.getTime (), 42);

		// create codec instances
		CompressedLicenseEncoder encoder = new CompressedLicenseEncoder (new JsonLicenseEncoder (this.keyPair.getPrivate ()));
		CompressedLicenseDecoder decoder = new CompressedLicenseDecoder (new JsonLicenseDecoder (this.keyPair.getPublic ()));

		// encode
		String encodedLicense = encoder.encode (license);

		// decode
		decoder.decode (encodedLicense, TestLicense.class);
	}

	/**
	 * Tests license validations (signature).
	 * @throws LicenseEncoderException
	 * @throws LicenseDecoderException
	 * @throws NoSuchAlgorithmException
	 */
	@Test (expected = LicenseInvalidException.class)
	public void validationSignatureTest () throws LicenseEncoderException, LicenseDecoderException, NoSuchAlgorithmException {
		// generate new key pair
		PrivateKey temporaryKey = SignatureUtility.generateKeyPair (KEY_SIZE).getPrivate ();

		// create license
		TestLicense license = this.generateTestLicense ();

		// create codec instances
		CompressedLicenseEncoder encoder = new CompressedLicenseEncoder (new JsonLicenseEncoder (temporaryKey));
		CompressedLicenseDecoder decoder = new CompressedLicenseDecoder (new JsonLicenseDecoder (this.keyPair.getPublic ()));

		// encode
		String encodedLicense = encoder.encode (license);

		// decode
		decoder.decode (encodedLicense, TestLicense.class);
	}

	/**
	 * A test license.
	 */
	public class TestLicense extends AbstractExpiringLicense {

		/**
		 * Stores a test value.
		 */
		protected int testValue;

		/**
		 * Constructs a new TestLicense.
		 */
		protected TestLicense () {
			super ();
		}

		/**
		 * Constructs a new TestLicense.
		 * @param licensee
		 * @param expiration
		 */
		protected TestLicense (ILicenseHolder licensee, Date expiration, int testValue) {
			super (licensee, expiration);
			this.testValue = testValue;
		}

		/**
		 * Returns the test value.
		 * @return
		 */
		public int getTestValue () {
			return this.testValue;
		}
	}
}