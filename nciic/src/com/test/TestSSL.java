package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.xml.sax.InputSource;

import com.csrc.nciic.model.Authinfo;
import com.csrc.nciic.model.SameAddressParam;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

@SuppressWarnings("unused")
public class TestSSL {
	SecureRandom secureRandom = new SecureRandom();

	@Test
	public void t01() throws Exception {
		//System.out.println(System.getProperty("java.library.path"));
		String path = this.getClass().getResource("/").toURI().getPath()
				+ "resp2.xml";
		System.out.println(formatToXml(txtToString(path, "gbk")));
	}
	
	public String formatToXml(String str) throws Exception {
		if (str == null || str.trim().equals("")) return null;
		
		str = str.replaceAll("&gt;", ">").replaceAll("&lt;", "<");
		
		String reg = "(<img\\s+[^>]*?)>";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1) + "/>");
		}
		matcher.appendTail(sb);
		
		
		reg = "(<INPUT.*?checkResult=)([^\\s]+)(\\s+/>)";
		pattern = Pattern.compile(reg);
		matcher = pattern.matcher(sb.toString());
		sb.delete(0, sb.length());
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1) + "\"" + 
					matcher.group(2) + "\"" + matcher.group(3));
		}
		matcher.appendTail(sb);
		//System.out.println(sb.toString());
		
		return sb.toString();
	}

	@Test
	public void tNciic() throws Exception {
		// String name = "高宝玉";
		// String idennumber = "21080419541008101x";

		// String name = "杨友占"; //4个同地址
		// String idennumber = "130221195112224610";

		String name = "陈江";
		String idennumber = "350524198903275596";

		Authinfo authinfo = getEntryAuthinfo();
		boolean auth_success = doAuthProcess(authinfo);
		if (!auth_success) {
			System.out.println("认证失败");
			return;
		}
		String resp = doIdentifyQuery(authinfo, name, idennumber);
		
		SameAddressParam sameAddressParam = new SameAddressParam();
		
		String reg = "\\([^\\)]*?\\)";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher;
		SAXReader reader = new SAXReader();
		Document doc;
		try {
			StringReader sr = new StringReader(resp); 
			InputSource is = new InputSource(sr); 
			doc = reader.read(is);
			Element rootElement = doc.getRootElement();
			Node fontElement = rootElement
					.selectSingleNode("//font[@style='cursor:pointer']");
			//Node inputElement = rootElement.selectSingleNode("//INPUT[@checkResult]");
			matcher = pattern.matcher(fontElement.asXML());
			if (matcher.find()) {
				String[] params = matcher.group(1).split(",");
				if (params.length == 5) {
					params[0] =params[0].trim();
					int endidx = params[0].length() - 1;
					sameAddressParam.setPk(params[0].substring(1, endidx));
					
					params[1] =params[1].trim();
					endidx = params[1].length() - 1;
					sameAddressParam.setVal(params[1].substring(1, endidx));
					
					params[2] =params[2].trim();
					endidx = params[2].length() - 1;
					sameAddressParam.setBm(params[2].substring(1, endidx));
					
					params[3] =params[3].trim();
					endidx = params[3].length() - 1;
					sameAddressParam.setId(params[3].substring(1, endidx));
					
					System.out.println(params[4]);
					params[4] =params[4].trim();
					endidx = params[4].length() - 1;
					sameAddressParam.setSd(params[4].substring(1, endidx));
					
					System.out.println(sameAddressParam);
					
				} else {
					System.out.println("同地址参数个数错误！！");
				}
 				System.out.println(matcher.group(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		getSameAddressInfo(authinfo, sameAddressParam, name, idennumber);
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

		String strAction = "https://web.nciic.org.cn/nciic_self/jsp/serv/services/services.jsp?servicesid=1191";
		URL myURL = new URL(strAction);
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL
				.openConnection();
		httpsConn.setSSLSocketFactory(ssf);
		httpsConn.setDoOutput(true);

		// 消息头
		httpsConn.addRequestProperty("Accept",
				"text/html, application/xhtml+xml, */*");
		httpsConn.addRequestProperty("Accept-Language", "zh-cn");
		// httpsConn.addRequestProperty("Referer",
		// "https://web.nciic.org.cn/nciic_self/"
		// + "jsp/serv/services/services.jsp?servicesid=1191");
		httpsConn.addRequestProperty("Accept-Encoding", "gzip, deflate");
		// httpsConn.addRequestProperty("Content-Type",
		// "text/xml; charset=UTF-8");
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
		// BufferedReader br = new BufferedReader(insr);
		// String line, str = "";
		// while ((line = br.readLine()) != null) {
		// str += line + "\n";
		// }
		int respInt = insr.read();
		System.out.println(respInt);
		int n_bytes = 0;
		while (respInt != -1) {
			// System.out.print((char) respInt);
			respInt = insr.read();
			n_bytes++;
		}
		System.out.println("n_bytes: " + n_bytes);
		// System.out.println(str);
		httpsConn.disconnect();

		System.out.println("###############GetSrviceJsp【结束】#############");
	}

	private String doIdentifyQuery(Authinfo authinfo, String name,
			String idennumber) throws Exception {
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
		if (name == null || name.trim().equals("") || idennumber == null
				|| idennumber.trim().equals("")) {
			System.out.println("字段信息不能为空!");
			return null;
		}

		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, secureRandom);
		SSLSocketFactory ssf = sslContext.getSocketFactory();

		String strAction = "https://web.nciic.org.cn/nciic_self/serv/ServicesParseAction.do?method=getResDetails";
		URL myURL = new URL(strAction);
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL
				.openConnection();
		httpsConn.setSSLSocketFactory(ssf);
		httpsConn.setRequestMethod("POST");
		httpsConn.setDoOutput(true);

		// 消息头
		httpsConn.addRequestProperty("Accept", "*/*");
		httpsConn.addRequestProperty("Accept-Language", "zh-cn");
		// httpsConn.addRequestProperty("Referer",
		// "https://web.nciic.org.cn/nciic_self/"
		// + "jsp/serv/services/services.jsp?servicesid=1191");
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

		// 写正文
		String xml_str = "<?xml version=\"1.0\"?><ROWS recordsperpage=\"10\"><PREDICATE></PREDICATE><ROW><BA fwid=\"1191\" "
				+ "tsfwbs=\"\"></BA><CNS><CN><BM>JCXX_CZRK</BM><L> and </L><FN sv=\"\">GMSFHM</FN><OPT>=</OPT><VAL>"
				+ idennumber
				+ "</VAL><FT>1</FT></CN><CN>"
				+ "<BM>JCXX_CZRK</BM><L> and </L><FN sv=\"\">XM</FN><OPT>=</OPT>"
				+ "<VAL>" + name + "</VAL><FT>3</FT></CN></CNS></ROW></ROWS>";
		httpsConn.getOutputStream().write(xml_str.getBytes());
		httpsConn.getOutputStream().flush();
		// httpsConn.getOutputStream().close();

		printResponseHeaders(httpsConn);

		InputStreamReader insr = new InputStreamReader(
				httpsConn.getInputStream(), "utf-8");
		BufferedReader br = new BufferedReader(insr);
		String line, str = "";
		while ((line = br.readLine()) != null) {
			str += line + "\n";
		}
		System.out.println(str);
		
		str = formatToXml(str);
		System.out.println(str);

		// int respInt = insr.read();
		// while (respInt != -1) {
		// System.out.print((char) respInt);
		// respInt = insr.read();
		// }

		httpsConn.disconnect();
		System.out.println("###############查询阶段【结束】#############");

		return str;
	}

	private String getSameAddressInfo(Authinfo authinfo,
			SameAddressParam sameAddressParam, String name, String idennumber)
			throws Exception {
		System.out.println("###############获取同地址阶段【开始】#############");
		String Auth_Content = authinfo.getAuth_Content();
		String fAvfGpbE26 = authinfo.getfAvfGpbE26();
		String JSESSIONID = authinfo.getJSESSIONID();
		if (Auth_Content == null || Auth_Content.trim().equals("")
				|| fAvfGpbE26 == null || fAvfGpbE26.trim().equals("")
				|| JSESSIONID == null || JSESSIONID.trim().equals("")) {
			System.out.println("认证原文不能为空!");
			return null;
		}
		if (sameAddressParam == null || sameAddressParam.is_empty()) {
			System.out.println("同地址参数信息不能为空!");
			return null;
		}
		String pk = sameAddressParam.getPk().trim();
		String val = sameAddressParam.getVal().trim();
		String bm = sameAddressParam.getBm().trim();
		String id = sameAddressParam.getId().trim();
		String sd = sameAddressParam.getSd().trim();

		if (name == null || name.trim().equals("") || idennumber == null
				|| idennumber.trim().equals("")) {
			System.out.println("字段信息不能为空!");
			return null;
		}

		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, secureRandom);
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		String strAction = "https://web.nciic.org.cn/nciic_self/jsp/serv/services/rela.jsp?PK="
				+ pk + "&VAL=" + val + "&BM=" + bm + "&ID=" + id + "&SD=" + sd;
		URL myURL = new URL(strAction);
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL
				.openConnection();
		httpsConn.setSSLSocketFactory(ssf);
		httpsConn.setDoOutput(true);

		// 消息头
		httpsConn.addRequestProperty("Accept", "*/*");
		httpsConn.addRequestProperty("Accept-Language", "zh-cn");
		// httpsConn.addRequestProperty("Referer",
		// "https://web.nciic.org.cn/nciic_self/"
		// + "jsp/serv/services/services.jsp?servicesid=1191");
		httpsConn.addRequestProperty("Accept-Encoding", "gzip, deflate");
		// httpsConn.addRequestProperty("Content-Type",
		// "text/xml; charset=UTF-8");
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
				httpsConn.getInputStream(), "utf-8");
		BufferedReader br = new BufferedReader(insr);
		String line, str = "";
		while ((line = br.readLine()) != null) {
			str += line + "\n";
		}
		System.out.println(str);

		// int respInt = insr.read();
		// while (respInt != -1) {
		// System.out.print((char) respInt);
		// respInt = insr.read();
		// }

		httpsConn.disconnect();
		System.out.println("###############查询阶段【结束】#############");

		return str;
	}

	public boolean doAuthProcess(Authinfo authinfo) throws Exception {
		String Auth_Content = authinfo.getAuth_Content();
		String fAvfGpbE26 = authinfo.getfAvfGpbE26();
		String JSESSIONID = authinfo.getJSESSIONID();

		if (Auth_Content == null || Auth_Content.trim().equals("")
				|| fAvfGpbE26 == null || fAvfGpbE26.trim().equals("")
				|| JSESSIONID == null || JSESSIONID.trim().equals("")) {
			System.out.println("认证原文不能为空!");
			return false;
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
			return false;
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
			return false;
		}

		String temp_DSign_Result = temp_DSign_Result_v.changeType(
				Variant.VariantString).getString();
		System.out.println(temp_DSign_Result);
		System.out.println(URLEncoder.encode(temp_DSign_Result, "utf-8"));

		ComThread.Release();

		return doAuthCore(authinfo, temp_DSign_Result);
	}

	private boolean doAuthCore(Authinfo authinfo, String temp_DSign_Result)
			throws Exception {
		System.out.println("###############认证阶段【开始】#############");
		String Auth_Content = authinfo.getAuth_Content();
		String fAvfGpbE26 = authinfo.getfAvfGpbE26();
		String JSESSIONID = authinfo.getJSESSIONID();

		if (Auth_Content == null || Auth_Content.trim().equals("")
				|| fAvfGpbE26 == null || fAvfGpbE26.trim().equals("")
				|| JSESSIONID == null || JSESSIONID.trim().equals("")) {
			System.out.println("认证原文不能为空!");
			return false;
		}

		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, secureRandom);
		SSLSocketFactory ssf = sslContext.getSocketFactory();

		String strAction = "https://web.nciic.org.cn/nciic_self/login.do";
		URL myURL = new URL(strAction);
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL
				.openConnection();
		httpsConn.setSSLSocketFactory(ssf);
		httpsConn.setRequestMethod("POST");
		httpsConn.setDoOutput(true);

		// 消息头
		httpsConn.addRequestProperty("Accept",
				"text/html, application/xhtml+xml, */*");
		httpsConn.addRequestProperty("Accept-Language", "zh-CN");
		httpsConn.addRequestProperty("Referer",
				"https://web.nciic.org.cn/nciic_self/");
		httpsConn.addRequestProperty("Accept-Encoding", "gzip, deflate");
		// httpsConn.addRequestProperty("Content-Type",
		// "application/x-www-form-urlencoded");
		httpsConn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
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

		// 写正文
		String paramstr = "method=crmlogin&username=zjhjzjdx52878"
				+ "&password=&verifyCode=&loginStyle=BlueTemplate"
				+ "&signed_data="
				+ URLEncoder.encode(temp_DSign_Result, "utf-8")
				+ "&original_jsp=" + URLEncoder.encode(Auth_Content, "utf-8")
				+ "&tempType=1&original="
				+ URLEncoder.encode(Auth_Content + "/", "utf-8");
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
		httpsConn.disconnect();
		System.out.println("###############认证阶段【结束】#############");

		String reg = "</noframes>";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		if (!matcher.find()) {
			System.out.println("$$$$认证失败！！！");
			return false;
		}

		return true;
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
		// httpsConn.addRequestProperty("Referer",
		// "https://web.nciic.org.cn/nciic_self/"
		// + "jsp/serv/services/services.jsp?servicesid=1191");
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
		String path = this.getClass().getResource("/").toURI().getPath()
				+ "req.xml";
		httpsConn.getOutputStream().write(txtToString(path, "utf-8").getBytes());

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

	@Test
	public void readXmlFile() throws Exception {
		String reg = "\\(([^\\)]*?)\\)";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher;
		
		String path = this.getClass().getResource("/").toURI().getPath();
		SAXReader reader = new SAXReader();
		Document doc;
		try {
			doc = reader.read(new File(path + "resp.xml"));
			Element rootElement = doc.getRootElement();
			Node fontElement = rootElement
					.selectSingleNode("//font[@style='cursor:pointer']");
			Node inputElement = rootElement
					.selectSingleNode("//INPUT[@checkResult]");
			System.out.println(fontElement.asXML());
			System.out.println(inputElement.asXML());
			matcher = pattern.matcher(fontElement.asXML());
			if (matcher.find()) {
				String[] params = matcher.group(1).split(",");
				if (params.length == 5) {
					SameAddressParam sameAddressParam = new SameAddressParam();
					params[0] =params[0].trim();
					int endidx = params[0].length() - 1;
					sameAddressParam.setPk(params[0].substring(1, endidx));
					
					params[1] =params[1].trim();
					endidx = params[1].length() - 1;
					sameAddressParam.setVal(params[1].substring(1, endidx));
					
					params[2] =params[2].trim();
					endidx = params[2].length() - 1;
					sameAddressParam.setBm(params[2].substring(1, endidx));
					
					params[3] =params[3].trim();
					endidx = params[3].length() - 1;
					sameAddressParam.setId(params[3].substring(1, endidx));
					
					System.out.println(params[4]);
					params[4] =params[4].trim();
					endidx = params[4].length() - 1;
					sameAddressParam.setSd(params[4].substring(1, endidx));
					
					System.out.println(sameAddressParam);
					
				} else {
					System.out.println("同地址参数个数错误！！");
				}
 				System.out.println(matcher.group(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public String txtToString(String path, String encode) throws Exception {
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, encode);
		BufferedReader br = new BufferedReader(isr);
		String line, str = "";
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (!line.equals("")) {
				str += line + "\n";
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
