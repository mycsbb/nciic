package com.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.print.URIException;

import org.junit.Test;

import com.csrc.nciic.model.Authinfo;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

@SuppressWarnings("unused")
public class TestSSL {
	SecureRandom secureRandom = new SecureRandom();
	
	@Test
	public void t01() {
		System.out.println(System.getProperty("java.library.path"));
	}

	@Test
	public void tNciic() throws Exception {
		String name = "陈江";
		String idennumber = "350524198903275596";
		Authinfo authinfo = getEntryAuthinfo();
		Thread.sleep(500);
		doAuthProcess(authinfo);
		Thread.sleep(500);
//		getServiceJsp(authinfo);
//		Thread.sleep(500);
		doIdentifyQuery(authinfo, name, idennumber);
	}

	private void getServiceJsp(Authinfo authinfo) throws Exception {
		System.out.println("###############GetSrviceJsp阶段【开始】#############");
		String Auth_Content = authinfo.getAuth_Content();
		String fAvfGpbE26 = authinfo.getfAvfGpbE26();
		String JSESSIONID = authinfo.getJSESSIONID();
		if (Auth_Content == null || Auth_Content.trim().equals("")
				|| fAvfGpbE26 == null || fAvfGpbE26.trim().equals("")
				|| JSESSIONID == null || JSESSIONID.trim().equals("")) {
			System.out.println("认证原文不能为空!");
			return;
		}
		
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, secureRandom);
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		
		String strAction="https://web.nciic.org.cn/nciic_self/jsp/serv/services/services.jsp?servicesid=1191";
		URL myURL = new URL(strAction);
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL
				.openConnection();
		httpsConn.setSSLSocketFactory(ssf);
		httpsConn.setDoOutput(true);
		
		// 消息头
		httpsConn.addRequestProperty("Accept", "text/html, application/xhtml+xml, */*");
		httpsConn.addRequestProperty("Accept-Language", "zh-cn");
//		httpsConn.addRequestProperty("Referer",
//				"https://web.nciic.org.cn/nciic_self/"
//						+ "jsp/serv/services/services.jsp?servicesid=1191");
		httpsConn.addRequestProperty("Accept-Encoding", "gzip, deflate");
		//httpsConn.addRequestProperty("Content-Type", "text/xml; charset=UTF-8");
		httpsConn
		.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		httpsConn.addRequestProperty("Host", "web.nciic.org.cn");
		// httpsConn.addRequestProperty("Content-Length", "340");
		httpsConn.addRequestProperty("DNT", "1");
		httpsConn.addRequestProperty("Connection", "Keep-Alive");
		httpsConn.addRequestProperty("Cache-Control", "no-cache");
		httpsConn
		.addRequestProperty(
				"Cookie",
				"crmLoginCookie=1; loginStyle=BlueTemplate; "
						+ "loginID=zjhjzjdx52878; loginPW=N11111; loginIDtmp=zjhjzjdx52878; "
						+ "fAvfGpbE26=" + fAvfGpbE26 + "; "
						+ "JSESSIONID=" + JSESSIONID);
		
		printRequestHeaders(httpsConn);
		
		// 连接
		httpsConn.connect();
		
		printResponseHeaders(httpsConn);
		
		InputStreamReader insr = new InputStreamReader(
				httpsConn.getInputStream(), "GBK");
//		BufferedReader br = new BufferedReader(insr);
//		String line, str = "";
//		while ((line = br.readLine()) != null) {
//			str += line + "\n";
//		}
		int respInt = insr.read();
		System.out.println(respInt);
		int n_bytes = 0;
		while (respInt != -1) {
			//System.out.print((char) respInt);
			respInt = insr.read();
			n_bytes++;
		}
		System.out.println("n_bytes: " + n_bytes);
		//System.out.println(str);
		httpsConn.disconnect();
		
