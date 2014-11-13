package com.brains.framework.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.brains.app.shopclient.common.Util;
import com.brains.framework.common.Const;
import com.brains.framework.exception.AppException;

import android.util.Log;


/**
 * Wrap of DefaultHttpClient
 * 
 * @author lds
 * 
 */
public class HttpClient {

	protected static final String TAG = "HttpClient";
	private final static boolean DEBUG = false;
    private String request_encoding = "UTF-8";
    private String response_encoding = "UTF-8";

	/** OK: Success! */
	public static final int OK = 200;
	/** Not Modified: There was no new data to return. */
	public static final int NOT_MODIFIED = 304;
	/**
	 * Bad Request: The request was invalid. An accompanying error message will
	 * explain why. This is the status code will be returned during rate
	 * limiting.
	 */
	public static final int BAD_REQUEST = 400;
	/** Not Authorized: Authentication credentials were missing or incorrect. */
	public static final int NOT_AUTHORIZED = 401;
	/**
	 * Forbidden: The request is understood, but it has been refused. An
	 * accompanying error message will explain why.
	 */
	public static final int FORBIDDEN = 403;
	/**
	 * Not Found: The URI requested is invalid or the resource requested, such
	 * as a user, does not exists.
	 */
	public static final int NOT_FOUND = 404;
	/**
	 * Not Acceptable: Returned by the Search API when an invalid format is
	 * specified in the request.
	 */
	public static final int NOT_ACCEPTABLE = 406;
	/**
	 * Internal Server Error: Something is broken. Please post to the group so
	 * the Weibo team can investigate.
	 */
	public static final int INTERNAL_SERVER_ERROR = 500;
	/** Bad Gateway: Weibo is down or being upgraded. */
	public static final int BAD_GATEWAY = 502;
	/**
	 * Service Unavailable: The Weibo servers are up, but overloaded with
	 * requests. Try again later. The search and trend methods use this to
	 * indicate when you are being rate limited.
	 */
	public static final int SERVICE_UNAVAILABLE = 503;

	private static final int CONNECTION_TIMEOUT_MS = 40 * 1000;
	private static final int SOCKET_TIMEOUT_MS = 40 * 1000;

	public static final int RETRIEVE_LIMIT = 20;
	public static final int RETRIED_TIME = 3;
	
	

	protected DefaultHttpClient mClient;
	private AuthScope mAuthScope;
	protected BasicHttpContext localcontext;
	private String mUserId;
	private String mPassword;
	private static boolean isAuthenticationEnabled = false;
	
//	/**
//	 * 不该提供该代码给Api使用。
//	 * 现木已成舟，暂开放使用
//	 * 后有删除可能
//	 * @return
//	 */
//	public DefaultHttpClient  getClient(){
//		return mClient;
//	}

	public HttpClient() {
		prepareHttpClient();
	}

	/**
	 * Setup Credentials for HTTP Basic Auth
	 * 
	 * @param username
	 * @param password
	 */
	public void setCredentials(String username, String password) {
		mUserId = username;
		mPassword = password;
		mClient.getCredentialsProvider().setCredentials(mAuthScope,
				new UsernamePasswordCredentials(username, password));
		Util.sysLog("xxx","222:" + mClient.getCredentialsProvider().getCredentials(mAuthScope).toString()); 
		isAuthenticationEnabled = true;
	}
	/**
	 * Empty the credentials
	 */
	public void reset() {
		setCredentials("", "");
	}
	
	/**
	 * @param hostname
	 *            the hostname (IP or DNS name)
	 * @param port
	 *            the port number. -1 indicates the scheme default port.
	 * @param scheme
	 *            the name of the scheme. null indicates the default scheme
	 */
	public void setProxy(String host, int port, String scheme) {
		HttpHost proxy = new HttpHost(host, port, scheme);
		mClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}

	public void removeProxy() {
		mClient.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
	}

	private void enableDebug() {
		Log.d(TAG, "enable apache.http debug");

		java.util.logging.Logger.getLogger("org.apache.http").setLevel(
				java.util.logging.Level.FINEST);
		java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(
				java.util.logging.Level.FINER);
		java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(
				java.util.logging.Level.OFF);
	}

