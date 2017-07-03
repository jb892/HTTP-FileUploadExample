/**
 *  All rights reserved by Daxiangyun.com
 *  
 *  Author: Bo.Ju
 *  
 *  E-mail: bo.ju@daxiangyun.com
 * 
 */

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
	
	/**
	 * This function takes the app_id and app_key provided by www.daxiangyun.com as inputs.
	 * Then, it sends a HTTP request to ask for a unique token for identity verification for further operations.
	 * @param app_id
	 * @param app_key
	 * @return token
	 * @throws IOException
	 */
	public static String getToken(String app_id, String app_key) throws IOException {
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
		// Plug-in app_id and app_key
		RequestBody body = RequestBody.create(mediaType, 
				"------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"app_id\"\r\n\r\n"
				+app_id
				+"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"security_key\"\r\n\r\n"
				+app_key
				+"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
		Request request = new Request.Builder()
		  .url("http://open.daxiangyun.com/dx/api/auth/token")
		  .post(body)
		  .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
		  .addHeader("cache-control", "no-cache")
		  .build();

		Response response = client.newCall(request).execute();
		
		String response_body = response.body().string();
		
		// JSON parsing begin.
		JSONParser JSParser= new JSONParser();
		JSONObject JSObj = null;
		try {
			JSObj = (JSONObject)JSParser.parse(response_body);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String token = null;
		
		// Check if the given JSON string contains a "data" keyword.
		if (JSObj.containsKey("data")) {
			JSONObject tmpObj = (JSONObject) JSObj.get("data");
			if (tmpObj.containsKey("token")) {
				token = (String)tmpObj.get("token");
			} else {
				System.err.println("Parsing Error! Unable to "
						+"find the requested \"token\" keywor"
						+"d in response JSON string.");
			}
		} else {
			System.err.println("Parsing Error! Unable to "
					+"find the requested \"data\" keywor"
					+"d in response JSON string.");
		}
		
		return token;
	}
	
	/**
	 * This function takes the unique token and filePath in and upload the 
	 * model to the Daxiangyun.com server.
	 * @param token
	 * @param filePath
	 * @return modelPath
	 * @throws IOException
	 */
	public static String upload(String token, String filePath) throws IOException {
		File file = new File(filePath);
		
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("multipart/form-data;"
				+" boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
		
		RequestBody body = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/x-tgif"), file))
				.addPart(RequestBody.create(mediaType, "----WebKitFormBoundary7MA4YWxkTrZu0gW--"))
				.build();
		Request request = new Request.Builder()
		  .url("https://open.daxiangyun.com/dx/api/f/v1/file?token="+token)
		  .post(body)
		  .addHeader("content-type", "multipart/form-data boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
		  .addHeader("cache-control", "no-cache")
		  .build();

		Response response = client.newCall(request).execute();
		
		String response_body = response.body().string();
		
		// JSON parsing begin.
		JSONParser JSParser= new JSONParser();
		JSONObject JSObj = null;
		try {
			JSObj = (JSONObject)JSParser.parse(response_body);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String path = null;
		
		// Check if the given JSON string contains a "data" keyword.
		if (JSObj.containsKey("data")) {
			JSONObject tmpObj = (JSONObject) JSObj.get("data");
			if (tmpObj.containsKey("path")) {
				path = (String)tmpObj.get("path");
			} else {
				System.err.println("Parsing Error! Unable to "
						+"find the requested \"path\" keywor"
						+"d in response JSON string.");
			}
		} else {
			System.err.println("Parsing Error! Unable to "
					+"find the requested \"data\" keywor"
					+"d in response JSON string.");
		}
		
		return path;
	}
	
	/**
	 * The getStatus function takes the unique token and modelPath in and
	 * output the status of the model.
	 * @param token
	 * @param path : modelPath
	 * @return status : [Ready, Convert, Fail]
	 * @throws IOException
	 */
	public static String getStatus(String token, String path) throws IOException {
		
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
		  .url("https://open.daxiangyun.com/dx/api/f/v1/file/status?token="+token
				  +"&path="+path)
		  .get()
		  .addHeader("cache-control", "no-cache")
		  .build();

		Response response = client.newCall(request).execute();
		
		String response_body = response.body().string();
		
		// JSON parsing begin.
		JSONParser JSParser= new JSONParser();
		JSONObject JSObj = null;
		try {
			JSObj = (JSONObject)JSParser.parse(response_body);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String status = null;
		
		// Check if the given JSON string contains a "data" keyword.
		if (JSObj.containsKey("data")) {
			JSONObject tmpObj = (JSONObject) JSObj.get("data");
			if (tmpObj.containsKey("status")) {
				status = (String)tmpObj.get("status");
			} else {
				System.err.println("Parsing Error! Unable to "
						+"find the requested \"status\" keywor"
						+"d in response JSON string.");
			}
		} else {
			System.err.println("Parsing Error! Unable to "
					+"find the requested \"data\" keywor"
					+"d in response JSON string.");
		}
		
		return status;
	}
	
}