		System.out.println("###############GetSrviceJsp【结束】#############");
	}
	private String doIdentifyQuery(Authinfo authinfo, String name, String idennumber) throws Exception {
		System.out.println("###############查询阶段【开始】#############");
		String Auth_Content = authinfo.getAuth_Content();
		String fAvfGpbE26 = authinfo.getfAvfGpbE26();
		String JSESSIONID = authinfo.getJSESSIONID();
		if (Auth_Content == null || Auth_Content.trim().equals("")
				|| fAvfGpbE26 == null || fAvfGpbE26.trim().equals("")
				|| JSESSIONID == null || JSESSIONID.trim().equals("")) {
			System.out.println("认证原文不能为空!");
			return null;
		}
		if (name == null || name.trim().equals("")
				|| idennumber == null || idennumber.trim().equals("")) {
			System.out.println("字段信息不能为空!");
			return null;
		}

		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, secureRandom);
		SSLSocketFactory ssf = sslContext.getSocketFactory();

		String strAction="https://web.nciic.org.cn/nciic_self/serv/ServicesParseAction.do?method=getResDetails";
		URL myURL = new URL(strAction);
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL
				.openConnection();
		httpsConn.setSSLSocketFactory(ssf);
		httpsConn.setRequestMethod("POST");
		httpsConn.setDoOutput(true);

		// 消息头
		httpsConn.addRequestProperty("Accept", "*/*");
		httpsConn.addRequestProperty("Accept-Language", "zh-cn");
//		httpsConn.addRequestProperty("Referer",
//				"https://web.nciic.org.cn/nciic_self/"
//						+ "jsp/serv/services/services.jsp?servicesid=1191");
		httpsConn.addRequestProperty("Accept-Encoding", "gzip, deflate");
		httpsConn.addRequestProperty("Content-Type", "text/xml; charset=UTF-8");
		httpsConn
				.addRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		httpsConn.addRequestProperty("Host", "web.nciic.org.cn");
		// httpsConn.addRequestProperty("Content-Length", "340");
		httpsConn.addRequestProperty("DNT", "1");
		httpsConn.addRequestProperty("Connection", "Keep-Alive");
		httpsConn.addRequestProperty("Cache-Control", "no-cache");
		httpsConn
				.addRequestProperty(
						"Cookie",
						"crmLoginCookie=1; loginStyle=BlueTemplate; "
								+ "loginID=zjhjzjdx52878; loginPW=N11111; loginIDtmp=zjhjzjdx52878; "
								+ "fAvfGpbE26=" + fAvfGpbE26 + "; "
								+ "JSESSIONID=" + JSESSIONID);

		printRequestHeaders(httpsConn);

		// 连接
		httpsConn.connect();

		//写正文
		System.out.println(txtToString());
		httpsConn.getOutputStream().write(txtToString().getBytes());
		httpsConn.getOutputStream().flush();
		//httpsConn.getOutputStream().close();

		printResponseHeaders(httpsConn);

		InputStreamReader insr = new InputStreamReader(
				httpsConn.getInputStream(), "utf-8");
