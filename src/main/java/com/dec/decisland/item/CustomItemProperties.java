package com.dec.decisland.item;

public class CustomItemProperties {
    public int burnTime;
    public float compostableChance;
    private CustomItemProperties(Builder builder) {
        this.burnTime = builder.burnTime;
        this.compostableChance = builder.compostableChance;
    }

    public static class Builder {
        private int burnTime = 0;
        private float compostableChance = 0;
        public Builder() {

        }
        public Builder burnTime(int burnTime) {
            this.burnTime = burnTime;
            return this;
        }
        public Builder compostableChance(float compostableChance) {
            this.compostableChance = compostableChance;
            return this;
        }
        public CustomItemProperties build() {
            return new CustomItemProperties(this);
        }
    }
}
