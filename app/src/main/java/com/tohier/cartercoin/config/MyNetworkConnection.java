package com.tohier.cartercoin.config;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.webkit.MimeTypeMap;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.android.util.remote.CountingRequestBody;
import com.tohier.android.util.remote.NetworkConnection;
import com.tohier.android.view.ZProgressHUD;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/5/22.
 */

public class MyNetworkConnection {
    private static Set<String> callTags = Collections.newSetFromMap(new WeakHashMap());
    private IContext context;

    private MyNetworkConnection(IContext context) {
        this.context = context;
    }

    public static void cancel(String callTag) {
        callTags.remove(callTag);
        OkHttpUtil.cancel(callTag);
    }

    public static void cancelAll() {
        Iterator var1 = callTags.iterator();

        while(var1.hasNext()) {
            String value = (String)var1.next();
            OkHttpUtil.cancel(value);
        }

        callTags.clear();
    }

    public static MyNetworkConnection getNetworkConnection(IContext context) {
        MyNetworkConnection networkConnection = new MyNetworkConnection(context);
        return networkConnection;
    }

    /** @deprecated */
    @Deprecated
    public void post(String callTag, String url, Map<String, String> parameters, Callback responseCallback) {
        if(StringUtils.isBlank(callTag)) {
            callTag = UUID.randomUUID().toString();
        }

        callTags.add(callTag);
        FormEncodingBuilder fb = new FormEncodingBuilder();
        Request request = null;
        if(parameters != null) {
            Set keys = parameters.keySet();
            Iterator var9 = keys.iterator();

            while(var9.hasNext()) {
                String formBody = (String)var9.next();
                if(parameters.get(formBody) != null) {
                    fb.add(formBody, (String)parameters.get(formBody));
                }
            }

            RequestBody formBody1 = fb.build();
            request = (new Request.Builder()).tag(callTag).url(url).post(formBody1).build();
        } else {
            request = (new Request.Builder()).tag(callTag).url(url).build();
        }

        if(this.isNetWorkInfo(request, responseCallback)) {
            if(responseCallback == null) {
                OkHttpUtil.enqueue(request);
            } else {
                OkHttpUtil.enqueue(request, responseCallback);
            }
        }

    }

    /** @deprecated */
    @Deprecated
    public void postString(String callTag, String url, String string, Callback responseCallback) {
        if(StringUtils.isBlank(callTag)) {
            callTag = UUID.randomUUID().toString();
        }

        callTags.add(callTag);
        FormEncodingBuilder fb = new FormEncodingBuilder();
        Request request = null;
        fb.add(string, string);
        RequestBody formBody = fb.build();
        request = (new Request.Builder()).tag(callTag).url(url).post(formBody).build();
        if(this.isNetWorkInfo(request, responseCallback)) {
            if(responseCallback == null) {
                OkHttpUtil.enqueue(request);
            } else {
                OkHttpUtil.enqueue(request, responseCallback);
            }
        }

    }

