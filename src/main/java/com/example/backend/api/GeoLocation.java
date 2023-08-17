package com.example.backend.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.example.backend.dto.GeoLocationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@Service
public class GeoLocation {

    @SuppressWarnings("unchecked")
    public GeoLocationResponse getGeoLocation(String ip) throws NoSuchAlgorithmException, InvalidKeyException {
        String hostName = "https://geolocation.apigw.ntruss.com";
        String requestUrl = "/geolocation/v2/geoLocation";
        String timestamp = Long.toString(System.currentTimeMillis());
        String accessKey = "68NSDY1QB7C7QACw2KsJ";
        String secretKey = "rY2VzROP9LsE1XIUz0hkyK2rmpbVXWd8Xb95i8nY";
        String method = "GET";

        String sigURL = "/geolocation/v2/geoLocation?ip=" + ip + "&ext=t&responseFormatType=json";

        String signature = makeSignature(sigURL, timestamp, method, accessKey, secretKey);

        RestTemplate restTemplate = new RestTemplate();

        // 헤더를 설정합니다.
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-ncp-apigw-timestamp", timestamp);
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", signature);

        // HttpEntity 객체를 생성합니다. GET 요청이므로 body는 null입니다.
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // URI를 구성하고 파라미터를 추가합니다.
        URI uri = UriComponentsBuilder.fromHttpUrl(hostName + requestUrl)
                .queryParam("ip", ip)
                .queryParam("ext", "t")
                .queryParam("responseFormatType", "json")
                .build()
                .encode()
                .toUri();

        // GET 요청을 실행하고 응답을 받습니다.
        ResponseEntity<GeoLocationResponse> response = restTemplate.exchange(uri, HttpMethod.GET, entity, GeoLocationResponse.class);
        // 응답의 본문을 반환합니다.
        return response.getBody();
    }

    public static String makeSignature(
            String url,
            String timestamp,
            String method,
            String accessKey,
            String secretKey
    ) throws NoSuchAlgorithmException, InvalidKeyException {

        String space = " ";
        String newLine = "\n";

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey;
        String encodeBase64String;
        try {
            signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
        } catch (UnsupportedEncodingException e) {
            encodeBase64String = e.toString();
        }


        return encodeBase64String;
    }
}
