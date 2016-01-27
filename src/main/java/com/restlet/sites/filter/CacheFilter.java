/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.filter;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CacheDirective;
import org.restlet.data.Status;
import org.restlet.engine.header.CacheDirectiveReader;
import org.restlet.engine.util.DateUtils;
import org.restlet.engine.util.StringUtils;
import org.restlet.routing.Filter;

import java.time.LocalTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter that simply add cache information based on expiration date, and/or
 * explicitly add cache directive.
 *
 * @author Thierry Boileau
 */

public class CacheFilter extends Filter {
    /**
     * follow the ngninx syntax
     */
    private static final Pattern expiresModificationDatePattern = Pattern.compile("modified([\\+|-]?[0-9]+)([ymdh]?)");
    private static final Pattern expiresCurrentDatePattern = Pattern.compile("([\\+|-]?[0-9]+)([ymdh]?)");

    private final Consumer<Response> setExpirationDateRule;
    private final Collection<CacheDirective> cacheDirectives;

    /**
     * Constructor.
     *
     * @param context Context.
     */
    public CacheFilter(Context context, CacheInstruction cacheInstruction) {
        super(context);
        this.setExpirationDateRule = getExpiresRule(cacheInstruction.getExpires());
        this.cacheDirectives = getCacheDirectives(cacheInstruction.getCacheControl());

    }

    @Override
    protected void afterHandle(Request request, Response response) {
        super.afterHandle(request, response);
        if (!Status.SUCCESS_OK.equals(response.getStatus())
                || !response.isEntityAvailable()) {
            return;
        }

        if (request.getResourceRef().toString(false, false).contains("nocache")) {
            response.getEntity().setModificationDate(null);
            response.getEntity().setExpirationDate(null);
            response.getEntity().setTag(null);
            response.getCacheDirectives().add(CacheDirective.noCache());

            return;
        }
        if (cacheDirectives != null) {
            response.getCacheDirectives().addAll(cacheDirectives);
        }
        if (setExpirationDateRule != null) {
            // apply the set expiration date rule
            setExpirationDateRule.accept(response);
        }
    }

    private static List<CacheDirective> getCacheDirectives(String cacheControlInstruction) {
        List<CacheDirective> cacheDirectives = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(cacheControlInstruction)) {
            return cacheDirectives;
        }
        new CacheDirectiveReader(cacheControlInstruction).addValues(cacheDirectives);

        return cacheDirectives;
    }

    private Consumer<Response> getExpiresRule(String expiresInstruction) {
        if (StringUtils.isNullOrEmpty(expiresInstruction)) {
            return null;
        }

        if (expiresInstruction.startsWith("@")) {
            // first case: the expiration date is static
            try {
                Date date = DateUtils.parse(expiresInstruction.substring(1), DateUtils.FORMAT_ISO_8601);
                if (date != null) {
                    return getSetExpirationDateRule(date);
                }
            } catch (Exception e) {
                // nothing
            }

            // second case: the expiration date is set a a fixed hour
            try {
                LocalTime localTime = LocalTime.parse(expiresInstruction.substring(1));
                return getSetHourRule(localTime.getHour(), localTime.getMinute(), localTime.getSecond());
            } catch (Exception e) {
                // nothing
            }

            getLogger().warning("The expires instruction declares an invalid date and won't be applied: " + expiresInstruction.substring(1));
            return null;
        }

        Matcher matcher = expiresModificationDatePattern.matcher(expiresInstruction);
        if (matcher.matches()) {
            final Integer delay = Integer.parseInt(matcher.group(1));
            String unitAsString = matcher.group(2);
            return getApplyDelayToModificationDateRule(expiresDateUnits, delay, unitAsString);
        }

        matcher = expiresCurrentDatePattern.matcher(expiresInstruction);
        if (matcher.matches()) {
            Integer delay = Integer.parseInt(matcher.group(1));
            String unitAsString = matcher.group(2);
            return getApplyDelayRule(expiresDateUnits, delay, unitAsString);
        }

        getLogger().warning("The expires instruction won't be applied: " + expiresInstruction);

        return null;
    }

    private Consumer<Response> getSetExpirationDateRule(Date date) {
        return response -> {
            response.getEntity().setExpirationDate(date);
        };
    }

    private Consumer<Response> getSetHourRule(int hour, int minute, int second) {
        return response -> {
            // compare the new date to the current one
            Calendar now = new GregorianCalendar();

            Calendar expirationDate = new GregorianCalendar();
            // adjust the expiration date
            expirationDate.set(Calendar.HOUR_OF_DAY, hour);
            expirationDate.set(Calendar.MINUTE, minute);
            expirationDate.set(Calendar.SECOND, second);

            // compare to the current date
            if (!expirationDate.after(now)) {
                // shift of one day
                expirationDate.add(Calendar.DATE, 1);
            }
            response.getEntity().setExpirationDate(expirationDate.getTime());
        };
    }

    private Consumer<Response> getApplyDelayRule(Map<String, Integer> units, Integer delay, String unitAsString) {
        if (StringUtils.isNullOrEmpty(unitAsString)) {
            unitAsString = "h";
        }

        final Integer unit = units.get(unitAsString);

        return response -> {
            Calendar c = new GregorianCalendar();
            c.setTime(new Date());
            c.add(unit, delay);
            response.getEntity().setExpirationDate(c.getTime());
        };
    }

    private Consumer<Response> getApplyDelayToModificationDateRule(Map<String, Integer> units, Integer delay, String unitAsString) {
        if (StringUtils.isNullOrEmpty(unitAsString)) {
            unitAsString = "h";
        }

        Integer unit = units.get(unitAsString);

        return response -> {
            if (response.getEntity().getModificationDate() == null) {
                Calendar c = new GregorianCalendar();
                c.setTime(new Date());
                c.add(unit, delay);
                response.getEntity().setExpirationDate(c.getTime());
            } else {
                Calendar c = new GregorianCalendar();
                c.setTime(response.getEntity().getModificationDate());
                c.add(unit, delay);
                response.getEntity().setExpirationDate(c.getTime());
            }
        };
    }

    private final static Map<String, Integer> expiresDateUnits;

    static {
        expiresDateUnits = new HashMap<>();
        expiresDateUnits.put("y", Calendar.YEAR);
        expiresDateUnits.put("m", Calendar.MONTH);
        expiresDateUnits.put("d", Calendar.DATE);
        expiresDateUnits.put("h", Calendar.HOUR);
    }

}
