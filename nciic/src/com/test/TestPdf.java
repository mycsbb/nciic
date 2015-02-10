package com.test;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import com.csrc.nciic.model.IdenDetailInfo;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class TestPdf {
	@Test
	public void t01() throws Exception {
		Document document = new Document(PageSize.A4, 70, 70, 50, 50);
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream("e:/89.pdf"));
//		document.open();
//		document.add(new Paragraph("vfjv"));
//		document.close();
	}

	public void ideninfoToPdf(List<IdenDetailInfo> detailInfoList, String pdfpath) throws Exception {
		if (detailInfoList == null || pdfpath == null 
				|| pdfpath.trim().equals("")) {
			System.out.println("ideninfoToPdf参数不能为空！");
			return;
		}
		
		File pdffile = new File(pdfpath);
		if (pdffile.exists() && !pdffile.isFile()) {
			System.out.println("输出pdf路径不是普通文件！！");
			return;
		}
		
		Document document;
		PdfWriter wr;
		BaseFont bf;
		Font font;
		Image image;
		if (!pdffile.exists()) {
			// 第一个参数是页面大小。接下来的参数分别是左、右、上和下页边距。
			document = new Document(PageSize.A4, 70, 70, 50, 50);
			wr = PdfWriter.getInstance(document, new FileOutputStream(pdfpath));
			document.open();
			bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
					BaseFont.NOT_EMBEDDED);
			bf = BaseFont.createFont("C:/Windows/Fonts/simfang.ttf",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);   
			font = new Font(bf, 12, Font.NORMAL);
			image = Image.getInstance ("e:\\000.jpg");
			image.setIndentationLeft(40);
			document.add(image);
			String str = "公民身份号码：" + detailInfo.getIdentify_no();
			Paragraph paragraph = new Paragraph(str, font);
			//paragraph.setIndentationLeft(160);
			document.add(paragraph);
			str = "姓名：" + detailInfo.getZh_name();
			paragraph = new Paragraph(str, font);
			document.add(paragraph);
			str = "曾用名：" + detailInfo.getUsed_name();
			paragraph = new Paragraph(str, font);
			document.add(paragraph);
			str = "姓别：" + detailInfo.getSex();
			paragraph = new Paragraph(str, font);
			document.add(paragraph);
			str = "民族：" + detailInfo.getNation();
			paragraph = new Paragraph(str, font);
			document.add(paragraph);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			str = "出生日期：" + sdf.format(detailInfo.getBirth());
			paragraph = new Paragraph(str, font);
			document.add(paragraph);
			str = "数据归属单位代码：" + detailInfo.getBelonged_unit();
			paragraph = new Paragraph(str, font);
			document.add(paragraph);
			str = "所属省市县（区）：" + detailInfo.getBelonged_county();
			paragraph = new Paragraph(str, font);
			document.add(paragraph);
			str = "籍贯省市县（区）：" + detailInfo.getOrigin_place();
			document.add(new Paragraph(str, font));
			str = "住址：" + detailInfo.getAddress();
			paragraph = new Paragraph(str, font);
			document.add(paragraph);
			str = "服务处所：" + detailInfo.getService_place();
			paragraph = new Paragraph(str, font);
			document.add(paragraph);
			str = "婚姻状况：" + detailInfo.getMarry_status();
			paragraph = new Paragraph(str, font);
			document.add(paragraph);
			str = "文化程度：" + detailInfo.getEducation();
			paragraph = new Paragraph(str, font);
			document.add(paragraph);
//			document.add(new Paragraph("ghcfkj", FontFactory.getFont(
//					FontFactory.COURIER, 14, Font.BOLD,
//					new BaseColor(255, 150, 200))));
			document.close();
			return;
		}
		
	}
	
	 // 表头  
    public static final String[] tableHeader= { "姓名", "性别", "年龄",  
                  "学院", "专业", "年级"};  
    // 数据表字段数  
    private static final int colNumber = 6;  
    // 表格的设置  
    private static final int spacing = 2;  
    // 表格的设置  
    private static final int padding = 2;  
    // 导出Pdf文挡  
    public static void exportPdfDocument() {  
           // 创建文Pdf文挡50, 50, 50,50左右上下距离  
           Document document = new Document(new Rectangle(1500, 2000), 50, 50, 50, 50);  
           try {  
                  //使用PDFWriter进行写文件操作  
                  PdfWriter.getInstance(document,new FileOutputStream(  
                                "e:\\33.pdf"));  
                  document.open();  
                  // 中文字体  
                  BaseFont bfChinese =BaseFont.createFont("STSong-Light",  
                                "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);  
                  Font fontChinese = new Font(bfChinese, 12, Font.NORMAL);  
                  // 创建有colNumber(6)列的表格  
                  PdfPTable datatable = new PdfPTable(colNumber);  
                  //定义表格的宽度  
                  int[] cellsWidth = { 8, 2,2, 8, 5, 3 };  
                  datatable.setWidths(cellsWidth);  
                  // 表格的宽度百分比  
                  datatable.setWidthPercentage(100);  
                  datatable.getDefaultCell().setPadding(padding);  
                  datatable.getDefaultCell().setBorderWidth(spacing);  
                  //设置表格的底色  
                  datatable.getDefaultCell().setHorizontalAlignment(  
                                Element.ALIGN_CENTER);  
                  // 添加表头元素  
                  for (int i = 0; i <colNumber; i++) {  
                         datatable.addCell(new Paragraph(tableHeader[i], fontChinese));  
                  }  
                  // 添加子元素  
                  for (int i = 0; i <colNumber; i++) {  
                         datatable.addCell(new Paragraph(tableHeader[i], fontChinese));  
                  }  
                  document.add(datatable);  
           } catch (Exception e) {  
                  e.printStackTrace();  
           }  
           document.close();  
    }  

	@Test
	public void t02() throws Exception {
		 exportPdfDocument();  
	}
}
