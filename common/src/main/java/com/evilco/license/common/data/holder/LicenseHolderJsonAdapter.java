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
package com.evilco.license.common.data.holder;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Allows (de-)serialization of license holders.
 * @author			Johannes "Akkarin" Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.com>
 */
public class LicenseHolderJsonAdapter implements JsonDeserializer<ILicenseHolder>, JsonSerializer<ILicenseHolder> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILicenseHolder deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		// get implementation name
		try {
			// get class instance
			Class<? extends ILicenseHolder> licenseClass = Class.forName (json.getAsJsonObject ().get ("implementationClassName").getAsString ()).asSubclass (ILicenseHolder.class);

			// de-serialize
			return context.deserialize (json, licenseClass);
		} catch (ClassCastException ex) {
			throw new JsonParseException (ex.getMessage (), ex);
		} catch (ClassNotFoundException ex) {
			throw new JsonParseException (ex.getMessage (), ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JsonElement serialize (ILicenseHolder src, Type typeOfSrc, JsonSerializationContext context) {
		// encode object
		JsonElement element = context.serialize (src, src.getClass ());

		// append name
		element.getAsJsonObject ().add ("implementationClassName", context.serialize (src.getClass ().getName ()));

		// return modified element
		return element;
	}
}