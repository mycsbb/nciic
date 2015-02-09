package com.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.xml.sax.InputSource;

import com.csrc.nciic.model.Authinfo;
import com.csrc.nciic.model.IdenDetailInfo;
import com.csrc.nciic.model.IdentifyInfo;
import com.csrc.nciic.model.SameAddressParam;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

@SuppressWarnings({ "unused", "unchecked" })
public class TestSSL {
	SecureRandom secureRandom = new SecureRandom();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String outfile = "e:/out" + sdf.format(new Date()) + ".xls";
	int cur_row_index = 0;

	// POIFSFileSystem fs = new POIFSFileSystem(is);

	@Test
	public void t01() throws Exception {
		String str = new String("  hjl	   hhh");
		String[] li = str.split("\\s+");
		System.out.println(li[0] + "$$" + li[1]);

		str = "&amp;";
		System.out.println(str.replaceAll("&amp;", "##"));

		List<IdentifyInfo> inputIdenList = new ArrayList<IdentifyInfo>();
		String input_path = this.getClass().getResource("/").toURI().getPath()
				+ "input.list";
		FileInputStream fis = new FileInputStream(input_path);
		InputStreamReader isr = new InputStreamReader(fis, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (!line.equals("")) {
				String[] strs = line.split("\\s+");
				if (strs.length >= 2) {
					IdentifyInfo identifyInfo = new IdentifyInfo(strs[1],
							strs[0]);
					inputIdenList.add(identifyInfo);
				}
			}
		}
		System.out.println(inputIdenList);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse("2008-07-10");
		System.out.println(date);
		// String name = "陈江";
		// String idennumber = "350524198903275596";
		String zh_name = "高宝玉";
		String identify_no = "21080419541008101X";
		// System.out.println(System.getProperty("java.library.path"));

		IdentifyInfo identifyInfo = new IdentifyInfo(identify_no, zh_name);
		String path = this.getClass().getResource("/").toURI().getPath()
				+ "resp2.xml";
		String resp = formatToXml(txtToString(path, "gbk"));
		// System.out.println(isQuerySuccess(resp));

		// Authinfo authinfo = getEntryAuthinfo();
		// boolean auth_success = doAuthProcess(authinfo);
		// if (!auth_success) {
		// System.out.println("认证失败");
		// return;
		// }
		// SameAddressParam sameAddressParam = getSameAddressParam(resp);
		// String sai_resp = getSameAddressInfo(authinfo, sameAddressParam);
		// String sai_resp = getSameAddressInfo(authinfo, sameAddressParam);
		// List<IdentifyInfo> list = parseXmlForIdenList(resp, identifyInfo);
		// System.out.println(resp);
		// doPersistence(resp);
		// authinfo.setAuth_Content("joze0l");
		// authinfo.setfAvfGpbE26("RpA7jcXNjifV");
		// authinfo.setJSESSIONID("Yf2DJYSQCC5Q9S2F210x9cJJvFqp9zLWTJqv2pF9VWnmNNJw8yZW!339253687");
		// getNciicImg_2(authinfo);
		IdenDetailInfo detailInfo =  xmlToIdenDetailInfo(resp);
		TestPdf test = new TestPdf();
		test.ideninfoToPdf(detailInfo);
	}

