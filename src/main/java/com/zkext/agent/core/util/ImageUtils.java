package com.zkext.agent.core.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.jfinal.kit.PathKit;

/**
 * 处理编辑裁剪图片
 */
public abstract class ImageUtils {

	private static String DEFAULT_THUMB_PREVFIX = "thumb_";
	private static String DEFAULT_CUT_PREVFIX = "cut_";
	private static Boolean DEFAULT_FORCE = false;

	/**
	 * 根据原图与裁切size截取局部图片
	 * 
	 * @param srcImg
	 *            源图片
	 * @param output
	 *            图片输出流
	 * @param rect
	 *            需要截取部分的坐标和大小
	 */
	public static void cutImage(File srcImg, OutputStream output, java.awt.Rectangle rect) {
		if (srcImg.exists()) {
			java.io.FileInputStream fis = null;
			ImageInputStream iis = null;
			try {
				fis = new FileInputStream(srcImg);
				String suffix = checkTypes(srcImg);
				// 将FileInputStream 转换为ImageInputStream
				iis = ImageIO.createImageInputStream(fis);
				// 根据图片类型获取该种类型的ImageReader
				ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
				reader.setInput(iis, true);
				ImageReadParam param = reader.getDefaultReadParam();
				param.setSourceRegion(rect);
				BufferedImage bi = reader.read(0, param);
				ImageIO.write(bi, suffix, output);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null)
						fis.close();
					if (iis != null)
						iis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new RuntimeException("the src image is not exist.");
		}
	}

	/**
	 * 根据原图与裁切size截取局部图片
	 * @param srcImg 源图片文件
	 * @param output 输出流
	 * @param x 要截取的区域的坐标x
	 * @param y 要截取的区域的坐标y
	 * @param width 要截取的区域的宽度
	 * @param height 要截取的区域的高度
	 */
	public static void cutImage(File srcImg, OutputStream output, int x, int y, int width, int height) {
		cutImage(srcImg, output, new java.awt.Rectangle(x, y, width, height));
	}

	/**
	 * 根据原图与裁切size截取局部图片
	 * @param srcImg 源图片文件
	 * @param destImgPath 目标文件路径,注意:是文件夹路径! 不是文件!
	 * @param rect 要截取的矩形区域
	 */
	public static void cutImage(File srcImg, String destImgPath, java.awt.Rectangle rect) {
		File destImg = new File(destImgPath);
		if (destImg.exists()) {
			String p = destImg.getPath();
			try {
				if (!destImg.isDirectory())
					p = destImg.getParent();
				if (!p.endsWith(File.separator))
					p = p + File.separator;
				cutImage(srcImg,
						new java.io.FileOutputStream(p + DEFAULT_CUT_PREVFIX + "_" + new java.util.Date().getTime() + "_" + srcImg.getName()), rect);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("the dest image is not exist.");
			}
		} else {
			throw new RuntimeException("the dest image folder is not exist.");
		}
	}

	/**
	 * 根据原图与裁切size截取局部图片
	 * @param srcImg 源图片文件
	 * @param destImg 目标文件路径,注意:是文件夹路径! 不是文件!
	 * @param x 要截取的区域的坐标x
	 * @param y 要截取的区域的坐标y
	 * @param width 要截取的区域的宽度
	 * @param height 要截取的区域的高度
	 */
	public static void cutImage(File srcImg, String destImg, int x, int y, int width, int height) {
		cutImage(srcImg, destImg, new java.awt.Rectangle(x, y, width, height));
	}
	
	/**
	 * 根据原图与裁切size截取局部图片
	 * @param srcImg 源图片文件路径
	 * @param destImg 目标文件路径,注意:是文件夹路径! 不是文件!
	 * @param x 要截取的区域的坐标x
	 * @param y 要截取的区域的坐标y
	 * @param width 要截取的区域的宽度
	 * @param height 要截取的区域的高度
	 */
	public static void cutImage(String srcImg, String destImg, int x, int y, int width, int height) {
		cutImage(new File(srcImg), destImg, new java.awt.Rectangle(x, y, width, height));
	}

