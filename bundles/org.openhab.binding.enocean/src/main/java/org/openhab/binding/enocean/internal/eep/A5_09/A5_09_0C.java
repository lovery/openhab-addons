/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.enocean.internal.eep.A5_09;

import static org.openhab.binding.enocean.internal.EnOceanBindingConstants.*;

import java.util.function.Function;

import org.openhab.binding.enocean.internal.messages.ERP1Message;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.types.State;

/**
 *
 * @author Zhivka Dimova - Initial contribution
 */
public class A5_09_0C extends A5_09_05 {

    public A5_09_0C(ERP1Message packet) {
        super(packet);
    }

    protected String[] VOC_Indentifications = new String[] { "VOCT", "Formaldehyde", "Benzene", "Styrene", "Toluene",
            "Tetrachloroethylene", "Xylene", "n-Hexane", "n-Octane", "Cyclopentane", "Methanol", "Ethanol",
            "1-Pentanol", "Acetone", "ethylene Oxide", "Acetaldehyde ue", "Acetic Acid", "Propionine Acid",
            "Valeric Acid", "Butyric Acid", "Ammoniac", "Hidrogen Sulfide", "Dimethylsulfide", "2-Butanol",
            "2-Methylpropanol", "Diethyl ether", "Naphthalene", "4-Phenylcyclohexene", "Limonene", "Trichloroethylene",
            "Isovaleric acid", "Indole", "Cadaverine", "Putrescine", "Caproic acid", "Ozone" };

    @Override
    protected State convertToStateImpl(String channelId, String channelTypeId,
            Function<String, State> getCurrentStateFunc, Configuration config) {

        if (channelId.equals(CHANNEL_VOC)) {
            double scaledVOC = getUnscaledVOCValue() * getScalingFactor();
            if (getBit(getDB_0(), 2)) {
                return new QuantityType<>(scaledVOC, Units.MICROGRAM_PER_CUBICMETRE);
            }
            return new QuantityType<>(scaledVOC, Units.PARTS_PER_BILLION);
        }

        return super.convertToStateImpl(channelId, channelTypeId, getCurrentStateFunc, config);
    }
}
