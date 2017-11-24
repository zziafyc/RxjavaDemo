package com.zzia.rxjavademo.base;

import com.zzia.rxjavademo.model.OssAuthMessage;
import com.zzia.rxjavademo.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by fyc on 2017/11/16.
 */

public interface ApiService {
    @GET("user/getAllUser")
    Observable<List<User>> getAllUser();

    @Multipart
    @POST("upload/uploadOneFile")
    Observable<HttpResult<Void>> uploadOnePic(@Part("file\"; filename=\"avatar.png\"") RequestBody file);

    @Multipart
    @POST("upload/uploadMultiFile")
    Observable<HttpResult<Void>> uploadMultiPic(@PartMap Map<String, RequestBody> params);

    @GET("user/getAuthMessage")
    Observable<HttpResult<OssAuthMessage>> getAuthMessage();
}