	/**
	 * Setup DefaultHttpClient
	 * 
	 * Use ThreadSafeClientConnManager.
	 * 
	 */
	private void prepareHttpClient() {
		if (DEBUG) {
			enableDebug();
		}

		// Create and initialize HTTP parameters
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 10);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setConnectionTimeout(params,CONNECTION_TIMEOUT_MS);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT_MS);
		
		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));

		// Create an HttpClient with the ThreadSafeClientConnManager.
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
				params, schemeRegistry);
		mClient = new DefaultHttpClient(cm, params);
		
		// Setup BasicAuth
		BasicScheme basicScheme = new BasicScheme();
		mAuthScope = new AuthScope(null, AuthScope.ANY_PORT);

		//		 mClient.setAuthSchemes(authRegistry);
		mClient.setCredentialsProvider(new BasicCredentialsProvider());

		// Generate BASIC scheme object and stick it to the local
		// execution context
		localcontext = new BasicHttpContext();
		localcontext.setAttribute("preemptive-auth", basicScheme);
		// first request interceptor
		mClient.addRequestInterceptor(preemptiveAuth, 0);
		// Support GZIP
		mClient.addResponseInterceptor(gzipResponseIntercepter);
	}

	/**
	 * HttpRequestInterceptor for DefaultHttpClient
	 */
	private static HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {
		@Override
		public void process(final HttpRequest request, final HttpContext context) {
			AuthState authState = (AuthState) context
					.getAttribute(ClientContext.TARGET_AUTH_STATE);
			CredentialsProvider credsProvider = (CredentialsProvider) context
					.getAttribute(ClientContext.CREDS_PROVIDER);
			HttpHost targetHost = (HttpHost) context
					.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

			if (authState.getAuthScheme() == null) {
				AuthScope authScope = new AuthScope(targetHost.getHostName(),
						targetHost.getPort());
				Credentials creds = credsProvider.getCredentials(authScope);
				Util.sysLog("xxx", "xxxxxxxx111");
				if (creds != null) {
					Util.sysLog("xxx", "xxxxxxxx2222"+creds.toString());
					authState.setAuthScheme(new BasicScheme());
					authState.setCredentials(creds);
				}
			}
		}
	};

	private static HttpResponseInterceptor gzipResponseIntercepter = new HttpResponseInterceptor() {

		@Override
		public void process(HttpResponse response, HttpContext context)
				throws org.apache.http.HttpException, IOException {
			HttpEntity entity = response.getEntity();
			Header ceheader = entity.getContentEncoding();
			if (ceheader != null) {
				HeaderElement[] codecs = ceheader.getElements();
				for (int i = 0; i < codecs.length; i++) {
					if (codecs[i].getName().equalsIgnoreCase("gzip")) {
						response.setEntity(new GzipDecompressingEntity(response
								.getEntity()));
						return;
					}
				}
			}

		}
	};

	static class GzipDecompressingEntity extends HttpEntityWrapper {

		public GzipDecompressingEntity(final HttpEntity entity) {
			super(entity);
		}

		@Override
		public InputStream getContent() throws IOException,
				IllegalStateException {

			// the wrapped entity's getContent() decides about repeatability
			InputStream wrappedin = wrappedEntity.getContent();
			return new GZIPInputStream(wrappedin);
		}

		@Override
		public long getContentLength() {
			// length of ungzipped content is not known
			return -1;
		}

	}

	public Response post(String url, ArrayList<BasicNameValuePair> params) throws AppException{
		return httpRequest(url, params, true, HttpPost.METHOD_NAME);
	}

	public Response post(String url, ArrayList<BasicNameValuePair> postParams,File file,String fileName) throws AppException{
		return httpRequest(url, postParams, file, fileName,  HttpPost.METHOD_NAME);
	}
	
	public Response get(String url, ArrayList<BasicNameValuePair> params,
			boolean authenticated) throws AppException {
		return httpRequest(url, params, authenticated, HttpGet.METHOD_NAME);
	}

	public Response get(String url, ArrayList<BasicNameValuePair> params) throws AppException {
		return httpRequest(url, params, false, HttpGet.METHOD_NAME);
	}

	public Response get(String url) throws AppException {
		return httpRequest(url, null, false, HttpGet.METHOD_NAME);
	}

	public Response get(String url, boolean authenticated) throws AppException {
		System.out.println("url(get)============================" + url);
		return httpRequest(url, null, authenticated, HttpGet.METHOD_NAME);
	}

	public Response httpRequest(String url,
			ArrayList<BasicNameValuePair> postParams, boolean authenticated,
			String httpMethod) throws AppException {
		System.out.println("url(post)============================" + url);
		return httpRequest(url, postParams, null, null, httpMethod);
	}

	/**
	 * Execute the DefaultHttpClient
	 * 
	 * @param url
	 *            target
	 * @param postParams
	 * @param file
	 *            can be NULL
	 * @param authenticated
	 *            need or not
	 * @param httpMethod
	 *            HttpPost.METHOD_NAME HttpGet.METHOD_NAME
	 *            HttpDelete.METHOD_NAME
	 * @return Response from server
	 * @throws HttpException
	 *      
	 */
	public Response httpRequest(String url,
			ArrayList<BasicNameValuePair> postParams, File file,
			String fileParamName, String httpMethod) throws AppException {
		Log.d(TAG, "Sending " + httpMethod + " request to " + url);
		HttpResponse response = null;
		Response res = new Response();
		HttpUriRequest method = null;
		
		try {
			URI uri = createURI(url);
			// Create POST, GET or DELETE METHOD
			method = createMethod(httpMethod, uri, file,fileParamName, postParams);
			// Setup ConnectionParams, Request Headers
			SetupHTTPConnectionParams(method);

			// Execute Request

			response = mClient.execute(method);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new AppException(Const.ERROR_NET);
		} catch (IOException e) {
			throw new AppException(Const.ERROR_NET);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new AppException(Const.ERROR_NET);
		}

		if (response != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			// It will throw a weiboException while status code is not 200
			handleResponseStatusCode(statusCode, response,res);
		} else {
			Log.e(TAG, "response is null");
		}
	
//		mClient.getConnectionManager().shutdown();
		return res;
	}

	/**
	 * CreateURI from URL string
	 * 
	 * @param url
	 * @return request URI
	 * @throws URISyntaxException 
	 * @throws HttpException
	 *             Cause by URISyntaxException
	 */
	protected URI createURI(String url) throws URISyntaxException {
		return new URI(url);
	}


	/**
	 * ??��??���?��File??ultipartEntity
	 * 
	 * @param filename
	 *            ??��??	 * @param file
	 *            ??��
	 * @param postParams
	 *            ?��?POST???
	 * @return �??件�??��??????ntity
	 * @throws UnsupportedEncodingException
	 */
	private MultipartEntity createMultipartEntity(String filename, File file,
			ArrayList<BasicNameValuePair> postParams)
			throws UnsupportedEncodingException {
		MultipartEntity entity = new MultipartEntity();
		// Don't try this. Server does not appear to support chunking.
		// entity.addPart("media", new InputStreamBody(imageStream, "media"));

		for (BasicNameValuePair param : postParams) {
			Log.d("xiect",param.getName()+" value:" + param.getValue());
			entity.addPart(param.getName(), new StringBody(param.getValue(),Charset.forName("UTF-8")));
		}
		
		Log.d("xiect",filename+" value:" + file.getName());
		entity.addPart(filename, new FileBody(file,"image/jpeg"));
		return entity;
	}

	/**
	 * Setup HTTPConncetionParams
	 * 
	 * @param method
	 */
	protected void SetupHTTPConnectionParams(HttpUriRequest method) {
//		HttpConnectionParams.setConnectionTimeout(method.getParams(),
//				CONNECTION_TIMEOUT_MS);
//		HttpConnectionParams
//				.setSoTimeout(method.getParams(), SOCKET_TIMEOUT_MS);
		mClient.setHttpRequestRetryHandler(requestRetryHandler);
		method.addHeader("Accept-Encoding", "gzip, deflate");
		method.addHeader("Accept-Charset", "UTF-8,*;q=0.5");
	}

	/**
	 * Create request method, such as POST, GET, DELETE
	 * 
	 * @param httpMethod
	 *            "GET","POST","DELETE"
	 * @param uri
	 *            请�???RI
	 * @param file
	 *            ??��null
	 * @param postParams
	 *            POST???
	 * @return httpMethod Request implementations for the various HTTP methods
	 *         like GET and POST.
	 * @throws UnsupportedEncodingException 
	 * @throws HttpException
	 *             createMultipartEntity ??UrlEncodedFormEntity�????OException
	 */
	protected HttpUriRequest createMethod(String httpMethod, URI uri,
			File file,String fileParamName, ArrayList<BasicNameValuePair> postParams) throws UnsupportedEncodingException {

		HttpUriRequest method;

		if (httpMethod.equalsIgnoreCase(HttpPost.METHOD_NAME)) {
			HttpPost post = new HttpPost(uri);

			HttpEntity entity = null;
			if (null != file) {
				entity = createMultipartEntity(fileParamName, file, postParams);
			} else if (null != postParams) {
				for (BasicNameValuePair param : postParams) {
					Log.d("xiect",param.getName()+" value:" + param.getValue());
				}
				String string = URLEncodedUtils.format(postParams, HTTP.UTF_8).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
				Util.sysLog("xiect","post value:" + string);
				StringEntity stringEntity = new StringEntity(string,HTTP.UTF_8);
				stringEntity.setContentType(URLEncodedUtils.CONTENT_TYPE);
				entity = stringEntity;
			}
			post.setEntity(entity);
			method = post;
		} else if (httpMethod.equalsIgnoreCase(HttpDelete.METHOD_NAME)) {
			method = new HttpDelete(uri);
		} else {
			method = new HttpGet(uri);
		}

		return method;
	}

	/**
	 * 解析http 状态code
	 * @param statusCode
	 * @return
	 */
	private static String getCause(int statusCode) {
		String cause = null;
		switch (statusCode) {
		case NOT_MODIFIED:
			break;
		case BAD_REQUEST:
			cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
			break;
		case NOT_AUTHORIZED:
			cause = "Authentication credentials were missing or incorrect.";
			break;
		case FORBIDDEN:
			cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
			break;
		case NOT_FOUND:
			cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
			break;
		case NOT_ACCEPTABLE:
			cause = "Returned by the Search API when an invalid format is specified in the request.";
			break;
		case INTERNAL_SERVER_ERROR:
			cause = "Something is broken.  Please post to the group so the Weibo team can investigate.";
			break;
		case BAD_GATEWAY:
			cause = "Weibo is down or being upgraded.";
			break;
		case SERVICE_UNAVAILABLE:
			cause = "Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
			break;
		default:
			cause = "";
		}
		return statusCode + ":" + cause;
	}


    /**
     * 通信结果处理
     * @throws AppException 
     */
	protected void handleResponseStatusCode(int statusCode, HttpResponse response,Response res) throws AppException {
		String msg = getCause(statusCode) + "\n";
		
		switch (statusCode) {
		// It's OK, do nothing
		case OK:
			String response_text;
			try {
				response_text = EntityUtils.toString(
						response.getEntity(),
						response_encoding
						);
				Log.d(TAG,"res:" + response_text);
				res.setTextOnSuccess( response_text );	
				response.getEntity().consumeContent();
			} catch (ParseException e) {
				throw new AppException(Const.ERROR_NET);
			} catch (IOException e) {
				throw new AppException(Const.ERROR_NET);
			}
			break;
		// Mine mistake, Check the Log
		case NOT_MODIFIED:
		case BAD_REQUEST:
		case NOT_FOUND:
		case NOT_ACCEPTABLE:
			Log.d(TAG,"HTTP ===== NOT_ACCEPTABLE");
			throw new AppException(Const.ERROR_NET);
		case NOT_AUTHORIZED:
			Log.d(TAG,"HTTP ===== NOT_AUTHORIZED");
			throw new AppException(Const.ERROR_NET);
		case FORBIDDEN:
			Log.d(TAG,"HTTP ===== FORBIDDEN");
			throw new AppException(Const.ERROR_NET);
		case INTERNAL_SERVER_ERROR:
		case BAD_GATEWAY:
		case SERVICE_UNAVAILABLE:
			throw new AppException(Const.ERROR_NET);
		default:
			throw new AppException(Const.ERROR_NET);
		}
	}

	public static String encode(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, HTTP.UTF_8);
	}

	public static String encodeParameters(ArrayList<BasicNameValuePair> params) throws UnsupportedEncodingException {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < params.size(); j++) {
			if (params.get(j).getName() == null
					|| params.get(j).getValue() == null) {
				continue;
			}
			// if (j != 0) {
			buf.append("&");
			// }
			buf.append(URLEncoder.encode(params.get(j).getName(), "UTF-8"))
			.append("=")
			.append(URLEncoder.encode(params.get(j).getValue(),
					"UTF-8"));
		}
		return buf.toString();
	}

	/**
	 * �?��????��?�??, 使�?HttpRequestRetryHandler?��?�??请�????常�?�?	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			// 设置?��?�??�?????�?��?��?�???��?�?�?	
			if (executionCount >= RETRIED_TIME) {
				// Do not retry if over max retry count
				return false;
			}

			if (exception instanceof NoHttpResponseException) {
				// Retry if the server dropped connection on us
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// Do not retry on SSL handshake exception
				return false;
			}
			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		}
	};

}
