/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.filter;

import com.restlet.sites.web.BaseApplication;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.Status;
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

    private static final Pattern expiresModificationDatePattern = Pattern.compile("modified([\\+|-]?[0-9]+)([ymdh]?)");
    private static final Pattern expiresCurrentDatePattern = Pattern.compile("([\\+|-]?[0-9]+)([ymdh]?)");

    private final Consumer<Response> setExpirationDateRule;
    private final CacheDirective cacheDirective;

    /**
     * Constructor.
     *
     * @param context Context.
     * @param next    The next Restlet to transmit the request to.
     */
    public CacheFilter(Context context, Restlet next, BaseApplication.CacheInstruction cacheInstruction) {
        super(context, next);
        this.setExpirationDateRule = getExpiresRule(cacheInstruction.expires);
        this.cacheDirective = getCacheDirective(cacheInstruction.cacheControl);
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
        if (cacheDirective != null) {
            response.getCacheDirectives().add(cacheDirective);
        }
        if (setExpirationDateRule != null) {
            // apply the set expiration date rule
            setExpirationDateRule.accept(response);
        }
    }

    private static CacheDirective getCacheDirective(String cacheControlInstruction) {
        if (StringUtils.isNullOrEmpty(cacheControlInstruction)) {
            return null;
        }
        if ("public".equals(cacheControlInstruction)) {
            return CacheDirective.publicInfo();
        } else if ("no-transform".equals(cacheControlInstruction)) {
            return CacheDirective.noTransform();
        } else if ("only-if-cached".equals(cacheControlInstruction)) {
            return CacheDirective.onlyIfCached();
        } else if ("must-revalidate".equals(cacheControlInstruction)) {
            return CacheDirective.mustRevalidate();
        } else if ("proxy-revalidate".equals(cacheControlInstruction)) {
            return CacheDirective.proxyMustRevalidate();
        } else if (cacheControlInstruction.startsWith("private")) {
            List<String> fields = getCacheControlParameters("private", cacheControlInstruction);
            return CacheDirective.privateInfo(fields);
        } else if (cacheControlInstruction.startsWith("max-age")) {
            List<String> fields = getCacheControlParameters("max-age", cacheControlInstruction);
            if (fields != null && !fields.isEmpty()) {
                return CacheDirective.maxAge(Integer.parseInt(fields.get(0)));
            }
        } else if (cacheControlInstruction.startsWith("max-stale")) {
            List<String> fields = getCacheControlParameters("max-stale", cacheControlInstruction);
            if (fields != null && !fields.isEmpty()) {
                return CacheDirective.maxStale(Integer.parseInt(fields.get(0)));
            }
            return CacheDirective.maxStale();
        } else if (cacheControlInstruction.startsWith("min-fresh")) {
            List<String> fields = getCacheControlParameters("min-fresh", cacheControlInstruction);
            if (fields != null && !fields.isEmpty()) {
                return CacheDirective.minFresh(Integer.parseInt(fields.get(0)));
            }
        } else if (cacheControlInstruction.startsWith("s-maxage")) {
            List<String> fields = getCacheControlParameters("s-maxage", cacheControlInstruction);
            if (fields != null && !fields.isEmpty()) {
                return CacheDirective.sharedMaxAge(Integer.parseInt(fields.get(0)));
            }
        }
        return null;
    }

    private static List<String> getCacheControlParameters(String cacheDirective, String cacheControlInstruction) {
        List<String> fields = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(cacheControlInstruction.substring(cacheDirective.length()));
        while (st.hasMoreTokens()) {
            fields.add(st.nextToken().trim());
        }
        return fields;
    }

    private Consumer<Response> getExpiresRule(String expiresInstruction) {
        if (StringUtils.isNullOrEmpty(expiresInstruction)) {
            return null;
        }

        if (expiresInstruction.startsWith("@")) {
            // first case: the expiration date is static
            try {
                Date date = DateUtils.parse(expiresInstruction.substring(1));
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

    private static Map<String, Integer> expiresDateUnits;

    static {
        expiresDateUnits = new HashMap<>();
        expiresDateUnits.put("y", Calendar.YEAR);
        expiresDateUnits.put("m", Calendar.MONTH);
        expiresDateUnits.put("d", Calendar.DATE);
        expiresDateUnits.put("h", Calendar.HOUR);
    }

}
