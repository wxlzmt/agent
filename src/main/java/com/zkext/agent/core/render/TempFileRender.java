package com.zkext.agent.core.render;

import java.io.File;

import com.jfinal.render.FileRender;

/**
 * 自定义fileRender, 在renderFile完成之后, 删除临时文件
* @ClassName: TempFileRender 
* @Description: render之后删除临时文件;
*
 */
public class TempFileRender extends FileRender {

	private File file;

	public TempFileRender(File file) {
		super(file);
		this.file = file;
	}

	public TempFileRender(File file, String downloadFileName) {
		super(file, downloadFileName);
	}

	@Override
	public void render() {
		try {
			super.render();
		} finally {
			if (null != file) {
				file.delete();
				file.deleteOnExit();
			}
		}
	}

}