//		BufferedReader br = new BufferedReader(insr);
//		String line, str = "";
//		while ((line = br.readLine()) != null) {
//			str += line + "\n";
//		}
		int respInt = insr.read();
		System.out.println(respInt);
		while (respInt != -1) {
			System.out.print((char) respInt);
			respInt = insr.read();
		}
		
		//System.out.println(str);
		httpsConn.disconnect();
		
		System.out.println("###############查询阶段【结束】#############");
		
		return "";
	}

	public void doAuthProcess(Authinfo authinfo) throws Exception {
		String Auth_Content = authinfo.getAuth_Content();
		String fAvfGpbE26 = authinfo.getfAvfGpbE26();
		String JSESSIONID = authinfo.getJSESSIONID();

		if (Auth_Content == null || Auth_Content.trim().equals("")
				|| fAvfGpbE26 == null || fAvfGpbE26.trim().equals("")
				|| JSESSIONID == null || JSESSIONID.trim().equals("")) {
			System.out.println("认证原文不能为空!");
			return;
		}

		String DSign_Subject = "CN=NCIICCA,C=CN";
		ComThread.InitSTA();
		// String clsid = "707C7D52-85A8-4584-8954-573EFCE77488";
		String progid = "JITComVCTK.JITVCTK";
		ActiveXComponent axc = new ActiveXComponent(progid);
		Dispatch.callN(axc, "SetCertChooseType", new Object[] { 1 });
		Dispatch.callN(axc, "SetCert", new Object[] { "SC", "", "", "",
				DSign_Subject, "" });
		Variant value = Dispatch.call(axc, "GetErrorCode");
		int ret = value.changeType(Variant.VariantInt).getInt();
		if (ret != 0) {
			Variant errmsg_v = Dispatch.callN(axc, "GetErrorMessage",
					new Object[] { ret });
			System.out.println("[执行SetCertChooseType或SetCert错误]错误码：" + ret
					+ ", 错误信息："
					+ errmsg_v.changeType(Variant.VariantString).getString());
			return;
		}
		Variant temp_DSign_Result_v = Dispatch.callN(axc, "DetachSignStr",
				new Object[] { "", Auth_Content });
		value = Dispatch.call(axc, "GetErrorCode");
		ret = value.changeType(Variant.VariantInt).getInt();
		if (ret != 0) {
			Variant errmsg_v = Dispatch.callN(axc, "GetErrorMessage",
					new Object[] { ret });
			System.out.println("[执行DetachSignStr错误]错误码：" + ret + ", 错误信息："
					+ errmsg_v.changeType(Variant.VariantString).getString());
			return;
		}

		String temp_DSign_Result = temp_DSign_Result_v.changeType(
				Variant.VariantString).getString();
		System.out.println(temp_DSign_Result);
		System.out.println(URLEncoder.encode(temp_DSign_Result, "utf-8"));

		ComThread.Release();

		doAuthCore(authinfo, temp_DSign_Result);
	}

	private void doAuthCore(Authinfo authinfo, String temp_DSign_Result) throws Exception {
		System.out.println("###############认证阶段【开始】#############");
		String Auth_Content = authinfo.getAuth_Content();
		String fAvfGpbE26 = authinfo.getfAvfGpbE26();
		String JSESSIONID = authinfo.getJSESSIONID();

		if (Auth_Content == null || Auth_Content.trim().equals("")
				|| fAvfGpbE26 == null || fAvfGpbE26.trim().equals("")
				|| JSESSIONID == null || JSESSIONID.trim().equals("")) {
			System.out.println("认证原文不能为空!");
			return;
		}

		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, secureRandom);
		SSLSocketFactory ssf = sslContext.getSocketFactory();

		String strAction="https://web.nciic.org.cn/nciic_self/login.do";
		URL myURL = new URL(strAction);
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL
				.openConnection();
		httpsConn.setSSLSocketFactory(ssf);
		httpsConn.setRequestMethod("POST");
		httpsConn.setDoOutput(true);

		// 消息头
		httpsConn.addRequestProperty("Accept", "text/html, application/xhtml+xml, */*");
		httpsConn.addRequestProperty("Accept-Language", "zh-CN");
		httpsConn.addRequestProperty("Referer", "https://web.nciic.org.cn/nciic_self/");
		httpsConn.addRequestProperty("Accept-Encoding", "gzip, deflate");
		//httpsConn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpsConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		httpsConn
				.addRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		httpsConn.addRequestProperty("Host", "web.nciic.org.cn");
		// httpsConn.addRequestProperty("Content-Length", "340");
		httpsConn.addRequestProperty("DNT", "1");
		httpsConn.addRequestProperty("Connection", "Keep-Alive");
		httpsConn.addRequestProperty("Cache-Control", "no-cache");
		httpsConn
				.addRequestProperty(
						"Cookie",
						"crmLoginCookie=1; loginStyle=BlueTemplate; "
								+ "loginID=zjhjzjdx52878; loginPW=N11111; loginIDtmp=zjhjzjdx52878; "
								+ "fAvfGpbE26=" + fAvfGpbE26 + "; "
								+ "JSESSIONID=" + JSESSIONID);

		printRequestHeaders(httpsConn);

		// 连接
		httpsConn.connect();

		//写正文
		String paramstr = "method=crmlogin&username=zjhjzjdx52878" +
				"&password=&verifyCode=&loginStyle=BlueTemplate" +
				"&signed_data=" + URLEncoder.encode(temp_DSign_Result, "utf-8") +
				"&original_jsp=" + Auth_Content + 
				"&tempType=1&original=" + Auth_Content + "" +
				URLEncoder.encode("/", "utf-8");
	    httpsConn.getOutputStream().write(paramstr.getBytes());
		httpsConn.getOutputStream().flush();
		httpsConn.getOutputStream().close();

		printResponseHeaders(httpsConn);

		InputStreamReader insr = new InputStreamReader(
				httpsConn.getInputStream(), "GBK");
		BufferedReader br = new BufferedReader(insr);
		String line, str = "";
		while ((line = br.readLine()) != null) {
			str += line + "\n";
		}
		System.out.println(str);
		httpsConn.disconnect();
		
		System.out.println("###############认证阶段【结束】#############");
	}

	@Test
	public void testX509TrustManager() throws Exception {
		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, new SecureRandom());
		// 从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		// 创建URL对象
		// URL myURL = new
		// URL("https://ebanks.gdb.com.cn/sperbank/perbankLogin.jsp");
		// String strAction="https://web.nciic.org.cn/nciic_self/";
		String strAction = "https://web.nciic.org.cn/nciic_self/serv/ServicesParseAction.do?method=getResDetails";
		URL myURL = new URL(strAction);
		// 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL
				.openConnection();
		httpsConn.setSSLSocketFactory(ssf);
		httpsConn.setRequestMethod("POST");
		httpsConn.setDoOutput(true);

		// 消息头
		httpsConn.addRequestProperty("Accept", "*/*");
		httpsConn.addRequestProperty("Accept-Language", "zh-cn");
