package org.sample.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public class DocumentUtil {

    /*传入文件路径 传入参数Map 组合后获取document*/
    public static Document getDocument(String fileUrl, Map<String, String> map) throws IOException {
        File file = new File(fileUrl);
        return getDocument(file, "UTF-8", map);
    }

    /*传入文件路径 传入参数Map 转码方式 获取document*/
    public static Document getDocument(String fileUrl, String charsetName, Map<String, String> map) throws IOException {
        File file = new File(fileUrl);
        return getDocument(file, charsetName, map);
    }

    /*传入文件 传入参数 使用默认UTF-8*/
    public static Document getDocument(File file, Map<String, String> map) throws IOException {
        return getDocument(file, "UTF-8", map);
    }

    /*读取文件到Document*/
    public static Document getDocument(File file, String charsetName, Map<String, String> map) throws IOException {
        // 解析模板文件为 doc
        Document doc = Jsoup.parse(file, charsetName);
        Element element = null;
        // 获取元素并操作元素对象赋值
        for (Map.Entry<String, String> entry : map.entrySet()) {
            element = doc.getElementById(entry.getKey());
            if (element != null) {
                element.html(entry.getValue()==null?"":entry.getValue());
            }
        }
        // 把超文本语音html转换为可扩展超文本语句xhtml
//        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);
        return doc;
    }

    /*传入到哪个文件路径 传入生成的document*/
    public static void write(String fileUrl, Document doc) throws IOException {
        File file = new File(fileUrl);
        write(file, "UTF-8", doc);
    }
    /*传入到哪个文件路径 传入生成的document*/
    public static void write(String fileUrl,String charsetName, Document doc) throws IOException {
        File file = new File(fileUrl);
        write(file, charsetName, doc);
    }
    /*传入到哪个文件 传入生成的document*/
    public static void write(File file, Document doc) throws IOException {
        write(file, "UTF-8", doc);
    }

    // 将document内容写入文件中
    public static void write(File file, String charsetName, Document doc) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file, false);
        // 设置输出流
        OutputStreamWriter osw = new OutputStreamWriter(fos, charsetName);
        // 讲doc写入文件中
        osw.write(doc.html());
        osw.close();
    }
}