	@Test
	public void tNciic() throws Exception {
		String zh_name = "高宝玉";
		String identify_no = "21080419541008101x";
		// String zh_name = "陈晓坚";
		// String identify_no = "445281198904120139";
		// String zh_name = "蔡佳芬";
		// String identify_no = "445281198510140022";
		// String name = "杨友占"; //4个同地址
		// String idennumber = "130221195112224610";
		// String name = "陈江";
		// String idennumber = "350524198903275596";

		List<IdentifyInfo> inputIdenList = new ArrayList<IdentifyInfo>();
		String input_path = this.getClass().getResource("/").toURI().getPath()
				+ "input.list";
		FileInputStream fis = new FileInputStream(input_path);
		InputStreamReader isr = new InputStreamReader(fis, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String line, str = "";
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (!line.equals("")) {
				String[] strs = line.split("\\s+");
				if (strs.length >= 2) {
					IdentifyInfo identifyInfo = new IdentifyInfo(strs[1],
							strs[0]);
					// inputIdenList.add(identifyInfo);
				}
			}
		}
		inputIdenList.add(new IdentifyInfo(identify_no, zh_name));
		List<IdentifyInfo> total_sa_denList = new ArrayList<IdentifyInfo>();

		Authinfo authinfo = getEntryAuthinfo();
		boolean auth_success = doAuthProcess(authinfo);
		if (!auth_success) {
			System.out.println("认证失败");
			return;
		}
		//doIdentifyQuery(authinfo, new IdentifyInfo(identify_no, zh_name), true);
		Iterator<IdentifyInfo> iter = inputIdenList.iterator();
		while (iter.hasNext()) {
			IdentifyInfo iden_info = iter.next();
			boolean is_contain = isContain(total_sa_denList, iden_info);
			if (is_contain)
				continue;
			List<IdentifyInfo> sa_denList = doIdentifyQuery(authinfo,
					iden_info, true);
			if (sa_denList != null) {
				total_sa_denList.addAll(sa_denList);
			} else {
				System.out.println("sa_denList为null");
			}
			
		}
		/*
		 * String resp = doQueryCore(authinfo, zh_name, identify_no);
		 * 
		 * resp = formatToXml(resp); System.out.println(resp); if
		 * (!isQuerySuccess(resp)) { System.out.println("查询失败！"); return; }
		 * 
		 * SameAddressParam sameAddressParam = getSameAddressParam(resp);
		 * 
		 * String sai_resp = getSameAddressInfo(authinfo, sameAddressParam,
		 * zh_name, identify_no);
		 */
	}

	private boolean isContain(List<IdentifyInfo> total_sa_denList,
			IdentifyInfo iden_info) {
		if (total_sa_denList == null || iden_info == null)
			return false;

		for (int i = 0; i < total_sa_denList.size(); i++) {
			if (total_sa_denList.get(i).getIdentify_no()
					.equalsIgnoreCase(iden_info.getIdentify_no())) {
				return true;
			}
		}
		return false;
	}

	public boolean isQuerySuccess(String resp) {
		if (resp == null || resp.trim().equals("")) {
			System.out.println("【判断查询是否成功】输入报文不能为空！");
			return false;
		}

		String reg = "(<img\\s+[^>]*?)>";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(resp);
		while (!matcher.find()) {
			return false;
		}

		return true;
	}

