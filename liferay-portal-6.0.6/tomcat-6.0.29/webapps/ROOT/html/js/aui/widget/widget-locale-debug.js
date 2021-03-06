/*
Copyright (c) 2010, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.com/yui/license.html
version: 3.2.0
build: nightly
*/
YUI.add('widget-locale', function(Y) {

/**
 * Provides string support for widget with BCP 47 language tag lookup. This module has been deprecated. It's replaced by the "intl" module which provides generic internationalization and BCP 47 language tag support with externalization.
 *
 * @module widget-locale
 * @deprecated This module has been deprecated. It's replaced by the "intl" module which provides generic internationalization and BCP 47 language tag support with externalization.
 */
var TRUE = true,
    LOCALE = "locale",
    INIT_VALUE = "initValue",
    HYPHEN = "-",
    EMPTY_STR = "",
    Widget = Y.Widget;

/**
 * @attribute locale
 * @deprecated Use Y.config.lang and Y.Intl externalization support
 * @description
 * The default locale for the widget. NOTE: Using get/set on the "strings" attribute will
 * return/set strings for this locale.
 * @default "en"
 * @type String
 */
Widget.ATTRS[LOCALE] = {
    value: "en"
};

Widget.ATTRS.strings.lazyAdd = false; Y.mix(Widget.prototype, {

    /**
     * Sets strings for a particular locale, merging with any existing
     * strings which may already be defined for the locale.
     *
     * @method _setStrings
     * @protected
     * @param {Object} strings The hash of string key/values to set
     * @param {Object} locale The locale for the string values being set
     */
    _setStrings : function(strings, locale) {
        var strs = this._strs;
        locale = locale.toLowerCase();

        if (!strs[locale]) {
            strs[locale] = {};
        }

        Y.aggregate(strs[locale], strings, TRUE);
        return strs[locale];
    },

    /**
     * Returns the strings key/value hash for a paricular locale, without locale lookup applied.
     *
     * @method _getStrings
     * @protected
     * @param {Object} locale
     */
    _getStrings : function(locale) {
        return this._strs[locale.toLowerCase()];
    },

    /**
     * Gets the entire strings hash for a particular locale, performing locale lookup.
     * <p>
     * If no values of the key are defined for a particular locale the value for the 
     * default locale (in initial locale set for the class) is returned.
     * </p>
     * @method getStrings
     * @param {String} locale (optional) The locale for which the string value is required. Defaults to the current locale, if not provided.
     */
    // TODO: Optimize/Cache. Clear cache on _setStrings call.
    getStrings : function(locale) {
    
        locale = (locale || this.get(LOCALE)).toLowerCase();
    
        Y.log("getStrings: For " + locale, "info", "widget"); 
    
        var defLocale = this.getDefaultLocale().toLowerCase(),
            defStrs = this._getStrings(defLocale),
            strs = (defStrs) ? Y.merge(defStrs) : {},
            localeSegments = locale.split(HYPHEN),
            localeStrs,
            i, l,
            lookup;
    
        // If locale is different than the default, or needs lookup support
        if (locale !== defLocale || localeSegments.length > 1) {
            lookup = EMPTY_STR;
            for (i = 0, l = localeSegments.length; i < l; ++i) {
                lookup += localeSegments[i];
    
                Y.log("getStrings: Merging in strings from: " + lookup, "info", "widget"); 
    
                localeStrs = this._getStrings(lookup);
                if (localeStrs) {
                    Y.aggregate(strs, localeStrs, TRUE);
                }
                lookup += HYPHEN;
            }
        }
    
        return strs;
    },
    
    /**
     * Gets the string for a particular key, for a particular locale, performing locale lookup.
     * <p>
     * If no values if defined for the key, for the given locale, the value for the 
     * default locale (in initial locale set for the class) is returned.
     * </p>
     * @method getString
     * @param {String} key The key.
     * @param {String} locale (optional) The locale for which the string value is required. Defaults to the current locale, if not provided.
     */
    getString : function(key, locale) {

        locale = (locale || this.get(LOCALE)).toLowerCase();
    
        Y.log("getString: For " + locale, "info", "widget"); 
    
        var defLocale = (this.getDefaultLocale()).toLowerCase(),
            strs = this._getStrings(defLocale) || {},
            str = strs[key],
            idx = locale.lastIndexOf(HYPHEN);
    
        // If locale is different than the default, or needs lookup support
        if (locale !== defLocale || idx != -1) {
            do {
                Y.log("getString: Performing lookup for: " + locale, "info", "widget"); 
    
                strs = this._getStrings(locale);
                if (strs && key in strs) {
                    str = strs[key];
                    break;
                }
                idx = locale.lastIndexOf(HYPHEN);
                // Chop of last locale segment
                if (idx != -1) {
                    locale = locale.substring(0, idx);
                }
    
            } while (idx != -1);
        }
    
        return str;
    },

    /**
     * Returns the default locale for the widget (the locale value defined by the
     * widget class, or provided by the user during construction).
     *
     * @method getDefaultLocale
     * @return {String} The default locale for the widget
     */
    getDefaultLocale : function() {
        return this._state.get(LOCALE, INIT_VALUE);
    },
    
    _strSetter : function(val) {
        return this._setStrings(val, this.get(LOCALE));
    },

    _strGetter : function(val) {
        return this._getStrings(this.get(LOCALE));
    }
}, true);


}, '3.2.0' ,{requires:['widget-base']});
