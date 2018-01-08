package me.jerryhanks.countrypicker;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jerry Hanks on 12/15/17.
 */

public class Util {

    public static final String AZ_STRING = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Loads a list of Country
     *
     * @param context The given context
     */
    public static List<Country> loadDataFromJson(Context context) {

        InputStream inputStream = context.getResources().openRawResource(R.raw.english);
        String jsonString = readJsonFile(inputStream);

        //create gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES);
        Gson gson = gsonBuilder.create();

        Country[] countries = gson.fromJson(jsonString, Country[].class);
        return Arrays.asList(countries);
    }


    public static Map<String, List<Country>> mapList(List<Country> countries) {
        Map<String, List<Country>> groups = new HashMap<>();
        char[] chars = AZ_STRING.toCharArray();
        for (char aChar : chars) {
            List<Country> group = new ArrayList<>();
            for (Country country : countries) {
                if (country.getName().toLowerCase().startsWith(String.valueOf(aChar))) {
                    group.add(country);
                }
            }

            groups.put(String.valueOf(aChar), group);
        }

        return groups;
    }

    /**
     * Reads json file given an inputStream and return a json string
     *
     * @param inputStream The input stream to read from
     */
    private static String readJsonFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte bufferByte[] = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(bufferByte)) != -1) {
                outputStream.write(bufferByte, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }

    /**
     * Util method used to hide the keyboard
     *
     * @param context The UI context
     */
    private static void hideKeyboard(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Returns image res based on country name code
     *
     * @param country
     * @return
     */
    static @DrawableRes
    int getFlagResID(Country country) {
        switch (country.getCode().toLowerCase()) {
            //this should be sorted based on country name code.
            case "ad": //andorra
                return R.drawable.ad;
            case "ae": //united arab emirates
                return R.drawable.ae;
            case "af": //afghanistan
                return R.drawable.ae;
            case "ag": //antigua & barbuda
                return R.drawable.ag;
            case "ai": //anguilla // Caribbean Islands
                return R.drawable.ai;//
            case "al": //albania
                return R.drawable.al;
            case "am": //armenia
                return R.drawable.am;
            case "ao": //angola
                return R.drawable.ao;
            case "aq": //antarctica // custom
                return R.drawable.aq;
            case "ar": //argentina
                return R.drawable.ar;
            case "at": //austria
                return R.drawable.at;
            case "au": //australia
                return R.drawable.au;
            case "aw": //aruba
                return R.drawable.aw;
            case "az": //azerbaijan
                return R.drawable.az;
            case "ba": //bosnia and herzegovina
                return R.drawable.ba;
            case "bb": //barbados
                return R.drawable.bb;
            case "bd": //bangladesh
                return R.drawable.bd;
            case "be": //belgium
                return R.drawable.be;
            case "bf": //burkina faso
                return R.drawable.bf;
            case "bg": //bulgaria
                return R.drawable.bg;
            case "bh": //bahrain
                return R.drawable.bh;
            case "bi": //burundi
                return R.drawable.bi;
            case "bj": //benin
                return R.drawable.bj;
            case "bl": //saint barthélemy
                return R.drawable.bl;// custom
            case "bm": //bermuda
                return R.drawable.bm;//
            case "bn": //brunei darussalam // custom
                return R.drawable.bn;
            case "bo": //bolivia, plurinational state of
                return R.drawable.bo;
            case "br": //brazil
                return R.drawable.br;
            case "bs": //bahamas
                return R.drawable.bs;
            case "bt": //bhutan
                return R.drawable.bt;
            case "bw": //botswana
                return R.drawable.bw;
            case "by": //belarus
                return R.drawable.by;
            case "bz": //belize
                return R.drawable.bz;
            case "ca": //canada
                return R.drawable.ca;
            case "cc": //cocos (keeling) islands
                return R.drawable.cc;// custom
            case "cd": //congo, the democratic republic of the
                return R.drawable.cd;
            case "cf": //central african republic
                return R.drawable.cf;
            case "cg": //congo
                return R.drawable.cg;
            case "ch": //switzerland
                return R.drawable.ch;
            case "ci": //côte d\'ivoire
                return R.drawable.ci;
            case "ck": //cook islands
                return R.drawable.ck;
            case "cl": //chile
                return R.drawable.cl;
            case "cm": //cameroon
                return R.drawable.cm;
            case "cn": //china
                return R.drawable.cn;
            case "co": //colombia
                return R.drawable.co;
            case "cr": //costa rica
                return R.drawable.cr;
            case "cu": //cuba
                return R.drawable.cu;
            case "cv": //cape verde
                return R.drawable.cv;
            case "cx": //christmas island
                return R.drawable.cx;
            case "cy": //cyprus
                return R.drawable.cy;
            case "cz": //czech republic
                return R.drawable.cz;
            case "de": //germany
                return R.drawable.de;
            case "dj": //djibouti
                return R.drawable.dj;
            case "dk": //denmark
                return R.drawable.dk;
            case "dm": //dominica
                return R.drawable.dm;
            case "do": //dominican republic
                return R.drawable._do;
            case "dz": //algeria
                return R.drawable.dz;
            case "ec": //ecuador
                return R.drawable.ec;
            case "ee": //estonia
                return R.drawable.ee;
            case "eg": //egypt
                return R.drawable.eg;
            case "er": //eritrea
                return R.drawable.er;
            case "es": //spain
                return R.drawable.es;
            case "et": //ethiopia
                return R.drawable.et;
            case "fi": //finland
                return R.drawable.fi;
            case "fj": //fiji
                return R.drawable.fj;
            case "fk": //falkland islands (malvinas)
                return R.drawable.fk; //
            case "fm": //micronesia, federated states of
                return R.drawable.fm;
            case "fo": //faroe islands
                return R.drawable.fo;
            case "fr": //france
                return R.drawable.fr;
            case "ga": //gabom
                return R.drawable.ga;
            case "gb": //united kingdom
                return R.drawable.gb;
            case "gd": //grenada
                return R.drawable.gd;
            case "ge": //georgia
                return R.drawable.ge;
            case "gf": //guyane
                return R.drawable.gf;
            case "gh": //ghana
                return R.drawable.gh;
            case "gi": //gibraltar
                return R.drawable.gi;
            case "gl": //greenland
                return R.drawable.gl;
            case "gm": //gambia
                return R.drawable.gm;
            case "gn": //guinea
                return R.drawable.gn;
            case "gq": //equatorial guinea
                return R.drawable.gq;
            case "gr": //greece
                return R.drawable.gr;
            case "gt": //guatemala
                return R.drawable.gt;
            case "gw": //guinea-bissau
                return R.drawable.gw;
            case "gy": //guyana
                return R.drawable.gy;
            case "hk": //hong kong
                return R.drawable.hk;
            case "hn": //honduras
                return R.drawable.hn;
            case "hr": //croatia
                return R.drawable.hr;
            case "ht": //haiti
                return R.drawable.ht;
            case "hu": //hungary
                return R.drawable.hu;
            case "id": //indonesia
                return R.drawable.id;
            case "ie": //ireland
                return R.drawable.ie;
            case "il": //israel
                return R.drawable.il;
            case "im": //isle of man
                return R.drawable.im;
            case "is": //Iceland
                return R.drawable.is;
            case "in": //india
                return R.drawable.in;
            case "iq": //iraq
                return R.drawable.iq;
            case "ir": //iran, islamic republic of
                return R.drawable.ir;
            case "it": //italy
                return R.drawable.it;
            case "jm": //jamaica
                return R.drawable.jm;
            case "jo": //jordan
                return R.drawable.jo;
            case "jp": //japan
                return R.drawable.jp;
            case "ke": //kenya
                return R.drawable.ke;
            case "kg": //kyrgyzstan
                return R.drawable.kg;
            case "kh": //cambodia
                return R.drawable.kh;
            case "ki": //kiribati
                return R.drawable.ki;
            case "km": //comoros
                return R.drawable.km;
            case "kn": //st kitts & nevis
                return R.drawable.kn;
            case "kp": //north korea
                return R.drawable.kp;
            case "kr": //south korea
                return R.drawable.kr;
            case "kw": //kuwait
                return R.drawable.kw;
            case "ky": //Cayman_Islands
                return R.drawable.ky;
            case "kz": //kazakhstan
                return R.drawable.kz;
            case "la": //lao people\'s democratic republic
                return R.drawable.la;
            case "lb": //lebanon
                return R.drawable.lb;
            case "lc": //st lucia
                return R.drawable.lc;
            case "li": //liechtenstein
                return R.drawable.li;
            case "lk": //sri lanka
                return R.drawable.lk;
            case "lr": //liberia
                return R.drawable.lr;
            case "ls": //lesotho
                return R.drawable.ls;
            case "lt": //lithuania
                return R.drawable.lt;
            case "lu": //luxembourg
                return R.drawable.lu;
            case "lv": //latvia
                return R.drawable.lv;
            case "ly": //libya
                return R.drawable.ly;
            case "ma": //morocco
                return R.drawable.ma;
            case "mc": //monaco
                return R.drawable.mc;
            case "md": //moldova, republic of
                return R.drawable.md;
            case "me": //montenegro
                return R.drawable.me;// custom
            case "mg": //madagascar
                return R.drawable.mg;
            case "mh": //marshall islands
                return R.drawable.mh;
            case "mk": //macedonia, the former yugoslav republic of
                return R.drawable.mk;
            case "ml": //mali
                return R.drawable.ml;
            case "mm": //myanmar
                return R.drawable.mm;
            case "mn": //mongolia
                return R.drawable.mn;
            case "mo": //macao
                return R.drawable.mo;
            case "mq": //martinique
                return R.drawable.mq;
            case "mr": //mauritania
                return R.drawable.mr;
            case "ms": //montserrat
                return R.drawable.ms;
            case "mt": //malta
                return R.drawable.mt;
            case "mu": //mauritius
                return R.drawable.mu;
            case "mv": //maldives
                return R.drawable.mv;
            case "mw": //malawi
                return R.drawable.mw;
            case "mx": //mexico
                return R.drawable.mx;
            case "my": //malaysia
                return R.drawable.my;
            case "mz": //mozambique
                return R.drawable.mz;
            case "na": //namibia
                return R.drawable.na;
            case "nc": //new caledonia
                return R.drawable.nc;
            case "ne": //niger
                return R.drawable.ne;
            case "ng": //nigeria
                return R.drawable.ng;
            case "ni": //nicaragua
                return R.drawable.ni;
            case "nl": //netherlands
                return R.drawable.nl;
            case "no": //norway
                return R.drawable.no;
            case "np": //nepal
                return R.drawable.np;
            case "nr": //nauru
                return R.drawable.nr;
            case "nu": //niue
                return R.drawable.nu;
            case "nz": //new zealand
                return R.drawable.nz;
            case "om": //oman
                return R.drawable.om;
            case "pa": //panama
                return R.drawable.pa;
            case "pe": //peru
                return R.drawable.pe;
            case "pf": //french polynesia
                return R.drawable.pf;
            case "pg": //papua new guinea
                return R.drawable.pg;
            case "ph": //philippines
                return R.drawable.ph;
            case "pk": //pakistan
                return R.drawable.pk;
            case "pl": //poland
                return R.drawable.pl;
            case "pm": //saint pierre and miquelon
                return R.drawable.pm;
            case "pn": //pitcairn
                return R.drawable.pn;
            case "pr": //puerto rico
                return R.drawable.pr;
            case "ps": //palestine
                return R.drawable.ps;
            case "pt": //portugal
                return R.drawable.pt;
            case "pw": //palau
                return R.drawable.pw;
            case "py": //paraguay
                return R.drawable.py;
            case "qa": //qatar
                return R.drawable.qa;
            case "re": //la reunion
                return R.drawable.re;
            case "ro": //romania
                return R.drawable.ro;
            case "rs": //serbia
                return R.drawable.rs; // custom
            case "ru": //russian federation
                return R.drawable.ru;
            case "rw": //rwanda
                return R.drawable.rw;
            case "sa": //saudi arabia
                return R.drawable.sa;
            case "sb": //solomon islands
                return R.drawable.sb;
            case "sc": //seychelles
                return R.drawable.sc;
            case "sd": //sudan
                return R.drawable.sd;
            case "se": //sweden
                return R.drawable.se; // custom
            case "si": //slovenia
                return R.drawable.si;
            case "sk": //slovakia
                return R.drawable.sk;
            case "sl": //sierra leone
                return R.drawable.sl;
            case "sm": //san marino
                return R.drawable.sm;
            case "sn": //senegal
                return R.drawable.sn;
            case "so": //somalia
                return R.drawable.so;
            case "sr": //suriname
                return R.drawable.sr;
            case "st": //sao tome and principe
                return R.drawable.st;
            case "sv": //el salvador
                return R.drawable.sv;
            case "sx": //sint maarten
                return R.drawable.sx;
            case "sy": //syrian arab republic
                return R.drawable.sy;
            case "sz": //swaziland
                return R.drawable.sz;
            case "tc": //turks & caicos islands
                return R.drawable.tc;
            case "td": //chad
                return R.drawable.td;
            case "tg": //togo
                return R.drawable.tg;
            case "th": //thailand
                return R.drawable.th;
            case "tj": //tajikistan
                return R.drawable.tj;
            case "tk": //tokelau
                return R.drawable.tk; // custom
            case "tl": //timor-leste
                return R.drawable.tl;
            case "tm": //turkmenistan
                return R.drawable.tm;
            case "tn": //tunisia
                return R.drawable.tn;
            case "to": //tonga
                return R.drawable.to;
            case "tr": //turkey
                return R.drawable.tr;
            case "tt": //trinidad & tobago
                return R.drawable.tt;
            case "tv": //tuvalu
                return R.drawable.tv;
            case "tw": //taiwan, province of china
                return R.drawable.tw;
            case "tz": //tanzania, united republic of
                return R.drawable.tz;
            case "ua": //ukraine
                return R.drawable.ua;
            case "ug": //uganda
                return R.drawable.ug;
            case "us": //united states
                return R.drawable.us;
            case "uy": //uruguay
                return R.drawable.uy;
            case "uz": //uzbekistan
                return R.drawable.uz;
            case "va": //holy see (vatican city state)
                return R.drawable.va;
            case "vc": //st vincent & the grenadines
                return R.drawable.vc;
            case "ve": //venezuela, bolivarian republic of
                return R.drawable.ve;
            case "vg": //british virgin islands
                return R.drawable.vg;
            case "vi": //us virgin islands
                return R.drawable.vi;
            case "vn": //vietnam
                return R.drawable.vn;
            case "vu": //vanuatu
                return R.drawable.vu;
            case "wf": //wallis and futuna
                return R.drawable.wf;
            case "ws": //samoa
                return R.drawable.ws;
            case "ye": //yemen
                return R.drawable.ye;
            case "yt": //mayotte
                return R.drawable.re;//no exact flag found
            case "za": //south africa
                return R.drawable.za;
            case "zm": //zambia
                return R.drawable.zm;
            case "zw": //zimbabwe
                return R.drawable.zw;
            default:
                return R.drawable.flag_tp;
        }
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float dpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float pixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
