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
package com.evilco.license.common.utility;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * Provides methods for the use of signature based license representations.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public class SignatureUtility {

	/**
	 * Defines the base algorithm used for signatures.
	 */
	public static final String ALGORITHM_SIGNATURE = "RSA";

	/**
	 * Defines the default key size.
	 */
	public static final int ALGORITHM_KEY_SIZE = 6144;

	/**
	 * Generates a new key pair.
	 * @param keySize The key size.
	 * @return The key pair.
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair generateKeyPair (int keySize) throws NoSuchAlgorithmException {
		// get generator
		KeyPairGenerator generator = KeyPairGenerator.getInstance (ALGORITHM_SIGNATURE);

		// set key size
		generator.initialize (keySize);

		// generate a new key pair
		return generator.generateKeyPair ();
	}

	/**
	 * Generates a new key pair.
	 * @return The key pair.
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair generateKeyPair () throws NoSuchAlgorithmException {
		return generateKeyPair (ALGORITHM_KEY_SIZE);
	}

	/**
	 * Returns the signature key factory.
	 * @return The key factory.
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyFactory getKeyFactory () throws NoSuchAlgorithmException {
		return KeyFactory.getInstance (ALGORITHM_SIGNATURE);
	}
}