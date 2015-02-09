package com.test;

import java.io.FileOutputStream;

import org.junit.Test;

import com.csrc.nciic.model.IdenDetailInfo;
import com.csrc.nciic.model.IdentifyInfo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class TestPdf {
	@Test
	public void t01() throws Exception {
		
	
	}

	public void ideninfoToPdf(IdenDetailInfo detailInfo) throws Exception {
		if (detailInfo == null) {
			System.out.println("ideninfoToPdf参数不能为空！");
			return;
		}
		// 第一个参数是页面大小。接下来的参数分别是左、右、上和下页边距。
		Document document = new Document(PageSize.A4, 40, 40, 40, 40);
		PdfWriter.getInstance(document, new FileOutputStream("e:\\99.pdf"));
		document.open();
		BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
				BaseFont.NOT_EMBEDDED);
		bf = BaseFont.createFont("C:/Windows/Fonts/simfang.ttf",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);   
		Font font = new Font(bf, 12, Font.NORMAL);
		String str = "公民身份号码：" + detailInfo.getIdentify_no();
		document.add(new Paragraph(str, font));
		str = "姓名：" + detailInfo.getZh_name();
		document.add(new Paragraph(str, font));
		str = "曾用名：" + detailInfo.getUsed_name();
		document.add(new Paragraph(str, font));
		str = "姓别：" + detailInfo.getSex();
		document.add(new Paragraph(str, font));
		str = "民族：" + detailInfo.getNation();
		document.add(new Paragraph(str, font));
		str = "出生日期：" + detailInfo.getBirth();
		document.add(new Paragraph(str, font));
		str = "数据归属单位代码：" + detailInfo.getBelonged_unit();
		document.add(new Paragraph(str, font));
		str = "所属省市县（区）：" + detailInfo.getBelonged_county();
		document.add(new Paragraph(str, font));
		str = "籍贯省市县（区）：" + detailInfo.getOrigin_place();
		document.add(new Paragraph(str, font));
		str = "住址：" + detailInfo.getAddress();
		document.add(new Paragraph(str, font));
		str = "服务处所：" + detailInfo.getService_place();
		document.add(new Paragraph(str, font));
		str = "婚姻状况：" + detailInfo.getMarry_status();
		document.add(new Paragraph(str, font));
		str = "文化程度：" + detailInfo.getEducation();
		document.add(new Paragraph(str, font));
		str = "相片：" + detailInfo.getImg();
		document.add(new Paragraph(str, font));
//		document.add(new Paragraph("ghcfkj", FontFactory.getFont(
//				FontFactory.COURIER, 14, Font.BOLD,
//				new BaseColor(255, 150, 200))));
		document.close();
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
