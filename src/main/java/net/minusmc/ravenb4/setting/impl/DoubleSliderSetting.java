package net.minusmc.ravenb4.setting.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minusmc.ravenb4.setting.Setting;
import net.minusmc.ravenb4.setting.impl.minmax.MinMaxDouble;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;



public class DoubleSliderSetting extends Setting<MinMaxDouble> {

    private final double minimum;
    private final double maximum;
    private final double intervals;

    public DoubleSliderSetting(String name, double minValue, double maxValue, double minimum, double maximum, double intervals) {
        super(name, new MinMaxDouble(minValue, maxValue));

        this.minimum = minimum;
        this.maximum = maximum;
        this.intervals = intervals;
    }

    @Override
    public String getSettingType() {
        return "doubleslider";
    }

    private double getMinDefaultValue() {
        return super.getValue().getMinValue();
    }

    private double getMaxDefaultValue() {
        return super.getValue().getMaxValue();
    }

    @Override
    public void resetToDefaults() {
        super.getValue().setMaxValue(getMaxDefaultValue());
        super.getValue().setMinValue(getMinDefaultValue());
    }

    @Override
    public JsonElement getConfigAsJson() {
        JsonObject data = new JsonObject();
        data.addProperty("type", getSettingType());
        data.addProperty("valueMax", round2(super.getValue().getMaxValue(), 2));
        data.addProperty("valueMin", round2(super.getValue().getMinValue(), 2));
        return data;
    }

    @Override
    public void applyConfigFromJson(JsonObject data) {
        if (!Objects.equals(data.get("type").getAsString(), getSettingType())) {
            return;
        }

        setMaxValue(data.get("valueMax").getAsDouble());
        setMinValue(data.get("valueMin").getAsDouble());
    }

    public double getMinValue() {
        return round2(super.getValue().getMinValue(), 2);
    }

    public double getMaxValue() {
        return round2(super.getValue().getMaxValue(), 2);
    }

    public void setMinValue(double newValue) {
        newValue = clampDouble(newValue, minimum, maximum);
        newValue = Math.round(newValue * (1.0 / intervals)) / (1.0 / intervals);
        super.getValue().setMinValue(newValue);
    }

    public void setMaxValue(double newValue) {
        newValue = clampDouble(newValue, minimum, maximum);
        newValue = Math.round(newValue * (1.0 / intervals)) / (1.0 / intervals);
        super.getValue().setMaxValue(newValue);
    }

    private double clampDouble(double value, double min, double max) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }

    private double round2(double value, int precision) {
        if (precision < 0) {
            return 0.0;
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}