//		httpsConn.addRequestProperty("Referer",
//				"https://web.nciic.org.cn/nciic_self/"
//						+ "jsp/serv/services/services.jsp?servicesid=1191");
		httpsConn.addRequestProperty("Accept-Encoding", "gzip, deflate");
		httpsConn.addRequestProperty("Content-Type", "text/xml; charset=UTF-8");
		httpsConn
				.addRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		httpsConn.addRequestProperty("Host", "web.nciic.org.cn");
		// httpsConn.addRequestProperty("Content-Length", "340");
		httpsConn.addRequestProperty("DNT", "1");
		httpsConn.addRequestProperty("Connection", "Keep-Alive");
		httpsConn.addRequestProperty("Cache-Control", "no-cache");
		// username=zjhjzjdx52878
		httpsConn
				.addRequestProperty(
						"Cookie",
						"crmLoginCookie=1; loginStyle=BlueTemplate; "
								+ "loginID=zjhjzjdx52878; loginPW=N11111; loginIDtmp=zjhjzjdx52878; "
								+ "fAvfGpbE26=aV2MLtibLhGU; "
								+ "JSESSIONID=yKJvJJyLvCj7m4yVPJTfGfZ5GVy0xlM6y7HBj4hpMrzTnFgQdn6l!828069114");
		printRequestHeaders(httpsConn);
		// 连接
		httpsConn.connect();

		// 写参数
		// original_jsp=Auth_Content, original=Auth_Content
		// String paramstr = "method=crmlogin&username=&password=" +
		// "&verifyCode=&loginStyle=BlueTemplate&" +
		// "signed_data=&original_jsp=&tempType=1&original=wdikqz%2F";
		// byte[] b = paramstr.getBytes();
		// byte[] b = txtToString().getBytes();
		// String post_txt = "<?xml version=\"1.0\"?>"
		// +
		// "<ROWS recordsperpage=\"10\"><PREDICATE></PREDICATE><ROW><BA fwid=\"1191\" "
		// + "tsfwbs=\"\"></BA><CNS><CN><BM>JCXX_CZRK</BM><L> and </L><FN "
		// +
		// "sv=\"\">GMSFHM</FN><OPT>=</OPT><VAL>350524198803275596</VAL><FT>1</FT></CN>"
		// + "<CN><BM>JCXX_CZRK</BM><L> and </L><FN sv=\"\">XM</FN><OPT>=</OPT>"
		// + "<VAL>陈江</VAL><FT>3</FT></CN></CNS></ROW></ROWS>";
		// byte[] b = post_txt.getBytes();
		// httpsConn.getOutputStream().write(b, 0, b.length);
		httpsConn.getOutputStream().write(txtToString().getBytes());

		httpsConn.getOutputStream().flush();
		// httpsConn.getOutputStream().close();

		printResponseHeaders(httpsConn);
		// System.out.println("Set-Cookie: "
		// +httpsConn.getRequestProperty("Set-Cookie"));
		// System.out.println("Content-Type: " +
		// httpsConn.getRequestProperty("Content-Type"));

		// 取得该连接的输入流，以读取响应内容
		InputStreamReader insr = new InputStreamReader(
				httpsConn.getInputStream(), "utf-8");
		// 读取服务器的响应内容并显示
		int respInt = insr.read();
		System.out.println(respInt);
		while (respInt != -1) {
			System.out.print((char) respInt);
			respInt = insr.read();
		}
	}

	public Authinfo getEntryAuthinfo() throws Exception {
		System.out.println("###############入口阶段【开始】#############");
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, secureRandom);
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		String strAction = "https://web.nciic.org.cn/nciic_self/";
		URL myURL = new URL(strAction);
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL
				.openConnection();
		httpsConn.setSSLSocketFactory(ssf);
		httpsConn.setDoOutput(true);

		// 消息头
		httpsConn.addRequestProperty("Accept",
				"text/html, application/xhtml+xml, */*");
		httpsConn.addRequestProperty("Accept-Language", "zh-CN");
		httpsConn
				.addRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		httpsConn.addRequestProperty("Accept-Encoding", "gzip, deflate");
		httpsConn.addRequestProperty("Host", "web.nciic.org.cn");
		httpsConn.addRequestProperty("Content-Type", "text/xml; charset=UTF-8");

		httpsConn.addRequestProperty("Host", "web.nciic.org.cn");
		// httpsConn.addRequestProperty("Content-Length", "340");
		httpsConn.addRequestProperty("DNT", "1");
		httpsConn.addRequestProperty("Connection", "Keep-Alive");
		httpsConn.addRequestProperty("Cache-Control", "no-cache");
		httpsConn
				.addRequestProperty(
						"Cookie",
						"crmLoginCookie=1; loginStyle=BlueTemplate; "
								+ "loginID=zjhjzjdx52878; loginPW=N11111; loginIDtmp=zjhjzjdx52878");

		printRequestHeaders(httpsConn);
		// 连接
		httpsConn.connect();

		String reg = "JSESSIONID=([^;]+);\\s*path=";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher;
		Authinfo authinfo = new Authinfo();
		printResponseHeaders(httpsConn);
		// System.out.println("###" + httpsConn.getHeaderField("Set-Cookie") +
		// "###");
		List<String> cookieList = httpsConn.getHeaderFields().get("Set-Cookie");
		if (cookieList.size() >= 2) {
			// System.out.println(cookieList.get(0));
			matcher = pattern.matcher(cookieList.get(0));
			if (matcher.find()) {
				System.out.println("JSESSIONID: " + matcher.group(1));
				authinfo.setJSESSIONID(matcher.group(1).trim());
			}
			reg = "fAvfGpbE26=([^;]+);\\s*path=";
			pattern = Pattern.compile(reg);
			matcher = pattern.matcher(cookieList.get(1));
			if (matcher.find()) {
				System.out.println("fAvfGpbE26: " + matcher.group(1));
				authinfo.setfAvfGpbE26(matcher.group(1).trim());
			}
		}

		InputStreamReader insr = new InputStreamReader(
				httpsConn.getInputStream(), "GBK");
		BufferedReader br = new BufferedReader(insr);
		String line, str = "";
		while ((line = br.readLine()) != null) {
			str += line + "\n";
		}

		reg = "var\\s{1}Auth_Content\\s{1}=\\s{1}'([^']+)';";
		pattern = Pattern.compile(reg);
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			System.out.println("Auth_Content: " + matcher.group(1));
			authinfo.setAuth_Content(matcher.group(1).trim());
		}
		httpsConn.disconnect();
		System.out.println("###############入口阶段【结束】#############");

		return authinfo;
	}

	public String txtToString() throws Exception {
		String path = this.getClass().getResource("/").toURI().getPath();
		FileInputStream fis = new FileInputStream(path + "req.xml");
		InputStreamReader isr = new InputStreamReader(fis, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String line, str = "";
		int i = 0;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (!line.equals("")) {
				if (i++ == 0) {
					str += line + "\r\n";
				} else {
					str += line;
				}
			}
		}
		// System.out.println(str);
		return str;
	}

	public void printRequestHeaders(HttpsURLConnection httpsConn) {
		System.out.println("-----------请求头----------");
		Map<String, List<String>> reqheader_map = httpsConn
				.getRequestProperties();
		for (String key : reqheader_map.keySet()) {
			System.out.print(key + ": ");
			List<String> hlist = reqheader_map.get(key);
			for (int i = 0; i < hlist.size(); i++) {
				System.out.println(hlist.get(i));
			}
		}
	}

	public void printResponseHeaders(HttpsURLConnection httpsConn) {
		System.out.println("-----------响应头----------");
		Map<String, List<String>> hmaps = httpsConn.getHeaderFields();
		for (String key : hmaps.keySet()) {
			System.out.print(key + ": ");
			List<String> hlist = hmaps.get(key);
			for (int i = 0; i < hlist.size(); i++) {
				System.out.println(hlist.get(i));
			}
		}
	}
}
