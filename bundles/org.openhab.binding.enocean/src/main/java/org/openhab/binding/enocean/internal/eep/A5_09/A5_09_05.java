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
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/**
 *
 * @author Zhivka Dimova - Initial contribution
 */
public class A5_09_05 extends A5_09 {

    public A5_09_05(ERP1Message packet) {
        super(packet);
    }

    protected String[] VOC_Indentifications = new String[] { "VOCT", "Formaldehyde", "Benzene", "Styrene", "Toluene",
            "Tetrachloroethylene", "Xylene", "n-Hexane", "n-Octane", "Cyclopentane", "Methanol", "Ethanol",
            "1-Pentanol", "Acetone", "ethylene Oxide", "Acetaldehyde ue", "Acetic Acid", "Propionine Acid",
            "Valeric Acid", "Butyric Acid", "Ammoniac", "Hidrogen Sulfide", "Dimethylsulfide", "2-Butanol",
            "2-Methylpropanol", "Diethyl ether", "VOC-Index", "Ozone" };

    protected double[] ScaleMultiplier = new double[] { 0.01, 0.1, 1, 10 };

    protected long getUnscaledVOCValue() {
        return getDBByOffsetSizeValue(0, 16);
    }

    protected double getVODID() {
        return getDB_1Value();
    }

    protected double getScalingFactor() {
        int smid = Long.valueOf(getDBByOffsetSizeValue(30, 2)).intValue();
        if (smid < 0 || smid >= ScaleMultiplier.length) {
            logger.debug("Invalid value according enocean specification for A5_09 Scale Multiplier {}", smid);
            return 1;
        }

        return ScaleMultiplier[smid];
    }

    protected String getVOCID() {
        int voc_id = getDB_1Value();
        if (voc_id == 255) {
            return VOC_Indentifications[VOC_Indentifications.length - 1];
        } else if (voc_id < 0 || voc_id >= VOC_Indentifications.length - 1) {
            logger.debug("Invalid value according enocean specification for A5_09 VOC Identification {}", voc_id);
            return null;
        }

        return VOC_Indentifications[voc_id];
    }

    @Override
    protected State convertToStateImpl(String channelId, String channelTypeId,
            Function<String, State> getCurrentStateFunc, Configuration config) {

        if (channelId.equals(CHANNEL_VOC)) {
            double scaledVOC = getUnscaledVOCValue() * getScalingFactor();
            return new QuantityType<>(scaledVOC, Units.PARTS_PER_BILLION);
        } else if (channelId.equals(CHANNEL_VOC_ID)) {
            return new StringType(getVOCID());
        }

        return UnDefType.UNDEF;
    }
}
