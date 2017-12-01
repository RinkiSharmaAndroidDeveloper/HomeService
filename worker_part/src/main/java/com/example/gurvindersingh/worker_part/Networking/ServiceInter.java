package com.example.gurvindersingh.worker_part.Networking;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created by Gurvinder Singh on 8/10/2016.
 */
public interface ServiceInter {

        @POST
        Call<ResponseBody> response(@Url String string);

        @Multipart
        @POST
        Call<ResponseBody> requestAddItem(@Url String url,
                                          @PartMap Map<String, RequestBody> image);




}