	public String formatToXml(String str) throws Exception {
		if (str == null || str.trim().equals(""))
			return null;

		str = str.replaceAll("&gt;", ">").replaceAll("&lt;", "<");
		str = str.replaceAll("&nbsp;", "").replaceAll("nbsp;", "");

		String reg = "(<img\\s+[^>]*?)>";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1) + "/>");
		}
		matcher.appendTail(sb);

		reg = "(<INPUT.*?checkResult=)([^\\s]+)(\\s*/>)";
		pattern = Pattern.compile(reg);
		matcher = pattern.matcher(sb.toString());
		sb.delete(0, sb.length());
		while (matcher.find()) {
			matcher.appendReplacement(
					sb,
					matcher.group(1) + "\"" + matcher.group(2) + "\""
							+ matcher.group(3));
		}
		matcher.appendTail(sb);
		System.out.println(sb.toString());

		return sb.toString();
	}

	private SameAddressParam getSameAddressParam(String resp) {
		if (resp == null || resp.trim().equals("")) {
			System.out.println("要解析的报文不能为空！");
			return null;
		}

		SameAddressParam sameAddressParam = new SameAddressParam();
		String reg = "\\(([^\\)]*?)\\)";
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
			if (fontElement == null) {
				System.out.println("【出错】font节点为空！！");
				return null;
			}
			// Node inputElement =
			// rootElement.selectSingleNode("//INPUT[@checkResult]");
			matcher = pattern.matcher(fontElement.asXML());
			if (matcher.find()) {
				System.out.println(matcher.group(1));
				String[] params = matcher.group(1).split(",");
				if (params.length == 5) {
					params[0] = params[0].trim();
					int endidx = params[0].length() - 1;
					sameAddressParam.setPk(params[0].substring(1, endidx));

					params[1] = params[1].trim();
					endidx = params[1].length() - 1;
					sameAddressParam.setVal(params[1].substring(1, endidx));

					params[2] = params[2].trim();
					endidx = params[2].length() - 1;
					sameAddressParam.setBm(params[2].substring(1, endidx));

					params[3] = params[3].trim();
					endidx = params[3].length() - 1;
					sameAddressParam.setId(params[3].substring(1, endidx));

					params[4] = params[4].trim();
					endidx = params[4].length() - 1;
					sameAddressParam.setSd(params[4].substring(1, endidx));

					System.out.println(sameAddressParam);
					return sameAddressParam;
				} else {
					System.out.println("同地址参数个数错误！！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public List<IdentifyInfo> doIdentifyQuery(Authinfo authinfo,
			IdentifyInfo iden_info, boolean withsameaddr) throws Exception {
		System.out.println("###############doIdentifyQuery【开始】#############");
		String Auth_Content = authinfo.getAuth_Content();
		String fAvfGpbE26 = authinfo.getfAvfGpbE26();
		String JSESSIONID = authinfo.getJSESSIONID();
		if (Auth_Content == null || Auth_Content.trim().equals("")
				|| fAvfGpbE26 == null || fAvfGpbE26.trim().equals("")
				|| JSESSIONID == null || JSESSIONID.trim().equals("")) {
			System.out.println("认证原文不能为空!");
			return null;
		}

		if (iden_info == null || iden_info.isEmpty()) {
			System.out.println("字段信息不能为空!");
			return null;
		}

		String resp = doQueryCore(authinfo, iden_info);
		resp = formatToXml(resp);
		System.out.println(resp);
		if (!isQuerySuccess(resp)) {
			System.out.println("查询失败！");
			return null;
		}

		//doPersistence(resp, authinfo);
		IdenDetailInfo detailInfo = xmlToIdenDetailInfo(resp);
		if (detailInfo != null && !detailInfo.isEmpty()) {
			objToExcel(authinfo, detailInfo, outfile);
		}

		if (withsameaddr) {
			SameAddressParam sameAddressParam = getSameAddressParam(resp);
			String sai_resp = getSameAddressInfo(authinfo, sameAddressParam);
			List<IdentifyInfo> idenList = parseXmlForIdenList(sai_resp,
					iden_info);
			List<SameAddressParam> sapList = parseXmlForSapList(sai_resp,
					iden_info);
			for (int i = 0; i < sapList.size(); i++) {
				resp = getRelation(authinfo, sapList.get(i));
				resp = formatToXml(resp);
				System.out.println(resp);
				if (!isQuerySuccess(resp)) {
					System.out.println("查询relation失败！");
					return null;
				}
				detailInfo = xmlToIdenDetailInfo(resp);
				if (detailInfo != null && !detailInfo.isEmpty()) {
					objToExcel(authinfo, detailInfo, outfile);
				}
			}
			System.out
					.println("###############doIdentifyQuery【结束】#############");

			return idenList;
		}

		System.out.println("###############doIdentifyQuery【结束】#############");
		return null;
	}

	private List<SameAddressParam> parseXmlForSapList(String sai_resp,
			IdentifyInfo identifyInfo) {
		if (sai_resp == null || sai_resp.trim().equals("")
				|| identifyInfo == null || identifyInfo.isEmpty()) {
			System.out.println("parseXmlForSapList参数输入不能为空");
			return null;
		}

		List<SameAddressParam> sapList = new ArrayList<SameAddressParam>();
		String pk = "";
		String bm = "";
		String id = "";
		String sd = "";

		SAXReader reader = new SAXReader();
		Document doc;
		try {
			StringReader sr = new StringReader(sai_resp);
			InputSource is = new InputSource(sr);
			doc = reader.read(is);
			Element rootElement = doc.getRootElement();

			Node rcnNode = rootElement.selectSingleNode("RESULT/RCN");
			if (rcnNode == null) {
				System.out.println("rcnNode为null");
				return null;
			}

			Node pkNode = rcnNode.selectSingleNode("PK");
			if (pkNode == null) {
				System.out.println("pkNode为null");
				return null;
			}
			pk = pkNode.getText().trim();
			Node bmNode = rcnNode.selectSingleNode("BM");
			if (bmNode == null) {
				System.out.println("bmNode为null");
				return null;
			}
			bm = bmNode.getText().trim();
			Node idNode = rcnNode.selectSingleNode("ID");
			if (idNode == null) {
				System.out.println("idNode为null");
				return null;
			}
			id = idNode.getText().trim();
			Node sdNode = rcnNode.selectSingleNode("SD");
			if (sdNode == null) {
				System.out.println("sdNode为null");
				return null;
			}
			sd = sdNode.getText().trim();

			List<Node> rowNodes = rootElement.selectNodes("RESULT/ROW");
			for (int i = 0; i < rowNodes.size(); i++) {
				IdentifyInfo idenif = new IdentifyInfo();
				Node idenno_node = rowNodes.get(i).selectSingleNode("GMSFHM");
				if (idenno_node == null
						|| idenno_node.getText().trim().equals("")
						|| idenno_node
								.getText()
								.trim()
								.equalsIgnoreCase(
										identifyInfo.getIdentify_no().trim()))
					continue;
				SameAddressParam sap = new SameAddressParam();
				sap.setPk(pk);
				sap.setBm(bm);
				sap.setId(id);
				sap.setSd(sd);
				Node val_node = rowNodes.get(i).selectSingleNode("VAL");
				if (val_node == null || val_node.getText().trim().equals("")) {
					continue;
				}
				sap.setVal(val_node.getText().trim());
				sapList.add(sap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return sapList;
	}

	private List<IdentifyInfo> parseXmlForIdenList(String sai_resp,
			IdentifyInfo identifyInfo) {
		if (sai_resp == null || sai_resp.trim().equals("")
				|| identifyInfo == null || identifyInfo.isEmpty()) {
			System.out.println("parseXmlForIdenList参数输入不能为空");
			return null;
		}

		List<IdentifyInfo> idenList = new ArrayList<IdentifyInfo>();

		SAXReader reader = new SAXReader();
		Document doc;
		try {
			StringReader sr = new StringReader(sai_resp);
			InputSource is = new InputSource(sr);
			doc = reader.read(is);
			Element rootElement = doc.getRootElement();

			List<Node> rowNodes = rootElement.selectNodes("RESULT/ROW");

			for (int i = 0; i < rowNodes.size(); i++) {
				IdentifyInfo idenif = new IdentifyInfo();
				Node idenno_node = rowNodes.get(i).selectSingleNode("GMSFHM");
				if (idenno_node == null
						|| idenno_node.getText().trim().equals("")
						|| idenno_node
								.getText()
								.trim()
								.equalsIgnoreCase(
										identifyInfo.getIdentify_no().trim()))
					continue;
				idenif.setIdentify_no(idenno_node.getText().trim());

				Node xm_node = rowNodes.get(i).selectSingleNode("XM");
				if (xm_node == null || xm_node.getText().trim().equals(""))
					continue;
				idenif.setZh_name(xm_node.getText().trim());
				idenList.add(idenif);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return idenList;
	}

	public IdenDetailInfo xmlToIdenDetailInfo(String resp) {
		System.out.println("###############doPersistence【开始】#############");
		if (resp == null || resp.trim().equals("")) {
			System.out.println("【doPersistence】输入不给为空！");
			return null;
		}

		IdenDetailInfo detailInfo = new IdenDetailInfo();

		SAXReader reader = new SAXReader();
		Document doc;
		try {
			StringReader sr = new StringReader(resp);
			InputSource is = new InputSource(sr);
			doc = reader.read(is);
			Element rootElement = doc.getRootElement();

			Node tableNode = rootElement
					.selectSingleNode("RESULT/ROW/TABLE/TR/TD/TABLE");
			if (tableNode == null) {
				System.out.println("tableNodes为空！");
				return null;
			}
			List<Node> trNodes = tableNode.selectNodes("TR");
			String str;
			for (int i = 0; i < trNodes.size(); i++) {
				List<Node> td_nodes = trNodes.get(i).selectNodes("TD");
				if (td_nodes.size() < 2) {
					System.out.println("【doPersistence】TD个数小于2！");
					return null;
				}
				String item_label = td_nodes.get(0).getText().trim();
				if (item_label.equals("公民身份号码")) {
					str = td_nodes.get(1).getText().trim();
					str = str.replaceAll("&amp;", "").replaceAll("amp;", "");
					detailInfo.setIdentify_no(str);
				} else if (item_label.equals("姓名")) {
					detailInfo.setZh_name(td_nodes.get(1).getText().trim());
				} else if (item_label.equals("曾用名")) {
					System.out.println(td_nodes.get(1).asXML());
					str = td_nodes.get(1).getText().trim();
					System.out.println("曾用名：");
					System.out.println(str);
					str = str.replaceAll("&amp;", "").replaceAll("amp;", "");
					System.out.println(str);
					str = str.replaceAll("&nbsp;", "").replaceAll("nbsp;", "");
					System.out.println(str);
					if (str.startsWith("&")) {
						detailInfo.setUsed_name("");
					} else {
						detailInfo.setUsed_name(str);
					}
				} else if (item_label.equals("性别")) {
					detailInfo.setSex(td_nodes.get(1).getText().trim());
				} else if (item_label.equals("民族")) {
					detailInfo.setNation(td_nodes.get(1).getText().trim());
				} else if (item_label.equals("出生日期")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = sdf.parse(td_nodes.get(1).getText().trim());
					detailInfo.setBirth(date);
				} else if (item_label.equals("数据归属单位代码")) {
					detailInfo.setBelonged_unit(td_nodes.get(1).getText()
							.trim());
				} else if (item_label.equals("所属省市县（区）")) {
					detailInfo.setBelonged_county(td_nodes.get(1).getText()
							.trim());
				} else if (item_label.equals("籍贯省市县（区）")) {
					detailInfo
							.setOrigin_place(td_nodes.get(1).getText().trim());
				} else if (item_label.equals("住址")) {
					detailInfo.setAddress(td_nodes.get(1).getText().trim());
				} else if (item_label.equals("服务处所")) {
					detailInfo.setService_place(td_nodes.get(1).getText()
							.trim());
				} else if (item_label.equals("婚姻状况")) {
					detailInfo
							.setMarry_status(td_nodes.get(1).getText().trim());
				} else if (item_label.equals("文化程度")) {
					detailInfo.setEducation(td_nodes.get(1).getText().trim());
				} else if (item_label.equals("相片")) {
					String xml = td_nodes.get(1).asXML();
					String reg = "src=\"([^\"]+?)\"";
					Pattern pattern = Pattern.compile(reg);
					Matcher matcher = pattern.matcher(xml);
					if (matcher.find()) {
						System.out.println("#########" + matcher.group(1));
						String img_url = "https://web.nciic.org.cn"
								+ matcher.group(1).replace("&amp;", "&");
						System.out.println("img_url: " + img_url);
						detailInfo.setImg(img_url);
					} else {
						System.out.println("img_url_not_found");
					}
				}
			}
			System.out.println(detailInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		System.out.println("###############doPersistence【结束】#############");
		return detailInfo;
	}

	private void objToExcel(Authinfo authinfo, IdenDetailInfo detailInfo,
			String outpath) throws IOException {
		if (detailInfo == null || outpath == null || outpath.trim().equals("")) {
			System.out.println("[objToExcel]参数错误！");
			return;
		}

		File outFile = new File(outpath);
		if (outFile.exists() && !outFile.isFile()) {
			System.out.println("输入文件不是普通文件！");
			return;
		}

		POIFSFileSystem ps;
		HSSFWorkbook wb;
		HSSFSheet sheet;
		HSSFRow row;
		HSSFCell c;
		CellStyle cs;
		FileInputStream fs = null;
		FileOutputStream fos = null;
		HSSFPatriarch patriarch;
		HSSFClientAnchor anchor;

		// ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		// BufferedImage bufferImg = ImageIO.read(new File("e:/000.jpg"));
		// ImageIO.write(bufferImg, "jpg", byteArrayOut);
		String img_url = detailInfo.getImg();
		ByteArrayOutputStream byteArrayOut = null;
		if (img_url != null && !img_url.trim().equals("")) {
			img_url = img_url.trim();
			try {
				byteArrayOut = getNciicImg(authinfo, img_url);
			} catch (Exception e1) {
				if (byteArrayOut != null)
					byteArrayOut.close();
				e1.printStackTrace();
			}
		}

		try {
			if (!outFile.exists()) {
				fos = new FileOutputStream(outpath);
				wb = new HSSFWorkbook();
				sheet = wb.createSheet("身份信息");
				sheet.setColumnWidth(0, 5400);
				sheet.setColumnWidth(5, 2660);
				sheet.setColumnWidth(6, 5600);
				sheet.setColumnWidth(7, 5600);
				sheet.setColumnWidth(8, 5600);
				sheet.setColumnWidth(9, 7600);
				sheet.setColumnWidth(10, 5600);
				sheet.setColumnWidth(12, 5600);
				sheet.setColumnWidth(13, 3814);

				// 创建样式
				cs = wb.createCellStyle();
				cs.setAlignment(CellStyle.ALIGN_CENTER);
				cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
				cs.setBorderBottom(CellStyle.BORDER_DOTTED);
				cs.setBorderLeft(CellStyle.BORDER_THIN);
				cs.setBorderRight(CellStyle.BORDER_THIN);
				cs.setBorderTop(CellStyle.BORDER_THIN);

				row = sheet.createRow(0);
				row.setHeightInPoints(30);

				c = row.createCell(0);
				c.setCellStyle(cs);
				c.setCellValue("公民身份号码");
				c = row.createCell(1);
				c.setCellStyle(cs);
				c.setCellValue("姓名");
				c = row.createCell(2);
				c.setCellStyle(cs);
				c.setCellValue("曾用名");
				c = row.createCell(3);
				c.setCellStyle(cs);
				c.setCellValue("性别");
				c = row.createCell(4);
				c.setCellStyle(cs);
				c.setCellValue("民族");
				c = row.createCell(5);
				c.setCellStyle(cs);
				c.setCellValue("出生日期");
				c = row.createCell(6);
				c.setCellStyle(cs);
				c.setCellValue("数据归属单位代码");
				c = row.createCell(7);
				c.setCellStyle(cs);
				c.setCellValue("所属省市县（区）");
				c = row.createCell(8);
				c.setCellStyle(cs);
				c.setCellValue("籍贯省市县（区）");
				c = row.createCell(9);
				c.setCellStyle(cs);
				c.setCellValue("住址");
				c = row.createCell(10);
				c.setCellStyle(cs);
				c.setCellValue("服务处所");
				c = row.createCell(11);
				c.setCellStyle(cs);
				c.setCellValue("婚姻状况");
				c = row.createCell(12);
				c.setCellStyle(cs);
				c.setCellValue("文化程度");
				c = row.createCell(13);
				c.setCellStyle(cs);
				c.setCellValue("相片");
				System.out.println(sheet.getLastRowNum() + "%%%%");
				row = sheet.createRow(1);
				row.setHeight((short) 2175);
				c = row.createCell(0);
				c.setCellStyle(cs);
				c.setCellValue(detailInfo.getIdentify_no());
				c = row.createCell(1);
				c.setCellStyle(cs);
				c.setCellValue(detailInfo.getZh_name());
				c = row.createCell(2);
				c.setCellStyle(cs);
				c.setCellValue(detailInfo.getUsed_name());
				c = row.createCell(3);
				c.setCellStyle(cs);
				c.setCellValue(detailInfo.getSex());
				c = row.createCell(4);
				c.setCellStyle(cs);
				c.setCellValue(detailInfo.getNation());
				c = row.createCell(5);
				c.setCellStyle(cs);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				c.setCellValue(sdf.format(detailInfo.getBirth()));
				c = row.createCell(6);
				c.setCellStyle(cs);
				c.setCellValue(detailInfo.getBelonged_unit());
				c = row.createCell(7);
				c.setCellStyle(cs);
				c.setCellValue(detailInfo.getBelonged_county());
				c = row.createCell(8);
				c.setCellStyle(cs);
				c.setCellValue(detailInfo.getOrigin_place());
				c = row.createCell(9);
				c.setCellStyle(cs);
				c.setCellValue(detailInfo.getAddress());
				c = row.createCell(10);
				c.setCellStyle(cs);
				c.setCellValue(detailInfo.getService_place());
				c = row.createCell(11);
				c.setCellStyle(cs);
				c.setCellValue(detailInfo.getMarry_status());
				c = row.createCell(12);
				c.setCellStyle(cs);
				c.setCellValue(detailInfo.getEducation());
				c = row.createCell(13);
				c.setCellStyle(cs);
				// c.setCellValue(detailInfo.getImg());
				patriarch = sheet.createDrawingPatriarch();
				anchor = new HSSFClientAnchor(13, 1, 25, 10, (short) 13, 1,
						(short) 14, 2);
				patriarch.createPicture(anchor, wb.addPicture(
						byteArrayOut.toByteArray(),
						HSSFWorkbook.PICTURE_TYPE_JPEG));

				wb.write(fos);
				fos.flush();
				fos.close();
				if (byteArrayOut != null)
					byteArrayOut.close();
				return;
			}
			fs = new FileInputStream(outpath);
			ps = new POIFSFileSystem(fs);
			wb = new HSSFWorkbook(ps);
			sheet = wb.getSheetAt(0);
			sheet.setColumnWidth(0, 5400);
			sheet.setColumnWidth(5, 2660);
			sheet.setColumnWidth(6, 5600);
			sheet.setColumnWidth(7, 5600);
			sheet.setColumnWidth(8, 5600);
			sheet.setColumnWidth(9, 7600);
			sheet.setColumnWidth(10, 5600);
			sheet.setColumnWidth(12, 5600);
			row = sheet.getRow(0);
			System.out.println("LastRowNum: " + sheet.getLastRowNum()
					+ ", [row0]LastCellNum: " + row.getLastCellNum());
			cs = wb.createCellStyle();
			cs.setAlignment(CellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			cs.setBorderBottom(CellStyle.BORDER_DOTTED);
			cs.setBorderLeft(CellStyle.BORDER_THIN);
			cs.setBorderRight(CellStyle.BORDER_THIN);
			cs.setBorderTop(CellStyle.BORDER_THIN);
			int cur_row_index = sheet.getLastRowNum() + 1;
			row = sheet.createRow((short) (cur_row_index));
			row.setHeight((short) 2175);
			c = row.createCell(0);
			c.setCellStyle(cs);
			c.setCellValue(detailInfo.getIdentify_no());
			c = row.createCell(1);
			c.setCellStyle(cs);
			c.setCellValue(detailInfo.getZh_name());
			c = row.createCell(2);
			c.setCellStyle(cs);
			c.setCellValue(detailInfo.getUsed_name());
			c = row.createCell(3);
			c.setCellStyle(cs);
			c.setCellValue(detailInfo.getSex());
			c = row.createCell(4);
			c.setCellStyle(cs);
			c.setCellValue(detailInfo.getNation());
			c = row.createCell(5);
			c.setCellStyle(cs);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			c.setCellValue(sdf.format(detailInfo.getBirth()));
			c = row.createCell(6);
			c.setCellStyle(cs);
			c.setCellValue(detailInfo.getBelonged_unit());
			c = row.createCell(7);
			c.setCellStyle(cs);
			c.setCellValue(detailInfo.getBelonged_county());
			c = row.createCell(8);
			c.setCellStyle(cs);
			c.setCellValue(detailInfo.getOrigin_place());
			c = row.createCell(9);
			c.setCellStyle(cs);
			c.setCellValue(detailInfo.getAddress());
			c = row.createCell(10);
			c.setCellStyle(cs);
			c.setCellValue(detailInfo.getService_place());
			c = row.createCell(11);
			c.setCellStyle(cs);
			c.setCellValue(detailInfo.getMarry_status());
			c = row.createCell(12);
			c.setCellStyle(cs);
			c.setCellValue(detailInfo.getEducation());
			c = row.createCell(13);
			c.setCellStyle(cs);
			patriarch = sheet.createDrawingPatriarch();
			anchor = new HSSFClientAnchor(13, cur_row_index, 13 + 10,
					cur_row_index + 10, (short) 13, cur_row_index, (short) 14,
					cur_row_index + 1);
			patriarch
					.createPicture(anchor, wb.addPicture(
							byteArrayOut.toByteArray(),
							HSSFWorkbook.PICTURE_TYPE_JPEG));
			System.out.println("PhysicalNumberOfCells: "
					+ row.getPhysicalNumberOfCells() + " " + ", LastCellNum: "
					+ row.getLastCellNum());
			fos = new FileOutputStream(outpath);
			wb.write(fos);
			fos.flush();
			fos.close();
			if (byteArrayOut != null)
				byteArrayOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (byteArrayOut != null)
					byteArrayOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public ByteArrayOutputStream getNciicImg(Authinfo authinfo, String img_url)
			throws Exception {
		System.out.println("###############GetImg阶段【开始】#############");
		String Auth_Content = authinfo.getAuth_Content();
		String fAvfGpbE26 = authinfo.getfAvfGpbE26();
		String JSESSIONID = authinfo.getJSESSIONID();
		if (Auth_Content == null || Auth_Content.trim().equals("")
				|| fAvfGpbE26 == null || fAvfGpbE26.trim().equals("")
				|| JSESSIONID == null || JSESSIONID.trim().equals("")) {
			System.out.println("认证原文不能为空!");
			return null;
		}

		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, secureRandom);
		SSLSocketFactory ssf = sslContext.getSocketFactory();

		// String strAction =
		// "https://web.nciic.org.cn/nciic_self/serv/ServicesPhotoAction.do?method=viewPhoto&id=1209082457&source=01&lx=502";
		URL myURL = new URL(img_url);
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL
				.openConnection();
		httpsConn.setSSLSocketFactory(ssf);
		// httpsConn.setDoOutput(true);

		// 消息头
		httpsConn.addRequestProperty("Accept",
				"image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5");
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

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedInputStream in = new BufferedInputStream(
				httpsConn.getInputStream());
		// File img = new File("e:/11.png");
		// BufferedOutputStream out = new BufferedOutputStream(
		// new FileOutputStream(img));
		byte[] buf = new byte[2048];
		int length = in.read(buf);
		while (length != -1) {
			out.write(buf, 0, length);
			length = in.read(buf);
		}
		in.close();

		httpsConn.disconnect();

		System.out.println("###############GetImg【结束】#############");
		return out;
	}

	private String doQueryCore(Authinfo authinfo, IdentifyInfo identifyInfo)
			throws Exception {
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

		if (identifyInfo == null || identifyInfo.isEmpty()) {
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
				+ identifyInfo.getIdentify_no()
				+ "</VAL><FT>1</FT></CN><CN>"
				+ "<BM>JCXX_CZRK</BM><L> and </L><FN sv=\"\">XM</FN><OPT>=</OPT>"
				+ "<VAL>"
				+ identifyInfo.getZh_name()
				+ "</VAL><FT>3</FT></CN></CNS></ROW></ROWS>";
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
			SameAddressParam sameAddressParam) throws Exception {
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

		// if (identifyInfo == null || identifyInfo.isEmpty()) {
		// System.out.println("字段信息不能为空!");
		// return null;
		// }

		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, secureRandom);
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		// String strAction =
		// "https://web.nciic.org.cn/nciic_self/jsp/serv/services/rela.jsp?PK="
		// + pk + "&VAL=" + val + "&BM=" + bm + "&ID=" + id + "&SD=" + sd;
		// serviceid=sap.id up_logID=sap.sd
		String strAction = "https://web.nciic.org.cn/nciic_self/serv/ServicesParseAction.do?method=getRes&serviceid="
				+ id + "&up_logID=" + sd;
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

		String xml = "<ROWS><ROW><BA fwid=\"" + id + "\"></BA><CNS><CN><BM>"
				+ bm + "</BM><L>and</L><FN>" + pk + "</FN><OPT>in</OPT><VAL>"
				+ val + "</VAL><FT>1</FT>" + "</CN></CNS></ROW></ROWS>";

		httpsConn.getOutputStream().write(xml.getBytes());
		httpsConn.getOutputStream().flush();

		printResponseHeaders(httpsConn);

		InputStreamReader insr = new InputStreamReader(
				httpsConn.getInputStream(), "UTF-8");
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
		System.out.println("###############获取同地址阶段【结束】#############");

		return str;
	}

	public String getRelation(Authinfo authinfo,
			SameAddressParam sameAddressParam) throws Exception {
		System.out.println("###############getRelation【开始】#############");
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
			System.out.println("relation参数信息不能为空!");
			return null;
		}
		String pk = sameAddressParam.getPk().trim();
		String val = sameAddressParam.getVal().trim();
		String bm = sameAddressParam.getBm().trim();
		String id = sameAddressParam.getId().trim();
		String sd = sameAddressParam.getSd().trim();

		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, secureRandom);
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		String strAction = "https://web.nciic.org.cn/nciic_self/serv/ServicesParseAction.do?method=getRela&PK="
				+ pk
				+ "&VAL="
				+ val
				+ "&BM="
				+ bm
				+ "&ID="
				+ id
				+ "&SD="
				+ sd
				+ "&FT=4&FLAG=1";
		URL myURL = new URL(strAction);
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL
				.openConnection();
		httpsConn.setSSLSocketFactory(ssf);
		httpsConn.setRequestMethod("POST");
		httpsConn.setDoOutput(true);

		// 消息头
		httpsConn.addRequestProperty("Accept", "*/*");
		httpsConn.addRequestProperty("Accept-Language", "zh-cn");
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

		printResponseHeaders(httpsConn);

		InputStreamReader insr = new InputStreamReader(
				httpsConn.getInputStream(), "UTF-8");
		BufferedReader br = new BufferedReader(insr);
		String line, str = "";
		while ((line = br.readLine()) != null) {
			str += line + "\n";
		}
		System.out.println(str);

		httpsConn.disconnect();
		System.out.println("###############getRelation【结束】#############");

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
		httpsConn.getOutputStream()
				.write(txtToString(path, "utf-8").getBytes());

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

		reg = "var\\s+Auth_Content\\s*=\\s*'([^']+)';";
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
					params[0] = params[0].trim();
					int endidx = params[0].length() - 1;
					sameAddressParam.setPk(params[0].substring(1, endidx));

					params[1] = params[1].trim();
					endidx = params[1].length() - 1;
					sameAddressParam.setVal(params[1].substring(1, endidx));

					params[2] = params[2].trim();
					endidx = params[2].length() - 1;
					sameAddressParam.setBm(params[2].substring(1, endidx));

					params[3] = params[3].trim();
					endidx = params[3].length() - 1;
					sameAddressParam.setId(params[3].substring(1, endidx));

					System.out.println(params[4]);
					params[4] = params[4].trim();
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
