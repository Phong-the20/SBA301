package fu.edu.sba301.slot32.model;

public class ProductSpecs {
    private int ports;
    private boolean hdmi;
    private int powerDeliveryW;
    private String switchType;
    private Integer dpi;

    public ProductSpecs() {}

    public ProductSpecs(int ports, boolean hdmi, int powerDeliveryW) {
        this.ports = ports;
        this.hdmi = hdmi;
        this.powerDeliveryW = powerDeliveryW;
    }

    public int getPorts() { return ports; }
    public void setPorts(int ports) { this.ports = ports; }

    public boolean isHdmi() { return hdmi; }
    public void setHdmi(boolean hdmi) { this.hdmi = hdmi; }

    public int getPowerDeliveryW() { return powerDeliveryW; }
    public void setPowerDeliveryW(int powerDeliveryW) { this.powerDeliveryW = powerDeliveryW; }

    public String getSwitchType() { return switchType; }
    public void setSwitchType(String switchType) { this.switchType = switchType; }

    public Integer getDpi() { return dpi; }
    public void setDpi(Integer dpi) { this.dpi = dpi; }

    @Override
    public String toString() {
        return "Specs{ports=" + ports + ", hdmi=" + hdmi + ", pdW=" + powerDeliveryW + "}";
    }
}
