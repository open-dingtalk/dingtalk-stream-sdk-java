package com.dingtalk.open.app.stream.protocol;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public class SpecificationVersion {

    private final int major;

    private final int minor;

    public final static SpecificationVersion SPEC1_0 = new SpecificationVersion(1, 0);


    public final SpecificationVersion parse(String specification) {
        if (specification == null || specification.isEmpty()) {
            throw new IllegalArgumentException("unknown specification version");
        }
        int index = specification.indexOf(".");
        if (index <= 0 || index >= (specification.length() - 1)) {
            throw new IllegalArgumentException("unknown specification version");
        }


        String major = specification.substring(0, index);
        String minor = specification.substring(index + 1);
        if (StringUtils.isDigital(major) && StringUtils.isDigital(minor)) {
            return new SpecificationVersion(Integer.valueOf(major), Integer.valueOf(minor));
        } else {
            throw new IllegalArgumentException("unknown specification version " + specification);
        }
    }


    public SpecificationVersion(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public String toString() {
        return major + "." + minor;
    }


}