    private boolean isNetWorkInfo(Request request, Callback responseCallback) {
        if(this.context == null) {
            return true;
        } else {
            ConnectivityManager cwjManager = (ConnectivityManager)this.context.getContext().getSystemService("connectivity");
            NetworkInfo networkInfo = cwjManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isAvailable()) {
                return true;
            } else {
                responseCallback.onFailure(request, new SocketException());
                Handler dataHandler = new Handler(this.context.getContext().getMainLooper()) {
                    public void handleMessage(Message msg) {
//                        ZProgressHUD.getInstance(MyNetworkConnection.this.context.getContext()).cancel();
//                        MyNetworkConnection.this.context.sToast("网络连接似乎断开了!");
                    }
                };
                dataHandler.sendEmptyMessage(0);
                return false;
            }
        }
    }

    public void postValues(String callTag, String url, Map<String, Object> parameters, Callback responseCallback) {
        this.postValues(callTag, url, parameters, responseCallback, true);
    }

    public void postValues(String callTag, String url, Map<String, Object> parameters, Callback responseCallback, boolean caniNetWork) {
        if(StringUtils.isBlank(callTag)) {
            callTag = UUID.randomUUID().toString();
        }

        callTags.add(callTag);
        FormEncodingBuilder fb = new FormEncodingBuilder();
        Request request = null;
        if(parameters != null) {
            Set keys = parameters.keySet();
            Iterator var10 = keys.iterator();

            label147:
            while(true) {
                while(true) {
                    String formBody;
                    Object o;
                    do {
                        if(!var10.hasNext()) {
                            RequestBody var18 = fb.build();
                            request = (new Request.Builder()).tag(callTag).url(url).post(var18).build();
                            break label147;
                        }

                        formBody = (String)var10.next();
                        o = parameters.get(formBody);
                    } while(o == null);

                    if(Collection.class.isAssignableFrom(o.getClass())) {
                        Collection var32 = (Collection)o;
                        Iterator var44 = var32.iterator();

                        while(var44.hasNext()) {
                            Object var36 = var44.next();
                            fb.add(formBody, String.valueOf(var36));
                        }
                    } else {
                        int var14;
                        int var15;
                        if(o instanceof String[]) {
                            String[] var30 = (String[])o;
                            String[] var45 = var30;
                            var15 = var30.length;

                            for(var14 = 0; var14 < var15; ++var14) {
                                String var35 = var45[var14];
                                fb.add(formBody, String.valueOf(var35));
                            }
                        } else if(o instanceof int[]) {
                            int[] var28 = (int[])o;
                            int[] var43 = var28;
                            var15 = var28.length;

                            for(var14 = 0; var14 < var15; ++var14) {
                                int var34 = var43[var14];
                                fb.add(formBody, String.valueOf(var34));
                            }
                        } else if(o instanceof float[]) {
                            float[] var26 = (float[])o;
                            float[] var42 = var26;
                            var15 = var26.length;

                            for(var14 = 0; var14 < var15; ++var14) {
                                float var33 = var42[var14];
                                fb.add(formBody, String.valueOf(var33));
                            }
                        } else {
                            int var37;
                            if(o instanceof double[]) {
                                double[] var24 = (double[])o;
                                double[] var40 = var24;
                                var37 = var24.length;

                                for(var15 = 0; var15 < var37; ++var15) {
                                    double var31 = var40[var15];
                                    fb.add(formBody, String.valueOf(var31));
                                }
                            } else if(o instanceof boolean[]) {
                                boolean[] var22 = (boolean[])o;
                                boolean[] var41 = var22;
                                var15 = var22.length;

                                for(var14 = 0; var14 < var15; ++var14) {
                                    boolean var29 = var41[var14];
                                    fb.add(formBody, String.valueOf(var29));
                                }
                            } else if(o instanceof byte[]) {
                                byte[] var21 = (byte[])o;
                                byte[] var39 = var21;
                                var15 = var21.length;

                                for(var14 = 0; var14 < var15; ++var14) {
                                    byte var27 = var39[var14];
                                    fb.add(formBody, String.valueOf(var27));
                                }
                            } else if(o instanceof short[]) {
                                short[] var20 = (short[])o;
                                short[] var38 = var20;
                                var15 = var20.length;

                                for(var14 = 0; var14 < var15; ++var14) {
                                    short var25 = var38[var14];
                                    fb.add(formBody, String.valueOf(var25));
                                }
                            } else if(o instanceof long[]) {
                                long[] var19 = (long[])o;
                                long[] var17 = var19;
                                var37 = var19.length;

                                for(var15 = 0; var15 < var37; ++var15) {
                                    long var23 = var17[var15];
                                    fb.add(formBody, String.valueOf(var23));
                                }
                            } else if(o instanceof char[]) {
                                char[] array = (char[])o;
                                char[] var16 = array;
                                var15 = array.length;

                                for(var14 = 0; var14 < var15; ++var14) {
                                    char object = var16[var14];
                                    fb.add(formBody, String.valueOf(object));
                                }
                            } else {
                                fb.add(formBody, String.valueOf(o));
                            }
                        }
                    }
                }
            }
        } else {
            request = (new Request.Builder()).tag(callTag).url(url).build();
        }

        if(!caniNetWork || this.isNetWorkInfo(request, responseCallback)) {
            if(responseCallback == null) {
                OkHttpUtil.enqueue(request);
            } else {
                OkHttpUtil.enqueue(request, responseCallback);
            }
        }

    }

    public void post(String callTag, File file, String fileName, String url, Map<String, String> parameters, Callback responseCallback, CountingRequestBody.Listener listener) {
        if(StringUtils.isBlank(callTag)) {
            callTag = UUID.randomUUID().toString();
        }

        callTags.add(callTag);
        MultipartBuilder multipartBuilder = (new MultipartBuilder()).type(MultipartBuilder.FORM);
        String extension = getExtension(file);
        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if(mime == null) {
            mime = "application/octet-stream";
        }

        multipartBuilder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"upload\";filename=\"" + fileName + "\""}), RequestBody.create(MediaType.parse(mime), file));
        Object requestBody2;
        if(parameters != null) {
            Set requestBody = parameters.keySet();
            Iterator b = requestBody.iterator();

            label143:
            while(true) {
                while(true) {
                    if(!b.hasNext()) {
                        break label143;
                    }

                    String headersMap = (String)b.next();
                    requestBody2 = parameters.get(headersMap);
                    if(Collection.class.isAssignableFrom(requestBody2.getClass())) {
                        Collection var37 = (Collection)requestBody2;
                        Iterator var50 = var37.iterator();

                        while(var50.hasNext()) {
                            Object var42 = var50.next();
                            multipartBuilder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + headersMap + "\""}), RequestBody.create((MediaType)null, String.valueOf(var42)));
                        }
                    } else {
                        int var17;
                        int var18;
                        if(requestBody2 instanceof String[]) {
                            String[] var35 = (String[])requestBody2;
                            String[] var51 = var35;
                            var18 = var35.length;

                            for(var17 = 0; var17 < var18; ++var17) {
                                String var41 = var51[var17];
                                multipartBuilder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + headersMap + "\""}), RequestBody.create((MediaType)null, String.valueOf(var41)));
                            }
                        } else if(requestBody2 instanceof int[]) {
                            int[] var33 = (int[])requestBody2;
                            int[] var49 = var33;
                            var18 = var33.length;

                            for(var17 = 0; var17 < var18; ++var17) {
                                int var40 = var49[var17];
                                multipartBuilder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + headersMap + "\""}), RequestBody.create((MediaType)null, String.valueOf(var40)));
                            }
                        } else if(requestBody2 instanceof float[]) {
                            float[] var31 = (float[])requestBody2;
                            float[] var48 = var31;
                            var18 = var31.length;

                            for(var17 = 0; var17 < var18; ++var17) {
                                float var38 = var48[var17];
                                multipartBuilder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + headersMap + "\""}), RequestBody.create((MediaType)null, String.valueOf(var38)));
                            }
                        } else {
                            int var43;
                            if(requestBody2 instanceof double[]) {
                                double[] var29 = (double[])requestBody2;
                                double[] var46 = var29;
                                var43 = var29.length;

                                for(var18 = 0; var18 < var43; ++var18) {
                                    double var36 = var46[var18];
                                    multipartBuilder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + headersMap + "\""}), RequestBody.create((MediaType)null, String.valueOf(var36)));
                                }
                            } else if(requestBody2 instanceof boolean[]) {
                                boolean[] var27 = (boolean[])requestBody2;
                                boolean[] var47 = var27;
                                var18 = var27.length;

                                for(var17 = 0; var17 < var18; ++var17) {
                                    boolean var34 = var47[var17];
                                    multipartBuilder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + headersMap + "\""}), RequestBody.create((MediaType)null, String.valueOf(var34)));
                                }
                            } else if(requestBody2 instanceof byte[]) {
                                byte[] var26 = (byte[])requestBody2;
                                byte[] var45 = var26;
                                var18 = var26.length;

                                for(var17 = 0; var17 < var18; ++var17) {
                                    byte var32 = var45[var17];
                                    multipartBuilder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + headersMap + "\""}), RequestBody.create((MediaType)null, String.valueOf(var32)));
                                }
                            } else if(requestBody2 instanceof short[]) {
                                short[] var25 = (short[])requestBody2;
                                short[] var44 = var25;
                                var18 = var25.length;

                                for(var17 = 0; var17 < var18; ++var17) {
                                    short var30 = var44[var17];
                                    multipartBuilder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + headersMap + "\""}), RequestBody.create((MediaType)null, String.valueOf(var30)));
                                }
                            } else if(requestBody2 instanceof long[]) {
                                long[] var24 = (long[])requestBody2;
                                long[] var20 = var24;
                                var43 = var24.length;

                                for(var18 = 0; var18 < var43; ++var18) {
                                    long var28 = var20[var18];
                                    multipartBuilder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + headersMap + "\""}), RequestBody.create((MediaType)null, String.valueOf(var28)));
                                }
                            } else if(requestBody2 instanceof char[]) {
                                char[] request = (char[])requestBody2;
                                char[] var19 = request;
                                var18 = request.length;

                                for(var17 = 0; var17 < var18; ++var17) {
                                    char object = var19[var17];
                                    multipartBuilder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + headersMap + "\""}), RequestBody.create((MediaType)null, String.valueOf(object)));
                                }
                            } else {
                                multipartBuilder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + headersMap + "\""}), RequestBody.create((MediaType)null, String.valueOf(requestBody2)));
                            }
                        }
                    }
                }
            }
        }

        RequestBody var21 = multipartBuilder.build();
        HashMap var22 = new HashMap();
        var22.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:30.0) Gecko/20100101 Firefox/30.0");
        String var23 = url.substring(url.indexOf("//") + 2);
        var22.put("Host", var23.substring(0, var23.indexOf("/")));
        var22.put("Connection", "Keep-Alive");
        var22.put("Cache-Control", "no-cache");
        requestBody2 = null;
        if(listener == null) {
            requestBody2 = var21;
        } else {
            requestBody2 = new CountingRequestBody(var21, listener);
        }

        Request var39 = (new Request.Builder()).tag(callTag).headers(Headers.of(var22)).url(url).post((RequestBody)requestBody2).build();
        if(this.isNetWorkInfo(var39, responseCallback)) {
            if(responseCallback == null) {
                OkHttpUtil.enqueue(var39);
            } else {
                OkHttpUtil.enqueue(var39, responseCallback);
            }
        }

    }

    private static String getExtension(File file) {
        String suffix = "";
        String name = file.getName();
        int idx = name.lastIndexOf(".");
        if(idx > 0) {
            suffix = name.substring(idx + 1);
        }

        return suffix;
    }
}

class OkHttpUtil {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    static {
        mOkHttpClient.setConnectTimeout(30L, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(30L, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30L, TimeUnit.SECONDS);
    }

    OkHttpUtil() {
    }

    static Call enqueue(Request request, Callback responseCallback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(responseCallback);
        return call;
    }

    static Call enqueue(Request request) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Response arg0) throws IOException {
            }

            public void onFailure(Request arg0, IOException arg1) {
            }
        });
        return call;
    }

    static void cancel(String callTag) {
        mOkHttpClient.cancel(callTag);
    }
}

