package edu.cnm.deepdive.jata.hilt;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.jata.R;
import edu.cnm.deepdive.jata.service.JataLongPollServiceProxy;
import edu.cnm.deepdive.jata.service.JataServiceProxy;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public final class JataModule {

  JataModule() {
  }

  @Provides
  @Singleton
  public JataServiceProxy provideProxy(@ApplicationContext Context context) {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create();
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(Level.valueOf(context.getString(R.string.log_level)));
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build();
    Retrofit retrofit = new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(client)
        .baseUrl(context.getString(R.string.base_url))
        .build();
    return retrofit.create(JataServiceProxy.class);
  }

  @Provides
  @Singleton
  public JataLongPollServiceProxy provideLongPollProxy(@ApplicationContext Context context) {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create();
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(Level.valueOf(context.getString(R.string.log_level)));
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .readTimeout(Duration.ZERO)
        .callTimeout(30, TimeUnit.SECONDS)
        .build();
    Retrofit retrofit = new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(client)
        .baseUrl(context.getString(R.string.base_url))
        .build();
    return retrofit.create(JataLongPollServiceProxy.class);
  }


}
