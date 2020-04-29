package com.example.myapplication.util

import com.example.myapplication.network.AllProductResponse
import com.example.myapplication.network.GetCart
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


enum class MarsApiFilter(val value: String) {
    SHOW_RENT("rent"),
    SHOW_BUY("buy"),
    SHOW_ALL("all")
}

//private const val BASE_URL = "http://18.218.168.60/hotshelf/api/"
private const val BASE_URL = "https://kanz.app/demo/api/"

enum class ApiStatus { LOADING, ERROR, DONE }


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getProperties] method
 */
interface CarzApiService {

    //
    /**
     * Returns a Coroutine [Deferred] [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */

//    @GET("everything?q=sports&apiKey=aa67d8d98c8e4ad1b4f16dbd5f3be348")
//    fun getNews(@Query("page") page: Int, @Query("pageSize") pageSize: Int): Single<WishListResponse>

    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("email") catId: String,
        @Field("password") min_price: String,
        @Field("device_type") device_type: String,
        @Field("device_id") device_id: String
    ): Deferred<ResponseModel>



    @FormUrlEncoded
    @POST("user/forget-password")
    fun forgetPassword(
        @Field("data") catId: String
    ): Deferred<ResponseModel>

    @FormUrlEncoded
    @POST("contact-us")
    fun contactus(
        @Field("name") catId: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("message") message: String
    ): Deferred<ResponseModel>


    @FormUrlEncoded
    @POST("user/change-password")
    fun changePassword(
        @Header("authorization") header: String,
        @Field("password") catId: String
    ): Deferred<ResponseModel>

    @FormUrlEncoded
    @POST("user/register")
    fun register(
        @Field("name") catId: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("device_type") device_type: String,
        @Field("device_id") device_id: String
    ): Deferred<ResponseModel>

    @FormUrlEncoded
    @POST("user/verify")
    fun verify(
        @Field("code") catId: String
    ): Deferred<ResponseModel>

    //@FormUrlEncoded
    @POST("homePage")
    fun getHome(
        @Header("authorization") header: String
    ): Deferred<Response>

    @POST("homePage")
    fun getHomewitoutauth(
    ): Deferred<Response>

    @GET("carts")
    fun getCart(
        @Header("authorization") header: String
        ): Deferred<ResponseModel>

    @GET("orders")
    fun getOrder(
        @Header("authorization") header: String
    ): Deferred<ResponseModel>

    @GET("wishlists")
    fun getWishList(
        @Header("authorization") header: String
    ): Deferred<ResponseModel>

    @GET("product/allProducts")
    fun getProduct(): Deferred<AllProductResponse>

    @GET("profile")
    fun getProfile(
        @Header("authorization") header: String
    ): Deferred<ResponseModel>

    @GET("getCategory")
    fun getCategory(
        @Header("authorization") header: String
    ): Deferred<ResponseModel>


    @GET("coupons")
    fun getCoupon(
        @Header("authorization") header: String
    ): Deferred<ResponseModel>

    @FormUrlEncoded
    @POST("profile/update")
    fun updateProfile(
        @Header("authorization") header: String,
        @Field("name") catId: String,
        @Field("phone") phone: String,
        @Field("address") address : String,
        @Field("password") password: String
    ): Deferred<ResponseModel>


    @FormUrlEncoded
    @POST("cart/store")
    fun addtoCartApi(
        @Header("authorization") header: String,
        @Field("product_id") productid: String,
        @Field("quantity") quantity: String
    ): Deferred<Response>


    @FormUrlEncoded
    @POST("wishlist/store")
    fun addtoWishListApi(
        @Header("authorization") header: String,
        @Field("product_id") productid: String,
        @Field("type") type: Int
    ): Deferred<Response>


    @FormUrlEncoded
    @POST("cart/delete")
    fun deleteCart(
        @Header("authorization") header: String,
        @Field("cart_id") productid: String
    ): Deferred<Response>

    @FormUrlEncoded
    @POST("wishlist/delete")
    fun deleteWishList(
        @Header("authorization") header: String,
        @Field("wishlist_id") productid: String
    ): Deferred<ResponseModel>

    @FormUrlEncoded
    @POST("cart/update")
    fun updateQuantity(
        @Header("authorization") header: String,
        @Field("order_id") productid: String,
        @Field("quantity") quantity: Int
    ): Deferred<Response>

    @FormUrlEncoded
    @POST("cart/update_coupon")
    fun donate(
        @Header("authorization") header: String,
        @Field("type") type: Int
    ): Deferred<ResponseModel>

    @FormUrlEncoded
    @POST("cart/promo-code")
    fun promocode(
        @Header("authorization") header: String,
        @Field("promo_code") type: String
    ): Deferred<ResponseModel>

    /*@Headers("Accept: application/vnd.yourapi.v1.full+json", "User-Agent: Your-App-Name")
    @GET("/tasks/{task_id}")
    fun getTask(@Path("task_id") taskId: Long): Task*/

    /*  companion object {
        private const val BASE_URL = "https://www.reddit.com/"
        fun create(): HotShelfApiService = create(HttpUrl.parse(BASE_URL)!!)
        fun create(httpUrl: HttpUrl): HotShelfApiService {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(HotShelfApiService::class.java)
        }
    }*/

/*    companion object {
        private const val BASE_URL = "https://www.reddit.com/"
        fun create(): HotShelfApiService = create(HttpUrl.parse(BASE_URL)!!)
        fun create(httpUrl: HttpUrl): HotShelfApiService {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HotShelfApiService::class.java)
        }
    }*/

    companion object {
        fun getService(): CarzApiService {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
//                .baseUrl("http://18.218.168.60/hotshelf/api/")
                .baseUrl("https://www.hotshelf.com/dev/api/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(CarzApiService::class.java)
            // .baseUrl("https://androidwave.com")
        }
    }
}

object CarzApi {
    val retrofitService: CarzApiService by lazy { retrofit.create(CarzApiService::class.java) }
}