	/**
	 * 根据图片路径生成缩略图
	 * 
	 * @param imagePath
	 *            原图片路径
	 * @param w
	 *            缩略图宽
	 * @param h
	 *            缩略图高
	 * @param prevfix
	 *            输出的结果文件名前面加前缀
	 * @param force
	 *            是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
	 */
	public static void thumbnailImage(File srcImg, OutputStream output, int w, int h, String prevfix, boolean force) {
		if (srcImg.exists()) {
			try {
				String suffix = checkTypes(srcImg);
				Image img = ImageIO.read(srcImg);
				// 根据原图与要求的缩略图比例，找到最合适的缩略图比例
				if (!force) {
					int width = img.getWidth(null);
					int height = img.getHeight(null);
					if ((width * 1.0) / w < (height * 1.0) / h) {
						if (width > w) {
							h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));

						}
					} else {
						if (height > h) {
							w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));

						}
					}
				}
				BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				Graphics g = bi.getGraphics();
				g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
				g.dispose();
				// 将图片保存在原目录并加上前缀
				ImageIO.write(bi, suffix, output);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("图片不存在!");
		}
	}

	/**
	 * 制造缩略图,输出到源文件所在目录,
	 * @param srcImg 源文件图片
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @param prevfix 输出的结果文件名前面加前缀
	 * @param force 是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
	 */
	public static void thumbnailImage(File srcImg, int w, int h, String prevfix, boolean force) {
		String p = srcImg.getAbsolutePath();
		try {
			if (!srcImg.isDirectory())
				p = srcImg.getParent();
			if (!p.endsWith(File.separator))
				p = p + File.separator;
			thumbnailImage(srcImg, new java.io.FileOutputStream(p + prevfix + srcImg.getName()), w, h, prevfix, force);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 制造缩略图,输出到源文件所在目录,
	 * 
	 * @param imagePath
	 *            源文件图片路径
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @param prevfix
	 *            输出的结果文件名前面加前缀
	 * @param force
	 *            是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
	 */
	public static void thumbnailImage(String imagePath, int w, int h, String prevfix, boolean force) {
		File srcImg = new File(imagePath);
		thumbnailImage(srcImg, w, h, prevfix, force);
	}

	/**
	 * 制造缩略图,输出到源文件所在目录,文件名前面加默认前缀
	 * 
	 * @param imagePath
	 *            源文件图片路径
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @param force
	 *            是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
	 */
	public static void thumbnailImage(String imagePath, int w, int h, boolean force) {
		thumbnailImage(imagePath, w, h, DEFAULT_THUMB_PREVFIX, DEFAULT_FORCE);
	}

	/**
	 * 制造缩略图,输出到源文件所在目录,文件名前面加默认前缀
	 * 
	 * @param imagePath
	 *            源文件图片路径
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 */
	public static void thumbnailImage(String imagePath, int w, int h) {
		thumbnailImage(imagePath, w, h, DEFAULT_FORCE);
	}

	/**
	 * 检查图片文件是否合法,如果不合法,则抛异常
	 * @param srcImg 图片文件路径
	 * @return 图片文件后缀(小写)
	 */
	private static String checkTypes(File srcImg) {
		// ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF,
		// gif]
		String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
		String suffix = null;
		// 获取图片后缀
		if (srcImg.getName().indexOf(".") > -1) {
			suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
		} // 类型和图片后缀全部小写，然后判断后缀是否合法
		if (suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase() + ",") < 0) {
			throw new RuntimeException("图片文件扩展名错误,允许的图片格式有:" + types);
		}
		return suffix;
	}

	public static void main(String[] args) {
		
		String base = PathKit.getRootClassPath()+File.separator+"test";
		
		String src = base+File.separator+"picture.jpg";
		ImageUtils.thumbnailImage(src, 100, 69);

		ImageUtils.cutImage(src, base, 536, 0, 536, 536);
		
		// new ImageUtils().thumbnailImage("imgs/Tulips.jpg", 150, 100);
		// new ImageUtils().cutImage("imgs/Tulips.jpg", "imgs", 250, 70, 300, 400);
	}

}