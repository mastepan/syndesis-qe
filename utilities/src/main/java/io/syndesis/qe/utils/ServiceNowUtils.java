package io.syndesis.qe.utils;

import org.apache.commons.io.IOUtils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import io.syndesis.qe.accounts.Account;
import io.syndesis.qe.accounts.AccountsDirectory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public class ServiceNowUtils {

//    public Response deleteAllRecords(String tableName) {
//        return null;
//    }
//
//    public Response getAllRecords(String url, String instanceName, String tableName, String userName, String password) {
//        String contentType = "";
//        String content = "";
////        String url = "https://ven01686.service-now.com/api/now/table/u_my_table";
//        RequestBody body = RequestBody.create(MediaType.parse(contentType), content).;
//        Request.Builder requestBuilder = new Request.Builder()
//                .url(url)
//                .addHeader("Authorization", Credentials.basic(userName, password))
//                .addHeader("Accept","application/json")
//                .get();
//
//        HttpUtils.doGetRequest(url,requestBuilder.build().headers());
//        return null;
//    }

//    }
//    public Response getAllRecords(String instanceName, String tableName, String userName, String password) {
//        String contentType = "";
//        String content = "";
//        String url = "https://ven01686.service-now.com/api/now/table/u_my_table";
//        RequestBody body = RequestBody.create(MediaType.parse(contentType), content).;
//        Request.Builder requestBuilder = new Request.Builder()
//                .url(url)
//                .addHeader("Authorization", Credentials.basic(userName, password))
//                .addHeader("Accept","application/json")
//                .
//                .get(body).;
//        Headers headers = new Headers("");
//        if (headers != null) {
//            requestBuilder.headers(headers);
//        }
//
//        return null;
//    }
}
