package com.cmpe.graphify.controller;

import com.cmpe.graphify.client.ServiceClient;
import com.cmpe.graphify.client.Validate;
import com.cmpe.graphify.util.ZipUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

@Controller
public class FileController {

    @RequestMapping(value = "/echofile", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody HashMap<String, Object> echoFile(MultipartHttpServletRequest request,
                                                          HttpServletResponse response) throws Exception {
        System.out.println("In controller");
        MultipartFile multipartFile = request.getFile("file");
        Long size = multipartFile.getSize();
        String contentType = multipartFile.getContentType();
        InputStream stream = multipartFile.getInputStream();
        File file = new File("D:\\"+multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        System.out.println(" file "+ file.getAbsolutePath());

        String host = request.getParameter("host");
        String port = request.getParameter("port");
        String user = request.getParameter("user");
        String pass = request.getParameter("password");
        String db = request.getParameter("db");

        String url = "jdbc:mysql://%s:%s/%s?user=%s&password=%s&autoReconnect=true&useSSL=false";
        url = String.format(url, host, port, db, user, pass);

        System.out.println(url +" file "+ file.getAbsolutePath());

        String outputDir = "D:\\readDir\\"+ Long.toString(System.currentTimeMillis());
        ZipUtil.unZipIt(file.getAbsolutePath(), outputDir);

        ServiceClient serviceClient = new ServiceClient("localhost", 8000, db);
        Validate validate = serviceClient.validate(url, outputDir);

        System.out.println(validate);

        byte[] bytes = IOUtils.toByteArray(stream);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("fileoriginalsize", size);
        map.put("contenttype", contentType);
        map.put("base64", new String(Base64Utils.encode(bytes)));



        return map;
    }
}