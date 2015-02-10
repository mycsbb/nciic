package com.test;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * 【功能描述：给PDF 加水印功能，（文字水印和图片水印）】 【功能详细描述：逐条详细描述功能】
 * 
 * @author 【lfssay】
 * @see 【相关类/方法】
 * @version 【类版本号, 2013-8-20 上午11:22:21】
 * @since 【产品/模块版本】
 */
public class PdfAddWaterMark {
	static Log log = LogFactory.getLog(PdfAddWaterMark.class);

	public static void main(String[] args) throws DocumentException,
			IOException {
		new PdfAddWaterMark().addWaterMark("E:/tt/1.pdf", "E:/tt/129.pdf",
				"国家图书馆藏", "E:/tt/1.jpg", 400, 800, 200, 800);
	}

	/**
	 * 
	 * 【功能描述：添加图片和文字水印】 【功能详细描述：功能详细描述】
	 * 
	 * @see 【类、类#方法、类#成员】
	 * @param srcFile
	 *            待加水印文件
	 * @param destFile
	 *            加水印后存放地址
	 * @param text
	 *            加水印的文本内容
	 * @param imgFile
	 *            加水印图片文件
	 * @param textWidth
	 *            文字横坐标
	 * @param textHeight
	 *            文字纵坐标
	 * @param imgWidth
	 *            图片横坐标
	 * @param imgHeight
	 *            图片纵坐标
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void addWaterMark(String srcFile, String destFile, String text,
			String imgFile, int textWidth, int textHeight, int imgWidth,
			int imgHeight) throws IOException, DocumentException {
		// 待加水印的文件
		PdfReader reader = new PdfReader(srcFile);
		// 加完水印的文件
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
				destFile));

		int total = reader.getNumberOfPages() + 1;
		PdfContentByte content;
		String path = this.getClass().getResource("/").getPath();
		// 设置字体
		// BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
		// BaseFont.EMBEDDED);
		// 加载字库来完成对字体的创建
		BaseFont font = BaseFont.createFont(path + "MSYH.TTF",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		// BaseFont base2 = BaseFont.createFont(BaseFont.HELVETICA,
		// BaseFont.WINANSI, BaseFont.EMBEDDED);
		// 水印文字
		String waterText = text;
		Image image = null;
		if (!StringUtils.isBlank(imgFile)) {
			image = Image.getInstance(imgFile);
			image.setAbsolutePosition(imgWidth, imgHeight);
			// 设置图片的显示大小
			image.scaleToFit(100, 125);
		}
		int j = waterText.length(); // 文字长度
		char c = 0;
		int high = 0;// 高度
		// 循环对每页插入水印
		for (int i = 1; i < total; i++) {
			// 水印的起始
			high = 50;
			// 水印在之前文本之上
			content = stamper.getOverContent(i);
			if (image != null) {
				content.addImage(image);
			}
			if (!StringUtils.isBlank(text)) {
				// 开始
				content.beginText();
				// 设置颜色 默认为蓝色
				content.setColorFill(BaseColor.BLUE);
				// content.setColorFill(Color.GRAY);
				// 设置字体及字号
				content.setFontAndSize(font, 38);
				// 设置起始位置
				// content.setTextMatrix(400, 880);
				content.setTextMatrix(textWidth, textHeight);
				// 开始写入水印
				content.showTextAligned(Element.ALIGN_LEFT, text, textWidth,
						textHeight, 45);
				// for (int k = 0; k < j; k++) {
				// content.setTextRise(14);
				// c = waterText.charAt(k);
				// // 将char转成字符串
				// content.showText(c + "");
				// high -= 5;
				// }
				content.endText();
			}
		}
		stamper.close();
		log.info("===" + srcFile + "===添加水印到==" + destFile + "==成功=====");
	}
}
