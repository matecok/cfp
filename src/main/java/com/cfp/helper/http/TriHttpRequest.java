package com.cfp.helper.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriHttpRequest {
	String method = "GET";
	String charsetName;
	String contentType="application/json";
	URL url;
	String body;
	List<String> postParamKeys = new ArrayList();
	List<String> postParamValues = new ArrayList();

	List<String> getParamKeys = new ArrayList();
	List<String> getParamValues = new ArrayList();

	Map<String, String> headers = new HashMap();

	HttpURLConnection conn = null;
	InputStream in = null;

	public int timeout = 10000;

	public static TriHttpRequest create(URL u, String charsetName) throws IOException {
		return new TriHttpRequest(u, charsetName);
	}

	public static TriHttpRequest create(String sUrl, String charsetName) throws IOException {
		return new TriHttpRequest(sUrl, charsetName);
	}

	public TriHttpRequest(URL u, String charsetName) throws IOException {
		this.url = u;
		this.charsetName = charsetName;
	}

	public TriHttpRequest(String sUrl, String charsetName) throws IOException {
		this.url = new URL(sUrl);
		this.charsetName = charsetName;
	}

	public TriHttpRequest(String sUrl) throws IOException {
		this.url = new URL(sUrl);
		this.charsetName = "utf-8";
	}
	public List<String> getPostKeys() {
		return this.postParamKeys;
	}

	public List<String> getPostValues() {
		return this.postParamValues;
	}

	public List<String> getGetKeys() {
		return this.getParamKeys;
	}

	public List<String> getGetValues() {
		return this.getParamValues;
	}


	public TriHttpRequest setBody(String body){
		this.body=body;
		return this;
	}

	public TriHttpRequest addPostValue(String key, String value) {
		this.postParamKeys.add(key);
		this.postParamValues.add(value);
		return this;
	}

	public TriHttpRequest addPostValue(String key, Object value) {
		addPostValue(key, value.toString());
		return this;
	}

	public TriHttpRequest setPostValues(Map<String, String> map) {
		this.postParamKeys = new ArrayList(map.keySet());
		this.postParamValues = new ArrayList(map.values());
		return this;
	}

	public TriHttpRequest addGetValue(String key, String value) {
		this.getParamKeys.add(key);
		this.getParamValues.add(value);
		return this;
	}

	public TriHttpRequest addGetValue(String key, Object value) {
		addGetValue(key, value.toString());
		return this;
	}

	public TriHttpRequest setGetValues(Map<String, String> map) {
		this.getParamKeys = new ArrayList(map.keySet());
		this.getParamValues = new ArrayList(map.values());
		return this;
	}

	public TriHttpRequest setHeader(String key, String value) {
		this.headers.put(key, value);
		return this;
	}

	public TriHttpRequest setHeaders(Map<String, String> map) {
		this.headers = map;
		return this;
	}

	public TriHttpRequest setMethod(String m) {
		this.method = m;
		return this;
	}

	public int timeout() {
		return this.timeout;
	}

	public TriHttpRequest setTimeout(int timeoutMilliseconds) {
		this.timeout = timeoutMilliseconds;
		return this;
	}

	private String getGetURL() {
		StringBuilder b = new StringBuilder();

		String prefix = "";
		int l = this.getParamKeys.size();
		for (int i = 0; i < l; ++i) {
			String k = (String) this.getParamKeys.get(i);

			b.append(prefix);
			prefix = "&";

			b.append(encode(k)).append("=").append(encode((String) this.getParamValues.get(i)));
		}

		return b.toString();
	}

	private String getPostParms() {
		StringBuilder b = new StringBuilder();

		String prefix = "";
		int l = this.postParamKeys.size();
		for (int i = 0; i < l; ++i) {
			String k = (String) this.postParamKeys.get(i);

			b.append(prefix);
			prefix = "&";

			b.append(encode(k)).append("=").append(encode((String) this.postParamValues.get(i)));
		}

		return b.toString();
	}

	public TriHttpResponse request() throws IOException {
		return request(this.method);
	}

	public void setContentType(String contentType){
		this.contentType=contentType;
	}

	public TriHttpResponse request(String requestMethod) throws IOException, SocketTimeoutException {
		if (this.getParamKeys.size() > 0) {
			String parms = getGetURL();

			String us = this.url.toString();
			if (us.contains("?"))
				this.url = new URL(this.url.toString().concat(parms));
			else {
				this.url = new URL(this.url.toString().concat("?").concat(parms));
			}
		}

		this.conn = ((HttpURLConnection) this.url.openConnection());
		this.conn.setRequestProperty("content-Type",this.contentType);
		this.conn.setConnectTimeout(this.timeout);
		this.conn.setRequestMethod(requestMethod.toUpperCase());
		this.conn.setDoInput(true);

		if (this.headers.size() > 0) {
			for (String k : this.headers.keySet()) {
				this.conn.setRequestProperty(k, (String) this.headers.get(k));
			}
		}
		if(this.body!=null){
			// POST请求
			this.conn.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(this.conn.getOutputStream());
			wr.write(this.body.getBytes());
			wr.flush();
			wr.close();
		}

		if (this.postParamKeys.size() > 0) {
			this.conn.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(this.conn.getOutputStream());
			wr.writeBytes(getPostParms());
			wr.flush();
			wr.close();
		}

		if (this.conn.getResponseCode() >= 400)
			this.in = this.conn.getErrorStream();
		else {
			this.in = this.conn.getInputStream();
		}

		return new TriHttpResponse(this.conn, this.in, this.charsetName);
	}

	public TriHttpResponse get() throws IOException, SocketTimeoutException {
		return request("GET");
	}

	public TriHttpResponse head() throws IOException, SocketTimeoutException {
		return request("HEAD");
	}

	public TriHttpResponse post() throws IOException, SocketTimeoutException {
		return request("POST");
	}

	public TriHttpResponse put() throws IOException, SocketTimeoutException {
		return request("PUT");
	}

	public TriHttpResponse delete() throws IOException, SocketTimeoutException {
		return request("DELETE");
	}
	
	public String patch() throws URISyntaxException {
		return requestPatch();
	}

	private String requestPatch() throws URISyntaxException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPatch httpPatch = new HttpPatch(url.toURI());
		httpPatch.setHeader("content-Type",this.contentType);
		if (this.headers.size() > 0) {
			for (String k : this.headers.keySet()) {
				httpPatch.setHeader(k, (String) this.headers.get(k));
			}
		}
		try {
			if(this.body!=null){
				StringEntity entity = new StringEntity(this.body,this.charsetName);
				httpPatch.setEntity(entity);
			}
			HttpResponse response = httpClient.execute(httpPatch);
			return EntityUtils.toString(response.getEntity());
		} catch (IOException   e) {
			e.printStackTrace();
		}  
		return "";
	}
	
	private String encode(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public void close() {
		if (this.in != null) {
			try {
				this.in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.conn != null)
			this.conn.disconnect();
	}

	public String getCharsetName() {
		return this.charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}
}
