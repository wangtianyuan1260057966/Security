package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dao.SignInfoMapper;
import com.tencent.wxcloudrun.model.SignInfo;
import com.tencent.wxcloudrun.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


@Service
public class MobileServiceImpl implements MobileService {
    final SignInfoMapper signInfoMapper;
    public final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MobileServiceImpl(@Autowired SignInfoMapper signInfoMapper) {
        this.signInfoMapper = signInfoMapper;
    }

    @Override
    public boolean firstCheck(String dept, String code) {

        String openId = getOpenId(code);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30);
        SignInfo id = signInfoMapper.getId(openId, dept, calendar.getTime());
        return id!=null;
    }

    public ApiResponse save(String data) {
//        ResponseObject responseObject = ResponseObject.newOkResponse();
//        Map<String, String> datas = (Map<String, String>) JSONArray.parse(data);
//        String openId = getOpenId(datas.get("code"));
//        String dept = datas.get("dept");
//        String path = saveImg(datas.get("userSign"));
//        List<RowMap> maps = DBSql.getMaps("select * from BO_EU_SECURITY_SIGN_INFO where OPENID = ? and DEPARTMENT = ?", new Object[]{openId, dept});
//        if (maps != null && maps.size() > 0) {
//            DBSql.update("update BO_EU_SECURITY_SIGN_INFO set company = ?,user_name = ?, user_job = ?,user_sign = ?, sign_time = ? where openid = ? and DEPARTMENT = ?",
//                    new Object[]{datas.get("company"), datas.get("userName"), datas.get("userJob"), path, simpleDateFormat.format(new Date()), openId, dept});
//        } else {
//            BO bo = new BO();
//            bo.set("DEPARTMENT", dept);
//            bo.set("COMPANY", datas.get("company"));
//            bo.set("USER_NAME", datas.get("userName"));
//            bo.set("USER_JOB", datas.get("userJob"));
//            bo.set("USER_SIGN", path);
//            bo.set("OPENID", openId);
//            bo.set("SIGN_TIME", simpleDateFormat.format(new Date()));
//            ProcessInstance createBOProcessInstance = SDK.getProcessAPI().createBOProcessInstance(ProcessModel.NOTE_INFO_DWGROUPID, "admin", "");
//
//            String bindid = createBOProcessInstance.getId();
//            SDK.getBOAPI().create("BO_EU_SECURITY_SIGN_INFO", bo, bindid, "admin");
//        }
//
        return ApiResponse.ok();
    }

    public ApiResponse getNote(String dept) {
//        ResponseObject responseObject = ResponseObject.newOkResponse();
//        String path = ProcessModel.EXPORT_PATH + dept + "\\note.doc";
////        System.out.println("22222" + path);
//        File fileSp = new File(path);  //在这里填写存放路径
//        if (!fileSp.exists()) {
//            path = ProcessModel.MAIN_PATH;
//        }
//        String content = "";
//        try {
//            FileInputStream file = new FileInputStream(path);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            OutputStream outStream = new BufferedOutputStream(baos);
//            HWPFDocument wordDocument = new HWPFDocument(file);
//            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
//            wordToHtmlConverter.processDocument(wordDocument);
//
//            Document htmlDocument = wordToHtmlConverter.getDocument();
//
//            DOMSource domSource = new DOMSource(htmlDocument);
//
//            StreamResult streamResult = new StreamResult(outStream);
//
//            TransformerFactory tf = TransformerFactory.newInstance();
//
//            Transformer serializer = tf.newTransformer();
//
//            serializer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
//
//            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
//
//            serializer.setOutputProperty(OutputKeys.METHOD, "html");
//
//            serializer.transform(domSource, streamResult);
//
//            content = baos.toString();
//
//            file.close();
//            baos.close();
//            outStream.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        content = content.substring(content.indexOf("<p class=\"p1\">"), content.lastIndexOf("</body>"));
//
//        responseObject.setData(content);
        return ApiResponse.ok();
    }

    private static String getOpenId(String code) {
        String appid = "wx79256d45af928fbe";
        String secret = "aaf8e9547aac54e9623502d9d761a930";

        //appid和secret是开发者分别是小程序ID和小程序密钥，开发者通过微信公众平台-》设置-》开发设置就可以直接获取，
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        BufferedReader in = null;
        String  openId = "";
        try {
            URL weChatUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = weChatUrl.openConnection();
            // 设置通用的请求属性
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            Map<String, String> datas = (Map<String, String>) JSONArray.parse(sb.toString());
            openId = datas.get("openid");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return openId;
    }

//    private String saveImg(String base) {
//        long time = System.currentTimeMillis();
//        String outFileName = "img_" + time + ".txt";
//        String path = ProcessModel.IMG_PATH + outFileName;
//        try {
//            File writeName = new File(path); // 相对路径，如果没有则要建立一个新的output.txt文件
//            if(!writeName.exists()) {
//                writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
//            }
//            FileWriter writer = new FileWriter(writeName);
//            BufferedWriter out = new BufferedWriter(writer);
//            out.write(base);
//            out.flush(); // 把缓存区内容压入文件
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return path;
//    }
}