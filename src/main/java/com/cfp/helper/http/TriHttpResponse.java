package com.cfp.helper.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class TriHttpResponse {
	InputStream in;
	BufferedReader reader;
	InputStreamReader inreader;
	int statuscode;
	Map<String, List<String>> header;
	HttpURLConnection conn;
	String recieveData;
	private String charsetName;

	public TriHttpResponse(HttpURLConnection conn, InputStream in, String charsetName) throws IOException {
		this.statuscode = conn.getResponseCode();
		this.in = in;
		this.header = conn.getHeaderFields();
		this.recieveData = null;
		this.charsetName = charsetName;
	}

	public boolean hasHeader(String key) {
		return this.header.containsKey(key);
	}

	public String getHeader(String key) {
		return ((String) ((List) this.header.get(key)).get(0));
	}

	public List<String> getHeaderList(String key) {
		return ((List) this.header.get(key));
	}

	public Map<String, List<String>> getHeaders() {
		return this.header;
	}

	public int getStatusCode() {
		return this.statuscode;
	}

	public InputStream getInputStream() {
		return this.in;
	}

	public InputStreamReader getInputStreamReader() throws UnsupportedEncodingException {
		if (this.inreader == null)
			this.inreader = new InputStreamReader(this.in, this.charsetName);
		return this.inreader;
	}

	public BufferedReader getReader() throws UnsupportedEncodingException {
		if (this.reader == null)
			this.reader = new BufferedReader(getInputStreamReader());
		return this.reader;
	}

	public String getReponse() {
		try {
			InputStreamReader r = getInputStreamReader();

			StringBuilder b = new StringBuilder();
			char[] buffer = new char[4096];
			int n = 0;
			while (-1 != (n = r.read(buffer))) {
				b.append(buffer, 0, n);
			}
			this.in.close();
			this.recieveData = b.toString();
			return this.recieveData;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
