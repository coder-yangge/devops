package com.devops.common.util;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.common.Context;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.jxls.util.JxlsHelper;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Map;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ExcelUtils
 * @description: Excel导入导出工具类
 * @date 2020/4/18 0:38
 */
public class ExcelUtils {

    public static final String TEMPLATE = "templates";

    /**
     * 导出Excel
     *
     * @param template     模板名称
     * @param param        数据
     * @param outputStream 输出流
     */
    public static void exportExcel(String template, Map<String, Object> param, OutputStream outputStream) {
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        InputStream inputStream = null;
        try {
            File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + TEMPLATE + File.separator + template);
            inputStream = new FileInputStream(file);
            jxlsHelper.setEvaluateFormulas(false).processTemplate(inputStream, outputStream, new Context(param));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 解析Excel
     *
     * @param inputStream Excel文件输入流
     * @param template    解析模板名称
     * @param data        返回数据
     * @return
     */
    public static boolean importExcel(InputStream inputStream, String template, Map<String, Object> data) {

        try {
            XLSReader reader = ReaderBuilder.buildFromXML(ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + TEMPLATE + File.separator + template));
            XLSReadStatus status = reader.read(inputStream, data);
            if (status.isStatusOK()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return false;
    }
}
