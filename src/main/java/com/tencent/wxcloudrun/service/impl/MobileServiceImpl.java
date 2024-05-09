package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spire.doc.*;
import com.spire.doc.documents.HorizontalAlignment;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.TableRowHeightType;
import com.spire.doc.documents.VerticalAlignment;
import com.spire.doc.fields.DocPicture;
import com.spire.doc.fields.TextRange;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.controller.CounterController;
import com.tencent.wxcloudrun.dao.*;
import com.tencent.wxcloudrun.dto.NoteRequest;
import com.tencent.wxcloudrun.dto.SignInfoRequest;
import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.service.MobileService;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.poi.hslf.blip.PNG;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


@Service
public class MobileServiceImpl implements MobileService {
    final SignInfoMapper signInfoMapper;
    final LoginInfoMapper loginInfoMapper;

    final OrgUserMapper orgUserMapper;

    final NoteMapper noteMapper;
    final Logger logger;

    final OrgDepartmentMapper orgDepartmentMapper;

    public MobileServiceImpl(@Autowired SignInfoMapper signInfoMapper, LoginInfoMapper loginInfoMapper, OrgUserMapper orgUserMapper, NoteMapper noteMapper, OrgDepartmentMapper orgDepartmentMapper) {
        this.signInfoMapper = signInfoMapper;
        this.loginInfoMapper = loginInfoMapper;
        this.orgUserMapper = orgUserMapper;
        this.noteMapper = noteMapper;
        this.orgDepartmentMapper = orgDepartmentMapper;
        this.logger = LoggerFactory.getLogger(CounterController.class);
        ;
    }

    @Override
    public ApiResponse loginCheck(String code) {

        String openId = getOpenId(code);
        logger.info("1111+" + openId);
        if (openId == null || openId.isEmpty()) {

            return ApiResponse.error("OpenId不存在");
        }
        OaCode user = loginInfoMapper.getUser(openId);
        if (user == null || user.getOaCode() == null || user.getOaCode().isEmpty()) {
            return ApiResponse.error("未注册uid");
        }
        return ApiResponse.ok(user);
    }

    @Override
    public ApiResponse setLab(String code, String dept) {

        if (dept == null || dept.isEmpty()) {
            return ApiResponse.error("dept不存在");
        }
        String openId = getOpenId(code);
        if (openId == null || openId.isEmpty()) {
            return ApiResponse.error("OpenId不存在");
        }
        loginInfoMapper.setLab(openId, dept);


        return ApiResponse.ok();
    }

    @Override
    public ApiResponse signIn(String code, String oaCode) {

        String openId = getOpenId(code);
        if (openId == null || openId.isEmpty()) {

            return ApiResponse.error("OpenId不存在");
        }
        OrgUser user = orgUserMapper.getUser(oaCode);
        logger.info(oaCode);
        logger.info(String.valueOf(user));
        if (user == null || user.getUserId() == null || user.getUserId().isEmpty()) {
            return ApiResponse.error("用户不存在");
        }

        OaCode open = loginInfoMapper.getUser(openId);
        if (open != null && open.getOaCode() != null && !open.getOaCode().isEmpty()) {
            return ApiResponse.error("微信号已绑定过");
        }
        OaCode userId = loginInfoMapper.getOpenId(oaCode);
        if (userId != null && userId.getOpenId() != null && !userId.getOpenId().isEmpty()) {
            return ApiResponse.error("用户已被注册");
        }
        logger.info(openId);
        loginInfoMapper.createLogin(UUID.randomUUID().toString(), openId, oaCode);

        return ApiResponse.ok(open);
    }

    @Override
    public ApiResponse getAllLab() {


        List<OrgDepartment> dept = orgDepartmentMapper.getDept();

        return ApiResponse.ok(dept);
    }

