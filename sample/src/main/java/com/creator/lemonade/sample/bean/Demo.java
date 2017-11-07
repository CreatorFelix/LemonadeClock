package com.creator.lemonade.sample.bean;

/**
 * @author Felix.Liang
 */
@SuppressWarnings("unused")
public class Demo {

    private String label;
    private int iconResId;
    private Class clazz;

    public Demo() {
    }

    public Demo(String label, int iconResId, Class clazz) {
        this.label = label;
        this.iconResId = iconResId;
        this.clazz = clazz;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
