package com.company.configuration;

import com.company.application.Mapper;
import com.company.application.data.BinData;
import com.company.domain.BinResource;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.AuthenticationUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.BinLookupApi;
import org.openapitools.client.model.BinResourceCountry;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.MissingResourceException;
import java.util.Optional;

@ApplicationScoped
public class MasterCardBinLookupConfiguration {

    @Produces
    @Dependent
    public PrivateKey masterCardBinLookupSigningKey(@ConfigProperty(name = "mastercard.bin.lookup.signing.key.path") String signingKeyPath,
                                                    @ConfigProperty(name = "mastercard.bin.lookup.signing.key.alias") String signingKeyAlias,
                                                    @ConfigProperty(name = "mastercard.bin.lookup.signing.key.password") String signingKeyPassword)
            throws UnrecoverableKeyException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        PrivateKey privateKey = AuthenticationUtils.loadSigningKey(signingKeyPath, signingKeyAlias,
                signingKeyPassword);
        return privateKey;
    }

    @Produces
    @Dependent
    public ApiClient masterCardBinLookupApiClient(@ConfigProperty(name = "mastercard.bin.lookup.consumer.key") String consumerKey,
                                                  @ConfigProperty(name = "mastercard.bin.lookup.base.path") String apiBasePath,
                                                  PrivateKey masterCardBinLookupSigningKey) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(apiBasePath);
        apiClient.setHttpClient(
                apiClient.getHttpClient()
                        .newBuilder()
                        .addInterceptor(new OkHttpOAuth1Interceptor(consumerKey, masterCardBinLookupSigningKey))
                        .build());
        return apiClient;
    }

    @Produces
    @Dependent
    public BinLookupApi masterCardBinLookupApi(ApiClient masterCardBinLookupApiClient) {
        return new BinLookupApi(masterCardBinLookupApiClient);
    }

    @Produces
    @ApplicationScoped
    public Mapper<org.openapitools.client.model.BinResource, BinData> BinResourceBinDataMapper() {
        return (org.openapitools.client.model.BinResource source) -> BinData.builder().binNum(source.getBinNum())
                .countryAlpha3(Optional.ofNullable(source.getCountry()).orElseThrow(() -> new MissingResourceException("Missing country resource",
                        BinResourceCountry.class.getName(), "country")).getAlpha3())
                .build();
    }

    @Produces
    @ApplicationScoped
    public Mapper<BinResource, BinData> domainBinResourceBinDataMapper() {
        return (BinResource source) -> BinData.builder().binNum(source.getBinNum().toString())
                .countryAlpha3(Optional.ofNullable(source.getCountryAlpha3()).orElseThrow(() -> new MissingResourceException("Missing country resource",
                        BinResourceCountry.class.getName(), "country")))
                .build();
    }
}
