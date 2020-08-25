package com.natchuz.hub.sponge.regions;

import com.flowpowered.math.vector.Vector3d;
import lombok.SneakyThrows;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegionParser {

    private static final Pattern FUNCTION_PATTERN = Pattern.compile("(?<name>[a-z]+)\\((?<params>(-?\\d+(\\.\\d+)? )*(-?\\d+(\\.\\d+)?))?\\);");

    /**
     * Parse without forced {@link }
     *
     * @see RegionParser#parse(String, boolean)
     */
    public static Region parse(String input) {
        return parse(input, false);
    }

    /**
     * Parses string to union using simple patterns:
     * <code>functionName(params...);</code> where params are separated by space
     * <p>
     * Example:
     * <code>cuboid(1 2.0 -1 -4.1 100)</code>
     * <p>
     * Note that parser is really sensitive
     *
     * @param input      string to parse
     * @param forceUnion when forcing union, function will always return union, empty union, or union with single region
     * @return Union if more than one region is defined, or single region
     * @throws RegionParserException when string is wrongly formatted, or function does not exist
     */
    @SneakyThrows(RegionParserException.class)
    public static Region parse(String input, boolean forceUnion) {
        Validate.notNull(input);
        Matcher matcher = FUNCTION_PATTERN.matcher(input);

        List<Region> regionList = new LinkedList<>();

        while (matcher.find()) {
            String functionName = matcher.group("name");
            String rawParams = matcher.group("params");
            double[] params = Arrays.stream(rawParams.split(" ")).mapToDouble(Double::parseDouble).toArray();

            if (functionName.equals("cuboid")) {
                if (params.length != 6)
                    throw new RegionParserException(input, "Sphere region parameters does not match " +
                            "<min x> <min y> <min z> <max x> <max y> <max z>, " +
                            "found " + params.length + "parameters : " + rawParams);
                regionList.add(new CuboidRegion(params[0], params[1], params[2], params[3], params[4], params[5]));
                continue;
            }

            if (functionName.equals("sphere")) {
                if (params.length != 4)
                    throw new RegionParserException(input, "Sphere region parameters does not match " +
                            "<base x> <base y> <base z> <radius>, " +
                            "found " + params.length + " parameters: " + rawParams);
                regionList.add(new SphereRegion(new Vector3d(params[0], params[1], params[2]), params[3]));
                continue;
            }

            if (functionName.equals("cylinder")) {
                if (params.length != 5)
                    throw new RegionParserException(input, "Cylinder region parameters does not match " +
                            "<base x> <base y> <base z> <radius> <height>, " +
                            "found " + params.length + " parameters: " + rawParams);
                regionList.add(new CylinderRegion(new Vector3d(params[0], params[1], params[2]), params[3], params[4]));
                continue;
            }

            throw new RegionParserException(input, "Unknown function: " + functionName);
        }

        if (regionList.size() > 1) {
            return new Union(regionList.toArray(new Region[0]));
        } else if (regionList.size() == 1) {
            return forceUnion ? new Union(regionList.get(0)) : regionList.get(0);
        } else {
            if (forceUnion)
                return new Union();
            else
                throw new RegionParserException(input, "String does not contain region definition, or is wrongly formatted!");
        }
    }

    public static class RegionParserException extends Exception {
        /**
         * @param input input of parse function
         * @param cause cause
         */
        public RegionParserException(String input, String cause) {
            super(cause + "\nInput was: " + input);
        }
    }
}