    @Override
    public ApiResponse firstCheck(String dept, String code) {

        String openId = getOpenId(code);
        if (openId == null || openId.isEmpty()) {
            return ApiResponse.error("invalid code");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30);
        SignInfo id = signInfoMapper.getId(openId, dept, calendar.getTime());
        return ApiResponse.ok(id != null);
    }

    @Override
    public ApiResponse save(SignInfoRequest data) {

        String openId = getOpenId(data.getCode());
        String dept = data.getDepartment();
        //String path = data.getUserSign();
        SignInfo id = signInfoMapper.getIds(openId, dept);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (id != null) {

                signInfoMapper.updateSign(data.getCompany(), data.getUserName(), data.getUserJob(), data.getUserSign(), simpleDateFormat.parse(data.getSignTime()), openId, dept);

            } else {
                String uuid = UUID.randomUUID().toString();
                signInfoMapper.createSign(uuid, data.getCompany(), data.getUserName(), data.getUserJob(), data.getUserSign(), simpleDateFormat.parse(data.getSignTime()), openId, dept);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return ApiResponse.ok("succ");
    }

    @Override
    public ApiResponse getNote(String dept) {
        String fileContent = "";
        String url = "https://api.weixin.qq.com/tcb/batchdownloadfile?access_token=" + getToken();// service.getAccessToken();
        String downLoadUrl = getDownLoadUrl(url, dept);
        if (downLoadUrl.isEmpty()) {
            return ApiResponse.error("获取下载地址错误");
        }
        fileContent = getFileContent(downLoadUrl);
        return ApiResponse.ok(fileContent);
    }

    private String getOpenId(String code) {
        String appid = "wx79256d45af928fbe";
        String secret = "aaf8e9547aac54e9623502d9d761a930";
        //appid和secret是开发者分别是小程序ID和小程序密钥，开发者通过微信公众平台-》设置-》开发设置就可以直接获取，
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        BufferedReader in = null;
        String openId = "";
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
                logger.info(line);
            }
            Map<String, String> datas = (Map<String, String>) JSONArray.parse(sb.toString());
            openId = datas.get("openid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return openId;
    }

    private String getToken() {
        //云托管建立在航天云检小程序账号下，所以使用航天云检的appid
        String appid = "wx35c9fed47d66ce6c";
        String secret = "3a3c4f118f09b1b1be1efb5fd650b4f3";
        //appid和secret是开发者分别是小程序ID和小程序密钥，开发者通过微信公众平台-》设置-》开发设置就可以直接获取，
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + appid + "&secret=" + secret;
        BufferedReader in = null;
        String token = "";
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
                logger.info(line);
            }
            Map<String, String> datas = (Map<String, String>) JSONArray.parse(sb.toString());
            token = datas.get("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    private String getDownLoadUrl(String url, String dept) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        String path = "";
        JSONObject req = new JSONObject();
        req.put("env", "prod-3gfoduuy7d743ea0"); // 云开发环境 从小程序-云开发面板里面找到
        JSONArray array = new JSONArray();
        JSONObject file = new JSONObject();
        if (dept.isEmpty()) {
            file.put("fileid", "cloud://prod-3gfoduuy7d743ea0.7072-prod-3gfoduuy7d743ea0-1324756753/note/note.doc");
        } else {
            file.put("fileid", "cloud://prod-3gfoduuy7d743ea0.7072-prod-3gfoduuy7d743ea0-1324756753/note/" + dept + "/note.doc");
        }

        array.add(file);
        req.put("file_list", array);
        try {
            StringEntity entity = new StringEntity(req.toString());
            httpPost.setEntity(entity);
            HttpResponse response1;

            response1 = httpClient.execute(httpPost);
            InputStream inputStream = response1.getEntity().getContent();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            Map<String, Object> result = (Map<String, Object>) JSONArray.parse(out.toString());
            JSONArray fileList = (JSONArray) result.get("file_list");
            path = ((JSONObject) fileList.get(0)).getString("download_url");
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    private String getFileContent(String fileUrl) {
        BufferedReader bf = null;
        String content = "";
        try {
            URL url = new URL(fileUrl);
            //建立URL链接
            URLConnection conn = url.openConnection();
            //设置模拟请求头
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //开始链接
            conn.connect();
            //因为要用到URLConnection子类的方法，所以强转成子类
            HttpURLConnection urlConn = (HttpURLConnection) conn;
            //响应200
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //字节或字符读取的方式太慢了，用BufferedReader封装按行读取
                InputStream inputStream = urlConn.getInputStream();
//                bf = new BufferedReader(new InputStreamReader(inputStream));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                HWPFDocument wordDocument = new HWPFDocument(inputStream);
                org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
                wordToHtmlConverter.processDocument(wordDocument);

                org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();

                DOMSource domSource = new DOMSource(htmlDocument);

                StreamResult streamResult = new StreamResult(baos);

                TransformerFactory tf = TransformerFactory.newInstance();

                Transformer serializer = tf.newTransformer();

                serializer.setOutputProperty(OutputKeys.ENCODING, "utf8");

                serializer.setOutputProperty(OutputKeys.INDENT, "yes");

                serializer.setOutputProperty(OutputKeys.METHOD, "html");

                serializer.transform(domSource, streamResult);

                content = baos.toString();
                content = content.substring(content.indexOf("<p class=\"p1\">"), content.lastIndexOf("</body>"));
                baos.close();
                inputStream.close();
                //通过已获取的文件内容   FTP上传至服务器新建文件中
            } else {
                System.out.println("无法链接到URL!");
            }

        } catch (Exception e) {
            e.printStackTrace();


        } finally {
            try {
                if (bf != null) {
                    bf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }


    @Override
    public ApiResponse getOtherNote(String dept) {
        if (dept.isEmpty()) {
            return ApiResponse.error("dept为空");
        }
        return ApiResponse.ok(noteMapper.getTemp(dept));
    }

    @Override
    public ApiResponse setNote(NoteRequest data) {

        String openId = getOpenId(data.getCode());
        String dept = data.getDept();
        Note note = noteMapper.getTemp(dept);
        String token = getToken();
        String url = "https://api.weixin.qq.com/tcb/batchdownloadfile?access_token=" + token;
        //获取模板doc文件下载路径
        String path = getDownLoadUrl(url, "");
        if (path.isEmpty()) {
            return ApiResponse.error("获取下载地址错误");
        }
        String noteUrl = setFile(path, dept, data.getOtherNote(), token);
        if (note != null) {
            noteMapper.updateNote(noteUrl, data.getOtherNote(), new Date(), openId, dept);
        } else {
            String uuid = UUID.randomUUID().toString();
            noteMapper.insertNote(uuid, noteUrl, data.getOtherNote(), dept, new Date(), openId);
        }

        return ApiResponse.ok("succ");
    }

    private String setFile(String fileUrl, String dept, String otherNote, String access_token) {
        String content = "";
        Document doc = new Document();
        ByteArrayOutputStream bos = null;
        ByteArrayOutputStream out = null;
        InputStream inputStream = null;
        InputStream stream = null;
        try {
            URL url = new URL(fileUrl);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.connect();
            HttpURLConnection urlConn = (HttpURLConnection) conn;
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //通过下载路径获得的note.doc，读取
                inputStream = urlConn.getInputStream();

                //加载到doc中
                doc.loadFromStream(inputStream, FileFormat.Doc);
                doc.replace("$DEPT", dept, false, true);
                Paragraph para = doc.getSections().get(doc.getSections().getCount() - 1).getParagraphs().get(doc.getSections().get(0).getParagraphs().getCount() - 2);//在段落起始、末尾添加书签的开始标签和结束标签，并命名书签
                //追加“其他”设置的内容
                TextRange tr = para.appendText(otherNote + "\n");
                tr.getCharacterFormat().setFontSize(11);
                tr.getCharacterFormat().setFontName("宋体");
                bos = new ByteArrayOutputStream();//创建输出流对象
                //处理完之后输出为outputStream
                doc.saveToStream(bos, FileFormat.Doc);
                byte[] array = bos.toByteArray();
                String uploadUrl = "https://api.weixin.qq.com/tcb/uploadfile?access_token=" + access_token;
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost httpPost = new HttpPost(uploadUrl);
                httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
                JSONObject req = new JSONObject();
                req.put("env", "prod-3gfoduuy7d743ea0");
                req.put("path", "note/" + dept + "/note.doc");
                StringEntity entity = new StringEntity(req.toString());
                httpPost.setEntity(entity);
                HttpResponse response1;
                response1 = httpClient.execute(httpPost);
                stream = response1.getEntity().getContent();
                out = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int bytesRead = 0;
                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                Map<String, Object> result = (Map<String, Object>) JSONArray.parse(out.toString());
                RestTemplate template = new RestTemplate();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                map.add("key", "note/" + dept + "/note.doc");
                map.add("Signature", result.get("authorization"));
                map.add("x-cos-security-token", result.get("token"));
                map.add("x-cos-meta-fileid", result.get("cos_file_id"));
                map.add("file", array);
                HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(map, httpHeaders);

                String upUrl = result.get("url").toString();
                ResponseEntity<Object> responseEntity = template.postForEntity(upUrl, httpEntity, Object.class);
                content = responseEntity.getHeaders().get("Location").toString();
            } else {
                ApiResponse.error("无法链接到下载URL!");
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();


        } finally {
            try {

                doc.close();
                if (bos != null) {
                    bos.close();
                }
                if (out != null) {
                    out.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (stream != null) {
                    stream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }


    @Override
    public ApiResponse getInfo(SignInfoRequest data) {


        List<SignInfo> dept = signInfoMapper.getAll(data.getCompany(), data.getSignTime(), data.getEndTime(), data.getDepartment());

        return ApiResponse.ok(dept);
    }


    @Override
    public ApiResponse getExport(String data, String dept) {

        //String ids = "'" + data.replaceAll(",", "','") + "'";
        List<String> ids = new ArrayList<>();
        ids.addAll(Arrays.asList(data.split(",")).subList(0, data.split(",").length));
        List<SignInfo> export = signInfoMapper.getExport(ids);
        String content = "";
        if (export != null && export.size() > 0) {
            String token = getToken();
            String url = "https://api.weixin.qq.com/tcb/batchdownloadfile?access_token=" + token;
            String path = getDownLoadUrl(url, dept);
            if (path.isEmpty()) {
                return ApiResponse.error("获取下载地址错误");
            }
            content = setExportFile(path, export, token);
        }

        return ApiResponse.ok(content);
    }

    private String setExportFile(String fileUrl, List<SignInfo> list, String access_token) {
        String content = "";
        Document document = new Document();
        ByteArrayOutputStream bos = null;
        ByteArrayOutputStream out = null;
        InputStream inputStream = null;
        InputStream stream = null;
        try {
            URL url = new URL(fileUrl);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.connect();
            HttpURLConnection urlConn = (HttpURLConnection) conn;
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //通过下载路径获得的note.doc，读取
                inputStream = urlConn.getInputStream();

                long time = System.currentTimeMillis();
                //加载到doc中
                document.loadFromStream(inputStream, FileFormat.Doc);
                String[] header = {"单位", "姓名", "职务", "签名", "日期"};

                Section sec = document.getLastSection();
                Table table = sec.addTable(true);
                table.resetCells(list.size() + 1, header.length); //设置表格第一行作为表头，写入表头数组内容，并格式化表头数据
                TableRow row = table.getRows().get(0);
                row.isHeader(true);
                row.setHeight(20);
                row.setHeightType(TableRowHeightType.Exactly);
                row.getRowFormat().setBackColor(Color.white);
                for (int i = 0; i < header.length; i++) {
                    row.getCells().get(i).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
                    Paragraph p = row.getCells().get(i).addParagraph();
                    p.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
                    TextRange range1 = p.appendText(header[i]);
                    range1.getCharacterFormat().setFontName("Arial");
                    range1.getCharacterFormat().setFontSize(12f);
                    range1.getCharacterFormat().setBold(true);
//            range1.getCharacterFormat().setTextColor(Color.white);
                } //写入剩余组内容到表格，并格式化数据
                for (int r = 0; r < list.size(); r++) {
                    TableRow dataRow = table.getRows().get(r + 1);
                    dataRow.setHeight(50);
                    dataRow.setHeightType(TableRowHeightType.Exactly);
                    dataRow.getRowFormat().setBackColor(Color.white);
                    for (int c = 0; c <= 4; c++) {
                        dataRow.getCells().get(c).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
                        TextRange range2 = null;
                        switch (c) {
                            case 0:
                                range2 = dataRow.getCells().get(c).addParagraph().appendText(list.get(r).getCompany());
                                break;
                            case 1:
                                range2 = dataRow.getCells().get(c).addParagraph().appendText(list.get(r).getUserName());
                                break;
                            case 2:
                                range2 = dataRow.getCells().get(c).addParagraph().appendText(list.get(r).getUserJob());
                                break;
                            case 3: {
                                DocPicture dp = dataRow.getCells().get(c).addParagraph().appendPicture(getByte(list.get(r).getUserSign()));
                                dp.getOwnerParagraph().getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
                                dp.getOwnerParagraph().getFormat().setKeepFollow(true);
                                dp.setWidth(100);
                                dp.setHeight(50);
                            }
                            case 4:
                                range2 = dataRow.getCells().get(c).addParagraph().appendText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(r).getSignTime()));
                                break;
                        }
                        if (c != 3) {
                            range2.getOwnerParagraph().getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
                            range2.getCharacterFormat().setFontName("Arial");
                            range2.getCharacterFormat().setFontSize(10f);
                        }
                    }
                    bos = new ByteArrayOutputStream();
                    //处理完之后输出为outputStream
                    document.saveToStream(bos, FileFormat.Doc);
                    byte[] array = bos.toByteArray();
                    String uploadUrl = "https://api.weixin.qq.com/tcb/uploadfile?access_token=" + access_token;
                    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                    HttpPost httpPost = new HttpPost(uploadUrl);
                    httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
                    JSONObject req = new JSONObject();
                    req.put("env", "prod-3gfoduuy7d743ea0");
                    req.put("path", "export/" + time + "export.doc");
                    StringEntity entity = new StringEntity(req.toString());
                    httpPost.setEntity(entity);
                    HttpResponse response1;
                    response1 = httpClient.execute(httpPost);
                    stream = response1.getEntity().getContent();
                    out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[8192];
                    int bytesRead = 0;
                    while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    Map<String, Object> result = (Map<String, Object>) JSONArray.parse(out.toString());
                    RestTemplate template = new RestTemplate();
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                    String downLoad = "export/" + time + "export.doc";
                    map.add("key", downLoad);
                    map.add("Signature", result.get("authorization"));
                    map.add("x-cos-security-token", result.get("token"));
                    map.add("x-cos-meta-fileid", result.get("cos_file_id"));
                    map.add("file", array);
                    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(map, httpHeaders);

                    String upUrl = result.get("url").toString();
                    template.postForEntity(upUrl, httpEntity, Object.class);
                    content = result.get("file_id").toString();
                }

            } else {
                ApiResponse.error("无法链接到下载URL!");
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();


        } finally {
            try {

                document.close();
                if (bos != null) {
                    bos.close();
                }
                if (out != null) {
                    out.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (stream != null) {
                    stream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    private byte[] getByte(String path) {
        URL url = null;
        byte[] imageBytes;
        try {
            url = new URL(path);

            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            imageBytes = outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return imageBytes;
    }

}