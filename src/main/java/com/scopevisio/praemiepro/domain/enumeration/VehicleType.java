package com.scopevisio.praemiepro.domain.enumeration;

public enum VehicleType implements TranslatedEnum<VehicleType> {

    SPORT(1.5f),
    SEDAN(1.25f),
    VAN(1.1f),
    SUV(1.3f),
    OLD_TIMER(2f);

    final float factor;

    VehicleType(float factor) {
        this.factor = factor;
    }

    public float getFactor() {
        return factor;
    }
}
